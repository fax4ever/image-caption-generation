package org.imgcapgen;

import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;

import org.imgcapgen.gallery.producer.NewImage;
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

    @Test
    void addImage() {
        given()
            .contentType(ContentType.JSON)
            .body(new NewImage("fabio", "city.png", "a large building with a clock in the sky"))
          .when()
            .post("new-image")
          .then()
            .statusCode(202);
    }
}
