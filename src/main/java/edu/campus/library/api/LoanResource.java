package edu.campus.library.api;

import edu.campus.library.domain.Loan;
import edu.campus.library.domain.NewLoan;
import edu.campus.library.service.LoanService;
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

@Path("/loans")
@ApplicationScoped
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class LoanResource {

    @Inject
    private LoanService service;

    @GET
    public List<Loan> list() {
        return service.findAll();
    }

    @POST
    public Response borrow(@Valid NewLoan request, @Context UriInfo uri) {

        Loan loan = service.borrow(request.bookId(), request.memberId());

        URI location = uri.getAbsolutePathBuilder().path(String.valueOf(loan.id())).build();

        return Response.created(location).entity(loan).build();

    }

    @POST
    @Path("/{id}/return")
    public Loan giveBack(@PathParam("id") long id) {
        return service.giveBack(id);
    }

}