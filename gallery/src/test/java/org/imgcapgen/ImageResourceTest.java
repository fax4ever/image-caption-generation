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
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.CoreMatchers.is;

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
              .get("/gallery/image/cache")
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
            .post("/gallery/new-image")
          .then()
            .statusCode(202);
    }

    @Test
    void imagesByUser() {
        List<Image> images = given()
              .when()
              .get("/gallery/image/user/fax")
              .then()
              .statusCode(200)
              .extract()
              .body()
              .jsonPath().getList(".", Image.class);

        assertThat(images).extracting("caption")
              .containsExactlyInAnyOrder("a living room with a white wall and a dog", "a cat sitting on the sofa");
    }

    @Test
    void imagesByCaption() {
        List<Image> images = given()
              .when()
              .get("/gallery/image/caption/cat")
              .then()
              .statusCode(200)
              .extract()
              .body()
              .jsonPath().getList(".", Image.class);

        assertThat(images).extracting("caption")
              .containsExactlyInAnyOrder("a living room with a white wall and a cat", "a cat sitting on the sofa");
    }

    @Test
    void imagesByMoment() {
        String from = "2024-03-03T13:11:22.908+00:00";
        String to = "2124-03-03T13:11:22.908+00:00";

        List<Image> images = given()
              .when()
              .get("/gallery/image/from/" + from + "/to/" + to)
              .then()
              .statusCode(200)
              .extract()
              .body()
              .jsonPath().getList(".", Image.class);

        assertThat(images).isNotEmpty();
    }
}
