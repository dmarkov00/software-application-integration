package approval.gui;


import approval.model.ApprovalReply;
import approval.model.ApprovalRequest;

class ApprovalListLine {
	
	private ApprovalRequest request;
	private ApprovalReply reply;
	
	public ApprovalListLine(ApprovalRequest request,  ApprovalReply reply) {
		setRequest(request);
		setReply(reply);
	}	
	
	public ApprovalRequest getRequest() {
		return request;
	}
	
	private void setRequest(ApprovalRequest request) {
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
	   return request.toString() + "  --->  " + ((reply!=null)?reply.toString():"waiting...");
	}
	
}
