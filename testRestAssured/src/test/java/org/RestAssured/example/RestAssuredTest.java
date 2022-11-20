package org.RestAssured.example;

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
    private static PlayerRs playerRs;
    private static Response response;

    private static String accessToken;
    private static String userName;
    private static String userPassword;

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
        installSpecs(requestBeforeAuthSpec(username, password), responseSpecOk200());

        Map<String, Object> tokenRqMap = Utils.readJSONFile("src/test/resources/TokenRq.json");

        response = given()
                        .body(tokenRqMap)
                        .post(testsProps.getProperty("tokenRq"))
                        .then().extract().response();

        accessToken = response.jsonPath().getString("access_token");
        Assertions.assertNotNull(accessToken);
    }

    @Test
    @Order(2)
    public void registerNewPlayer() {

        installSpecs(requestAuthSpec(accessToken), responseSpecOk201());

        userName = UUID.randomUUID().toString();
        userPassword = Base64.getEncoder().encodeToString(userName.substring(0, 16).getBytes());
        PlayerRq player = PlayerRq.builder()
                .username(userName)
                .email(userName + "@gmail.com")
                .password_repeat(userPassword)
                .password_change(userPassword)
                .build();

        response = given()
                .body(player)
                .post(testsProps.getProperty("playerRq"))
                .then().extract().response();
        playerRs = response.as(PlayerRs.class);

        response.then().assertThat().
                body(matchesJsonSchemaInClasspath("playerRs.json"));
    }

    @Test
    @Order(3)
    public void authAsNewPlayer() {

        String username = testsProps.getProperty("username");
        String password = testsProps.getProperty("password");
        installSpecs(requestBeforeAuthSpec(username, password), responseSpecOk200());

        Map<String, Object> resourseOwner = Utils.readJSONFile("src/test/resources/resourseOwnerGrantRq.json");

        resourseOwner.put("username", userName);
        resourseOwner.put("password", userPassword);

        response = given()
                .body(resourseOwner)
                .post(testsProps.getProperty("tokenRq"))
                .then().extract().response();
        accessToken = response.jsonPath().getString("access_token");

        Assertions.assertNotNull(accessToken);
    }

    @Test
    @Order(4)
    public void getSinglePlayerProfile() {
        installSpecs(requestAuthSpec(accessToken), responseSpecOk200());

        response = given()
                .get(testsProps.getProperty("playerRq") + "/" + playerRs.getId())
                .then().extract().response();

        response.then().assertThat().
                body(matchesJsonSchemaInClasspath("playerRs.json"));
    }

    @Test
    @Order(5)
    public void getAnotherPlayerProfile() {
        installSpecs(requestAuthSpec(accessToken), responseSpecError404());

        int randomNum = ThreadLocalRandom.current().nextInt(1, Integer.parseInt(playerRs.getId()) + 1);
        response = given(requestSpecification)
                .get(testsProps.getProperty("playerRq") + "/" + randomNum)
                .then().extract().response();
    }
}