package broker.backend;

import models.client.TravelRefundRequest;
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

    public TravelRefundRequest calculateCosts(TravelRefundRequest travelRefundRequest) {

        // Get distance
        Double distance = calculateDistance(travelRefundRequest);
        // Get price
        Double pricePerKm = getPricePerKilometer();
        // Calculate costs
        Double costs = distance * pricePerKm;
        // Set the new value
        travelRefundRequest.setCosts(costs);

        return travelRefundRequest;
    }

    private Double getPricePerKilometer() {
        URI baseURI = UriBuilder.fromUri("http://localhost:8080/priceperkm8/rest/price").build();

        WebTarget serviceTarget = client.target(baseURI);

        Response response = serviceTarget.request().accept(MediaType.TEXT_PLAIN).get();


        if (response.getStatus() == Response.Status.OK.getStatusCode()) {
            Double result = response.readEntity(Double.class);

            return result;
        } else {
            return -1d;
        }
    }

    /**
     * Calls the google distance api and calculates the distance between 2 cities
     */
    private Double calculateDistance(TravelRefundRequest travelRefundRequest) {
        String origin = travelRefundRequest.getOrigin().getCity();
        String destination = travelRefundRequest.getDestination().getCity();

        URI baseURI = UriBuilder.fromUri("https://maps.googleapis.com/maps/api/distancematrix/json").build();

        WebTarget serviceTarget = client.target(baseURI);

        // Setup api call with origin, destination, and api key for query parameters
        Response response = serviceTarget.queryParam("origins", origin)
                .queryParam("destinations", destination)
                .queryParam("key", "AIzaSyCFdBTXKEu81l6IarCzI91JENbvShPnWQ4")
                .request().accept(MediaType.APPLICATION_JSON).get();


        if (response.getStatus() == Response.Status.OK.getStatusCode()) {

            String result = response.readEntity(String.class);

            // Get the distance in double
            Double distance = extractDistanceFromAPIResponse(result);

            return distance;
        } else {
            return -1d;
        }
    }

    Double extractDistanceFromAPIResponse(String distanceResponse) {

        // Split response into segments
        String segments[] = distanceResponse.split(":");
        // Grab the segment that contains the distance value
        String segment = segments[7];
        // Extract only the numbers from the segment
        String digits = segment.replaceAll("[^0-9.]", "");

        // Round and divide by 1000(value is returned in meters and we need km)
        int roundedDistance = Integer.parseInt(digits) / 1000;

        //Cast to double and return
        return (double) roundedDistance;
    }
}
