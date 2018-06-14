package client.backend;

import gateways.MessageReceiverGateway;
import gateways.MessageSenderGateway;
import models.client.TravelRefundReply;
import models.client.TravelRefundRequest;
import org.apache.activemq.command.ActiveMQTextMessage;
import utils.TravelRefundSerializer;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;

public abstract class BrokerAppGateway {
    // Having an extra reply queue parameter allows me to work with multiple instances of client
    private MessageSenderGateway sender = new MessageSenderGateway("brokerRequestQueue", ReplyQueue.REPLY_QUEUE_NAME);
    private MessageReceiverGateway receiver;
    private TravelRefundSerializer travelRefundSerializer = new TravelRefundSerializer();

    public BrokerAppGateway() {
        MessageReceiverGateway receiver = new MessageReceiverGateway(ReplyQueue.REPLY_QUEUE_NAME);

        receiver.setListener(new MessageListener() {
            @Override
            public void onMessage(Message message) {

                handleMessage(message);
            }
        });
    }

    private void handleMessage(Message message) {
        String travelRefundReplyAsJSON = null;
        String messageCorrelationID = null;
        try {
            // Retrieve the text from the Message object
            travelRefundReplyAsJSON = ((ActiveMQTextMessage) message).getText();
            // Retrieve the correlation ID
            messageCorrelationID = ((ActiveMQTextMessage) message).getCorrelationId();
        } catch (JMSException e) {
            e.printStackTrace();
        }
        // Deserialize JSON
        TravelRefundSerializer travelRefundSerializer = new TravelRefundSerializer();
        TravelRefundReply travelRefundReply = travelRefundSerializer.replyFromString(travelRefundReplyAsJSON);

        onTravelRefundReplyArrived(travelRefundReply, messageCorrelationID);
    }

    /**
     * @return A message id which I store in a map inside the ClientController, later I use that Id to correlate request with response
     */
    public String requestTravelRefund(TravelRefundRequest travelRefundRequest) {

        // Serialize the object to JSON
        String travelRefundRequestAsJSON = travelRefundSerializer.requestToString(travelRefundRequest);

        // Generate a message
        Message msg = sender.createTextMessage(travelRefundRequestAsJSON);

        sender.send(msg);

        try {
            return msg.getJMSMessageID();
        } catch (JMSException e) {
            e.printStackTrace();
        }
        return null;
    }

    public abstract void onTravelRefundReplyArrived(TravelRefundReply travelRefundReply, String correlationID);

}
