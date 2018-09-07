package client;

import java.awt.*;
import java.io.IOException;
import java.net.UnknownHostException;

import javax.swing.*;

import client.Tools;

public class ClientDisplay extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = -385365104285393596L;
	ClientCanvas drawing;
	Chat chatAndNetwork;
	Tools tools;
	Menu menu;
	ClientSocket networkSocket;
	
	public ClientDisplay() {

		// The window
		super("Collaborative drawing");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		GridLayout layout = new GridLayout(1,0);
		setBackground(Color.white);
		setLayout(layout);

		// Creating components
		menu = new Menu();
		tools = new Tools();
		drawing = new ClientCanvas();
		chatAndNetwork = new Chat();

		// Setting borders
		setJMenuBar(menu);
		add(tools);
		tools.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		tools.setColorChooser(Color.BLUE);
		add(drawing, layout);
		add(chatAndNetwork, layout);
		chatAndNetwork.setBorder(BorderFactory.createLineBorder(Color.black));
		
	}
	
	public void connect() {
		try {
			networkSocket = new ClientSocket();
			//Connecting the chat and the socket
			networkSocket.setChat(chatAndNetwork);
			chatAndNetwork.setSocket(networkSocket);
			
			//Connecting the canvas and the socket
			networkSocket.setCanvas(drawing);
			drawing.setSocket(networkSocket);
			
			//Connecting the tools panel and the socket. Only the socket needs the get methods from the tools
			networkSocket.setTools(tools);
			
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public static void main(String[] args) {
		ClientDisplay client = new ClientDisplay();
		client.connect();
		client.setBounds(0, 0, 2000, 2000);
		client.setVisible(true);
		while(true) {
			client.drawing.repaint();
			client.networkSocket.handleRecep();
		}

	}

}