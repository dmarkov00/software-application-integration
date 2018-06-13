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
import models.approval.ApprovalRequest;

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

        if(rbApprove.isSelected()){
            System.out.println("approve");
        } else if(rbReject.isSelected()){
            System.out.println("rejected");
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
