package broker.gui;

import broker.backend.ApprovalAppGateway;
import broker.backend.ClientAppGateway;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ListView;
import models.approval.ApprovalReply;
import models.client.TravelRefundRequest;

import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

public class BrokerController implements Initializable {

    @FXML
    private ListView<BrokerListLine> lvBrokerRequestReply;
    // This map is used to correlate list line requests with replies, by using JMS id and JMS correlation id
    private Map<String, BrokerListLine> messageIDToBrokerListLineMap = new HashMap<>();

    private ClientAppGateway clientAppGateway = new ClientAppGateway() {
        @Override
        public void onTravelRefundRequestArrived(TravelRefundRequest travelRefundRequest, String messageID) {

            //Create a ListView line with the request and add it to the list view
            BrokerListLine listViewLine = new BrokerListLine(travelRefundRequest, null);

            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    lvBrokerRequestReply.getItems().add(listViewLine);
                }
            });


        }
    };

    private ApprovalAppGateway approvalAppGateway = new ApprovalAppGateway() {
        @Override
        public void onApprovalReplyArrived(ApprovalReply approvalReply, String messageCorrelationID) {
            // Retrieve the list line with the same Id as the correlation id
            BrokerListLine brokerListLine = findListLineByCorrelationID(messageCorrelationID);

            brokerListLine.setReply(approvalReply);
        }
    };

    private BrokerListLine findListLineByCorrelationID(String correlationID) {

        for (Map.Entry<String, BrokerListLine> listLine : messageIDToBrokerListLineMap.entrySet()) {
            if (listLine.getKey().equals(correlationID)) {
                return listLine.getValue();
            }
        }
        return null;
    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

}
