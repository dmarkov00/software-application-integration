package loanclient.model;

/**
 *
 * This class stores all information about a bank offer
 * as a response to a client loan request.
 */
public class LoanReply {

        private double interest; // the interest that the bank offers for the requested loan
        private String bankID; // the unique quote identification of the bank which makes the offer

    public LoanReply() {
        super();
        this.interest = 0;
        this.bankID = "";
    }
    public LoanReply(double interest, String quoteID) {
        super();
        this.interest = interest;
        this.bankID = quoteID;
    }

    public double getInterest() {
        return interest;
    }

    public void setInterest(double interest) {
        this.interest = interest;
    }

    public String getQuoteID() {
        return bankID;
    }

    public void setQuoteID(String quoteID) {
        this.bankID = quoteID;
    }
    
    @Override
    public String toString(){
        return " interest="+String.valueOf(interest) + " quoteID="+String.valueOf(bankID);
    }
}
