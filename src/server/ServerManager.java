package server;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Iterator;
import java.util.Vector;

import javax.swing.JFrame;
import javax.swing.JLabel;


public class ServerManager extends JFrame {

	private static final long serialVersionUID = 1L;
	ServerSocket socket;
	Vector<ServerThread> ThreadList;
	JLabel infos;
	String servInfo;
	
	public ServerManager() {
		super("Server manager");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(400, 300);
		ThreadList = new Vector<ServerThread>();
		infos = new JLabel("");
		add(infos);
		try {
			socket = new ServerSocket(9999);
		} catch (IOException e) {
			e.printStackTrace();
		}
		writeInfos();
	}
	
	private void writeInfos(){
		try {
			infos.setText(""
					+ "<html>"
						+ "<body>"
							+ "Server is launched <br />"
							+ "IP adress: "+ InetAddress.getLocalHost() +"<br />"
							+"Port: " + socket.getLocalPort() + "<br />"
							+"Clients connected: " + ThreadList.size()
						+ "</body>"
					+ "</html>");
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		ServerManager server = new ServerManager();
		
		server.setVisible(true);
		while (true) {
			try {
				server.addNewClient(server.socket.accept());
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

	}

	private void addNewClient(Socket accept) throws IOException {
		ServerThread newThread = new ServerThread(accept, this);
		ThreadList.add(newThread);
		newThread.start();
		writeInfos();
	}
	
	public void disconnectClient(ServerThread clientThread) {
		ThreadList.removeElement(clientThread);
		writeInfos();
	}
	
	public void sendToClients(Object toSend) throws IOException {
		Iterator<ServerThread> iter = ThreadList.iterator();
		while(iter.hasNext()) {
			iter.next().sendToClient(toSend);
		}
	}
}
