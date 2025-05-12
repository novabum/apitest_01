package in.reques;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.lessThan;

public class RequesTest {

    @BeforeAll
    public static void setup() {
        RestAssured.baseURI = "https://reqres.in/api";
        requestSpecification = given()
                .header("x-api-key", "reqres-free-v1");
    }


    @Test
    public void listUsers() {
        given().get("/users?page=2").then().statusCode(200);
    }

    @Test
    public void getSingleUser() {
        int expectedID = 2;
        String expectedEmail = "janet.weaver@reqres.in";
        Response response =
                given().get("/users/2");
        response
                .then().statusCode(200)
                .body("data.id", equalTo(expectedID))
                .body("data.email", equalTo(expectedEmail))
        ;

        response.prettyPrint();
    }

    @Test
    public void singleUserNotFound() {
        given().get("/users/23").then().statusCode(404);
    }

    @Test
    public void listResource() {
        String expectedName = "fuchsia rose";

        Response response =
                given().get("/unknown");
        response
                .then().statusCode(200)
                .body("data[1].name", equalTo(expectedName));

        response.prettyPrint();
    }

    @Test
    public void singleResource() {
        String expectedColor = "#C74375";

        int resouceID = 2;
        Response response =
                given().get("/unknown/" + resouceID);
        response
                .then().statusCode(200)
                .body("data.color", equalTo(expectedColor))
        ;

        response.prettyPrint();
    }

    @Test
    public void singleResourceNotFound() {
        int resouceID = 23;

        given().get("/unknown/" + resouceID)
                .then().statusCode(404);
    }

    @Test
    public void create() {
        String name = "John Doe";
        String job = "excavator";

        String body = """
                {
                    "name": "%s",
                    "job": "%s"
                }
                """.formatted(name, job);
        Response response =
                given().body(body).post("/users");

        response
                .then().statusCode(201);

        response.prettyPrint();
    }

    @Test
    public void update() {
        int expectedStatus = 200;
        String name = "John Doe";
        String job = "excavator";
        String body = """
                {
                    "name": "%s",
                    "job": "%s"
                }
                """.formatted(name, job);
        Response response =
                given().body(body).put("/users/2");
        response
                .then().statusCode(expectedStatus);

        response.prettyPrint();
    }

    @Test
    public void updatePatch() {
        int expectedStatus = 200;
        String name = "Dill Doe";
        String job = "excavator";
        String body = """
                {
                    "name": "%s",
                    "job": "%s"
                }
                """.formatted(name, job);
        Response response =
                given().body(body).patch("/users/2");
        response
                .then().statusCode(expectedStatus);

        response.prettyPrint();
    }

    @Test
    public void delete() {
        int expectedStatus = 200;
        int userID = 2;

        given().get("/users/" + userID).then().statusCode(expectedStatus);
    }

    @Test
    public void registerSuccessful() {
        int expectedStatus = 200;
        String email = "eve.holt@reqres.in";
        String pass = "pistol";

        String body = """
                {
                    "email": "%s",
                    "password": "%s"
                }
                """.formatted(email, pass);

        Response response =
                given()
                        .contentType(ContentType.JSON)
                        .body(body)
                        .when()
                        .post("/register");
        response
                .then()
                .statusCode(expectedStatus);

        response.prettyPrint();
    }

    @Test
    public void registerUnsuccessful() {
        String email = "eve.holt@reqres.in";
        int expectedStatus = 400;

        String body = """
                {
                    "email": "%s",
                }
                """.formatted(email);

        Response response =
                given()
                        .baseUri("https://reqres.in/api")
                        .contentType("application/json")
                        .body(body)
//                        .when() //kihagyható a when? Mikor kötelező?
                        .post("/register");
        response
                .then()
//                .body("error", equalTo("Missing password"))
                .statusCode(expectedStatus)
        ;

        response.prettyPrint(); //HTLM-ben jön a response
    }

    @Test
    public void loginSuccessful() {
        int expectedStatus = 200;
        String email = "eve.holt@reqres.in";
        String pass = "cityslicka";

        String body = """
                {
                    "email": "%s",
                    "password": "%s"
                }
                """.formatted(email, pass);

        Response response =
                given().contentType(ContentType.JSON).body(body).post("/login");
        response
                .then().statusCode(expectedStatus);

        String token = response.jsonPath().getString("token");

        response.prettyPrint();
        System.out.println(token);
    }

    @Test
    public void loginUnsuccessful() {
        String email = "eve.holt@reqres.in";
        int expectedStatus = 400;

        String body = """
                {
                    "email": "%s",
                }
                """.formatted(email);

        given().contentType(ContentType.JSON).body(body).post("/login").then().statusCode(expectedStatus);
    }

    @Test
    public void delayedResponse() {
        int delay = 3;

        Response response =
                given().get("/users?delay=" + delay);
        response
                .then().statusCode(200).time(lessThan(5000L));

        System.out.println(response.time());

    }
}
