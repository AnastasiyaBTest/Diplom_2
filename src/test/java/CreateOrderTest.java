import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.response.ValidatableResponse;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import site.nomoreparties.stellaburgers.*;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

public class CreateOrderTest {

    private Order order;
    private OrderClient orderClient;
    private String authorization;

    @Before
    public void setUp() {
        RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();
        orderClient = new OrderClient();
        UserClient userClient = new UserClient();
        User user = User.getRandom();
        userClient.create(user);
        authorization =  userClient.login(UserCredentialsForLogin.from(user)).extract().path("accessToken").toString().substring(7);
    }

    @Test
    @DisplayName("Order can be created with valid ingredients")
    public void orderCanBeCreatedWithIngredientsWithoutAuthorized() {

        ValidatableResponse responseGetInformationAboutIngredients = orderClient.getInformationAboutIngredients();
        List<String> listOfIngredients = responseGetInformationAboutIngredients.extract().jsonPath().getJsonObject("data._id");
        ValidatableResponse responseCreate =  orderClient.createWithoutAuthorized(new Order(new String[]{listOfIngredients.get(0),listOfIngredients.get(1)}));

        int statusCodeCreate = responseCreate.extract().statusCode();
        String name = responseCreate.extract().path("name");

        Assert.assertEquals(statusCodeCreate, 200);

        assertThat("Order is null", name, is(not(nullValue())));

    }

    @Test
    @DisplayName("Order can be created with valid ingredients")
    public void orderCanBeCreatedWithIngredientsWithAuthorized() {

        ValidatableResponse responseGetInformationAboutIngredients = orderClient.getInformationAboutIngredients();
        List<String> listOfIngredients = responseGetInformationAboutIngredients.extract().jsonPath().getJsonObject("data._id");
        ValidatableResponse responseCreate =  orderClient.createWithAuthorized(new Order(new String[]{listOfIngredients.get(0),listOfIngredients.get(1)}),authorization);

        int statusCodeCreate = responseCreate.extract().statusCode();
        String name = responseCreate.extract().path("name");

        Assert.assertEquals(statusCodeCreate, 200);

        assertThat("Order is null", name, is(not(nullValue())));

    }

    @Test
    @DisplayName("Order can not be created without ingredients")
    public void orderCanNotBeCreatedWithoutIngredients() {

        ValidatableResponse responseGetInformationAboutIngredients = orderClient.getInformationAboutIngredients();
        List<String> listOfIngredients = responseGetInformationAboutIngredients.extract().jsonPath().getJsonObject("data._id");
        ValidatableResponse responseCreate =  orderClient.createWithoutAuthorized(Order.getOrderWithoutIngredients());

        int statusCodeCreate = responseCreate.extract().statusCode();
        String message = responseCreate.extract().path("message");

        Assert.assertEquals(statusCodeCreate, 400);
        Assert.assertEquals(message, "Ingredient ids must be provided");

    }

    @Test
    @DisplayName("500 Internal Server Error. Order can not be created with nonexistent hash")
    public void orderCanNotBeCreatedWithNonexistentIngredients() {

        ValidatableResponse responseGetInformationAboutIngredients = orderClient.getInformationAboutIngredients();
        List<String> listOfIngredients = responseGetInformationAboutIngredients.extract().jsonPath().getJsonObject("data._id");
        ValidatableResponse responseCreate =  orderClient.createWithoutAuthorized(Order.getOrderWithDefaultHashIngredients());

        int statusCodeCreate = responseCreate.extract().statusCode();

        Assert.assertEquals(statusCodeCreate, 500);

    }

}
