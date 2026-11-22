package edu.campus.library.api;

import jakarta.ws.rs.NotFoundException;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;
import java.util.Map;

@Provider
public class NotFoundMapper implements ExceptionMapper<NotFoundException> {

    @Override
    public Response toResponse(NotFoundException exception) {

        var body = Map.of(
                "type", "about:blank",
                "title", "Resource not found",
                "status", 404);

        return Response.status(404).type("application/problem+json").entity(body).build();

    }

}