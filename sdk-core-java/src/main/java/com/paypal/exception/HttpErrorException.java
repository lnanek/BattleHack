package com.paypal.exception;

/**
 * HttpErrorException denotes errors that occur in HTTP call
 * 
 */
public class HttpErrorException extends BaseException {

	/**
	 * Serial version UID
	 */
	private static final long serialVersionUID = -4312358746964758546L;

	public HttpErrorException(String msg) {
		super(msg);
	}

	public HttpErrorException(String msg, Throwable exception) {
		super(msg, exception);
	}
}
