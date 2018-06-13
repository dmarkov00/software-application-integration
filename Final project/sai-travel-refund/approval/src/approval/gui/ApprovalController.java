package approval.gui;

import approval.backend.BrokerAppGateway;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import models.approval.ApprovalReply;
import models.approval.ApprovalRequest;
import models.client.TravelRefundRequest;

import java.net.URL;
import java.util.ResourceBundle;

public class ApprovalController implements Initializable {

    @FXML
    private ListView<ApprovalListLine> lvRequestReply;
    @FXML
    private RadioButton rbApprove;
    @FXML
    private RadioButton rbReject;
    @FXML
    private Button btnSendReply;

    private String approvalName;

    private BrokerAppGateway brokerAppGateway;


    ApprovalController(String approvalName) {
        this.approvalName = approvalName;
        brokerAppGateway = new BrokerAppGateway(approvalName) {

            @Override
            public void onApprovalRequestArrived(ApprovalRequest approvalRequest) {

                // Create new line for visualizing
                ApprovalListLine approvalListLine = new ApprovalListLine(approvalRequest, null);

                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        lvRequestReply.getItems().add(approvalListLine);
                    }
                });
            }
        };
    }


    private void sendApprovalReply() {
        ApprovalListLine approvalListLine = lvRequestReply.getSelectionModel().getSelectedItems().get(0);
        int aggregationID = approvalListLine.getRequest().aggregationID;
        ApprovalReply approvalReply = null;
        if (rbApprove.isSelected()) {

            approvalReply = new ApprovalReply(true, "");

        } else if (rbReject.isSelected()) {
            switch (approvalName) {
                case "Financial Department":
                    approvalReply = new ApprovalReply(false, "Internship administration");
                    break;

                case "Internship Administration":
                    approvalReply = new ApprovalReply(false, "Financial department");
                    break;
            }
        }

        brokerAppGateway.sendApprovalReply(approvalReply, aggregationID);

        approvalListLine.setReply(approvalReply);
        lvRequestReply.refresh();
    }

//    private ApprovalListLine getRequestReply(ApprovalRequest request) {
//
//        for (int i = 0; i < lvRequestReply.getItems().size(); i++) {
//            ApprovalListLine rr = lvRequestReply.getItems().get(i);
//            if (rr.getRequest() == request) {
//                return rr;
//            }
//        }
//
//        return null;
//    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        ToggleGroup radioButtonsGroup = new ToggleGroup();
        rbApprove.setToggleGroup(radioButtonsGroup);
        rbReject.setToggleGroup(radioButtonsGroup);
        btnSendReply.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                sendApprovalReply();
            }
        });
    }
}
