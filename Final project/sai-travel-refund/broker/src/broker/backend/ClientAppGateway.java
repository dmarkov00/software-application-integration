package broker.backend;

import gateways.MessageReceiverGateway;
import gateways.MessageSenderGateway;
import models.client.TravelRefundRequest;

import javax.jms.Message;
import javax.jms.MessageListener;

public abstract class ClientAppGateway {
    private MessageSenderGateway sender;

    protected ClientAppGateway() {
        MessageReceiverGateway receiver = new MessageReceiverGateway("brokerRequestQueue");

        receiver.setListener(new MessageListener() {
            @Override
            public void onMessage(Message message) {
                System.out.println(message);

                onTravelRefundRequestArrived("Received a message from a class");
            }
        });
    }


    public abstract void onTravelRefundRequestArrived(String travelRefundRequest);

}
