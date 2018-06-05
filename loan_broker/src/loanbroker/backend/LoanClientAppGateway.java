package loanbroker.backend;

import gateways.MessageReceiverGateway;
import gateways.MessageSenderGateway;
import loanbroker.model.LoanReply;
import loanbroker.model.LoanRequest;

public class LoanClientAppGateway {
    private MessageSenderGateway sender;
    private MessageReceiverGateway receiver;

    public void  onLoanRequestArrived(LoanRequest request){

    }
    public void sendLoanReply(LoanRequest request, LoanReply reply){

    }
}
