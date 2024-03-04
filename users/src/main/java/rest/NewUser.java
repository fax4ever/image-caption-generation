package rest;

import jakarta.validation.constraints.NotBlank;

public class NewUser {

   @NotBlank(message="name may not be blank")
   public String name;

   @NotBlank(message="pass may not be blank")
   public String pass;

   public boolean pro;

   public NewUser() {
   }

   public NewUser(String name, String pass, boolean pro) {
      this.name = name;
      this.pass = pass;
      this.pro = pro;
   }
}
