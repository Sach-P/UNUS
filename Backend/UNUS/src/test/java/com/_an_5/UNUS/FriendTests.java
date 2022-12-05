package com._an_5.UNUS;


import com._an_5.UNUS.Users.User;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.Before;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;

import java.util.HashMap;

import static io.restassured.RestAssured.given;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class FriendTests {


    private User user1;
    private User user2;

    @LocalServerPort
    private int port;

    @Before
    public void setUp() throws Exception {
        RestAssured.port = port;
    }

//    @Test
//    public void addFreind(){
//        String requestBody = "{\n" +
//                "  \"username\": \"TestUser\",\n" +
//                "  \"password\": \"1234\" \n}";
//
//        Response res = given()
//                .header("Content-type", "application/json")
//                .and()
//                .body(requestBody)
//                .when()
//                .post("/signup")
//                .then()
//                .extract().response();
//
//        Assertions.assertEquals("passed", res.jsonPath().getString("message"));
//
//        res = given()
//                .header("Content-type", "application/json")
//                .and()
//                .body(requestBody)
//                .when()
//                .post("/login")
//                .then()
//                .extract().response();
//
//        HashMap<String, Object> json = res.jsonPath().getJsonObject("user");
//
//        Assertions.assertEquals("passed", res.jsonPath().getString("verification"));
//        Assertions.assertEquals("TestUser", json.get("username"));
//        Assertions.assertEquals(null, json.get("password")); // we are json ignoring the password
//
//        int id = (int)json.get("id");
//
//        res = given()
//                .header("Content-type", "application/json")
//                .when()
//                .delete("/user/" + id)
//                .then()
//                .extract().response();
//
//        Assertions.assertEquals("passed", res.jsonPath().getString("message"));
//
//    }

}
