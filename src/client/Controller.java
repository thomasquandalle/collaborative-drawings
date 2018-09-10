package client;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import utils.DrawingInstruction;
import utils.Message;

import java.io.*;
import java.util.Vector;


public class Controller
{
    /* =================================
            Constant variables
==================================== */

    private static final int MAX_SIZE = 20;
    private static final int INITIAL_SIZE = 5;
    private static final String SYSTEM_NAME = "System";

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
    private ComboBox<Integer> sizePicker;
    @FXML
    private Button connect;
    @FXML
    private ToggleButton circleShape;
    @FXML
    private HBox shapeBox;

    /* ==========================
            Custom variables
    ============================== */

    private ClientSocket socket;
    private SocketListener listener;
    private boolean connected;
    private String userName;
    private ToggleButton selected;

    public Controller()
    {
        listener = new SocketListener(this);
    }

    void addMessage(Message message){
        //First message
        if(outputText.getText().equalsIgnoreCase("")){
            outputText.setText(message.getMessage());
            return;
        }
        outputText.setText(outputText.getText()+"\n"+message.getMessage());
    }

    void addInstruction(DrawingInstruction instruction){
        clientCanvas.addInstruction(instruction);
        clientCanvas.update();
    }

    void disconnectSocket(){
        if(connected){
            socket.disconnect();
            connected = false;
        }
        clientCanvas.setDisable(true);
        chatEntry.setDisable(true);
        connect.setDisable(false);
        addMessage(new Message(SYSTEM_NAME, "You've been disconnected from the server"));
    }

    @FXML
    private void initialize()
    {
        selected = circleShape;
        selected.setSelected(true);

        //Initializing ComboBox
        for(int i = 1; i<MAX_SIZE; i++ ){
            sizePicker.getItems().add(i);
        }
        sizePicker.setValue(INITIAL_SIZE);

        //Disable as long as you're not connected
        clientCanvas.setDisable(true);
        chatEntry.setDisable(true);


        clientCanvas.addEventHandler(MouseEvent.ANY,
                e -> {
            if(e.getEventType() == MouseEvent.MOUSE_DRAGGED){
                double relativeX = ((e.getX() + 0.0) / clientCanvas.getWidth());
                double relativeY = ((e.getY() + 0.0) / clientCanvas.getHeight());
                if (relativeX <= 1 && relativeY <= 1 && relativeX >= 0 && relativeY >= 0) {
                    socket.sendInstruction(relativeX, relativeY, sizePicker.getValue(), Shape.getShapeFromId(selected.getId()), colorPicker.getValue());
                }
            }
                });

        colorPicker.setValue(new Color(0,0,0,1));

        userName = username.getText();
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
        connect.setDisable(true);
        try {
            socket = new ClientSocket(listener);
            chatEntry.setDisable(false);
            clientCanvas.setDisable(false);
            connected  = true;
            socket.sendMessage(username.getText() + " just connected to the server", SYSTEM_NAME);
        } catch (IOException e) {
            addMessage(new Message(SYSTEM_NAME, "Couldn't connect to the server, please try again"));
            connect.setDisable(false);
        }
    }

    @FXML
    private void setUsername() {
        String trimmedUsername = username.getText().trim();
        if(trimmedUsername.equalsIgnoreCase("")){
            username.setText("Anonymous");
            return;
        }
        socket.sendMessage( userName + " is now known as " + trimmedUsername, SYSTEM_NAME);
        userName = trimmedUsername;
        username.setText(trimmedUsername);

    }

    @FXML
    private void toggle(ActionEvent actionEvent) {
        ToggleButton event = (ToggleButton) actionEvent.getSource();
        selected.setSelected(false);
        selected = event;
        event.setSelected(true);
    }

    @FXML
    private void save(){
        FileChooser saveDialog = new FileChooser();
        saveDialog.setTitle("Save your drawing");
        File path = saveDialog.showSaveDialog(shapeBox.getScene().getWindow());
        if(path != null && path.canWrite()){
            try {
                FileOutputStream fileWrite = new FileOutputStream(path);
                System.out.println(fileWrite);
                ObjectOutputStream out = new ObjectOutputStream(fileWrite);
                Vector<DrawingInstruction> log = socket.getLog();
                out.writeObject(log);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @FXML
    private void load(){
        FileChooser saveDialog = new FileChooser();
        saveDialog.setTitle("Open your drawing");
        File path = saveDialog.showOpenDialog(shapeBox.getScene().getWindow());
        if(path != null){
            try {
                FileInputStream fileWrite = new FileInputStream(path);
                Vector <DrawingInstruction> test;
                try (ObjectInputStream out = new ObjectInputStream(fileWrite)) {
                    test = (Vector <DrawingInstruction>) out.readObject();
                    socket.readLog(test);
                }
                catch(StreamCorruptedException e){
                    e.getLocalizedMessage();
                    addMessage(new Message("System", "Invalid file chosen"));
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
