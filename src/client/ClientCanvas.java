package client;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.util.Iterator;
import java.util.Vector;

import utils.DrawingInstruction;

public class ClientCanvas extends Canvas {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2822873274177188845L;
	private MouseMotionListener mouseMovement;
	private Vector<DrawingInstruction> instructionList;
	private ClientSocket socket;

	public ClientCanvas() {
		super();

		instructionList = new Vector<DrawingInstruction>();
		mouseMovement = new MouseMotionListener() {
			public void mouseMoved(MouseEvent e) {
			}

			@Override
			public void mouseDragged(MouseEvent e) {
				socket.sendInstruction(e.getX(), e.getY());
			}
		};

		setBackground(Color.WHITE);

		addMouseMotionListener(mouseMovement);
	}

	public void update(Graphics g) {
		paint(g);
	}

	public void paint(Graphics g) {
		Iterator<DrawingInstruction> iter = instructionList.iterator();
		while (iter.hasNext()) {
			DrawingInstruction current = iter.next();
			g.setColor(current.getColor());
			g.fillRect(current.getX(), current.getY(), current.getSize(), current.getSize());
		}
		instructionList = new Vector<DrawingInstruction>();
	}

	public void setSocket(ClientSocket networkSocket) {
		socket = networkSocket;
	}
	public void addInstruction(DrawingInstruction instruction) {
		instructionList.add(instruction);
	}
}
