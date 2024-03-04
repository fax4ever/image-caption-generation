package rest;

import jakarta.validation.constraints.NotBlank;

public class ValidatePassword {

   @NotBlank(message="username may not be blank")
   public String username;

   @NotBlank(message="password may not be blank")
   public String password;

   public Boolean valid;
   public Boolean proUser;

   public ValidatePassword() {
   }

   public ValidatePassword(String username, String password) {
      this.username = username;
      this.password = password;
   }
}
