package org.imgcapgen.gallery.messaging.processor;

import java.util.Date;
import java.util.Optional;

import org.eclipse.microprofile.reactive.messaging.Incoming;
import org.eclipse.microprofile.reactive.messaging.Message;
import org.imgcapgen.gallery.model.Image;
import org.infinispan.client.hotrod.RemoteCache;

import io.quarkus.infinispan.client.Remote;
import io.quarkus.runtime.Startup;
import io.smallrye.mutiny.Uni;
import io.smallrye.reactive.messaging.rabbitmq.IncomingRabbitMQMetadata;
import io.vertx.core.json.JsonObject;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import io.quarkus.logging.Log;

@ApplicationScoped
@Startup
public class ImageProcessor {

   @Inject
   @Remote("images")
   RemoteCache<String, Image> cache;

   @Incoming("images")
   public Uni<Image> process(Message<Object> message) {
      Optional<IncomingRabbitMQMetadata> metadata = message.getMetadata(IncomingRabbitMQMetadata.class);
      if (metadata.isEmpty()) {
         Log.warn("message metadata is empty");
      } else {
         Optional<String> contentType = metadata.get().getContentType();
         if (contentType.isEmpty()) {
            Log.warn("contentType is empty");
         } else {
            Log.infov("contentType is %s", contentType.get());
         }
      }

      Object payload = message.getPayload();
      JsonObject event;
      if (payload instanceof byte[]) {
         String json = new String((byte[]) payload);
         event = new JsonObject(json);
      } else if (payload instanceof String) {
         event = new JsonObject((String) payload);
      } else if (payload instanceof JsonObject) {
         event = (JsonObject) payload;
      } else {
         String errorMessage = "Unexpected payload type " + payload.getClass();
         Log.error(errorMessage);
         return Uni.createFrom().failure(new RuntimeException(errorMessage));
      }

      Image image = new Image(event.getString("user"), event.getString("file"), event.getString("caption"), new Date());
      return Uni.createFrom().future(cache.putAsync(image.key(), image));
   }
}
