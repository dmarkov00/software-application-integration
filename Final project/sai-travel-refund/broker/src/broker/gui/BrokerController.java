package broker.gui;

import broker.backend.ClientAppGateway;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ListView;
import models.client.TravelRefundRequest;

import java.net.URL;
import java.util.ResourceBundle;

public class BrokerController implements Initializable {

    @FXML
    private ListView<BrokerListLine> lvBrokerRequestReply;

    private ClientAppGateway clientAppGateway = new ClientAppGateway() {
        @Override
        public void onTravelRefundRequestArrived(TravelRefundRequest travelRefundRequest) {

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

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

}
