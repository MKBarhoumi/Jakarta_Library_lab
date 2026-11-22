package edu.campus.library.api;

import edu.campus.library.domain.Member;
import edu.campus.library.domain.NewMember;
import edu.campus.library.service.MemberService;
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

@Path("/members")
@ApplicationScoped
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class MemberResource {
    @Inject
    private MemberService service;

    @GET
    public List<Member> list() {
        return service.findAll();
    }

    @POST
    public Response create(@Valid NewMember request, @Context UriInfo uri) {

        Member created = service.create(request);
        URI location = uri.getAbsolutePathBuilder().path(String.valueOf(created.id())).build();

        return Response.created(location).entity(created).build();

    }

}