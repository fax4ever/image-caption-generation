package model;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.infinispan.api.annotations.indexing.Basic;
import org.infinispan.api.annotations.indexing.Indexed;
import org.infinispan.protostream.GeneratedSchema;
import org.infinispan.protostream.annotations.AutoProtoSchemaBuilder;
import org.infinispan.protostream.annotations.ProtoFactory;
import org.infinispan.protostream.annotations.ProtoField;
import org.infinispan.protostream.annotations.ProtoName;

@Indexed
@ProtoName("user")
public class User {

   private String username;
   private String encodedPassword;

   @ProtoFactory
   public User(String username, String encodedPassword) {
      this.username = username;
      this.encodedPassword = encodedPassword;
   }

   @Basic
   @ProtoField(value = 1, required = true)
   public String getUsername() {
      return username;
   }

   @ProtoField(value = 2, required = true)
   public String getEncodedPassword() {
      return encodedPassword;
   }

   public static String oneWayEncode(String password) throws NoSuchAlgorithmException {
      /* MessageDigest instance for hashing using SHA256 */
      MessageDigest md = MessageDigest.getInstance("SHA-256");
      /* digest() method called to calculate message digest of an input and return array of byte */
      return new String(md.digest(password.getBytes(StandardCharsets.UTF_8)));
   }

   @AutoProtoSchemaBuilder(includeClasses = {User.class},
         schemaPackageName = "org.imgcapgen",
         schemaFilePath = "proto",
         schemaFileName = "user-schema.proto")
   interface UserSchema extends GeneratedSchema {
   }
}
