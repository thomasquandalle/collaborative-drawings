package utils;

import java.awt.Color;
import java.io.Serializable;

public class DrawingInstruction implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private double x, y;
	private int size;
	private String shape;
	private Color color;

	public DrawingInstruction(double relativeX, double relativeY, int size, String shape, Color color) {
		this.x = relativeX;
		this.y = relativeY;
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

	public double getX() {
		return x;
	}

	public double getY() {
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
