package site.nomoreparties.stellaburgers;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.qameta.allure.Step;
import io.restassured.response.ValidatableResponse;

import static io.restassured.RestAssured.given;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserClient extends RestAssuredClient {
    private static final String USER_PATH = "api/auth/";

    @Step
    public ValidatableResponse create(User user) {

        return given()
                .spec(getBaseSpec())
                .body(user)
                .when()
                .post(USER_PATH + "register")
                .then();
    }

    @Step
    public ValidatableResponse login(UserCredentialsForLogin credentials) {
        return given()
                .spec(getBaseSpec())
                .body(credentials)
                .when()
                .post(USER_PATH + "login")
                .then();
    }

    @Step
    public ValidatableResponse updateWithAuthorization(UserCredentialsForUpdate credentials) {
        return given()
                .spec(getBaseSpec()).auth().oauth2(credentials.authorization)
                .body(credentials)
                .when()
                .patch(USER_PATH + "user")
                .then();
    }

    @Step
    public ValidatableResponse updateWithoutAuthorization(UserCredentialsForUpdate credentials) {
        return given()
                .spec(getBaseSpec())
                .body(credentials)
                .when()
                .patch(USER_PATH + "user")
                .then();
    }

    @Step
    public ValidatableResponse logout(String token) {
        String json = "{\"token\":" +"\""+ token +"\"}";
        return given()
                .spec(getBaseSpec())
                .body(json)
                .when()
                .post(USER_PATH + "logout")
                .then().assertThat().statusCode(200);
    }


}
