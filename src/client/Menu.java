package client;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

public class Menu extends JMenuBar implements ActionListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	JMenu file;
	JMenuItem open;
	JMenuItem save;
	JMenuItem disconnect;

	public Menu() {
		super();

		file = new JMenu("File");
		open = new JMenuItem("Open");
		open.addActionListener(this);
		disconnect = new JMenuItem("Quit");
		disconnect.addActionListener(this);
		save = new JMenuItem("Save");
		save.addActionListener(this);
		add(file);
		file.add(save);
		file.add(open);
		file.add(disconnect);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == disconnect) {
			System.exit(0);
		}
	}

}
