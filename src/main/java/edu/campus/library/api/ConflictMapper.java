package edu.campus.library.api;

import edu.campus.library.service.ConflictException;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;
import java.util.Map;

@Provider
public class ConflictMapper implements ExceptionMapper<ConflictException> {

    @Override
    public Response toResponse(ConflictException exception) {

        var body = Map.of(
                "type", "about:blank",
                "title", "Conflict",
                "status", 409,
                "detail", exception.getMessage());

        return Response.status(409).type("application/problem+json").entity(body).build();

    }

}