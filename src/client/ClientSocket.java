package client;

import gherkin.lexer.Listener;
import javafx.scene.paint.Color;
import utils.DrawingInstruction;
import utils.Message;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;

public class ClientSocket extends Socket {

	private ObjectInputStream inputStream;
	private ObjectOutputStream outputStream;
	private SocketListener clientListener;
	private ListenerThread listenerThread;

	public ClientSocket(SocketListener listener) throws IOException {
		super(InetAddress.getLocalHost(), 9999);
		initStreams();
        clientListener = listener;
		startListening();
	}
	
	private void initStreams() {
		try {
			outputStream = new ObjectOutputStream(getOutputStream());
			inputStream = new ObjectInputStream(getInputStream());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	private void startListening(){
        listenerThread = new ListenerThread(inputStream, clientListener);
        listenerThread.start();
    }

	public void sendInstruction(double relativeX, double relativeY, int size, String shape, Color color) {
		DrawingInstruction toSend  = new DrawingInstruction(relativeX, relativeY, size, shape, color);
		try {
			outputStream.writeObject(toSend);
			outputStream.reset();
			outputStream.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void sendMessage(String message, String sender) {
	    String trimmedMessage = message.trim();
	    if(trimmedMessage.equalsIgnoreCase("")){
	        return;
        }
		Message toSend  = new Message(sender, trimmedMessage);
		try {
			outputStream.writeObject(toSend);
			outputStream.reset();
			outputStream.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

    public void disconnect() {
        listenerThread.disconnect();
        try {
            inputStream.close();
            outputStream.close();
            close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

class ListenerThread extends Thread{
    private ObjectInputStream inputStream;
    private SocketListener inputHandler;
    private boolean connected; //Will be useful in the future to disconnect

    public ListenerThread(ObjectInputStream stream, SocketListener handler){
        inputStream = stream;
        inputHandler = handler;
        connected = true;
    }

    public void run(){
        while(connected){
            try {
                inputHandler.handleRecep(inputStream.readObject());
            } catch (IOException e) {
                disconnect();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
        return;
    }

    public void disconnect(){
        connected = false;
    }
}
