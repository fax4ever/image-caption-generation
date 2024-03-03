package rest;

public class NewUser {

   public String name;
   public String pass;
   public Boolean pro;

   public NewUser() {
   }

   public NewUser(String name, String pass, Boolean pro) {
      this.name = name;
      this.pass = pass;
      this.pro = pro;
   }
}
