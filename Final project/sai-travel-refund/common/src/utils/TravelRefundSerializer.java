package utils;

import com.google.gson.Gson;
import models.client.TravelRefundReply;
import models.client.TravelRefundRequest;

public class TravelRefundSerializer {

    private Gson gson = new Gson();

    public String requestToString(TravelRefundRequest travelRefundRequest) {
        return gson.toJson(travelRefundRequest);
    }

    public TravelRefundRequest requestFromString(String travelRefundRequest) {
        return gson.fromJson(travelRefundRequest, TravelRefundRequest.class);
    }

    public String replyToString(TravelRefundReply travelRefundReply) {
        return gson.toJson(travelRefundReply);
    }

    public TravelRefundReply replyFromString(String travelRefundReply) {
        return gson.fromJson(travelRefundReply, TravelRefundReply.class);
    }


}
