package approval.gui;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

public class ApprovalMainInternshipAdministration extends Application {
    private final String APPLICATION_NAME = "Internship Administration";

    private final int WIDTH = 400;
    private final int HEIGHT = 280;

    @Override
    public void start(Stage primaryStage) throws Exception {
        primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent event) {
                Platform.exit();
                System.exit(0);
            }
        });

        FXMLLoader loader = new FXMLLoader(getClass().getResource("approval.fxml"));
        // Create a controller instance
        ApprovalController controller = new ApprovalController(this.APPLICATION_NAME);
        // Set it in the FXMLLoader
        loader.setController(controller);


        Parent root = loader.load();
        primaryStage.setTitle("Travel Approval - " + this.APPLICATION_NAME);

        primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent t) {
                Platform.exit();
                System.exit(0);
            }
        });
        primaryStage.setScene(new Scene(root, WIDTH, HEIGHT));
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
