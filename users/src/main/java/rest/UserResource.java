package rest;

import java.security.NoSuchAlgorithmException;

import org.infinispan.client.hotrod.RemoteCache;
import org.jboss.resteasy.reactive.RestResponse;

import io.quarkus.infinispan.client.Remote;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import model.User;

@Path("user")
public class UserResource {

   @Inject
   @Remote("users")
   RemoteCache<String, User> cache;

   @GET
   @Path("cache")
   @Produces(MediaType.TEXT_PLAIN)
   public String cacheName() {
      return cache.getName();
   }

   @POST
   @Consumes(MediaType.APPLICATION_JSON)
   @Produces(MediaType.TEXT_PLAIN)
   public RestResponse<Void> createUser(@Valid NewUser event) throws NoSuchAlgorithmException {
      User user = new User(event.name, User.oneWayEncode(event.pass), event.pro);
      cache.put(user.getUsername(), user);
      return RestResponse.accepted();
   }

   @POST
   @Consumes(MediaType.APPLICATION_JSON)
   @Produces(MediaType.APPLICATION_JSON)
   @Path("validate")
   public RestResponse<ValidatePassword> validatePassword(@Valid ValidatePassword validatePassword)
         throws NoSuchAlgorithmException {
      User user = cache.get(validatePassword.username);
      if (user == null) {
         validatePassword.valid = false;
         return RestResponse.ok(validatePassword);
      }
      if (!User.oneWayEncode(validatePassword.password).equals(user.getEncodedPassword())) {
         validatePassword.valid = false;
         return RestResponse.ok(validatePassword);
      }
      validatePassword.valid = true;
      validatePassword.proUser = user.getProUser();
      return RestResponse.ok(validatePassword);
   }
}
