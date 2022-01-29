import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import site.nomoreparties.stellaburgers.User;
import site.nomoreparties.stellaburgers.UserClient;

@RunWith(Parameterized.class)
public class CreateUserParameterizedTest {


    private final User user;
    private final int expectedCodeResult;
    private final String expectedMessage;

    public CreateUserParameterizedTest(User user, int expectedCodeResult, String expectedMessage) {
        this.user = user;
        this.expectedCodeResult = expectedCodeResult;
        this.expectedMessage = expectedMessage;
    }

    @Parameterized.Parameters
    public static Object[] getTestData() {
        return new Object[][] {
                {User.getUserWithoutEmail(), 403, "Email, password and name are required fields"},
                {User.getUserWithoutPassword(), 403, "Email, password and name are required fields"},
                {User.getUserWithoutName(), 403, "Email, password and name are required fields"},
        };
    }

    @Test
    @DisplayName("User can not be created with invalid data")
    public void createCourierWithInvalidData() {
        ValidatableResponse response = new UserClient().create(user);

        int actualCodeResult = response.extract().statusCode();
        String actualMessage = response.extract().path("message");

        Assert.assertEquals(actualCodeResult, expectedCodeResult);
        Assert.assertEquals(actualMessage, expectedMessage);

    }
}
