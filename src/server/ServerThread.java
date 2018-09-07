package server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.SocketException;


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
		while (true) {
			try {
				parent.sendToClients(clientInput.readObject());
			} catch (ClassNotFoundException | IOException e) {
				if (e instanceof SocketException) {
					parent.disconnectClient(this);
					break;
				}
				e.printStackTrace();
			}
		}
	}

	public void sendToClient(Object toSend) throws IOException {
		clientOutput.writeObject(toSend);
		clientOutput.reset();
		clientOutput.flush();
	}

}
