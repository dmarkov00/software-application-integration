package approval.gui;

import approval.backend.BrokerAppGateway;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
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
        // Retrieve the index of the selected item from the list view
        int selectedItemIndex = lvRequestReply.getSelectionModel().getSelectedIndex();

        // Making a check if any of the items is selected, only then proceed
        if (selectedItemIndex >= 0) {

            // Check if any of the radio buttons is selected
            if (rbApprove.isSelected() || rbReject.isSelected()) {

                // Retrieving the selected line from the list view as a ApprovalListLine object
                ApprovalListLine approvalListLine = lvRequestReply.getSelectionModel().getSelectedItems().get(0);

                // If the retrieved ApprovalListLine has an reply object assigned, this means that it has already been answered
                if (approvalListLine.getReply() != null) {
                    new Alert(Alert.AlertType.INFORMATION, "You already replied to this one").show();
                    return;
                }
                // Extract the aggregation id from the ApprovalRequest object(we set it earlier in the BrokerAppGateway)
                int aggregationID = approvalListLine.getRequest().aggregationID;

                ApprovalReply approvalReply = null;

                if (rbApprove.isSelected()) {
                    approvalReply = new ApprovalReply(true, "");

                } else if (rbReject.isSelected()) {
                    approvalReply = new ApprovalReply(false, approvalName);
                }

                // Sending the reply via JMS
                brokerAppGateway.sendApprovalReply(approvalReply, aggregationID);

                // Updating the list view
                approvalListLine.setReply(approvalReply);
                lvRequestReply.refresh();
            } else {
                new Alert(Alert.AlertType.INFORMATION, "Please select a radio button").show();
            }
        } else {
            new Alert(Alert.AlertType.INFORMATION, "Select an item form the list view first.").show();
        }
    }


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
