package approval.backend;

import gateways.MessageReceiverGateway;
import models.approval.ApprovalRequest;

import javax.jms.Message;
import javax.jms.MessageListener;

/**
 * Used as a combined gateway for working wi both financial dep. and internship administration
 */
public abstract class BrokerAppGateway {
    public BrokerAppGateway() {
        MessageReceiverGateway financialDepartmentReceiver = new MessageReceiverGateway("financialDepartmentRequestQueue");

        MessageReceiverGateway internshipAdministrationReceiver = new MessageReceiverGateway("internshipAdministrationRequestQueue");

        financialDepartmentReceiver.setListener(new MessageListener() {
            @Override
            public void onMessage(Message message) {
                handleFinancialDepartmentMessage(message);
            }
        });

        internshipAdministrationReceiver.setListener(new MessageListener() {
            @Override
            public void onMessage(Message message) {
                handleInternshipAdministrationMessage(message);
            }
        });

    }

    private void handleInternshipAdministrationMessage(Message message) {

    }

    private void handleFinancialDepartmentMessage(Message message) {

    }

    public abstract void onInternshipAdministrationApprovalRequestArrived(ApprovalRequest approvalRequest);

    public abstract void onFinancialDepartmentApprovalRequestArrived(ApprovalRequest approvalRequest);


}
