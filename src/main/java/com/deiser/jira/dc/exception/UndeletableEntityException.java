package com.deiser.jira.dc.exception;

/**
 * Custom exception
 */
public class UndeletableEntityException extends RuntimeException {

	// Serial
	private static final long	serialVersionUID	= -5331873901410036305L;


	/**
	 * Constructor
	 */
	public UndeletableEntityException() {
		super();
	}


	/**
	 * Constructor with message
	 * 
	 * @param message
	 *            The message to set
	 */
	public UndeletableEntityException(String message) {
		super(message);
	}


	/**
	 * Constructor with exception
	 * 
	 * @param cause
	 *            The exception to set
	 */
	public UndeletableEntityException(Throwable cause) {
		super(cause);
	}


	/**
	 * Constructor with message and exception
	 * 
	 * @param message
	 *            The message to set
	 * @param cause
	 *            The exception to set
	 */
	public UndeletableEntityException(String message, Throwable cause) {
		super(message, cause);
	}

}
