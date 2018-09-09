package client;
import java.io.IOException;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;

import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

public class Main extends Application
{
    public static void main(String[] args)
    {
        Application.launch(args);
    }

    @Override
    public void start(Stage stage) throws IOException
    {
        // Create the Pane and all Details
        FXMLLoader loader = new FXMLLoader(getClass().getResource("client.fxml"));
        Pane root =  loader.load();
        stage.setMaxWidth(1200);
        stage.setMaxHeight(800);
        // Create the Scene
        Scene scene = new Scene(root);
        // Set the Scene to the Stage
        stage.setScene(scene);
        // Set the Title to the Stage
        stage.setTitle("Collaborative drawings with JavaFx");

        //Need to disconnect the sockets when leaving
        stage.setOnCloseRequest(event -> {
            Controller connectedController = (Controller) (loader.getController());
            connectedController.disconnectSocket();
        });
        // Display the Stage
        stage.show();


    }
}
