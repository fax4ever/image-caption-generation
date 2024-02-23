package org.imgcapgen;

import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;

@QuarkusTest
class CiaoRestTest {
    @Test
    void testHelloEndpoint() {
        given()
          .when().get("/ciao/blablabla")
          .then()
             .statusCode(200)
             .body(is("ciao blablabla"));
    }

}