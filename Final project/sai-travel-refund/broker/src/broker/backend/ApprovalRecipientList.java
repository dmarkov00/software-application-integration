package broker.backend;

import gateways.MessageSenderGateway;
import models.approval.ApprovalRequest;
import net.sourceforge.jeval.*;
import utils.ApprovalSerializer;

import javax.jms.JMSException;
import javax.jms.Message;


public class ApprovalRecipientList {


    private static int aggregationID; // Used by the aggregator to merge replies

    public void sendApprovalRequest(ApprovalRequest approvalRequest, String messageID) {
        Evaluator evaluator = new Evaluator();


        // A rule is created only for the financial department because Internship Administration is always notified
        String financialDepartment = "#{costs} >=50";

        evaluator.putVariable("costs", Double.toString(approvalRequest.getCosts()));

        String result = null;
        try {
            result = evaluator.evaluate(financialDepartment);
        } catch (EvaluationException e) {
            e.printStackTrace();
        }

        boolean shouldSendToFinancialDepartment = result.equals("1.0");

        if (shouldSendToFinancialDepartment) {
            // We generate new aggregation id only in case both of the approval request are going be made
            aggregationID++;

            // Send an approval request to the financial department
            this.requestApproval(approvalRequest, "financialDepartmentRequestQueue", aggregationID, messageID);

            // Send an approval request to the internship administration
            this.requestApproval(approvalRequest, "internshipAdministrationRequestQueue", aggregationID, messageID);
            return;
        }
        // Send an approval request to the internship administration
        this.requestApproval(approvalRequest, "internshipAdministrationRequestQueue", 0, messageID);


    }

    private void requestApproval(ApprovalRequest approvalRequest, String requestQueue, int aggregationID, String messageID) {
        ApprovalSerializer approvalSerializer = new ApprovalSerializer();

        // In this case of "sender" a reply queue is not needed to be specified because we work with only one broker with known reply queue names
        MessageSenderGateway sender = new MessageSenderGateway(requestQueue, "empty");

        // Serialize the object to JSON
        String approvalRequestAsJSON = approvalSerializer.requestToString(approvalRequest);

        // Generate a message
        Message msg = sender.createTextMessage(approvalRequestAsJSON);

        try {
            // Set aggregation id
            msg.setIntProperty("aggregationID", aggregationID);

            // Set the message id of the original travel refund request, so we correlate to later when displaying the list in the broker
            msg.setJMSCorrelationID(messageID);

        } catch (JMSException e) {
            e.printStackTrace();
        }

        sender.send(msg);
    }

}
