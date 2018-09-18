package utils;

import java.util.ArrayList;
import java.util.Arrays;

public enum Commands {

    CONNECT("/connect", "connect"),
    NICK("/nick", "setUsername"),
    ;

    private  String commandType;
    private String method;

    Commands(String commandType, String method) {
        this.commandType = commandType;
        this.method = method;
    }

    private String getCommandType(){
        return commandType;
    }

    private String getMethod(){
        return method;
    }


    static public  ArrayList<String> getMethodAndArgsString(String command, String[] args){
        ArrayList<String> response = new ArrayList <String>();
        Arrays.asList(Commands.values())
                .forEach(commandCheck -> {
           if(commandCheck.getCommandType().equalsIgnoreCase(command)){
               response.add(commandCheck.getMethod());
               response.addAll(new ArrayList <>(Arrays.asList(args)));
           }
        });
        return response;
    }


}
