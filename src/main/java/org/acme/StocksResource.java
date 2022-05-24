package org.acme;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import io.smallrye.mutiny.Multi;
import io.vertx.core.json.JsonObject;
import org.eclipse.microprofile.reactive.messaging.Channel;

@Path("/stocks")
public class StocksResource {

    @Inject
    @Channel("stocks")
    Multi<String> stocks;

    @GET
    @Path("/stream")
    @Produces(MediaType.SERVER_SENT_EVENTS) // denotes that server side events (SSE) will be produced
    //@SseElementType("text/plain") // denotes that the contained data, within this SSE, is just regular text/plain data
    public Multi<String> stream() {
        return stocks.map(s -> {
            JsonObject json = new JsonObject(s);
            return json.getString("stock") + " : " + json.getDouble("price");
        });
    }
}
