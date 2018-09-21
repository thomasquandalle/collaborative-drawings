package server;

import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.SocketException;
import java.util.Iterator;
import java.util.Vector;

import utils.*;

public class ServerThread extends Thread {
	private ObjectInputStream clientInput;
	private ObjectOutputStream clientOutput;
	private ServerManager parent;

	public ServerThread(Socket client, ServerManager server) throws IOException {
		clientInput = new ObjectInputStream(client.getInputStream());
		clientOutput = new ObjectOutputStream(client.getOutputStream());
		parent = server;
	}

	public void run() {
		try {
			synchronizeClientWithServer();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		while (true) {
			try {
				parent.sendToClients(clientInput.readObject());
			} catch (ClassNotFoundException | IOException e) {
				if (e instanceof SocketException || e instanceof EOFException) {
					parent.disconnectClient(this);
					break;
				}
				e.printStackTrace();
			}
		}
	}

	private void synchronizeClientWithServer() throws IOException {
		Vector<DrawingInstruction> drawingLog = parent.getDrawingLog();
		Vector<Message> messageLog = parent.getMessageLog();
		Vector<NetworkImage> imageLog = parent.getImageLog();
		Iterator<Message> messageIterator = messageLog.iterator();
		while(messageIterator.hasNext()) {
			sendToClient(messageIterator.next());
		}
		Iterator<NetworkImage> imageIterator = imageLog.iterator();
		while(imageIterator.hasNext()) {
			sendToClient(imageIterator.next());
		}
		Iterator<DrawingInstruction> drawingIterator = drawingLog.iterator();
		while(drawingIterator.hasNext()) {
			sendToClient(drawingIterator.next());
		}
	}

	public synchronized void sendToClient(Object toSend) throws IOException {

		clientOutput.writeObject(toSend);
		clientOutput.reset();
		clientOutput.flush();
	}

}
