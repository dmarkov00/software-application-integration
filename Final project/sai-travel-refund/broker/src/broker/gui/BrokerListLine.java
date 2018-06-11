package broker.gui;

import models.client.TravelRefundReply;
import models.client.TravelRefundRequest;

public class BrokerListLine {
    private TravelRefundRequest request;
    private TravelRefundReply reply;

    public BrokerListLine(TravelRefundRequest request, TravelRefundReply reply) {
        this.request = request;
        this.reply = reply;
    }

    public TravelRefundRequest getRequest() {
        return request;
    }

    private void setRequest(TravelRefundRequest request) {
        this.request = request;
    }

    public TravelRefundReply getReply() {
        return reply;
    }

    public void setReply(TravelRefundReply reply) {
        this.reply = reply;
    }

    @Override
    public String toString() {
        return request.toString() + "  --->  " + ((reply != null) ? reply.toString() : "waiting...");
    }
}
