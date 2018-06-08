package loanclient.backend;

import gateways.MessageReceiverGateway;
import gateways.MessageSenderGateway;
import loanclient.model.LoanReply;
import loanclient.model.LoanRequest;

import javax.jms.Message;

public class LoanBrokerAppGateway {
    private MessageSenderGateway sender;
    private MessageReceiverGateway receiver;
    private LoanSerializer loanSerializer = new LoanSerializer();

    public void applyForLoan(LoanRequest loanRequest) {
        // Serialize to JSON
        String loanRequestAsJSON = loanSerializer.requestToString(loanRequest);
        sender = new MessageSenderGateway("loanBrokerRequestQueue", ReplyQueue.REPLY_QUEUE_NAME);
        // Add a dependency
        Message msg = sender.createTextMessage(loanRequestAsJSON);

        sender.send(msg);
    }

    public void onLoanReplyArrived(LoanRequest request, LoanReply reply) {

    }
}
