package utils;

import com.google.gson.Gson;
import models.approval.ApprovalReply;
import models.approval.ApprovalRequest;

public class ApprovalSerializer {

    private Gson gson = new Gson();

    public String requestToString(ApprovalRequest approvalRequest) {
        return gson.toJson(approvalRequest);
    }

    public ApprovalRequest requestFromString(String approvalRequest) {
        return gson.fromJson(approvalRequest, ApprovalRequest.class);
    }

    public String replyToString(ApprovalReply approvalReply) {
        return gson.toJson(approvalReply);
    }

    public ApprovalReply replyFromString(String approvalReply) {
        return gson.fromJson(approvalReply, ApprovalReply.class);
    }

}
