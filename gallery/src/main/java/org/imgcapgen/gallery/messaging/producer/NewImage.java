package org.imgcapgen.gallery.messaging.producer;

import jakarta.validation.constraints.NotBlank;

public class NewImage {

   @NotBlank(message="user may not be blank")
   public String user;

   @NotBlank(message="file may not be blank")
   public String file;

   @NotBlank(message="caption may not be blank")
   public String caption;

   public NewImage() {
   }

   public NewImage(String user, String file, String caption) {
      this.user = user;
      this.file = file;
      this.caption = caption;
   }
}
