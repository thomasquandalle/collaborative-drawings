package utils;

import java.io.Serializable;
import java.util.Date;

public class Message implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	String sender, message;
	Date date;
	public Message(String sender, String message) {
		this.sender = sender;
		this.message = message;
		date = new Date();
	}

	public String getMessage() {
		return date + " - " + sender + ": " + message;
	}
}
