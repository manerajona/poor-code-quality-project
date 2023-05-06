package com.manerajona.ports.input.rs;

import com.manerajona.common.constants.ResourceConstants;
import com.manerajona.common.exceptions.ResourceNotFoundException;
import com.manerajona.core.LoanService;
import com.manerajona.core.domain.Loan;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.UriBuilder;

import java.net.URI;
import java.util.UUID;

@Path("/loans")
public class LoanResource {

    @Inject
    private LoanService service;

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response create(Loan loan) {
        try {
            UUID guid = service.createLoan(loan);
            URI location = UriBuilder.fromUri(ResourceConstants.URI + "/loans/{guid}").build(guid);

            return Response.created(location).build();
        } catch (Exception ignored) {
            return Response.serverError().build();
        }
    }

    @GET
    @Path("/{guid}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response get(@PathParam("guid") String guid) {
        try {
            Loan loan = service.findLoan(UUID.fromString(guid));

            return Response.ok(loan).build();
        } catch (ResourceNotFoundException ignored) {
            return Response.status(Response.Status.NOT_FOUND).build();
        } catch (Exception ignored) {
            return Response.serverError().build();
        }
    }

    @PUT
    @Path("/{guid}")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response get(@PathParam("guid") String guid, Loan loan) {
        boolean renegotiated = service.renegotiateLoan(UUID.fromString(guid), loan);
        return renegotiated ? Response.noContent().build() : Response.serverError().build();
    }
}