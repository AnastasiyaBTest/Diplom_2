package site.nomoreparties.stellaburgers;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.github.javafaker.Faker;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserCredentialsForUpdate {

    public final String email;
    public final String password;
    public final String name;
    public final String authorization;
    static Faker faker = new Faker();

    public UserCredentialsForUpdate(String email, String password, String name, String authorization) {
        this.email = email;
        this.password = password;
        this.name = name;
        this.authorization = authorization;
    }

    public static UserCredentialsForUpdate from(User user,String authorization) {
        return new UserCredentialsForUpdate(user.email, user.password, user.name, authorization);
    }

    public static UserCredentialsForUpdate getUserCredentialsWithNewCredentials(User user,String authorization){
        user.setEmail(faker.internet().emailAddress());
        user.setPassword(faker.internet().password());
        user.setName(faker.name().firstName());
        return new UserCredentialsForUpdate(user.email, user.password, user.name, authorization);
    }

    public static UserCredentialsForUpdate getUserCredentialsWithoutAuthorization(User user){
        user.setEmail(faker.internet().emailAddress());
        user.setPassword(faker.internet().password());
        user.setName(faker.name().firstName());
        return new UserCredentialsForUpdate(user.email, user.password, user.name, null);
    }
}
