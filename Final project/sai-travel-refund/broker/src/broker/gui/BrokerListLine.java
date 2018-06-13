package broker.gui;

import models.approval.ApprovalReply;
import models.client.TravelRefundRequest;

/**
 * The BrokerListLine is a combination of TravelRefundRequest and ApprovalReply
 */
public class BrokerListLine {
    private TravelRefundRequest request;
    private ApprovalReply reply;

    public BrokerListLine(TravelRefundRequest request, ApprovalReply reply) {
        this.request = request;
        this.reply = reply;
    }

    public TravelRefundRequest getRequest() {
        return request;
    }

    private void setRequest(TravelRefundRequest request) {
        this.request = request;
    }

    public ApprovalReply getReply() {
        return reply;
    }

    public void setReply(ApprovalReply reply) {
        this.reply = reply;
    }

    @Override
    public String toString() {
        return request.toString() + "  --->  " + ((reply != null) ? reply.toString() : "waiting...");
    }
}
