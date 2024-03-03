package org.imgcapgen.gallery.messaging.producer;

public class NewImage {

   public String user;
   public String file;
   public String caption;

   public NewImage() {
   }

   public NewImage(String user, String file, String caption) {
      this.user = user;
      this.file = file;
      this.caption = caption;
   }
}
