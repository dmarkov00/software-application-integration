package client.model;

/**
 *
 * This class stores all information about a travel approval reply as a response
 * to a travel refundation request.
 */
public class TravelRefundReply {

    private boolean approved;
    private String reasonRejected;
    private double costs;

    public TravelRefundReply() {
        super();
        setApproved(false);
        setReasonRejected(null);
        setCosts(0);
    }

    public TravelRefundReply(boolean approved, String reasonRejected, double costs) {
        super();
        setApproved(approved);
        setReasonRejected(reasonRejected);
        setCosts(costs);
    }

    public boolean isApproved() {
        return approved;
    }

    public void setApproved(boolean approved) {
        this.approved = approved;
    }

    public String getReasonRejected() {
        return reasonRejected;
    }

    public void setReasonRejected(String reasonRejected) {
        this.reasonRejected = reasonRejected;
    }
    
        public double getCosts() {
        return costs;
    }

    public void setCosts(double costs) {
        this.costs = costs;
    }

    @Override
    public String toString() {
        return (approved ? ("approved, costs="+costs) : ("rejected by " + reasonRejected));
    }
}
