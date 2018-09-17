package client;

import javafx.scene.control.TextField;

import java.net.InetAddress;
import java.net.UnknownHostException;

public class ConnectController {

    public TextField port;
    public TextField address;



    public ConnectController(){

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
