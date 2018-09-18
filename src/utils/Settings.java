package utils;

import java.io.Serializable;

public class Settings implements Serializable {

    private String username;
    private String ipAddress;
    private String port;

    public Settings(String username, String ipAddress, String port){
        this.username = username;
        this.ipAddress = ipAddress;
        this.port = port;
    }

    public String[] getSettings(){
        String[] result = {username, ipAddress, port};
        return result;
    }
}
