package broker.backend;

import gateways.MessageReceiverGateway;
import gateways.MessageSenderGateway;
import models.approval.ApprovalRequest;
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
    private ApprovalRecipientList approvalRecipientList = new ApprovalRecipientList();

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

        // Calculate content enriching(update the costs value if needed)
        travelRefundRequest = callContentEnricher(travelRefundRequest);

        // Create an ApprovalRequest object
        ApprovalRequest approvalRequest = new ApprovalRequest(travelRefundRequest.getTeacher(), travelRefundRequest.getStudent(), travelRefundRequest.getCosts());

        // Send approval requests via the recipient list
        approvalRecipientList.sendApprovalRequest(approvalRequest);

        // Pass the travel refund request to the GUI Controller
        onTravelRefundRequestArrived(travelRefundRequest);
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

    protected abstract void onTravelRefundRequestArrived(TravelRefundRequest travelRefundRequest);

    public void sendLoanReply() {

    }
}
