package controllers;

import javafx.scene.control.TextField;
import utils.Settings;

public class SettingsController extends PopUpController{

    public TextField port;
    public TextField ipAddress;
    public TextField username;


    public SettingsController(){

    }


    public void initialize(){

    }

    public Settings getSettings(){
        return new Settings(username.getText(), ipAddress.getText(), port.getText());
    }
    public void setSettings(Settings settings){
        String[] args = settings.getSettings();
        username.setText(args[0]);
        ipAddress.setText(args[1]);
        port.setText(args[2]);
    }
}
