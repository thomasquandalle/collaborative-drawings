package client;

import javafx.event.ActionEvent;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.fxml.FXML;
import javafx.scene.text.Text;
import javafx.util.Callback;
import utils.DrawingInstruction;
import utils.Message;

import java.io.IOException;


public class Controller
{
    /* =================================
            Constant variables
==================================== */

    private final int MAX_SIZE = 20;
    private final int INITIAL_SIZE = 5;

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
    @FXML
    ComboBox<Integer> sizePicker;

    /* ==========================
            Custom variables
    ============================== */

    private ClientSocket socket;
    private SocketListener listener;

    public Controller()
    {
        listener = new SocketListener(this);
    }

    void addMessage(Message message){
        outputText.setText(outputText.getText()+"\n"+message.getMessage());
    }

    void addInstruction(DrawingInstruction instruction){
        clientCanvas.addInstruction(instruction);
        clientCanvas.update();
    }

    void disconnectSocket(){
        socket.disconnect();
    }

    @FXML
    private void initialize()
    {

        //Initializing ComboBox
        for(int i = 1; i<MAX_SIZE; i++ ){
            sizePicker.getItems().add(i);
        };
        sizePicker.setValue(INITIAL_SIZE);

        clientCanvas.addEventHandler(MouseEvent.MOUSE_DRAGGED,
                e -> {
                    double relativeX = ((e.getX() + 0.0) / clientCanvas.getWidth());
                    double relativeY = ((e.getY() + 0.0) / clientCanvas.getHeight());
                    if (relativeX <= 1 && relativeY <= 1 && relativeX >= 0 && relativeY >= 0) {
                        socket.sendInstruction(relativeX, relativeY, sizePicker.getValue(), "square", colorPicker.getValue());
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
