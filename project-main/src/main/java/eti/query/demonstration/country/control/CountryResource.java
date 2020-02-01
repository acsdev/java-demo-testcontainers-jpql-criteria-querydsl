package eti.query.demonstration.country.control;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;

@Path("country")
public class CountryResource {

    @Inject
    private CountryStore store;

    @GET
    public Response getCountry() {
        return Response.ok(store.jpqlFindAll()).build();
    }

}