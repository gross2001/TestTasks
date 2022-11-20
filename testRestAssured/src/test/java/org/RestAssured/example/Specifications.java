package org.RestAssured.example;

import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;
import static io.restassured.RestAssured.requestSpecification;
import static io.restassured.RestAssured.responseSpecification;

public class Specifications {

    public static RequestSpecification requestBeforeAuthSpec(String username, String password) {
        return (new RequestSpecBuilder()
                .setContentType(ContentType.JSON)
                .build()
                .auth().preemptive().basic(username, password));
    }

    public static RequestSpecification requestAuthSpec(String accessToken) {
        return (new RequestSpecBuilder()
                .setContentType(ContentType.JSON)
                .build())
                .header("Accept", ContentType.JSON.getAcceptHeader())
                .auth().oauth2(accessToken);
    }

    public static ResponseSpecification responseSpecOk200() {
        return (new ResponseSpecBuilder()
                .expectStatusCode(200)
                .build());
    }

    public static ResponseSpecification responseSpecOk201() {
        return (new ResponseSpecBuilder()
                .expectStatusCode(201)
                .build());
    }

    public static ResponseSpecification responseSpecError404() {
        return (new ResponseSpecBuilder()
                .expectStatusCode(404)
                .build());
    }


    public static void installSpecs(RequestSpecification requestSpec, ResponseSpecification responseSpec) {
         requestSpecification = requestSpec;
         responseSpecification = responseSpec;
    }

    public static void clearSpecs() {
        requestSpecification = null;
        responseSpecification = null;
    }
}
