package edu.campus.library;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.ws.rs.DefaultValue;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.MediaType;

@Path("/hello")
@ApplicationScoped
public class HelloResource {

    public record Greeting(String message) { }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Greeting hello(@QueryParam("name") @DefaultValue("world") String name) {
        return new Greeting("Hello, " + name);
    }
}
