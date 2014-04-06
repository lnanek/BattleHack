package com.paypal.sdk.openidconnect;

import com.paypal.core.rest.JSONFormatter;

public class Error {

	/**
	 * A single ASCII error code from the following enum.
	 */
	private String error;
	
	/**
	 * A resource ID that indicates the starting resource in the returned results.
	 */
	private String errorDescription;
	
	/**
	 * A URI identifying a human-readable web page with information about the error, used to provide the client developer with additional information about the error.
	 */
	private String errorUri;
	
	/**
	 * Default Constructor
	 */
	public Error() {
	}

	/**
	 * Parameterized Constructor
	 */
	public Error(String error) {
		this.error = error;
	}
	
	/**
	 * Setter for error
	 */
	public void setError(String error) {
		this.error = error;
 	}
 	
 	/**
	 * Getter for error
	 */
	public String getError() {
		return this.error;
	}
	
	/**
	 * Setter for errorDescription
	 */
	public void setErrorDescription(String errorDescription) {
		this.errorDescription = errorDescription;
 	}
 	
 	/**
	 * Getter for errorDescription
	 */
	public String getErrorDescription() {
		return this.errorDescription;
	}
	
	/**
	 * Setter for errorUri
	 */
	public void setErrorUri(String errorUri) {
		this.errorUri = errorUri;
 	}
 	
 	/**
	 * Getter for errorUri
	 */
	public String getErrorUri() {
		return this.errorUri;
	}
	

	/**
	 * Returns a JSON string corresponding to object state
	 * 
	 * @return JSON representation
	 */
	public String toJSON() {
		return JSONFormatter.toJSON(this);
	}

	@Override
	public String toString() {
		return toJSON();
	}
}