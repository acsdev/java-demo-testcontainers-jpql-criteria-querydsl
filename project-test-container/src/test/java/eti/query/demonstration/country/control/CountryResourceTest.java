package eti.query.demonstration.country.control;

import org.junit.Assert;
import org.junit.Test;
import org.testcontainers.containers.output.OutputFrame;
import org.testcontainers.containers.output.ToStringConsumer;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

public class CountryResourceTest extends ConfigContainerTest {

    @Test
    public void testGetCountry() {

        Client client = ClientBuilder.newClient();

        String url = getAppServerURLBase().concat("/demo/country");

        Response response = client.target(url).request(MediaType.APPLICATION_JSON).get();

        Assert.assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
        System.out.println(response.getEntity());

    }
}
