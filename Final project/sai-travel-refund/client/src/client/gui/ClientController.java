package client.gui;

import client.model.Address;
import client.model.ClientTravelMode;
import client.model.TravelRefundReply;
import client.model.TravelRefundRequest;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;

import java.net.URL;
import java.util.ResourceBundle;

public class ClientController implements Initializable {
    @FXML
    private ComboBox cbTravelMode;
    @FXML
    private TextField tfCosts;
    @FXML
    private Label lbCosts;
    @FXML
    private ListView<ClientListLine> lvRequestReply;



    @Override
    public void initialize(URL location, ResourceBundle resources) {
        cbTravelMode.getItems().addAll(
                "car",
                "public transport"
        );
        cbTravelMode.getSelectionModel().select(0);
        jcbModeItemStateChanged();
    }

    private ClientListLine getRequestReply(TravelRefundRequest request) {

        for (int i = 0; i < lvRequestReply.getItems().size(); i++) {
            ClientListLine rr = lvRequestReply.getItems().get(i);
            if (rr.getRequest() == request) {
                return rr;
            }
        }

        return null;
    }
    @FXML
    private void jbSendActionPerformed() {
            // TO DO create and send the TravelRefundRequest
        System.out.println("ho");
    }

    @FXML
    private void jcbModeItemStateChanged() {
        int mode = cbTravelMode.getSelectionModel().getSelectedIndex();
        int costs;
        if (mode == ClientTravelMode.PUBLIC_TRANSPORT.ordinal()){
            costs = 60;
            tfCosts.setEditable(true);
            tfCosts.setVisible(true);
            lbCosts.setVisible(true);
        } else {
            costs = 0;
            tfCosts.setEditable(false);
            tfCosts.setVisible(false);
            lbCosts.setVisible(false);
        }
        tfCosts.setText(Integer.toString(costs));
    }
}
