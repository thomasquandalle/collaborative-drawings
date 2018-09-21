package client;

import javafx.scene.paint.Color;
import sun.nio.ch.Net;
import utils.DrawingInstruction;
import utils.Message;
import utils.NetworkImage;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.util.Iterator;
import java.util.Vector;

public class ClientSocket extends Socket {

	private ObjectInputStream inputStream;
	private ObjectOutputStream outputStream;
	private SocketListener clientListener;
	private ListenerThread listenerThread;
    private Vector <DrawingInstruction> test;


    public ClientSocket(SocketListener listener, String ipAddress, int port) throws IOException {
	    super(InetAddress.getByName(ipAddress), port);
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

	public void sendInstruction(double relativeX, double relativeY, int size, Shape shape, Color color) {
		DrawingInstruction toSend  = new DrawingInstruction(relativeX, relativeY, size, shape, color);
		try {
            sendToServer(toSend);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

    public void sendInstruction(DrawingInstruction toSend) {
        try {
            sendToServer(toSend);
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
			sendToServer(toSend);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void sendImage(byte [] pixelsBuffer, int width, int height){
        NetworkImage toSend  = new NetworkImage(pixelsBuffer, width, height);
        try {
            sendToServer(toSend);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

	private void sendToServer(Object toSend) throws IOException {
        outputStream.writeObject(toSend);
        outputStream.reset();
        outputStream.flush();
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

    public Vector<DrawingInstruction> getLog() {
        return clientListener.getDrawingLog();
    }

    public void readInstructionLog(Vector<DrawingInstruction> test) {
        this.test = test;
        Iterator<DrawingInstruction> iter = test.iterator();
	    while(iter.hasNext()){
            sendInstruction(iter.next());
        }
    }

    public Vector<NetworkImage> getImage() {
	    return clientListener.getImage();
    }

    public void readImageLog(Vector<NetworkImage> toSendImage) throws IOException {
        Iterator<NetworkImage> iter = toSendImage.iterator();
        while(iter.hasNext()){
            sendToServer(iter.next());
        }
    }
}

class ListenerThread extends Thread{
    private ObjectInputStream inputStream;
    private SocketListener inputHandler;
    private boolean connected; //Will be useful in the future to disconnect

    ListenerThread(ObjectInputStream stream, SocketListener handler){
        inputStream = stream;
        inputHandler = handler;
        connected = true;
    }

    public void run(){
        while(connected){
            try {
                inputHandler.handleRecep(inputStream.readObject());
            } catch (IOException e) {
                inputHandler.handleRecep(e);
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    void disconnect(){
        connected = false;
    }

}
