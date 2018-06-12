package broker.backend;

import gateways.MessageReceiverGateway;
import gateways.MessageSenderGateway;
import models.client.TravelRefundRequest;
import utils.TravelRefundSerializer;

import javax.jms.Message;

public class InternshipAdministrationAppGateway {

    // In this case of "sender" a reply queue is not needed to be specified because we work with only one broker with known queue name
    private MessageSenderGateway sender = new MessageSenderGateway("internshipAdministrationRequestQueue", null);
    private MessageReceiverGateway receiver;
    private TravelRefundSerializer travelRefundSerializer = new TravelRefundSerializer();

    public void requestTravelRefundApproval(TravelRefundRequest travelRefundRequest) {
        // Serialize the object to JSON
        String travelRefundRequestAsJSON = travelRefundSerializer.requestToString(travelRefundRequest);

        // Generate a message
        Message msg = sender.createTextMessage(travelRefundRequestAsJSON);

        sender.send(msg);
    }
}
