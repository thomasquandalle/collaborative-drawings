package client;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

import javafx.scene.paint.Color;
import utils.DrawingInstruction;
import utils.Message;

public class ClientSocket extends Socket {

	private ObjectInputStream inputStream;
	private ObjectOutputStream outputStream;
	private SocketListener clientListener;

	public ClientSocket(SocketListener listener) throws UnknownHostException, IOException {
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
        ListenerThread listenerThread = new ListenerThread(inputStream, clientListener);
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
	
	public void sendMessage(String message) {
		Message toSend  = new Message("Thomas", message);
		try {
			outputStream.writeObject(toSend);
			outputStream.reset();
			outputStream.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}

class ListenerThread extends Thread{
    private ObjectInputStream inputStream;
    SocketListener inputHandler;
    public ListenerThread(ObjectInputStream stream, SocketListener handler){
        inputStream = stream;
        inputHandler = handler;
    }

    public void run(){
        while(true){
            try {
                inputHandler.handleRecep(inputStream.readObject());
            } catch (IOException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
    }
}
