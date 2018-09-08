package client;

import javafx.event.EventHandler;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.fxml.FXML;
import javafx.scene.control.ColorPicker;
import javafx.scene.text.Text;
import utils.DrawingInstruction;
import utils.Message;

import java.io.IOException;


public class Controller
{
    /* =================================
                FXLM variables
    ==================================== */
    @FXML
    // The reference of inputText will be injected by the FXML loader
    private ColorPicker colorPicker;

    // The reference of outputText will be injected by the FXML loader
    @FXML
    private Text outputText;

    @FXML
    private ClientCanvas clientCanvas;

    @FXML
    private TextField chatEntry;

    private ClientSocket socket;
    private SocketListener listener;
    /* ==========================
            Custom variables
    ============================== */


    public Controller()
    {
        listener = new SocketListener(this);
    }

    public void addMessage(Message message){
        outputText.setText(outputText.getText()+"\n"+message.getMessage());
    }

    public void addInstruction(DrawingInstruction instruction){
        clientCanvas.addInstruction(instruction);
        clientCanvas.update();
    }
    @FXML
    private void initialize()
    {
        final GraphicsContext gc = clientCanvas.getGraphicsContext2D();
        clientCanvas.addEventHandler(MouseEvent.MOUSE_DRAGGED,
                new EventHandler<MouseEvent>() {
                    @Override
                    public void handle(MouseEvent e) {
                        double relativeX = ((e.getX() + 0.0) / clientCanvas.getWidth());
                        double relativeY = ((e.getY() + 0.0) / clientCanvas.getHeight());
                        if (relativeX <= 1 && relativeY <= 1 && relativeX >= 0 && relativeY >= 0) {
                            socket.sendInstruction(relativeX, relativeY, 10, "square", colorPicker.getValue());
                        }
                    }
                });
        colorPicker.setValue(new Color(0,0,0,1));

        connect();
    }


    @FXML
    private void changeColor()
    {
        Color x = colorPicker.getValue();
        clientCanvas.getGraphicsContext2D().setFill(x);
    }

    @FXML
    private void sendMessage() {
        socket.sendMessage(chatEntry.getText());
        chatEntry.setText("");
    }

    @FXML
    private void connect(){
        try {
            socket = new ClientSocket(listener);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
