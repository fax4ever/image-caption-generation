package org.imgcapgen.gallery.messaging.producer;

import org.eclipse.microprofile.reactive.messaging.Channel;
import org.eclipse.microprofile.reactive.messaging.Emitter;
import org.jboss.resteasy.reactive.RestResponse;

import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

@Path("new-image")
public class NewImageResource {

   @Channel("new-images")
   Emitter<NewImage> imageEmitter;

   @POST
   @Consumes(MediaType.APPLICATION_JSON)
   @Produces(MediaType.TEXT_PLAIN)
   public RestResponse<Void> newImage(NewImage event) {
      imageEmitter.send(event);
      return RestResponse.accepted();
   }
}
