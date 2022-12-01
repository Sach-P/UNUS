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
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;

import java.util.HashMap;

import static io.restassured.RestAssured.when;
import static io.restassured.RestAssured.given;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class UserTests {

    @LocalServerPort
    private int port;

    @Before
    public void setUp() throws Exception {
        RestAssured.port = port;
    }


    //Tests signup and login and delete user
    @Test
    public void createUserTest(){
        String requestBody = "{\n" +
                "  \"username\": \"TestUser\",\n" +
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
        Assertions.assertEquals("TestUser", json.get("username"));
        Assertions.assertEquals(null, json.get("password")); // we are json ignoring the password

        int id = (int)json.get("id");

        res = given()
                .header("Content-type", "application/json")
                .when()
                .delete("/user/" + id)
                .then()
                .extract().response();

        Assertions.assertEquals("passed", res.jsonPath().getString("message"));

    }


    @Test
    public void updateUserTest(){
        String requestBody = "{\n" +
                "  \"username\": \"TestUser\",\n" +
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
        Assertions.assertEquals("TestUser", json.get("username"));
        Assertions.assertEquals(null, json.get("password")); // we are json ignoring the password

        int id = (int)json.get("id");

        requestBody = "{\n" +
                "  \"username\": \"TestUser2\",\n" +
                "  \"password\": \"1234\" \n +" +
                " \"gamesPlayed\": \"20\" \n}";

        res = given()
                .header("Content-type", "application/json")
                .and()
                .body(requestBody)
                .when()
                .post("/user/" + id)
                .then()
                .extract().response();

        Assertions.assertEquals("TestUser2", res.jsonPath().getString("username"));
        Assertions.assertEquals(20, res.jsonPath().getInt("gamesPlayed"));


        res = given()
                .header("Content-type", "application/json")
                .when()
                .delete("/user/" + id)
                .then()
                .extract().response();

        Assertions.assertEquals("passed", res.jsonPath().getString("message"));

    }




}
