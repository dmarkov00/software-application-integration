package broker.backend;

import gateways.MessageReceiverGateway;
import gateways.MessageSenderGateway;
import models.client.ClientTravelMode;
import models.client.TravelRefundRequest;
import org.apache.activemq.command.ActiveMQTextMessage;
import utils.TravelRefundSerializer;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;

public abstract class ClientAppGateway {
    private MessageSenderGateway sender;
    private CostsContentEnricher costsContentEnricher = new CostsContentEnricher();

    protected ClientAppGateway() {
        MessageReceiverGateway receiver = new MessageReceiverGateway("brokerRequestQueue");

        receiver.setListener(new MessageListener() {
            @Override
            public void onMessage(Message message) {
                handleMessage(message);
            }
        });
    }


    private void handleMessage(Message message) {
        String travelRefundRequestAsJSON = null;

        // Retrieve the text from the Message object
        try {
            travelRefundRequestAsJSON = ((ActiveMQTextMessage) message).getText();
        } catch (JMSException e) {
            e.printStackTrace();
        }
        // Deserialize JSON
        TravelRefundSerializer travelRefundSerializer = new TravelRefundSerializer();
        TravelRefundRequest travelRefundRequest = travelRefundSerializer.requestFromString(travelRefundRequestAsJSON);

        // Calculate content enriching
        travelRefundRequest = callContentEnricher(travelRefundRequest);

        // Pass the data to the Controller
        onTravelRefundRequestArrived(travelRefundRequest);
    }

    /**
     * "Enriches" the object if it is need, otherwise returns the same object that was passed
     */
    private TravelRefundRequest callContentEnricher(TravelRefundRequest travelRefundRequest) {

        if (travelRefundRequest.getMode() == ClientTravelMode.CAR) {
            costsContentEnricher.getPricePerKilometer();
        }
        return travelRefundRequest;

    }

    protected abstract void onTravelRefundRequestArrived(TravelRefundRequest travelRefundRequest);

    public void sendLoanReply() {

    }
}
