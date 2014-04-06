package com.paypal.exception;

/**
 * ClientActionRequiredException, encapsulates instances where client has to
 * take actions based or errors in API call.
 * 
 */
public class ClientActionRequiredException extends BaseException {

	/**
	 * Serial version UID
	 */
	private static final long serialVersionUID = -15345584654755445L;

	public ClientActionRequiredException(String message) {
		super(message);
	}

	public ClientActionRequiredException(String message, Throwable exception) {
		super(message, exception);
	}
}
