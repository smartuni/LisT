/**
 * 
 */
package de.haw.mqttlistener.util;

/**
 * Exception fuer falsch geformtes JSON.
 * @author Lydia Pflug
 * 20.11.2017
 */
public final class JsonMalFormedException extends Exception {
	
	private static final long serialVersionUID = -567701589827694503L;

	public JsonMalFormedException(String json) {
		super(json);
	}

}
