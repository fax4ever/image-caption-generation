package rest;

import org.infinispan.client.hotrod.RemoteCache;

import io.quarkus.infinispan.client.Remote;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
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

}
