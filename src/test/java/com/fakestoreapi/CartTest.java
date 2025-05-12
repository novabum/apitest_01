package com.fakestoreapi;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

import io.restassured.response.Response;


public class CartTest {

    @BeforeAll
    public static void setup() {
        RestAssured.baseURI = "https://fakestoreapi.com";
    }

    @Test
    public void getCarts() {
        Response response =
                given()
                        .when()
                        .get("/carts");

        response
                .then()
                .statusCode(200);

        response.getBody().prettyPrint();
    }

    @Test
    public void addNewCart() {
        int ID = 12;
        int productID = 21;
        Float price = 11.21F;

        String body = """
                {
                  "id": %d,
                  "userId": 0,
                  "products": [
                    {
                      "id": %d,
                      "title": "string",
                      "price": %s,
                      "description": "string",
                      "category": "string",
                      "image": "http://example.com"
                    }
                  ]
                }
                """.formatted(ID, productID, price);

        given()
                .contentType(ContentType.JSON)
                .body(body)
                .when()
                .post("/carts")
                .then()
                .statusCode(200);
    }

    @Test
    public void getSingleCart() {
        int cartID = 1;
        int expectedFirstProductID = 1;
        int expectedFirstProductQuantity = 4;

        Response response =
                given().when().get("/carts/" + cartID);

        response
                .then()
                .statusCode(200)
                .body("id", equalTo(cartID))
                .body("products[0].productId", equalTo(expectedFirstProductID))
                .body("products[0].quantity", equalTo(expectedFirstProductQuantity))
        ;
        response.prettyPrint();
    }

    @Test
    public void updateCart() {
        int cartID = 1;
        String body = """
                {
                  "id": 0,
                  "userId": 0,
                  "products": [
                    {
                      "id": 0,
                      "title": "string",
                      "price": 0.1,
                      "description": "string",
                      "category": "string",
                      "image": "http://example.com"
                    }
                  ]
                }
                """;

        given().contentType(ContentType.JSON).body(body).put("/carts/" + cartID).then().statusCode(200);
    }

    @Test
    public void deleteCart() {
        int cartID = 1;
        given().delete("/carts/" +cartID).then().statusCode(200);
    }
}
