package broker.backend;

import gateways.MessageReceiverGateway;
import gateways.MessageSenderGateway;
import models.approval.ApprovalReply;
import models.approval.ApprovalRequest;
import utils.ApprovalSerializer;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;

public abstract class ApprovalAppGateway {

    private ApprovalSerializer approvalSerializer = new ApprovalSerializer();
    private ApprovalRepliesAggregator approvalRepliesAggregator = new ApprovalRepliesAggregator();

    public ApprovalAppGateway() {
        MessageReceiverGateway receiver = new MessageReceiverGateway("approvalReplyQueue");
        receiver.setListener(new MessageListener() {
            @Override
            public void onMessage(Message message) {

                String messageCorrelationID = null;
                try {
                    messageCorrelationID = message.getJMSCorrelationID();
                } catch (JMSException e) {
                    e.printStackTrace();
                }

                // Pass data to the aggregator
                ApprovalReply approvalReply = approvalRepliesAggregator.aggregateData(message);

                // If approvalReply is null, this means that the aggregator still needs to receive more data to generate a reply
                if (approvalReply == null) {
                    return;
                }

                onApprovalReplyArrived(approvalReply, messageCorrelationID);
            }
        });
    }

    public void requestApproval(ApprovalRequest approvalRequest, String requestQueue, int aggregationID) {

        // In this case of "sender" a reply queue is not needed to be specified because we work with only one broker with known reply queue names
        MessageSenderGateway sender = new MessageSenderGateway(requestQueue, "empty");

        // Serialize the object to JSON
        String approvalRequestAsJSON = approvalSerializer.requestToString(approvalRequest);

        // Generate a message
        Message msg = sender.createTextMessage(approvalRequestAsJSON);

        // Set aggregation id
        try {
            msg.setIntProperty("aggregationID", aggregationID);
        } catch (JMSException e) {
            e.printStackTrace();
        }

        sender.send(msg);
    }

    public abstract void onApprovalReplyArrived(ApprovalReply approvalReply, String messageID);
}
