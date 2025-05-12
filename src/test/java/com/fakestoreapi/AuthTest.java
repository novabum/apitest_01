package com.fakestoreapi;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.notNullValue;

public class AuthTest {

    @BeforeAll
    public static void setup() {
        RestAssured.baseURI = "https://fakestoreapi.com";
    }

    @Test
    public void testAuth() {
        String credentialsJson = """
                {
                "username": "mor_2314",
                "password": "83r5^_"
                }
                """;
        Response response = given()
                .contentType(ContentType.JSON)
                .body(credentialsJson)
                .when()
                .post("/auth/login")
                .then()
                .statusCode(200)
                .body("token", notNullValue())
                .extract().response();

        String token = response.jsonPath().getString("token");
        System.out.println(token);

        given()
                .header("Authorization", "Bearer" + token)
                .get("/carts").then().statusCode(200).body("size()", greaterThan(0));
    }

}
