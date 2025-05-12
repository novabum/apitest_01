package com.fakestoreapi;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static io.restassured.RestAssured.proxy;
import static org.hamcrest.Matchers.*;


public class UserTest {

    @BeforeAll
    public static void setup() {
        RestAssured.baseURI = "https://fakestoreapi.com";
    }

    @Test
    public void getAllUsers() {
        String fourthUserExpectedPhone = "1-765-789-6734";

        Response response =
                given().get("/users");
        response
                .then().statusCode(200)
                .body("[3].phone", equalTo(fourthUserExpectedPhone));

        response.prettyPrint();
    }

    @Test
    public void getSingleUser() {
        int userID = 1;
        String expectedCity = "kilcoole";
        String expectedEmail = "john@gmail.com";
        String expectedFirstName = "john";
        String expectedLastName = "doe";
        String expectedPhone = "1-570-236-7033";

        Response response =
                given().get("/users/" + userID);
        response
                .then().statusCode(200)
                .body("address.city", equalTo(expectedCity))
                .body("name.firstname", equalTo(expectedFirstName))
                .body("name.lastname", equalTo(expectedLastName))
                .body("email", equalTo(expectedEmail))
                .body("phone", equalTo(expectedPhone))
        ;

        response.prettyPrint();
    }

    @Test
    public void addNewUser() {
        int userID = 7;
        String username = "Fuzzy";
        String email = "light@year.hu";
        String password = "secret11";

        String body = """
                {
                  "id": %d,
                  "username": "%s",
                  "email": "%s",
                  "password": "%s"
                }
                """.formatted(userID, username, email, password);

        given()
                .contentType(ContentType.JSON)
                .body(body)
                .post("/users")
                .then().statusCode(200);
    }

    @Test
    public void updateUser(){
        int userID = 1;
        String username = "Lazy";
        String email = "test@oftheyear.hu";
        String password = "secret123";

        String body = """
                {
                  "id": %d,
                  "username": "%s",
                  "email": "%s",
                  "password": "%s"
                }
                """.formatted(userID, username, email, password);

        given().put("/users/" + userID)
                .then().statusCode(200);
    }

    @Test
    public void deleteUser(){
        int userID = 1;
        given().delete("/users/" + userID).then().statusCode(200);
    }

}
