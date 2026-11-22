package edu.campus.library.api;

import edu.campus.library.domain.Book;
import edu.campus.library.domain.NewBook;
import edu.campus.library.service.BookService;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.UriInfo;
import java.net.URI;
import java.util.List;

@Path("/books")
@ApplicationScoped
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class BookResource {
    @Inject
    private BookService service;

    @GET
    public List<Book> list() {
        return service.findAll();
    }

    @GET
    @Path("/{id}")
    public Book get(@PathParam("id") long id) {
        return service.find(id);
    }

    @POST
    public Response create(@Valid NewBook request, @Context UriInfo uri) {
        Book created = service.create(request);
        URI location = uri.getAbsolutePathBuilder().path(String.valueOf(created.id())).build();

        return Response.created(location).entity(created).build();
    }

    @PUT
    @Path("/{id}")
    public Book update(@PathParam("id") long id, @Valid NewBook request) {
        return service.update(id, request);
    }

    @DELETE
    @Path("/{id}")
    public Response delete(@PathParam("id") long id) {
        service.delete(id);
        return Response.noContent().build();
    }

}