package site.nomoreparties.stellaburgers;

import com.github.javafaker.Faker;
import org.apache.commons.lang3.RandomStringUtils;

public class UserCredentialsForLogin {

    public final String email;
    public final String password;

    static Faker faker = new Faker();

    public UserCredentialsForLogin(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public static UserCredentialsForLogin from(User user) {
        return new UserCredentialsForLogin(user.email, user.password);
    }


    public static UserCredentialsForLogin getUserCredentialsWithInvalidEmailAndPassword(User user){
        user.setEmail(faker.internet().emailAddress());
        user.setPassword(faker.internet().password());
        return new UserCredentialsForLogin(user.email, user.password);
    }
}
