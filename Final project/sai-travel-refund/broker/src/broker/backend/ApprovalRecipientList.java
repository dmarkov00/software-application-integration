package broker.backend;

import net.sourceforge.jeval.*;

public class ApprovalRecipientList {

    // We create a rule only for the financial department because Internship Administration is always notified
    private String financialDepartment = "#{cost} >=50";
    Evaluator evaluator = new Evaluator();

    public void sendApprovalRequest() {

    }

}
