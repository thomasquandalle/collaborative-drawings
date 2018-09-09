package client;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import utils.DrawingInstruction;

import java.util.Iterator;
import java.util.Vector;

public class ClientCanvas extends Canvas {

    private Vector<DrawingInstruction> instructionsList;

    public ClientCanvas(){
        instructionsList = new Vector <DrawingInstruction>();
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
                g.fillRect(x, y, current.getSize(), current.getSize());
            }

            instructionsList = new Vector<DrawingInstruction>();
        }
    }

    public void update(){
        draw();
    }

    public void addInstruction(DrawingInstruction instruction) {
        instructionsList.add(instruction);
    }
}
