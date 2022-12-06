package com._an_5.UNUS;


import com._an_5.UNUS.Users.User;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.Before;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.HashMap;

import static io.restassured.RestAssured.given;


@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
//@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class FriendTests {


    HashMap<String, Object> user1;
    HashMap<String, Object> user2;

    @LocalServerPort
    int port;

    @Before
    public void setUp() {
        RestAssured.port = port;
        RestAssured.baseURI = "http://localhost";
    }

    @Test
    void contextLoads() {
    }

    private void createUsers(){
        String requestBody = "{\n" +
                "  \"username\": \"(FriendTests)user1\",\n" +
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
                "  \"username\": \"(FriendTests)user2\",\n" +
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

    /*Test to send a friend request from user1 to user2; user2 will decline the friend requet*/
    @Test
    public void sendAndDeclineFriendRequest(){

        createUsers();

        Response res = given()
                .header("Content-type", "application/json")
                .and()
                .when()
                .post("/user/" + user1.get("id") + "/send-friend-request?friendId=" + user2.get("id"))
                .then()
                .extract().response();

        Assertions.assertEquals("passed", res.jsonPath().getString("message"));



        String requestBody = "{\n" +
                "  \"status\": \"declined\" \n}";


        res = given()
                .header("Content-type", "application/json")
                .and()
                .body(requestBody)
                .when()
                .put("/user/" + user2.get("id") + "/pending-friend-requests?friendId=" + user1.get("id"))
                .then()
                .extract().response();

        Assertions.assertEquals("declined", res.jsonPath().getString("message"));
        deleteUsers();
    }

    /*Test to send a friend request from user1 to user2, this time user2 will accept the request. user2 will then remove
     *user1 as a friend.
     */
    @Test
    public void AcceptFriendRequestAndRemoveFriend(){

        createUsers();

        Response res = given()
                .header("Content-type", "application/json")
                .and()
                .when()
                .post("/user/" + user1.get("id") + "/send-friend-request?friendId=" + user2.get("id"))
            .then()
                .extract().response();

        Assertions.assertEquals("passed", res.jsonPath().getString("message"));

    String requestBody = "{\n" +
            "  \"status\": \"accepted\" \n}";

    res = given()
                .header("Content-type", "application/json")
                .and()
                .body(requestBody)
                .when()
                .put("/user/" + user2.get("id") + "/pending-friend-requests?friendId=" + user1.get("id"))
            .then()
                .extract().response();

        Assertions.assertEquals("accepted", res.jsonPath().getString("message"));

    res = given()
                .header("Content-type", "application/json")
                .and()
                .when()
                .delete("/user/" + user2.get("id") + "/friends/remove-friend?friendId=" + user1.get("id"))
            .then()
                .extract().response();

        Assertions.assertEquals("passed", res.jsonPath().getString("message"));

    deleteUsers();

}

}
