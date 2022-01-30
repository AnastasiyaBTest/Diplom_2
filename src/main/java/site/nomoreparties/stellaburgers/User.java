package site.nomoreparties.stellaburgers;

import com.github.javafaker.Faker;
import org.apache.commons.lang3.RandomStringUtils;

public class User {

    public String name;
    public String password;
    public String email;


    static Faker faker = new Faker();

    public User(String name, String password, String email){
        this.name = name;
        this.password = password;
        this.email = email;
    }

    public User(){}

    public User setName(String name){
        this.name = name;
        return this;
    }


    public User setPassword(String password){
        this.password = password;
        return this;
    }

    public User setEmail(String email){
        this.email = email;
        return this;
    }

    public static User getRandom(){

        final String name = faker.name().firstName();
        final String password = faker.internet().password();
        final String email = faker.internet().emailAddress();
        return new User(name, password, email);
    }

    public static User getUserWithoutEmail(){
        return new User().
                setName(faker.name().firstName()).
                setPassword(faker.internet().password());
    }

    public static User getUserWithoutName(){
        return new User().
                setEmail(faker.internet().emailAddress()).
                setPassword(faker.internet().password());
    }

    public static User getUserWithoutPassword(){
        return new User().
                setName(faker.name().firstName()).
                setEmail(faker.internet().emailAddress());
    }




}
