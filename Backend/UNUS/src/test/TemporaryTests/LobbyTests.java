package com._an_5.UNUS;


import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.junit.Before;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.HashMap;

import static io.restassured.RestAssured.given;


@ExtendWith(SpringExtension.class)
//@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class LobbyTests {


    HashMap<String, Object> user1;
    HashMap<String, Object> user2;

    @LocalServerPort
    int port;

    @Before
    public void setUp() {
        RestAssured.port = port;
        RestAssured.baseURI = "http://localhost";
    }


    private void createUsers(){
        String requestBody = "{\n" +
                "  \"username\": \"(LobbyTests)user1\",\n" +
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

        Assertions.assertEquals("passed", res.jsonPath().getString("verification"));
        user1 = res.jsonPath().getJsonObject("user");

        requestBody = "{\n" +
                "  \"username\": \"(LobbyTests)user2\",\n" +
                "  \"password\": \"1234\" \n}";

        res = given()
                .header("Content-type", "application/json")
                .and()
                .body(requestBody)
                .when()
                .post("/signup")
                .then()
                .extract().response();

        res = given()
                .header("Content-type", "application/json")
                .and()
                .body(requestBody)
                .when()
                .post("/login")
                .then()
                .extract().response();

        Assertions.assertEquals("passed", res.jsonPath().getString("verification"));
        user2 = res.jsonPath().getJsonObject("user");
    }

    private void deleteUsers(){
        Response res = given()
                .header("Content-type", "application/json")
                .when()
                .delete("/user/" + user1.get("id"))
                .then()
                .extract().response();

        Assertions.assertEquals("passed", res.jsonPath().getString("message"));

        res = given()
                .header("Content-type", "application/json")
                .when()
                .delete("/user/" + user2.get("id"))
                .then()
                .extract().response();

        Assertions.assertEquals("passed", res.jsonPath().getString("message"));
    }

    /*exhaustive test case to test creating a lobby, getting the lobbies id from a user, deleting a lobby, and checking if a lobby exists*/
    @Test
    public void createALobby(){

        createUsers();

        String requestBody = "{\n" +
                "  \"private\": \"false\" \n}";

        Response res = given()
                .header("Content-type", "application/json")
                .and()
                .body(requestBody)
                .when()
                .post("/lobbies/create-lobby?userId=" + user1.get("id"))
                .then()
                .extract().response();

        Assertions.assertEquals("success", res.jsonPath().getString("message"));


        res = given()
                .contentType(ContentType.JSON)
                .when()
                .get("/user/get-lobby/" + user1.get("id"))
                .then()
                .extract().response();

        int lobbyId = Integer.parseInt(res.jsonPath().getJsonObject("id"));

//        res = given()
//                .contentType(ContentType.TEXT)
//                .when()
//                .get("/lobbies/lobby-exits/" + lobbyId)
//                .then()
//                .extract().response();
//
//        Assertions.assertEquals(true, Boolean.parseBoolean(res.getBody().asString()));


        res = given()
                .header("Content-type", "application/json")
                .when()
                .delete("/lobbies/delete-lobby/" + lobbyId + "?userId=" + user1.get("id"))
                .then()
                .extract().response();

        Assertions.assertEquals("success", res.jsonPath().getString("message"));

        res = given()
                .contentType(ContentType.TEXT)
                .when()
                .get("/lobbies/lobby-exits/" + lobbyId)
                .then()
                .extract().response();

        Assertions.assertEquals(false, Boolean.parseBoolean(res.getBody().asString()));

        deleteUsers();
    }

}
