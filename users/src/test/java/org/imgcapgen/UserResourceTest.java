package org.imgcapgen;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import io.quarkus.test.junit.QuarkusTest;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;

@QuarkusTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class UserResourceTest {

   @Test
   void cacheName() {
      given()
            .when()
            .get("/user/cache")
            .then()
            .statusCode(200)
            .body(is("users"));
   }

}
