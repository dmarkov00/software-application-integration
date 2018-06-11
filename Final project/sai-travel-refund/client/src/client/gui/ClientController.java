package client.gui;

import client.backend.BrokerAppGateway;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import models.client.Address;
import models.client.ClientTravelMode;
import models.client.TravelRefundRequest;

import java.net.URL;
import java.util.ResourceBundle;

public class ClientController implements Initializable {
    @FXML
    public TextField tfOriginStreet;
    @FXML
    public TextField tfOriginNumber;
    @FXML
    public TextField tfOriginCity;
    @FXML
    public TextField tfTeacher;
    @FXML
    public TextField tfDestinationStreet;
    @FXML
    public TextField tfDestinationNumber;
    @FXML
    public TextField tfDestinationCity;
    @FXML
    public TextField tfStudent;
    @FXML
    private ComboBox cbTravelMode;
    @FXML
    private TextField tfCosts;
    @FXML
    private Label lbCosts;
    @FXML
    private ListView<ClientListLine> lvRequestReply;

    private BrokerAppGateway brokerAppGateway = new BrokerAppGateway();

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

        // Retrieve data from fields
        Address origin = new Address(tfOriginStreet.getText(), Integer.parseInt(tfOriginNumber.getText()),
                tfOriginCity.getText());
        Address destination = new Address(tfDestinationStreet.getText(), Integer.parseInt(tfDestinationNumber.getText()),
                tfDestinationCity.getText());
        String teacher = tfTeacher.getText();
        String student = tfStudent.getText();
        double costs = Double.parseDouble(tfCosts.getText());

        // Create object
        TravelRefundRequest travelRefundRequest = new TravelRefundRequest(teacher, student, origin, destination, costs);

        // Call the app gateway
        brokerAppGateway.requestTravelRefund(travelRefundRequest);
    }

    @FXML
    private void jcbModeItemStateChanged() {
        int mode = cbTravelMode.getSelectionModel().getSelectedIndex();
        int costs;
        if (mode == ClientTravelMode.PUBLIC_TRANSPORT.ordinal()) {
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
