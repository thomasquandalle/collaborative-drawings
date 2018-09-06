package utils;

import java.awt.Color;
import java.io.Serializable;

public class DrawingInstruction implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int x, y, size;
	private String shape;
	private Color color;

	public DrawingInstruction(int x, int y, int size, String shape, Color color) {
		this.x = x;
		this.y = y;
		this.size = size;
		this.shape = shape;
		this.color = color;

	}
	
	public String toString() {
		return ("Instruction: \n"
				+ "x: " + x + "\n"
				+ "y: " + y + "\n"
				+ "shape: " + shape + "\n"
				+ "size: " + size + "\n"
				+ "color: " + color.toString() + "\n");
	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}

	public int getSize() {
		return size;
	}

	public String getShape() {
		return shape;
	}

	public Color getColor() {
		return color;
	}
}
