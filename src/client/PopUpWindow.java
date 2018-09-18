package client;

import controllers.ConnectController;
import controllers.PopUpController;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.DialogPane;
import javafx.scene.layout.Pane;

import java.io.IOException;

public class PopUpWindow extends Dialog<ButtonType>{
    FXMLLoader loader;

    public PopUpWindow(String fxmlFile, String title,String[] buttonsText, ButtonBar.ButtonData[] types){
        super();
        loader = new FXMLLoader(getClass().getResource(fxmlFile));
        Pane root = null;
        try {
            root = loader.load();
        } catch (IOException e) {
            System.out.println("Couldn't load the fxml file");
            return;
        }
        setTitle(title);
        //Adding the buttons
        DialogPane dialogPane = getDialogPane();
        dialogPane.setContent(root);

        final int numberOfButtons = Math.min(buttonsText.length, types.length);
        for(int i=0; i<numberOfButtons; i++){
            ButtonType button = new ButtonType(buttonsText[i], types[i]);
            dialogPane.getButtonTypes().add(button);
        }
    }

    public PopUpController getController(){
        return loader.getController();
    }


}
