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
public class TeamTests {


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
                "  \"username\": \"(TeamTests)user1\",\n" +
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
                "  \"username\": \"(TeamTests)user2\",\n" +
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

    /*Exhaustive test to check creating a team, joining a team, leaving a team, and deleting a team*/
    @Test
    public void createATeam(){

        createUsers();

        String requestBody =  "{\n" +
                "  \"teamName\": \"(TeamTests)team1\",\n" +
                "  \"isPrivate\": \"false\" \n}";

        Response res = given()
                .header("Content-type", "application/json")
                .and()
                .body(requestBody)
                .when()
                .post("/teams/create-team?userId=" + user1.get("id"))
                .then()
                .extract().response();

        Assertions.assertEquals("success", res.jsonPath().getString("message"));


        res = given()
                .contentType(ContentType.JSON)
                .when()
                .get("/user/get-team/" + user1.get("id"))
                .then()
                .extract().response();


        int teamId = Integer.parseInt(res.jsonPath().getJsonObject("id"));

        res = given()
                .header("Content-type", "application/json")
                .when()
                .put("/teams/join-team/" + teamId + "?userId=" + user2.get("id"))
                .then()
                .extract().response();

        Assertions.assertEquals("success", res.jsonPath().getString("message"));


        requestBody =  "{\n" +
                "  \"win\": \"true\" \n}";

        res = given()
                .header("Content-type", "application/json")
                .and()
                .body(requestBody)
                .when()
                .put("/gameEnd/" + user2.get("id"))
                .then()
                .extract().response();

        Assertions.assertEquals("success", res.jsonPath().getString("message"));

        res = given()
                .contentType(ContentType.JSON)
                .when()
                .get("/user/" + user2.get("id"))
                .then()
                .extract().response();

        Assertions.assertEquals(1, res.jsonPath().getInt("gamesPlayed"));
        Assertions.assertEquals(1, res.jsonPath().getInt("gamesWon"));

        res = given()
                .contentType(ContentType.JSON)
                .when()
                .get("/teams/" + teamId)
                .then()
                .extract().response();

        Assertions.assertEquals(1, res.jsonPath().getInt("wins"));


        res = given()
                .header("Content-type", "application/json")
                .when()
                .put("/teams/leave-team/" + teamId + "?userId=" + user2.get("id"))
                .then()
                .extract().response();

        Assertions.assertEquals("success", res.jsonPath().getString("message"));
        res = given()
                .header("Content-type", "application/json")
                .when()
                .delete("/teams/delete-team/" + teamId + "?userId=" + user1.get("id"))
                .then()
                .extract().response();

        Assertions.assertEquals("success", res.jsonPath().getString("message"));


        deleteUsers();
    }

}
