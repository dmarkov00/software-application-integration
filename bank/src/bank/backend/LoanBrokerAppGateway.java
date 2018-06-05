package bank.backend;

import bank.model.BankInterestReply;
import bank.model.BankInterestRequest;
import gateways.MessageReceiverGateway;
import gateways.MessageSenderGateway;


public class LoanBrokerAppGateway {
    MessageSenderGateway sender;
    MessageReceiverGateway receiver;

    public void sendBankInterestReply(BankInterestReply bankInterestReply) {

    }

    public void onBankInterestRequestArrived(BankInterestRequest request, BankInterestReply reply){
    }
}
