package org.imgcapgen.gallery.rest;

import org.imgcapgen.gallery.model.Image;
import org.infinispan.client.hotrod.RemoteCache;

import io.quarkus.infinispan.client.Remote;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

@Path("image")
public class ImageResource {

   @Inject
   @Remote("images")
   RemoteCache<String, Image> booksCache;

   @GET
   @Path("cache")
   @Produces(MediaType.TEXT_PLAIN)
   public String cacheName() {
      return booksCache.getName();
   }
}
