package client;

import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import utils.Message;

public class Chat extends JPanel implements ActionListener {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JTextField chatEntry;
	private JScrollPane scrollablePaneChat;
	private JLabel chatDisplay;
	private JScrollPane affichageInfosScroll;
	private JTextArea affichageInfos;
	private ClientSocket socket;
	public Chat() {
		super();
		affichageInfos = new JTextArea("Clients list");
		affichageInfos.setFocusable(false);
		chatEntry = new JTextField("");
		chatEntry.addActionListener(this);

		chatDisplay = new JLabel("");
		chatDisplay.setBackground(Color.white);
		scrollablePaneChat = new JScrollPane(chatDisplay);
		affichageInfosScroll = new JScrollPane(affichageInfos);

		GridBagLayout gridbagRes = new GridBagLayout();
		GridBagConstraints cRes = new GridBagConstraints();
		cRes.weightx = 0.5;
		cRes.weighty = 1;
		cRes.fill = GridBagConstraints.BOTH;
		setLayout(gridbagRes);
		add(affichageInfosScroll, cRes);
		cRes.gridwidth = GridBagConstraints.REMAINDER;
		cRes.weightx = 3;
		add(scrollablePaneChat, cRes);
		cRes.gridwidth = GridBagConstraints.REMAINDER;
		cRes.weighty = 0.1;
		add(chatEntry, cRes);
	}

	@Override
	public void actionPerformed(ActionEvent arg0) {
		socket.sendMessage(chatEntry.getText());
		chatEntry.setText("");
	}

	public void addTextMessage(Message message){
		chatDisplay.setText(chatDisplay.getText()+"\n"+ message.getMessage());
	}
	public void setSocket(ClientSocket networkSocket) {
		socket = networkSocket;		
	}

}
