import com.fasterxml.jackson.annotation.JsonInclude;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.response.ValidatableResponse;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import site.nomoreparties.stellaburgers.User;
import site.nomoreparties.stellaburgers.UserClient;
import site.nomoreparties.stellaburgers.UserCredentialsForLogin;
import site.nomoreparties.stellaburgers.UserCredentialsForUpdate;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class CreateUserTest {

    private User user;
    private UserClient userClient;
    private String token;
    private String authorization;

    @Before
    public void setUp() {
        RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();
        userClient = new UserClient();
        user = User.getRandom();
    }

    @After
   public void tearDown() {
       userClient.logout(token);
   }

    @Test
    @DisplayName("User can be created with valid data and login")
    public void userCanBeCreatedWithValidDataAndLogin() {

        ValidatableResponse responseCreate = userClient.create(user);
        ValidatableResponse responseLogin =  userClient.login(UserCredentialsForLogin.from(user));

        int statusCodeCreate = responseCreate.extract().statusCode();
        boolean isCreated = responseCreate.extract().path("success");

        int statusCodeLogin = responseLogin.extract().statusCode();
        boolean isLogined = responseLogin.extract().path("success");
        token = responseLogin.extract().path("refreshToken");


        Assert.assertEquals(statusCodeCreate, 200);
        Assert.assertTrue(isCreated);

        Assert.assertEquals(statusCodeLogin, 200);
        Assert.assertTrue(isLogined);
    }

    @Test
    @DisplayName("403 Forbidden. User can not be created with existing credentials")
    public void userCanNotBeCreatedIfHeWasCreated()  {

        userClient.create(user);
        ValidatableResponse response = userClient.create(user);

        int statusCode = response.extract().statusCode();
        String messageError = response.extract().path("message");
        token = userClient.login(UserCredentialsForLogin.from(user)).extract().path("refreshToken");

        Assert.assertEquals(statusCode, 403);
        Assert.assertEquals(messageError, "User already exists");
    }

    @Test
    @DisplayName("403 Forbidden. User can not be logined with invalid credentials")
    public void userCanNotBeLoginedWithInvalidLoginAndPassword()  {

        ValidatableResponse responseCreate =  userClient.create(user);
        ValidatableResponse response = userClient.
                login(UserCredentialsForLogin.
                        getUserCredentialsWithInvalidEmailAndPassword(user));

        int statusCode = response.extract().statusCode();
        String messageError = response.extract().path("message");
        token = responseCreate.extract().path("refreshToken");

        Assert.assertEquals(statusCode, 401);
        Assert.assertEquals(messageError, "email or password are incorrect");
    }

    @Test
    @DisplayName("User can be updated with authorization")
    public void userCanUpdatedCredentialsWithAuthorization()  {

        ValidatableResponse response = userClient.create(user);

        authorization = response.extract().path("accessToken").toString().substring(7);
        token = response.extract().path("refreshToken");
        String name =  response.extract().jsonPath().getJsonObject("user.name");
        String email = response.extract().jsonPath().getJsonObject("user.email");
        String password = user.password;

        ValidatableResponse responseUpdate = userClient.
                updateWithAuthorization(UserCredentialsForUpdate.
                        getUserCredentialsWithNewCredentials(user,authorization));

        int statusCode = responseUpdate.extract().statusCode();
        boolean isSuccess = responseUpdate.extract().path("success");
        String nameBeforeUpdate =  responseUpdate.extract().jsonPath().getJsonObject("user.name");
        String emailBeforeUpdate = responseUpdate.extract().jsonPath().getJsonObject("user.email");
        String passwordBeforeUpdate = user.password;

        Assert.assertEquals(statusCode, 200);
        Assert.assertTrue(isSuccess);
        Assert.assertNotEquals(name, nameBeforeUpdate);
        Assert.assertNotEquals(email, emailBeforeUpdate);
        Assert.assertNotEquals(password, passwordBeforeUpdate);
    }

    @Test
    @DisplayName("401 Unauthorized. User can not be updated without authorization")
    public void userCanNotUpdatedCredentialsWithoutAuthorization()  {

        ValidatableResponse response = userClient.create(user);

        authorization = response.extract().path("accessToken").toString().substring(7);
        token = response.extract().path("refreshToken");

        ValidatableResponse responseUpdate = userClient.
                updateWithoutAuthorization(UserCredentialsForUpdate.
                        getUserCredentialsWithoutAuthorization(user));

        int statusCode = responseUpdate.extract().statusCode();
        String messageError = responseUpdate.extract().path("message");


        Assert.assertEquals(statusCode, 401);
        Assert.assertEquals(messageError, "You should be authorised");
    }
}
