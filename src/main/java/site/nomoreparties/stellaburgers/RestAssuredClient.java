package site.nomoreparties.stellaburgers;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class RestAssuredClient {
    public RequestSpecification getBaseSpec() {
        return new RequestSpecBuilder()
                .setContentType(ContentType.JSON)
                .setBaseUri("https://stellarburgers.nomoreparties.site/")
                .build();
    }
}
