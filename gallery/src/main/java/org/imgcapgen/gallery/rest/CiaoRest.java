package org.imgcapgen.gallery.rest;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

@Path("ciao")
public class CiaoRest {

   @GET
   @Path("{name}")
   @Produces(MediaType.TEXT_PLAIN)
   public String ciao(@PathParam("name") String name) {
      return "ciao " + name;
   }
}
