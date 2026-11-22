package edu.campus.library.api;

import jakarta.ws.rs.BadRequestException;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;
import java.util.Map;

@Provider
public class BadRequestMapper implements ExceptionMapper<BadRequestException> {

    @Override
    public Response toResponse(BadRequestException exception) {

        var body = Map.of(
                "type", "about:blank",
                "title", "Bad request",
                "status", 400);

        return Response.status(400).type("application/problem+json").entity(body).build();
    }

}