package utils;

import client.Shape;
import javafx.scene.paint.Color;
import java.io.Serializable;

public class DrawingInstruction implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private double x, y;
	private int size;
	private double red, green, blue;
	private Shape shape;

	public DrawingInstruction(double relativeX, double relativeY, int size, Shape shape, Color color) {
		this.x = relativeX;
		this.y = relativeY;
		this.size = size;
		this.shape = shape;
		this.red = color.getRed();
		this.green = color.getGreen();
		this.blue = color.getBlue();

	}
	
	public String toString() {
		return ("Instruction: \n"
				+ "x: " + x + "\n"
				+ "y: " + y + "\n"
				+ "shape: " + shape + "\n"
				+ "size: " + size + "\n"
				+ "color: " + red + "," + green + "," + blue + "\n");
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

	public Shape getShape() {
		return shape;
	}

	public Color getColor() {
		return new Color(red, green, blue, 1);
	}
}
