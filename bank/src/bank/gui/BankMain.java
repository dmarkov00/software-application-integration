package bank.gui;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ListView;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class BankMain extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("bank.fxml"));
        primaryStage.setTitle("BANK - ABN Amro");

        primaryStage.setScene(new Scene(root, 600,350));
        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }
}
