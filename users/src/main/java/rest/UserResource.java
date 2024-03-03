package rest;

import java.security.NoSuchAlgorithmException;

import org.infinispan.client.hotrod.RemoteCache;
import org.jboss.resteasy.reactive.RestResponse;

import io.quarkus.infinispan.client.Remote;
import jakarta.inject.Inject;
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
   public RestResponse<Void> createUser(NewUser event) throws NoSuchAlgorithmException {
      User user = new User(event.name, User.oneWayEncode(event.pass), event.pro);
      cache.put(user.getUsername(), user);
      return RestResponse.accepted();
   }
}
