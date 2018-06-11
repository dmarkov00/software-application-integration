package broker.gui;

import broker.backend.ClientAppGateway;
import javafx.fxml.Initializable;

import java.net.URL;
import java.util.ResourceBundle;

public class BrokerController implements Initializable {

    private ClientAppGateway clientAppGateway = new ClientAppGateway() {
        @Override
        public void onTravelRefundRequestArrived(String travelRefundRequest) {
            System.out.println(travelRefundRequest);
        }
    };

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

}
