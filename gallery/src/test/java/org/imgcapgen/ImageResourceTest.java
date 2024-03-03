package org.imgcapgen;

import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;

import org.eclipse.microprofile.reactive.messaging.Channel;
import org.eclipse.microprofile.reactive.messaging.Emitter;
import org.imgcapgen.gallery.messaging.producer.NewImage;
import org.imgcapgen.gallery.model.Image;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;

import java.util.List;

@QuarkusTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class ImageResourceTest {

    @Channel("new-images")
    Emitter<NewImage> imageEmitter;

    @BeforeAll
    void beforeAll() {
        imageEmitter.send(new NewImage("fax", "ciao.jpg", "a cat sitting on the sofa"));
        imageEmitter.send(new NewImage("bla", "ciao.jpg", "a dog sitting on the sofa"));
        imageEmitter.send(new NewImage("fax", "hello.jpg", "a living room with a white wall and a dog"));
        imageEmitter.send(new NewImage("bla", "hello.jpg", "a living room with a white wall and a cat"));
    }

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
            .post("/new-image")
          .then()
            .statusCode(202);
    }

    @Test
    void imagesByUser() {
        List<Image> images = given()
              .when()
              .get("/image/user/fax")
              .then()
              .statusCode(200)
              .extract()
              .body()
              .jsonPath().getList(".", Image.class);

        assertThat(images, notNullValue());
    }

    @Test
    void imagesByCaption() {
        List<Image> images = given()
              .when()
              .get("/image/caption/cat")
              .then()
              .statusCode(200)
              .extract()
              .body()
              .jsonPath().getList(".", Image.class);

        assertThat(images, notNullValue());
    }
}
