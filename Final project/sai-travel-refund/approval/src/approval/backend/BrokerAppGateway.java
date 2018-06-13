package approval.backend;

import gateways.MessageReceiverGateway;
import gateways.MessageSenderGateway;
import models.approval.ApprovalReply;
import models.approval.ApprovalRequest;
import org.apache.activemq.command.ActiveMQTextMessage;
import utils.ApprovalSerializer;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import java.util.HashMap;
import java.util.Map;

/**
 * Used as a combined gateway for working wi both financial dep. and internship administration
 */
public abstract class BrokerAppGateway {

    private MessageSenderGateway sender = new MessageSenderGateway("approvalReplyQueue", "empty"); // Reply queue is not needed

    private ApprovalSerializer approvalSerializer = new ApprovalSerializer();

    protected BrokerAppGateway(String approvalName) {
        MessageReceiverGateway receiver = null;
        switch (approvalName) {
            case "Financial Department":
                receiver = new MessageReceiverGateway("financialDepartmentRequestQueue");
                break;

            case "Internship Administration":
                receiver = new MessageReceiverGateway("internshipAdministrationRequestQueue");
                break;
        }

        receiver.setListener(new MessageListener() {
            @Override
            public void onMessage(Message message) {
                handleMessage(message);
            }
        });


    }

    private void handleMessage(Message message) {


        String approvalRequestAsJSON = null;
        int aggregationID = 0;
        try {
            approvalRequestAsJSON = ((ActiveMQTextMessage) message).getText();

            // Retrieve the aggregation id, used later on when sending reply to this request message
            aggregationID = message.getIntProperty("aggregationID");

        } catch (JMSException e) {
            e.printStackTrace();
        }

        // Deserialize JSON
        ApprovalRequest approvalRequest = approvalSerializer.requestFromString(approvalRequestAsJSON);

        // Set aggregation id later reference when sending replies
        approvalRequest.aggregationID = aggregationID;

        onApprovalRequestArrived(approvalRequest);
    }

    public void sendApprovalReply(ApprovalReply approvalReply, int aggregationID) {
        String approvalReplyAsJSON = approvalSerializer.replyToString(approvalReply);

        Message msg = sender.createTextMessage(approvalReplyAsJSON);

        try {
            // Setting aggregation if, which is later used in the aggregator, inside broker app
            msg.setIntProperty("aggregationID", aggregationID);

        } catch (JMSException e) {
            e.printStackTrace();
        }
        sender.send(msg);
    }

    public abstract void onApprovalRequestArrived(ApprovalRequest approvalRequest);

}
