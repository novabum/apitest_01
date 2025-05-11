package com.fakestoreapi;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

public class ProductsTest {
    @BeforeAll
    public static void setup() {
        RestAssured.baseURI = "https://fakestoreapi.com";
    }

    @Test
    public void getAllProducts() {
        given()
                .when()
                .get("/products")
                .then()
                .statusCode(200)
                .contentType(ContentType.JSON)
                .body("size()", greaterThan(0))
                .body("[0].id", notNullValue()) //Arrayben az első
                .body("[0].id", equalTo(1))
                .body("[1].id", equalTo(2))
                .body("[0].title", notNullValue())

        ;
    }

    @Test
    public void getSingleProduct() {
        int productID = 1;
        given()
                .when()
                .get("/products/" + productID)
                .then()
                .statusCode(200)
                .contentType(ContentType.JSON)
                .body("id", equalTo(productID))
                .body("title", equalTo("Fjallraven - Foldsack No. 1 Backpack, Fits 15 Laptops"));
    }

    @Test
    public void addProduct() {
        String title = "Test product 2";
        float price = 29.99F;
        String description = "Very Descriptive";

        String rawBody =
                """
                        {
                          "id": 112,
                          "title": "%s",
                          "price": %s,
                          "description": "%s",
                          "category": "Very category",
                          "image": "http://example.com"
                        }
                        """;

        String body = String.format(rawBody, title, price, description);

        given()
                .contentType(ContentType.JSON)
                .body(body).when().post("/products")
                .then()
                .statusCode(200)
                .body("id", notNullValue())
                .body("title", notNullValue())
                .body("title", equalTo("Test product 2"))
                .body("price", notNullValue())
                .body("price", equalTo(price))
                .body("description", equalTo(description));

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
