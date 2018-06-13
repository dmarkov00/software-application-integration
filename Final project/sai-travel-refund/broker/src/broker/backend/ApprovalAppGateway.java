package broker.backend;

import gateways.MessageReceiverGateway;
import gateways.MessageSenderGateway;
import models.approval.ApprovalRequest;
import utils.ApprovalSerializer;

import javax.jms.JMSException;
import javax.jms.Message;

public class ApprovalAppGateway {

    private MessageReceiverGateway receiver;
    private ApprovalSerializer approvalSerializer = new ApprovalSerializer();

    public void requestApproval(ApprovalRequest approvalRequest,String requestQueue, int aggregationID) {

        // In this case of "sender" a reply queue is not needed to be specified because we work with only one broker with known reply queue names
        MessageSenderGateway sender = new MessageSenderGateway(requestQueue, "empty");

        // Serialize the object to JSON
        String approvalRequestAsJSON = approvalSerializer.requestToString(approvalRequest);

        // Generate a message
        Message msg = sender.createTextMessage(approvalRequestAsJSON);

        // Set aggregation id
        try {
            msg.setIntProperty("aggregationID",aggregationID);
        } catch (JMSException e) {
            e.printStackTrace();
        }

        sender.send(msg);
    }
}
