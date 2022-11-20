package org.RestAssured.example;

import org.RestAssured.example.model.PlayerRq;
import org.RestAssured.example.model.PlayerRs;
import org.junit.jupiter.api.*;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

import io.restassured.response.Response;
import static io.restassured.RestAssured.*;
import static io.restassured.module.jsv.JsonSchemaValidator.*;
import static org.RestAssured.example.Specifications.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class RestAssuredTest {

    private static Properties testsProps;

    private static int userId;
    private static String userName;
    private static String userPassword;
    private static String accessToken;

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

    @AfterEach
    public void clearAfterTest() {
        clearSpecs();
    }

    @Test
    @Order(1)
    public void accessTokenRequest() {
        String username = testsProps.getProperty("username");
        String password = testsProps.getProperty("password");
        installSpecs(requestBeforeAuthSpec(username, password), responseSpec(200));

        Map<String, Object> tokenRqMap = Utils.readJSONFile("src/test/resources/TokenRq.json");

        Response response = given()
                        .body(tokenRqMap)
                        .post(testsProps.getProperty("tokenRq"))
                        .then().extract().response();

        accessToken = response.jsonPath().getString("access_token");
        Assertions.assertNotNull(accessToken);
    }

    @Test
    @Order(2)
    public void registerNewPlayer() {

        installSpecs(requestAuthSpec(accessToken), responseSpec(201));

        PlayerRq player = DataGenerator.randomPlayerRq();
        userName = player.getUsername();
        userPassword = player.getPassword_change();

        Response response = given()
                .body(player)
                .post(testsProps.getProperty("playerRq"))
                .then().extract().response();

        PlayerRs playerRs = response.as(PlayerRs.class);
        userId = Integer.parseInt(playerRs.getId());

        response.then().assertThat().
                body(matchesJsonSchemaInClasspath("playerRsSchema.json"));
    }

    @Test
    @Order(3)
    public void authAsNewPlayer() {

        String username = testsProps.getProperty("username");
        String password = testsProps.getProperty("password");
        installSpecs(requestBeforeAuthSpec(username, password), responseSpec(200));

        Map<String, Object> resourseOwner = Utils.readJSONFile("src/test/resources/resourseOwnerGrantRq.json");

        resourseOwner.put("username", userName);
        resourseOwner.put("password", userPassword);

        Response response = given()
                .body(resourseOwner)
                .post(testsProps.getProperty("tokenRq"))
                .then().extract().response();
        accessToken = response.jsonPath().getString("access_token");

        Assertions.assertNotNull(accessToken);
    }

    @Test
    @Order(4)
    public void getSinglePlayerProfile() {
        installSpecs(requestAuthSpec(accessToken), responseSpec(200));

        Response response = given()
                .get(testsProps.getProperty("playerRq") + "/" + userId)
                .then().extract().response();

        response.then().assertThat().
                body(matchesJsonSchemaInClasspath("playerRsSchema.json"));
    }

    @Test
    @Order(5)
    public void getAnotherPlayerProfile() {
        installSpecs(requestAuthSpec(accessToken), responseSpec(404));

        int randomNum = ThreadLocalRandom.current().nextInt(1, userId + 1);
        Response response = given(requestSpecification)
                .get(testsProps.getProperty("playerRq") + "/" + randomNum)
                .then().extract().response();
    }
}