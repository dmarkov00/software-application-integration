package loanbroker.gui;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class LoanBrokerMain extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("loanclient.fxml"));
        primaryStage.setTitle("Loan Client");
        primaryStage.setScene(new Scene(root, 500,300));
        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }
}