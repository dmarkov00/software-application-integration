package broker.backend;

import models.approval.ApprovalRequest;
import net.sourceforge.jeval.*;


public class ApprovalRecipientList {

    private Evaluator evaluator = new Evaluator();
    private ApprovalAppGateway approvalAppGateway;

    private static int aggregationID; // Used by the aggregator to merge replies

    public void sendApprovalRequest(ApprovalRequest approvalRequest) {
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
            approvalAppGateway.requestApproval(approvalRequest, "financialDepartmentRequestQueue", aggregationID);
        }
        // Send an approval request to the internship administration
        approvalAppGateway.requestApproval(approvalRequest, "internshipAdministrationRequestQueue", aggregationID);


    }

}
