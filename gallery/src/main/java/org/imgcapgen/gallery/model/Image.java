package org.imgcapgen.gallery.model;

import java.util.Date;

import org.infinispan.api.annotations.indexing.Basic;
import org.infinispan.api.annotations.indexing.Indexed;
import org.infinispan.api.annotations.indexing.Text;
import org.infinispan.protostream.GeneratedSchema;
import org.infinispan.protostream.annotations.AutoProtoSchemaBuilder;
import org.infinispan.protostream.annotations.ProtoFactory;
import org.infinispan.protostream.annotations.ProtoField;
import org.infinispan.protostream.annotations.ProtoName;

@Indexed
@ProtoName("image")
public class Image {

   private String username;
   private String filename;
   private String caption;
   private Date moment;

   @ProtoFactory
   public Image(String username, String filename, String caption, Date moment) {
      this.username = username;
      this.filename = filename;
      this.caption = caption;
      this.moment = moment;
   }

   @Basic
   @ProtoField(value = 1, required = true)
   public String getUsername() {
      return username;
   }

   @ProtoField(value = 2, required = true)
   public String getFilename() {
      return filename;
   }

   @Text
   @ProtoField(value = 3, required = true)
   public String getCaption() {
      return caption;
   }

   @Basic
   @ProtoField(value = 4, required = true)
   public Date getMoment() {
      return moment;
   }

   public static String key(String username, String filename) {
      return username + "-" + filename;
   }

   public String key() {
      return key(username, filename);
   }

   @AutoProtoSchemaBuilder(includeClasses = {Image.class},
         schemaPackageName = "org.imgcapgen",
         schemaFilePath = "proto",
         schemaFileName = "image-schema.proto")
   interface ImageSchema extends GeneratedSchema {
   }
}
