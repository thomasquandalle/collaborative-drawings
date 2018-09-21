package client;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.PixelFormat;
import javafx.scene.image.WritableImage;
import utils.DrawingInstruction;

import java.util.Iterator;
import java.util.Vector;

public class ClientCanvas extends Canvas {

    private Vector<DrawingInstruction> instructionsList;

    public ClientCanvas(){
        instructionsList = new Vector <>();
    }

    private void draw() {
        synchronized (instructionsList) {
            GraphicsContext g = getGraphicsContext2D();
            Iterator<DrawingInstruction> iterator = instructionsList.iterator();
            while (iterator.hasNext()) {
                DrawingInstruction current = iterator.next();
                g.setFill(current.getColor());
                int x = (int) Math.ceil(current.getX() * getWidth());
                int y = (int) Math.ceil(current.getY() * getHeight());
                if(current.getShape() == Shape.SQUARE){
                    g.fillRect(x, y, current.getSize(), current.getSize());
                }
                else if(current.getShape() == Shape.CIRCLE){
                    g.fillOval(x , y, current.getSize(), current.getSize());
                }

            }

            instructionsList = new Vector<>();
        }
    }

    public void update(){
        draw();
    }

    public void importImage(byte[] buffer, int width, int height){
        WritableImage background  = new WritableImage((int)getWidth(),(int) getHeight());
        background.getPixelWriter().setPixels(0,0, width, height, PixelFormat.getByteBgraInstance(), buffer, 0, width*4);
        getGraphicsContext2D().drawImage(background, 0,0);

    }


    public void addInstruction(DrawingInstruction instruction) {
        instructionsList.add(instruction);
    }

    public void clear(){
        GraphicsContext g = getGraphicsContext2D();
        g.clearRect(0,0, getWidth(), getHeight());
    }
}
