package loanclient.backend;

import com.google.gson.Gson;
import loanclient.model.LoanReply;
import loanclient.model.LoanRequest;

public class LoanSerializer {
    private  Gson gson = new Gson();

    public String requestToString(LoanRequest loanRequest)
    {
        return gson.toJson(loanRequest);
    }
    public LoanRequest requestFromString(String loanRequest){
        return gson.fromJson(loanRequest, LoanRequest.class);
    }
    public String replyToString(LoanReply loanReply)
    {
        return gson.toJson(loanReply);
    }
    public LoanReply replyFromString(String loanReply){
        return gson.fromJson(loanReply, LoanReply.class);
    }
}
