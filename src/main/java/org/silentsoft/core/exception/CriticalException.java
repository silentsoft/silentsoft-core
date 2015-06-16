package org.silentsoft.core.exception;

import org.slf4j.helpers.MessageFormatter;

public class CriticalException extends Exception {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -8448482622499103299L;

	public CriticalException(String message) {
		super(message);
	}
	
	public CriticalException(Throwable cause) {
		super(cause);
	}
	
	public CriticalException(String message, Throwable cause) {
		super(message, cause);
	}

	public CriticalException(String message, Object[] parameters) {
		super(MessageFormatter.arrayFormat(message, parameters).getMessage());
	}
	
	public CriticalException(String message, Object[] parameters, Throwable cause) {
		super(MessageFormatter.arrayFormat(message, parameters).getMessage(), cause);
	}
}
