package broker.gui;

import broker.backend.ClientAppGateway;
import javafx.fxml.Initializable;

import java.net.URL;
import java.util.ResourceBundle;

public class BrokerController  implements Initializable {



    @Override
    public void initialize(URL location, ResourceBundle resources) {
        ClientAppGateway clientAppGateway = new ClientAppGateway();
        clientAppGateway.setTravelRefundRequestListener();
    }
}
