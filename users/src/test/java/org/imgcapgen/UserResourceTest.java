package org.imgcapgen;

import org.junit.jupiter.api.Test;

import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import rest.NewUser;
import rest.ValidatePassword;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.CoreMatchers.is;

@QuarkusTest
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

   @Test
   void addImage() {
      String body = "{\"name\":\"mark\", \"pass\":\"mark\", \"pro\":true}";
      given()
            .contentType(ContentType.JSON)
            .body(body)
            .when()
            .post("/user")
            .then()
            .statusCode(202);
   }

   @Test
   void validatePassword() {
      NewUser bob = new NewUser("bob", "blablabla", false);
      NewUser joe = new NewUser("joe", "easypass", true);
      NewUser[] users = {bob, joe};

      for (NewUser user : users) {
         given()
               .contentType(ContentType.JSON)
               .body(user)
               .when()
               .post("/user")
               .then()
               .statusCode(202);
      }

      ValidatePassword validatePassword = given()
            .contentType(ContentType.JSON)
            .body(new ValidatePassword("not-exising", "not-existing"))
            .when()
            .post("/user/validate")
            .then()
            .extract()
            .body()
            .jsonPath().getObject(".", ValidatePassword.class);

      assertThat(validatePassword.valid).isFalse();

      validatePassword = given()
            .contentType(ContentType.JSON)
            .body(new ValidatePassword("bob", "wrong"))
            .when()
            .post("/user/validate")
            .then()
            .extract()
            .body()
            .jsonPath().getObject(".", ValidatePassword.class);

      assertThat(validatePassword.valid).isFalse();

      validatePassword = given()
            .contentType(ContentType.JSON)
            .body(new ValidatePassword("bob", "blablabla"))
            .when()
            .post("/user/validate")
            .then()
            .extract()
            .body()
            .jsonPath().getObject(".", ValidatePassword.class);

      assertThat(validatePassword.valid).isTrue();
      assertThat(validatePassword.proUser).isFalse();

      validatePassword = given()
            .contentType(ContentType.JSON)
            .body(new ValidatePassword("joe", "easypass"))
            .when()
            .post("/user/validate")
            .then()
            .extract()
            .body()
            .jsonPath().getObject(".", ValidatePassword.class);

      assertThat(validatePassword.valid).isTrue();
      assertThat(validatePassword.proUser).isTrue();
   }
}
