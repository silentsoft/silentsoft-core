package org.silentsoft.core.exception;

import org.slf4j.helpers.MessageFormatter;

public class ActionException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3581071804406013120L;
	
	public ActionException(String message) {
		super(message);
	}
	
	public ActionException(Throwable cause) {
		super(cause);
	}
	
	public ActionException(String message, Throwable cause) {
		super(message, cause);
	}

	public ActionException(String message, Object[] parameters) {
		super(MessageFormatter.arrayFormat(message, parameters).getMessage());
	}
	
	public ActionException(String message, Object[] parameters, Throwable cause) {
		super(MessageFormatter.arrayFormat(message, parameters).getMessage(), cause);
	}
}
