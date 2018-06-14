package broker.backend;

import gateways.MessageReceiverGateway;
import gateways.MessageSenderGateway;
import models.approval.ApprovalRequest;
import models.client.ClientTravelMode;
import models.client.TravelRefundReply;
import models.client.TravelRefundRequest;
import org.apache.activemq.command.ActiveMQTextMessage;
import utils.TravelRefundSerializer;

import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;

public abstract class ClientAppGateway {
    private CostsContentEnricher costsContentEnricher = new CostsContentEnricher();
    private ApprovalRecipientList approvalRecipientList = new ApprovalRecipientList();
    private TravelRefundSerializer travelRefundSerializer = new TravelRefundSerializer();

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
        String messageID = null;
        Destination travelRefundReplyQueue = null;
        try {
            // Retrieve the text from the Message object
            travelRefundRequestAsJSON = ((ActiveMQTextMessage) message).getText();
            // Retrieve message ID
            messageID = message.getJMSMessageID();
            // Extract the reply queue
            travelRefundReplyQueue = message.getJMSReplyTo();

        } catch (JMSException e) {
            e.printStackTrace();
        }
        // Deserialize JSON
        TravelRefundRequest travelRefundRequest = travelRefundSerializer.requestFromString(travelRefundRequestAsJSON);

        // Calculate content enriching(update the costs value if needed)
        travelRefundRequest = callContentEnricher(travelRefundRequest);

        // Set the reply queue, so we know to which client to answer
        travelRefundRequest.replyQueue = "";

        // Pass the travel refund request to the GUI Controller
        onTravelRefundRequestArrived(travelRefundRequest, messageID);

        // Create an ApprovalRequest object
        ApprovalRequest approvalRequest = new ApprovalRequest(travelRefundRequest.getTeacher(), travelRefundRequest.getStudent(), travelRefundRequest.getCosts());

        // Send approval requests via the recipient list and pass the message id of the original travel refund request, so later we can correlate to it
        approvalRecipientList.sendApprovalRequest(approvalRequest, messageID);
    }

    /**
     * "Enriches" the object if it is need, otherwise returns the same object that was passed
     */
    private TravelRefundRequest callContentEnricher(TravelRefundRequest travelRefundRequest) {

        if (travelRefundRequest.getMode() == ClientTravelMode.CAR) {
            return costsContentEnricher.calculateCosts(travelRefundRequest);
        }
        return travelRefundRequest;

    }

    protected abstract void onTravelRefundRequestArrived(TravelRefundRequest travelRefundRequest, String messageID);

    public void sendTravelRefundReply(TravelRefundReply travelRefundReply, String correlationID, String clientReplyQueue) {

        MessageSenderGateway sender = new MessageSenderGateway("travelRefundReplyQueue", "empty"); // That is the default reply queue of the client

        // Serialize to JSON
        String travelRefundReplyAsJSON = travelRefundSerializer.replyToString(travelRefundReply);

        Message message = sender.createTextMessage(travelRefundReplyAsJSON);

        try {
            message.setJMSCorrelationID(correlationID);

        } catch (JMSException e) {
            e.printStackTrace();
        }

        sender.send(message);
    }
}
