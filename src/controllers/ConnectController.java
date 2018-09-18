package controllers;

import javafx.scene.control.TextField;
import utils.Settings;

import java.net.InetAddress;
import java.net.UnknownHostException;

public class ConnectController extends PopUpController{

    public TextField port;
    public TextField address;



    public ConnectController(){

    }


    public void initialize(){
    }

    public void setSettings(Settings settings){
        address.setText(settings.getSettings()[1]);
        port.setText(settings.getSettings()[2]);
    }


    public String getIPAdress(){
        return address.getText();
    }

    public int getPort() {
        try{
            return Integer.parseInt(port.getText());
        }
        catch(Exception e){
            return -1;
        }
    }
}
