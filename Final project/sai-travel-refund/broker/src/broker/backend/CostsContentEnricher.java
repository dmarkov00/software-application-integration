package broker.backend;

import org.glassfish.jersey.client.ClientConfig;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;
import java.net.URI;

public class CostsContentEnricher {

    private ClientConfig config = new ClientConfig();
    private Client client = ClientBuilder.newClient(config);

    public Double getPricePerKilometer() {
        URI baseURI = UriBuilder.fromUri("http://localhost:8080/priceperkm8/rest/price").build();

        WebTarget serviceTarget = client.target(baseURI);

        Response response = serviceTarget.request().accept(MediaType.TEXT_PLAIN).get();

        Double result = response.readEntity(Double.class);
        System.out.println(result);
        if (response.getStatus() == Response.Status.OK.getStatusCode()) {
            return result;
        } else {
            return -1d;
        }
    }


}
