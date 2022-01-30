package site.nomoreparties.stellaburgers;

import io.qameta.allure.Step;
import io.restassured.response.ValidatableResponse;

import static io.restassured.RestAssured.given;

public class OrderClient extends RestAssuredClient
{
    @Step
    public ValidatableResponse createWithoutAuthorized(Order order) {

        return given()
                .spec(getBaseSpec())
                .body(order)
                .when()
                .post("api/orders").then();
    }

    @Step
    public ValidatableResponse createWithAuthorized(Order order, String authorization) {

        return given()
                .spec(getBaseSpec()).auth().oauth2(authorization)
                .body(order)
                .when()
                .post("api/orders").then();
    }


    @Step
    public ValidatableResponse getInformationAboutIngredients() {

        return given()
                .spec(getBaseSpec())
                .when()
                .get("api/ingredients").then();
    }

    @Step
    public ValidatableResponse getAllOrdersByUserWithoutAuthorized() {

        return given()
                .spec(getBaseSpec())
                .when()
                .get("api/orders").then();
    }

    @Step
    public ValidatableResponse getAllOrdersByUserWithAuthorized(String authorization) {

        return given()
                .spec(getBaseSpec()).auth().oauth2(authorization)
                .when()
                .get("api/orders").then();
    }

}
