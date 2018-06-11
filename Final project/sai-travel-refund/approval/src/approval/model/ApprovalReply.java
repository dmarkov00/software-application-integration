package approval.model;

/**
 * This class stores information about the approval reply for a travel
 * refundation request for a specific teacher.
 *
 */
public class ApprovalReply {

    private boolean approved;
    private String reasonRejected;

    public ApprovalReply() {
        super();
        setApproved(false);
        setReasonRejected(null);
    }

    public ApprovalReply(boolean approved, String reasonRejected) {
        super();
        setApproved(approved);
        setReasonRejected(reasonRejected);
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

    @Override
    public String toString() {
        return (approved ? "approved" : ("rejected by " + reasonRejected));
    }
}
