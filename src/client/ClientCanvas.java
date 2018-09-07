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
				ClientCanvas source = (ClientCanvas) (e.getSource());
				double relativeX = ((e.getX()+0.0) / source.getWidth());
				double relativeY = ((e.getY()+0.0) / source.getHeight());
				if(relativeX <= 1  && relativeY <=1 && relativeX >= 0 && relativeY>=0 ) {
					socket.sendInstruction(relativeX, relativeY);
				}
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
			adaptInstructionToCanvas();
			int x = (int) Math.ceil(current.getX()*getWidth());
			int y =  (int) Math.ceil(current.getY()*getHeight());
			g.fillRect(x , y, current.getSize(), current.getSize());
		}
		instructionList = new Vector<DrawingInstruction>();
	}

	private void adaptInstructionToCanvas() {
		
	}

	public void setSocket(ClientSocket networkSocket) {
		socket = networkSocket;
	}
	public void addInstruction(DrawingInstruction instruction) {
		instructionList.add(instruction);
	}
}
