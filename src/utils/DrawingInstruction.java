package utils;

import java.awt.Color;

public class DrawingInstruction {
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
