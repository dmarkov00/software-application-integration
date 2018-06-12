package broker.backend;

import gateways.MessageReceiverGateway;
import gateways.MessageSenderGateway;
import models.approval.ApprovalRequest;
import utils.ApprovalSerializer;


import javax.jms.Message;

public class FinancialDepartmentAppGateway {
    // In this case of "sender" a reply queue is not needed to be specified because we work with only one broker with known queue name
    private MessageSenderGateway sender = new MessageSenderGateway("financialDepartmentRequestQueue", "empty");
    private MessageReceiverGateway receiver;
    private ApprovalSerializer approvalSerializer = new ApprovalSerializer();

    public void requestApproval(ApprovalRequest approvalRequest) {
        // Serialize the object to JSON
        String approvalRequestAsJSON = approvalSerializer.requestToString(approvalRequest);

        // Generate a message
        Message msg = sender.createTextMessage(approvalRequestAsJSON);

        sender.send(msg);
    }
}
