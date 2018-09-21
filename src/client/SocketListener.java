package client;

import controllers.Controller;
import utils.DrawingInstruction;
import utils.Message;
import utils.NetworkImage;

import java.io.IOException;
import java.util.Vector;

public class SocketListener {
    private Controller controller;
    private Vector<DrawingInstruction> drawingLog;
    private Vector<NetworkImage> imageLog;
    private Vector<String> order;
    public SocketListener(Controller controller){
        this.controller = controller;
        drawingLog = new Vector<>();
        imageLog = new Vector <>();
        order = new Vector<>();
    }

    public void handleRecep(Object reception) {
            if(reception instanceof DrawingInstruction) {
                DrawingInstruction castReception = (DrawingInstruction) reception;
                addToDrawingLog(castReception);
                controller.addInstruction(castReception);
                order.add("Instruction");
            }
            if(reception instanceof Message) {
                controller.addMessage((Message)(reception));
            }
            if(reception instanceof NetworkImage){
                controller.addImage((NetworkImage) reception);
                imageLog.add((NetworkImage) reception);
                order.add("Image");
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

    public Vector<NetworkImage> getImages() {
        return imageLog;
    }

    public Vector<String> getOrder(){
        return order;
    }
}
