package org.RestAssured.example;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.TestMethodOrder;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;

import io.restassured.http.ContentType;
import io.restassured.response.Response;
import static io.restassured.RestAssured.*;
import static io.restassured.module.jsv.JsonSchemaValidator.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class RestAssuredTest {

    private static Properties testsProps;
    private static PlayerRq playerRq;
    private static PlayerRs playerRs;
    private static String accessToken;
    private static Response response;

    @BeforeAll
    public static void readProperties() {
        testsProps = new Properties();
        try (InputStream input = new FileInputStream("src/test/resources/test.properties")) {

            testsProps.load(input);
        }  catch (IOException ex) {
            ex.printStackTrace();
        }

        baseURI = testsProps.getProperty("baseURI");
    }

    @Test
    @Order(1)
    public void accessTokenRequest() {
        Map<String, Object> tokenRqMap = Utils.readJSONFile("src/test/resources/TokenRq.json");
        response = given()
                .contentType(ContentType.JSON)
                .auth().preemptive().basic(testsProps.getProperty("username"), testsProps.getProperty("password"))
                        .body(tokenRqMap)
                        .post("/v2/oauth2/token")
                        .then().extract().response();

        accessToken = response.jsonPath().getString("access_token");
        Assertions.assertEquals(200, response.statusCode());
        Assertions.assertNotNull(accessToken);
    }

    @Test
    @Order(2)
    public void registerNewPlayer() {

        playerRq = Utils.readJSONFileAsPlayer("src/test/resources/playerRq.json");

        String randomUUID = UUID.randomUUID().toString();
        playerRq.setUsername(randomUUID);
        playerRq.setEmail(randomUUID + "@gmail.com");

        requestSpecification = given().auth().oauth2(accessToken)
                .header("Accept", ContentType.JSON.getAcceptHeader())
                .contentType(ContentType.JSON);

        response = given(requestSpecification)
                .body(playerRq)
                .post("/v2/players")
                .then().extract().response();
        playerRs = response.as(PlayerRs.class);

        Assertions.assertEquals(201, response.statusCode());
        response.then().assertThat().
                body(matchesJsonSchemaInClasspath("playerRs.json"));
    }

    @Test
    @Order(3)
    public void authAsNewPlayer() {

        Map<String, Object> resourseOwner = Utils.readJSONFile("src/test/resources/resourseOwnerGrantRq.json");

        resourseOwner.put("username", playerRq.getUsername());
        resourseOwner.put("password", playerRq.getPassword_change());
        response = given()
                .contentType(ContentType.JSON)
                .auth().preemptive().basic(testsProps.getProperty("username"), testsProps.getProperty("password"))
                .body(resourseOwner)
                .post("/v2/oauth2/token")
                .then().extract().response();
        accessToken = response.jsonPath().getString("access_token");

        Assertions.assertEquals(200, response.statusCode());
        Assertions.assertNotNull(accessToken);
    }

    @Test
    @Order(4)
    public void getSinglePlayerProfile() {
        requestSpecification = given().auth().oauth2(accessToken)
                .header("Accept", ContentType.JSON.getAcceptHeader())
                .contentType(ContentType.JSON);

        response = given(requestSpecification)
                .get("/v2/players/" + playerRs.getId())
                .then().extract().response();

        Assertions.assertEquals(200, response.statusCode());
        response.then().assertThat().
                body(matchesJsonSchemaInClasspath("playerRs.json"));
    }

    @Test
    @Order(5)
    public void getAnotherPlayerProfile() {
        requestSpecification = given().auth().oauth2(accessToken)
                .header("Accept", ContentType.JSON.getAcceptHeader())
                .contentType(ContentType.JSON);

        response = given(requestSpecification)
                .get("/v2/players/" + "100")
                .then().extract().response();

        Assertions.assertEquals(404, response.statusCode());
    }
}