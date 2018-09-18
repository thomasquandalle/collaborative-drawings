package client;

import controllers.Controller;
import utils.DrawingInstruction;
import utils.Message;

import java.io.IOException;
import java.util.Vector;

public class SocketListener {
    private Controller controller;
    private Vector<DrawingInstruction> drawingLog;
    public SocketListener(Controller controller){
        this.controller = controller;
        drawingLog = new Vector<>();
    }

    public void handleRecep(Object reception) {
            if(reception instanceof DrawingInstruction) {
                DrawingInstruction castReception = (DrawingInstruction) reception;
                addToDrawingLog(castReception);
                controller.addInstruction(castReception);
            }
            if(reception instanceof Message) {
                controller.addMessage((Message)(reception));
            }
            if(reception instanceof IOException) {
                controller.disconnectSocket();
            }
    }

    private void addToDrawingLog(DrawingInstruction reception) {
        drawingLog.add(reception);
    }

    public Vector<DrawingInstruction> getDrawingLog(){
        return drawingLog;
    }
}
