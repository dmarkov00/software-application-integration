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



    public abstract void onApprovalReplyArrived(ApprovalReply approvalReply, String messageID);
}
