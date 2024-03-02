package org.imgcapgen.gallery.messaging;

import java.util.Date;

import org.eclipse.microprofile.reactive.messaging.Incoming;
import org.eclipse.microprofile.reactive.messaging.Message;
import org.imgcapgen.gallery.model.Image;
import org.infinispan.client.hotrod.RemoteCache;

import io.quarkus.infinispan.client.Remote;
import io.smallrye.mutiny.Uni;
import io.vertx.core.json.JsonObject;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class ImageProcessor {

   @Inject
   @Remote("images")
   RemoteCache<String, Image> cache;

   @Incoming("images")
   public Uni<Image> process(Message<JsonObject> message) {
      JsonObject event = message.getPayload();
      Image image = new Image(event.getString("user"), event.getString("file"), event.getString("caption"), new Date());
      return Uni.createFrom()
            .future(cache.putAsync(image.key(), image));
   }
}
