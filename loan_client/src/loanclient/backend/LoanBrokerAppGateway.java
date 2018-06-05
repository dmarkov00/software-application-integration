package loanclient.backend;

import gateways.MessageReceiverGateway;
import gateways.MessageSenderGateway;
import loanclient.model.LoanReply;
import loanclient.model.LoanRequest;

public class LoanBrokerAppGateway {
    MessageSenderGateway sender;
    MessageReceiverGateway receiver;

    public void applyForLoan(LoanRequest loanRequest) {

    }

    public void onLoanReplyArrived(LoanRequest request, LoanReply reply) {

    }
}
