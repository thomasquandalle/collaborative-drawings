package client;
/**
 * Tools panel for the left side of the client
 */

import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JColorChooser;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.colorchooser.AbstractColorChooserPanel;

public class Tools extends JPanel implements ActionListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	// The different tools we have
	private JButton circle;
	private JButton square;
	private JTextField sizeChoice;
	private JColorChooser colorChoice;
	private String shape = "•";
	private int size = 10;

	public Tools() {
		super();
		setLayout(new GridLayout(2, 2));

		circle = new JButton("•");
		square = new JButton("▄");

		sizeChoice = new JTextField(size + "");

		colorChoice = new JColorChooser();
		// Keeping only the RVB panel
		colorChoice.setPreviewPanel(new JPanel());
		AbstractColorChooserPanel[] panels = colorChoice.getChooserPanels();
		for (int i = 0; i < panels.length; i++) {
			AbstractColorChooserPanel panel = panels[i];
			if (!panel.getDisplayName().equalsIgnoreCase("RVB")) {
				colorChoice.removeChooserPanel(panel);
			}
		}

		// Adding action listeners
		circle.addActionListener(this);
		square.addActionListener(this);
		sizeChoice.addActionListener(this);

		// Adding to the layout
		add(square);
		add(circle);
		add(sizeChoice);
		add(colorChoice);
	}

	public void setColorChooser(Color color) {
		this.colorChoice.setColor(color);
	}

	public Color getColorChosen() {
		return this.colorChoice.getColor();
	}

	public int getSizePencil() {
		return size;
	}

	public String getShape() {
		return shape;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() instanceof JButton) {
			shape = ((JButton) (e.getSource())).getText();
		}
		if (e.getSource() == sizeChoice) {
			try {
				size = Integer.parseInt(sizeChoice.getText());
			} catch (NumberFormatException ex) {
				sizeChoice.setText(size + "");
			}
		}
	}

}
