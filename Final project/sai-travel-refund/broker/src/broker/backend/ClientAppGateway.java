package broker.backend;

import gateways.MessageReceiverGateway;
import gateways.MessageSenderGateway;
import models.client.TravelRefundRequest;

import javax.jms.Message;
import javax.jms.MessageListener;

public class ClientAppGateway {
    private MessageSenderGateway sender;
    private MessageReceiverGateway receiver = new MessageReceiverGateway("brokerRequestQueue");

//    public void onTravelRefundRequestArrived(TravelRefundRequest travelRefundRequest) {
//
//        // Visualize the refund request
//
//
//        // Send the message to approval request or the other stuff via their classes
//    }

    public void setTravelRefundRequestListener() {
        receiver.setListener(new MessageListener() {
            @Override
            public void onMessage(Message message) {
                System.out.println("Received a message");
            }
        });
    }
}
