package controllers;

import client.*;
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
import utils.Settings;

import java.io.*;
import java.net.InetAddress;
import java.net.UnknownHostException;
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
    private MenuItem connect;
    @FXML
    private ToggleButton circleShape;
    @FXML
    private HBox shapeBox;
    @FXML
    private MenuItem save;
    @FXML
    private MenuItem load;

    /* ==========================
            Custom variables
    ============================== */

    private ClientSocket socket;
    private SocketListener listener;
    private boolean connected;
    private String userName;
    private ToggleButton selected;

    private Settings settings;


    public Controller()
    {
        String localHostString = "";


        try{
            localHostString = InetAddress.getLocalHost().getHostAddress();
        }
        catch (UnknownHostException e) {
            e.printStackTrace();
        }

        listener = new SocketListener(this);
        File settingsFile = new File("settings.dat");

            if(!settingsFile.exists()){


            settings  = new Settings("Anonymous", localHostString, "9999");

            try {
                writeSettings();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        else {
            try {
                settings = (Settings) new ObjectInputStream(new FileInputStream(settingsFile)).readObject();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                settings  = new Settings("Anonymous", localHostString, "9999");
            }
        }
    }

    public void addMessage(Message message){
        //First message
        if(outputText.getText().equalsIgnoreCase("")){
            outputText.setText(message.getMessage());
            return;
        }
        outputText.setText(outputText.getText()+"\n"+message.getMessage());
    }

    public void addInstruction(DrawingInstruction instruction){
        clientCanvas.addInstruction(instruction);
        clientCanvas.update();
    }

    public void disconnectSocket(){
        if(connected){
            socket.disconnect();
            disableComponents();
            connected = false;
            clientCanvas.clear();
            connect.setText("Connect");
            addMessage(new Message(SYSTEM_NAME, "You've been disconnected from the server"));
        }
    }

    private void disableComponents(){
        clientCanvas.setDisable(true);
        chatEntry.setDisable(true);
        save.setDisable(true);
        load.setDisable(true);
    }

    private void ableComponents(){
        chatEntry.setDisable(false);
        clientCanvas.setDisable(false);
        load.setDisable(false);
        save.setDisable(false);
    }

    private void writeSettings() throws IOException{
        File settingsFile = new File("settings.dat");
        new ObjectOutputStream(new FileOutputStream(settingsFile)).writeObject(settings);
    }

    private void setUsername(String newName){
        username.setText(newName);
        setUsername();
    }


    @FXML
    private void initialize()
    {


        //INITIALIZE FIRST VALUES
        selected = circleShape;
        selected.setSelected(true);
        colorPicker.setValue(new Color(0,0,0,1));

        //Initializing ComboBox
        for(int i = 1; i<MAX_SIZE; i++ ){
            sizePicker.getItems().add(i);
        }
        sizePicker.setValue(INITIAL_SIZE);

        //DISABLE CONNECTION-REQUIRED COMPONENTS
        disableComponents();

        //INITIALIZE CANVAS LISTENER
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
        setUsername(settings.getSettings()[0]);
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

        if(connect.getText().equalsIgnoreCase("disconnect")){
            disconnectSocket();
            outputText.setText("");
            chatEntry.setText("");
            clientCanvas.clear();

            return;
        }

        disconnectSocket();

        //Load what's going to be in the connect dialog
        String[] buttonsText = {"Login", "Cancel"};
        ButtonBar.ButtonData[] types = {ButtonBar.ButtonData.OK_DONE, ButtonBar.ButtonData.CANCEL_CLOSE};

        PopUpWindow dialog = new PopUpWindow("../fxmlFiles/connect.fxml", "Connect", buttonsText , types);
        ConnectController controllerConnect = (ConnectController) dialog.getController();
        controllerConnect.setSettings(settings);
        dialog.showAndWait();
        ButtonBar.ButtonData resultData = dialog.getResult().getButtonData();


        String ipAdress = "";
        int port = -1;
        if (resultData == ButtonBar.ButtonData.OK_DONE) {

            ipAdress = controllerConnect.getIPAdress();
            port = controllerConnect.getPort();
        }

        if(resultData == ButtonBar.ButtonData.CANCEL_CLOSE){
            return;
        }
        connect.setDisable(true);
        connect.setText("Disconnect");
        try {
            //CONNECT
            socket = new ClientSocket(listener, ipAdress, port);

            //ABLE THE BUTTONS
           ableComponents();
            connected  = true;


            //SEND CONFIRMATION ON UI
            socket.sendMessage(username.getText() + " just connected to the server", SYSTEM_NAME);
        } catch (IOException e) {
            addMessage(new Message(SYSTEM_NAME, "Couldn't connect to the server, please try again"));
            connect.setText("Connect");
        }
        connect.setDisable(false);
    }

    @FXML
    private void setUsername() {
        //REMOVING USELESS BLANK CHARACTERS
        String trimmedUsername = username.getText().trim();

        //USERNAME VERIFICATION
        if(trimmedUsername.equalsIgnoreCase("")){
            username.setText("Anonymous");
            return;
        }

        if(connected){
            //SEND TO SERVER AND OTHER USERS THE CHANGE
            socket.sendMessage( userName + " is now known as " + trimmedUsername, SYSTEM_NAME);
        }


        //CHANGE THE VALUES IN THE CONTROLLER AND THE UI
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
        //Create file chooser
        FileChooser saveDialog = new FileChooser();
        saveDialog.setTitle("Open your drawing");

        //Get the path
        File path = saveDialog.showOpenDialog(shapeBox.getScene().getWindow());
        if(path != null){
            try {
                //Load the file in the software
                FileInputStream fileWrite = new FileInputStream(path);
                Vector <DrawingInstruction> toSend = new Vector <DrawingInstruction>();
                try {
                    //Try to read the log in the file
                    ObjectInputStream in = new ObjectInputStream(fileWrite);
                    toSend = (Vector <DrawingInstruction>) in.readObject();
                }
                catch(StreamCorruptedException e){
                    addMessage(new Message("System", "Invalid file chosen"));
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }

                //Send the log to the server
                socket.readLog(toSend);
            } catch (IOException e) {
                addMessage(new Message("System", "Couldn't load the file to the server"));
            }
        }
    }

    @FXML
    private void openSettings() {
        String[] buttonsText = {"Save", "Cancel"};
        ButtonBar.ButtonData[] types = {ButtonBar.ButtonData.OK_DONE, ButtonBar.ButtonData.CANCEL_CLOSE};
        PopUpWindow settingsWindow = new PopUpWindow("../fxmlFiles/settings.fxml", "Settings", buttonsText, types);
        ( (SettingsController) settingsWindow.getController()).setSettings(settings);
        settingsWindow.showAndWait();
        ButtonBar.ButtonData resultData = settingsWindow.getResult().getButtonData();
        if (resultData == ButtonBar.ButtonData.OK_DONE) {
            settings = ( (SettingsController) settingsWindow.getController()).getSettings();
            try {
                writeSettings();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if(resultData == ButtonBar.ButtonData.CANCEL_CLOSE){
            return;
        }

    }
}
