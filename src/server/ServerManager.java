package server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

import javax.swing.JFrame;

import client.ClientSocket;

public class ServerManager extends JFrame {
	ServerSocket socket;

	public ServerManager() {
		super("Server manager");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		try {
			socket = new ServerSocket(9999);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		ServerManager server = new ServerManager();
		server.setVisible(true);
		try {
			Socket client = server.socket.accept();
			ObjectInputStream input = new ObjectInputStream(client.getInputStream());
			ObjectOutputStream output = new ObjectOutputStream(client.getOutputStream());
			while(true) {
				output.writeObject(input.readObject());
				output.reset();
				output.flush();
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();

		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
}
