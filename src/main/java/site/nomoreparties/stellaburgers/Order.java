package site.nomoreparties.stellaburgers;

import org.apache.commons.lang3.RandomStringUtils;

import java.util.ArrayList;
import java.util.List;

public class Order {

    public String[] ingredients;

    public Order(String[] ingredients){
        this.ingredients = ingredients;
    }

    public Order() {

    }

    public static Order getOrderWithoutIngredients(){
        Order order = new Order();
        order.ingredients = null;

        return order;
    }

    public static Order getOrderWithDefaultHashIngredients(){
        final String hash1 = RandomStringUtils.randomAlphabetic(10);
        final String hash2 = RandomStringUtils.randomAlphabetic(10);

        return new Order(new String[]{hash1,hash2});
    }


}
