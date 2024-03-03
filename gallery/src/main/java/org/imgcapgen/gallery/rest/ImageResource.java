package org.imgcapgen.gallery.rest;

import java.util.Date;
import java.util.List;

import org.imgcapgen.gallery.model.Image;
import org.infinispan.client.hotrod.RemoteCache;
import org.infinispan.client.hotrod.Search;
import org.infinispan.query.dsl.Query;
import org.infinispan.query.dsl.QueryFactory;

import io.quarkus.infinispan.client.Remote;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
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

   @GET
   @Path("/user/{user}")
   public List<Image> imagesByUser(@PathParam("user") String user, @QueryParam("offset") Integer offset,
                                   @QueryParam("limit") Integer limit) {
      QueryFactory queryFactory = Search.getQueryFactory(booksCache);
      Query<Image> query = queryFactory.create("from org.imgcapgen.image where username = :user order by moment desc");
      query.setParameter("user", user);
      if (offset != null) {
         query.startOffset(offset);
      }
      if (limit != null) {
         query.maxResults(limit);
      }
      return query.list();
   }

   @GET
   @Path("/caption/{caption}")
   public List<Image> imagesByCaption(@PathParam("caption") String caption, @QueryParam("offset") Integer offset,
                                   @QueryParam("limit") Integer limit) {
      QueryFactory queryFactory = Search.getQueryFactory(booksCache);
      Query<Image> query = queryFactory.create("from org.imgcapgen.image where caption : :caption order by moment desc");
      query.setParameter("caption", caption);
      if (offset != null) {
         query.startOffset(offset);
      }
      if (limit != null) {
         query.maxResults(limit);
      }
      return query.list();
   }

   @GET
   @Path("/from/{from}/to/{to}")
   public List<Image> imagesByMoment(@PathParam("from") Date from, @PathParam("to") Date to,
                                      @QueryParam("offset") Integer offset, @QueryParam("limit") Integer limit) {
      QueryFactory queryFactory = Search.getQueryFactory(booksCache);
      Query<Image> query = queryFactory.create("from org.imgcapgen.image where moment between :from and :to order by moment desc");
      query.setParameter("from", from);
      query.setParameter("to", to);
      if (offset != null) {
         query.startOffset(offset);
      }
      if (limit != null) {
         query.maxResults(limit);
      }
      return query.list();
   }
}
