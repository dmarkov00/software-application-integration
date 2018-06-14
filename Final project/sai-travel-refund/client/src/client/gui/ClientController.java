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
import models.client.TravelRefundReply;
import models.client.TravelRefundRequest;

import java.net.URL;
import java.util.HashMap;
import java.util.Map;
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

    private Map<String, ClientListLine> messageIDToClientListLineMap = new HashMap<>();

    private BrokerAppGateway brokerAppGateway = new BrokerAppGateway() {
        @Override
        public void onTravelRefundReplyArrived(TravelRefundReply travelRefundReply, String correlationID) {

            // Retrieve the list line with the same message Id as the correlation id
            ClientListLine clientListLine = findListLineByCorrelationID(correlationID);

            // Update the list line with the new reply
            clientListLine.setReply(travelRefundReply);

            lvRequestReply.refresh();
        }
    };

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        cbTravelMode.getItems().addAll("car", "public transport");
        cbTravelMode.getSelectionModel().select(0);
        jcbModeItemStateChanged();

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

        // Call the app gateway and send a request
        String messageID = brokerAppGateway.requestTravelRefund(travelRefundRequest);

        //Create a ListView line with the request and add it to the list view
        ClientListLine listViewLine = new ClientListLine(travelRefundRequest, null);

        //Update the map, so later we can correlate with the reply
        messageIDToClientListLineMap.put(messageID, listViewLine);

        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                lvRequestReply.getItems().add(listViewLine);
            }
        });
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

    private ClientListLine findListLineByCorrelationID(String correlationID) {

        for (Map.Entry<String, ClientListLine> listLine : messageIDToClientListLineMap.entrySet()) {
            if (listLine.getKey().equals(correlationID)) {
                return listLine.getValue();
            }
        }
        return null;
    }


    //    private ClientListLine getRequestReply(TravelRefundRequest request) {
//
//        for (int i = 0; i < lvRequestReply.getItems().size(); i++) {
//            ClientListLine rr = lvRequestReply.getItems().get(i);
//            if (rr.getRequest() == request) {
//                return rr;
//            }
//        }
//
//        return null;
//    }
}
