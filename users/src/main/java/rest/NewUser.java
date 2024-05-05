package rest;

import jakarta.validation.constraints.NotBlank;

public class NewUser {

   @NotBlank(message="name may not be blank")
   public String userName;

   @NotBlank(message="pass may not be blank")
   public String password;

   public boolean pro;

   public String fullName;

   public String email;

   public NewUser() {
   }

   public NewUser(String userName, String password, boolean pro, String fullName, String email) {
      this.userName = userName;
      this.password = password;
      this.pro = pro;
      this.fullName = fullName;
      this.email = email;
   }
}
