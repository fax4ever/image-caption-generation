package org.imgcapgen;

import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;

@QuarkusTest
class ImageResourceTest {

    @Test
    void cacheName() {
        given()
          .when()
              .get("/image/cache")
          .then()
             .statusCode(200)
             .body(is("images"));
    }
}
