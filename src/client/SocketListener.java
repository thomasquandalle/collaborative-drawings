package client;

import utils.DrawingInstruction;
import utils.Message;

import java.io.IOException;

public class SocketListener {
    private Controller controller;

    public SocketListener(Controller controller){
        this.controller = controller;
    }

    public void handleRecep(Object reception) {
            if(reception instanceof DrawingInstruction) {
                controller.addInstruction((DrawingInstruction) reception);
            }
            if(reception instanceof Message) {
                controller.addMessage((Message)(reception));
            }
    }
}
