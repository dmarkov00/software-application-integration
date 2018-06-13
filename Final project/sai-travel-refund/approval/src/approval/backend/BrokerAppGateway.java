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

        try {
            approvalRequestAsJSON = ((ActiveMQTextMessage) message).getText();
        } catch (JMSException e) {
            e.printStackTrace();
        }

        // Deserialize JSON
        ApprovalRequest approvalRequest = approvalSerializer.requestFromString(approvalRequestAsJSON);

        onApprovalRequestArrived(approvalRequest);
    }

    public void sendApprovalReply(ApprovalReply approvalReply) {
        String approvalReplyAsJSON = approvalSerializer.replyToString(approvalReply);

        Message msg = sender.createTextMessage(approvalReplyAsJSON);


        sender.send(msg);
    }

    public abstract void onApprovalRequestArrived(ApprovalRequest approvalRequest);

}
