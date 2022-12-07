package com._an_5.UNUS;

import com.fasterxml.jackson.core.io.JsonEOFException;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Before;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.HashMap;

import static io.restassured.RestAssured.when;
import static io.restassured.RestAssured.given;

@ExtendWith(SpringExtension.class)
//@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class UserTests {

    @LocalServerPort
    int port;

    @Before
    public void setUp() {
        RestAssured.port = port;
        RestAssured.baseURI = "http://localhost";
    }


    /* Tests signup and login and delete user */
    @Test
    public void createUserTest(){
        String requestBody = "{\n" +
                "  \"username\": \"(UserTests)user1\",\n" +
                "  \"password\": \"1234\" \n}";

        Response res = given()
                .header("Content-type", "application/json")
                .and()
                .body(requestBody)
                .when()
                .post("/signup")
                .then()
                .extract().response();

        Assertions.assertEquals("passed", res.jsonPath().getString("message"));

        res = given()
                .header("Content-type", "application/json")
                .and()
                .body(requestBody)
                .when()
                .post("/login")
                .then()
                .extract().response();

        HashMap<String, Object> json = res.jsonPath().getJsonObject("user");

        Assertions.assertEquals("passed", res.jsonPath().getString("verification"));
        Assertions.assertEquals("(UserTests)user1", json.get("username"));
//        Assertions.assertEquals(null, json.get("password")); // we are json ignoring the password

        int id = (int)json.get("id");

        res = given()
                .header("Content-type", "application/json")
                .when()
                .delete("/user/" + id)
                .then()
                .extract().response();

        Assertions.assertEquals("passed", res.jsonPath().getString("message"));

    }

    /*
    * this test is for updating a user
    * it tests signup, login, get user, update user, and delete user
    */
    @Test
    public void updateUserTest(){
        String requestBody = "{\n" +
                "  \"username\": \"(UserTests)user1\",\n" +
                "  \"password\": \"1234\" \n}";

        Response res = given()
                .header("Content-type", "application/json")
                .and()
                .body(requestBody)
                .when()
                .post("/signup")
                .then()
                .extract().response();

        Assertions.assertEquals("passed", res.jsonPath().getString("message"));

        res = given()
                .header("Content-type", "application/json")
                .and()
                .body(requestBody)
                .when()
                .post("/login")
                .then()
                .extract().response();

        HashMap<String, Object> json = res.jsonPath().getJsonObject("user");

        Assertions.assertEquals("passed", res.jsonPath().getString("verification"));
        Assertions.assertEquals("(UserTests)user1", json.get("username"));
//        Assertions.assertEquals(null, json.get("password")); // we are json ignoring the password

        int id = (int)json.get("id");

        res = given()
                .contentType(ContentType.JSON)
                .when()
                .get("/user/" + id)
                .then()
                .extract().response();

        json = res.getBody().as(new HashMap<String, Object>().getClass());

        json.replace("username", "(UserTests)user2");
        json.replace("gamesPlayed", 20);

        res = given()
                .header("Content-type", "application/json")
                .and()
                .body(json)
                .when()
                .put("/user/" + id)
                .then()
                .extract().response();

        Assertions.assertEquals("(UserTests)user2", res.jsonPath().getString("username"));
        Assertions.assertEquals(20, res.jsonPath().getInt("gamesPlayed"));


        res = given()
                .header("Content-type", "application/json")
                .when()
                .delete("/user/" + id)
                .then()
                .extract().response();

        Assertions.assertEquals("passed", res.jsonPath().getString("message"));

    }

    /*
     * Test for creating and deleting a guest user
     */
    @Test
    public void createGuestUsersTest(){

        Response res = given()
                .header("Content-type", "application/json")
                .when()
                .post("/create-guest-user")
                .then()
                .extract().response();

        Assertions.assertEquals("guest", res.jsonPath().getString("role"));
        int id = res.jsonPath().getInt("id");

        res = given()
                .header("Content-type", "application/json")
                .when()
                .delete("/user/" + id)
                .then()
                .extract().response();

        Assertions.assertEquals("passed", res.jsonPath().getString("message"));

    }



}
