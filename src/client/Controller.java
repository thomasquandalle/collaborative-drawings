package client;

import javafx.event.ActionEvent;
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
    private ColorPicker colorPicker;
    @FXML
    private Text outputText;
    @FXML
    private ClientCanvas clientCanvas;
    @FXML
    private TextField chatEntry;
    @FXML
    private TextField username;

    /* ==========================
            Custom variables
    ============================== */

    private ClientSocket socket;
    private SocketListener listener;

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

    public void disconnectSocket(){
        socket.disconnect();
    }

    @FXML
    private void initialize()
    {

        clientCanvas.addEventHandler(MouseEvent.MOUSE_DRAGGED,
                e -> {
                    double relativeX = ((e.getX() + 0.0) / clientCanvas.getWidth());
                    double relativeY = ((e.getY() + 0.0) / clientCanvas.getHeight());
                    if (relativeX <= 1 && relativeY <= 1 && relativeX >= 0 && relativeY >= 0) {
                        socket.sendInstruction(relativeX, relativeY, 10, "square", colorPicker.getValue());
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
        socket.sendMessage(chatEntry.getText(), username.getText());
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

    @FXML
    private void setUsername() {
        String trimmedUsername = username.getText().trim();
        if(trimmedUsername.equalsIgnoreCase("")){
            username.setText("Anonymous");
            return;
        }
        username.setText(trimmedUsername);
        username.setDisable(true);
    }
}
