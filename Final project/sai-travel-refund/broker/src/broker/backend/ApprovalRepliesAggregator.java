package broker.backend;

import models.approval.ApprovalReply;
import org.apache.activemq.command.ActiveMQTextMessage;
import utils.ApprovalSerializer;

import javax.jms.JMSException;
import javax.jms.Message;
import java.util.ArrayList;
import java.util.List;

public class ApprovalRepliesAggregator {

    private List<Message> messageList = new ArrayList<>();
    private ApprovalSerializer approvalSerializer = new ApprovalSerializer();


    public ApprovalReply aggregateData(Message newMessage) {

        int aggregationID = 0;
        try {
            // Retrieve aggregation ID
            aggregationID = newMessage.getIntProperty("aggregationID");
        } catch (JMSException e) {
            e.printStackTrace();
        }
        // If aggregation id is 0, this means that the message was initially send to only one approval application, so these is no need for aggregation
        if (aggregationID == 0) {
            return generateReply(newMessage);
        } else {
            // Check if the current aggregation id is contained into the list of messages
            // If is contained, the message is returned, which results into 2 messages with this ID that we can aggregate
            // If null is returned we add the message with the non existing aggregation id to the list
            Message aggregationMessage = checkIfMessageListContainsMessageWithAggregationID(aggregationID);

            if (aggregationMessage != null) {

                // Clean up
                messageList.remove(aggregationMessage);

                // Put the messages together
                List<Message> messagesForAggregation = new ArrayList<>();
                messagesForAggregation.add(newMessage);
                messagesForAggregation.add(aggregationMessage);

                // Combine the two messages into one ApprovalReply
                return generateReplyByAggregating(messagesForAggregation);
            } else {

                messageList.add(newMessage);
                return null;
            }
        }
    }

    private ApprovalReply generateReply(Message message) {
        String approvalReplyAsJSON = null;

        // Retrieve the text from the Message object
        try {
            approvalReplyAsJSON = ((ActiveMQTextMessage) message).getText();
        } catch (JMSException e) {
            e.printStackTrace();
        }

        // Deserialize JSON and return
        return approvalSerializer.replyFromString(approvalReplyAsJSON);
    }

    private ApprovalReply generateReplyByAggregating(List<Message> messagesForAggregation) {

        StringBuilder reasonsRejected = new StringBuilder();
        boolean isApproved = false;

        for (Message message : messagesForAggregation) {
            String approvalReplyAsJSON = null;

            // Retrieve the text from the Message object
            try {
                approvalReplyAsJSON = ((ActiveMQTextMessage) message).getText();
            } catch (JMSException e) {
                e.printStackTrace();
            }

            // Deserialize JSON
            ApprovalReply approvalReply = approvalSerializer.replyFromString(approvalReplyAsJSON);

            // Append the reasons rejected
            reasonsRejected.append(approvalReply.getReasonRejected());

            isApproved = approvalReply.isApproved();
        }

        return new ApprovalReply(isApproved, reasonsRejected.toString());
    }

    private Message checkIfMessageListContainsMessageWithAggregationID(int aggregationID) {
        for (Message message : messageList) {

            try {

                if (message.getIntProperty("aggregationID") == aggregationID) {
                    return message;
                }

            } catch (JMSException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

}
