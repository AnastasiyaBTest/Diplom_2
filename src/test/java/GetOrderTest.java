import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.response.ValidatableResponse;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import site.nomoreparties.stellaburgers.*;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;

public class GetOrderTest {

    private OrderClient orderClient;
    private UserClient userClient;
    private User user;
    private String authorization;

    @Before
    public void setUp() {
        RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();

        orderClient = new OrderClient();
        userClient = new UserClient();
        user = User.getRandom();
        userClient.create(user);
        authorization = userClient.login(UserCredentialsForLogin.from(user)).extract().path("accessToken").toString().substring(7);

        ValidatableResponse responseGetInformationAboutIngredients = orderClient.getInformationAboutIngredients();
        List<String> listOfIngredients = responseGetInformationAboutIngredients.extract().jsonPath().getJsonObject("data._id");
        ValidatableResponse responseCreateOrder =  orderClient.createWithAuthorized(new Order(new String[]{listOfIngredients.get(0),listOfIngredients.get(1)}),authorization);


    }

    @Test
    @DisplayName("401 Unauthorized. Order can not be getting without authorized")
    public void orderCanNotBeGetWithoutAuthorized() {

        ValidatableResponse response =  orderClient.getAllOrdersByUserWithoutAuthorized();

        int statusCodeCreate = response.extract().statusCode();
        String message = response.extract().path("message");

        Assert.assertEquals(statusCodeCreate, 401);
        Assert.assertEquals(message, "You should be authorised");

    }

    @Test
    @DisplayName("Order can be getting with authorized")
    public void orderCanBeGetWithAuthorized() {

        ValidatableResponse response =  orderClient.getAllOrdersByUserWithAuthorized(authorization);

        int statusCodeCreate = response.extract().statusCode();
        boolean isSuccess = response.extract().path("success");
        int countOrders = response.extract().path("totalToday");

        Assert.assertEquals(statusCodeCreate, 200);
        Assert.assertTrue(isSuccess);
        assertThat(countOrders, is(not(0)));


    }
}
