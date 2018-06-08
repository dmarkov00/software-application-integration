package loanclient.backend;

import gateways.MessageReceiverGateway;
import gateways.MessageSenderGateway;
import loanclient.model.LoanReply;
import loanclient.model.LoanRequest;

import javax.jms.Message;
import javax.jms.MessageListener;

public abstract class LoanBrokerAppGateway {
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

    public abstract void onLoanReplyArrived(LoanRequest request, LoanReply reply);

    public void onReply(LoanRequest request, LoanReply reply) {

        receiver.setListener(new MessageListener() {
            @Override
            public void onMessage(Message message) {
                // TODO
            }
        });
    }

}
