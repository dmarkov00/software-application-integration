package client.backend;

import client.model.TravelRefundRequest;
import gateways.MessageReceiverGateway;
import gateways.MessageSenderGateway;

import javax.jms.Message;

public class BrokerAppGateway {
    private MessageSenderGateway sender = new MessageSenderGateway("brokerRequestQueue", ReplyQueue.REPLY_QUEUE_NAME);
    private MessageReceiverGateway receiver;
    private TravelRefundSerializer travelRefundSerializer = new TravelRefundSerializer();

    public void requestTravelRefund(TravelRefundRequest travelRefundRequest) {

        // Serialize the object to JSON
        String travelRefundRequestAsJSON = travelRefundSerializer.requestToString(travelRefundRequest);

        // Generate a message
        Message msg = sender.createTextMessage(travelRefundRequestAsJSON);

        sender.send(msg);
    }
}
