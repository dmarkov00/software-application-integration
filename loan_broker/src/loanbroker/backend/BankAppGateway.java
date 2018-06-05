package loanbroker.backend;

import gateways.MessageReceiverGateway;
import gateways.MessageSenderGateway;
import loanbroker.model.BankInterestReply;
import loanbroker.model.BankInterestRequest;

public class BankAppGateway {

    private MessageSenderGateway sender;
    private MessageReceiverGateway receiver;

    public void onBankReplyArrived(BankInterestReply reply, BankInterestRequest request){

    }

    public void sendBankRequest(BankInterestRequest request){

    }

}
