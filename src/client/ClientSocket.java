package client;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import utils.DrawingInstruction;

public class ClientSocket extends Socket {

	private ObjectInputStream inputStream;
	private ObjectOutputStream outputStream;
	private Chat clientChat;
	private ClientCanvas drawing;
	private Tools tools;


	public ClientSocket() throws UnknownHostException, IOException {
		super(InetAddress.getLocalHost(), 9999);
		initStreams();
	}

	public ClientSocket(String arg0, int arg1) throws UnknownHostException, IOException {
		super(arg0, arg1);
		initStreams();
	}

	public ClientSocket(InetAddress arg0, int arg1) throws IOException {
		super(arg0, arg1);
		initStreams();
	}
	
	private void initStreams() {
		System.out.println("Initialzing");
		try {
			outputStream = new ObjectOutputStream(getOutputStream());
			System.out.println("Initialized");
			inputStream = new ObjectInputStream(getInputStream());
			System.out.println("Initialized");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	
	public void setChat(Chat chatAndNetwork) {
		clientChat = chatAndNetwork;
	}

	public void setCanvas(ClientCanvas drawing) {
		this.drawing = drawing;
	}

	public void setTools(Tools tools) {
		this.tools = tools;
	}

	public void sendInstruction(int x, int y) {
		DrawingInstruction toSend  = new DrawingInstruction(x, y, tools.getSizePencil(), tools.getShape(), tools.getColorChosen());
		System.out.println(toSend);
		try {
			outputStream.writeObject(toSend);
			outputStream.reset();
			outputStream.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void handleRecep() {
		try {
			Object reception = inputStream.readObject();
			if(reception instanceof DrawingInstruction) {
				drawing.addInstruction((DrawingInstruction) reception);
			}
			
		} catch (ClassNotFoundException | IOException e) {
			e.printStackTrace();
		}

		
	}

}
