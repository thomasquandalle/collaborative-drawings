package client;

import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import java.net.InetAddress;
import java.net.UnknownHostException;

public class connectController {

    public TextField port;
    public TextField address;



    public connectController(){

    }


    public void initialize(){
        try {
            address.setText( InetAddress.getLocalHost().getHostAddress());
        } catch (UnknownHostException e) {
        }
            port.setText(9999+"");
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
