package com.paypal.core.nvp;

import java.util.HashMap;
import java.util.Map;

import com.paypal.core.APICallPreHandler;
import com.paypal.core.Constants;
import com.paypal.core.CredentialManager;
import com.paypal.core.credential.CertificateCredential;
import com.paypal.core.credential.ICredential;
import com.paypal.core.credential.SignatureCredential;
import com.paypal.core.credential.ThirdPartyAuthorization;
import com.paypal.core.credential.TokenAuthorization;
import com.paypal.exception.ClientActionRequiredException;
import com.paypal.exception.InvalidCredentialException;
import com.paypal.exception.MissingCredentialException;
import com.paypal.sdk.exceptions.OAuthException;

/**
 * <code>PlatformAPICallPreHandler</code> is an implementation of
 * {@link APICallPreHandler} for NVP based API service
 * 
 */
public class PlatformAPICallPreHandler implements APICallPreHandler {

	/**
	 * Service Name
	 */
	private final String serviceName;

	/**
	 * API method
	 */
	private final String method;

	/**
	 * Raw payload from stubs
	 */
	private final String rawPayLoad;

	/**
	 * API Username for authentication
	 */
	private String apiUserName;

	/**
	 * {@link ICredential} for authentication
	 */
	private ICredential credential;

	/**
	 * Access token if any for authorization
	 */
	private String accessToken;

	/**
	 * TokenSecret if any for authorization
	 */
	private String tokenSecret;

	/**
	 * SDK Name used in tracking
	 */
	private String sdkName;

	/**
	 * SDK Version
	 */
	private String sdkVersion;

	/**
	 * PortName to which a particular operation is bound;
	 */
	private String portName;

	/**
	 * Internal variable to hold headers
	 */
	private Map<String, String> headers;

	/**
	 * Map used for to override ConfigManager configurations
	 */
	private Map<String, String> configurationMap = null;

	/**
	 * @deprecated Private Constructor
	 */
	private PlatformAPICallPreHandler(String rawPayLoad, String serviceName,
			String method) {
		super();
		this.rawPayLoad = rawPayLoad;
		this.serviceName = serviceName;
		this.method = method;
	}

	/**
	 * Private Constructor
	 * 
	 * @param rawPayLoad
	 * @param serviceName
	 * @param method
	 * @param configurationMap
	 */
	private PlatformAPICallPreHandler(String rawPayLoad, String serviceName,
			String method, Map<String, String> configurationMap) {
		super();
		this.rawPayLoad = rawPayLoad;
		this.serviceName = serviceName;
		this.method = method;
		this.configurationMap = configurationMap;
	}

	/**
	 * PlatformAPICallPreHandler
	 * 
	 * @deprecated
	 * @param serviceName
	 *            Service Name
	 * @param rawPayLoad
	 *            Payload
	 * @param method
	 *            API method
	 * @param apiUserName
	 *            API Username
	 * @param accessToken
	 *            Access Token
	 * @param tokenSecret
	 *            Token Secret
	 * @throws MissingCredentialException
	 * @throws InvalidCredentialException
	 */
	public PlatformAPICallPreHandler(String rawPayLoad, String serviceName,
			String method, String apiUserName, String accessToken,
			String tokenSecret) throws InvalidCredentialException,
			MissingCredentialException {
		this(rawPayLoad, serviceName, method);
		this.apiUserName = apiUserName;
		this.accessToken = accessToken;
		this.tokenSecret = tokenSecret;
		initCredential();
	}

	/**
	 * PlatformAPICallPreHandler
	 * 
	 * @deprecated
	 * @param serviceName
	 *            Service Name
	 * @param rawPayLoad
	 *            Payload
	 * @param method
	 *            API method
	 * @param credential
	 *            {@link ICredential} instance
	 */
	public PlatformAPICallPreHandler(String rawPayLoad, String serviceName,
			String method, ICredential credential) {
		this(rawPayLoad, serviceName, method);
		if (credential == null) {
			throw new IllegalArgumentException(
					"Credential is null in PlatformAPICallPreHandler");
		}
		this.credential = credential;
	}

	/**
	 * PlatformAPICallPreHandler
	 * 
	 * @param serviceName
	 *            Service Name
	 * @param rawPayLoad
	 *            Payload
	 * @param method
	 *            API method
	 * @param credential
	 *            {@link ICredential} instance
	 * @param sdkName
	 *            SDK Name
	 * @param sdkVersion
	 *            sdkVersion
	 * @param portName
	 *            Port Name
	 * @param configurationMap
	 */
	public PlatformAPICallPreHandler(String rawPayLoad, String serviceName,
			String method, ICredential credential, String sdkName,
			String sdkVersion, String portName,
			Map<String, String> configurationMap) {
		this(rawPayLoad, serviceName, method, configurationMap);
		if (credential == null) {
			throw new IllegalArgumentException(
					"Credential is null in PlatformAPICallPreHandler");
		}
		this.credential = credential;
		this.sdkName = sdkName;
		this.sdkVersion = sdkVersion;
		this.portName = portName;
	}

	/**
	 * PlatformAPICallPreHandler
	 * 
	 * @param serviceName
	 *            Service Name
	 * @param rawPayLoad
	 *            Payload
	 * @param method
	 *            API method
	 * @param apiUserName
	 *            API Username
	 * @param accessToken
	 *            Access Token
	 * @param tokenSecret
	 *            Token Secret
	 * @param sdkName
	 *            SDK Name
	 * @param sdkVersion
	 *            sdkVersion
	 * @param portName
	 *            Port Name
	 * @param configurationMap
	 * @throws MissingCredentialException
	 * @throws InvalidCredentialException
	 */
	public PlatformAPICallPreHandler(String rawPayLoad, String serviceName,
			String method, String apiUserName, String accessToken,
			String tokenSecret, String sdkName, String sdkVersion,
			String portName, Map<String, String> configurationMap)
			throws InvalidCredentialException, MissingCredentialException {
		this(rawPayLoad, serviceName, method, configurationMap);
		this.apiUserName = apiUserName;
		this.accessToken = accessToken;
		this.tokenSecret = tokenSecret;
		this.sdkName = sdkName;
		this.sdkVersion = sdkVersion;
		this.portName = portName;
		initCredential();
	}

	/**
	 * @return the sdkName
	 */
	public String getSdkName() {
		return sdkName;
	}

	/**
	 * @deprecated
	 * @param sdkName
	 *            the sdkName to set
	 */
	public void setSdkName(String sdkName) {
		this.sdkName = sdkName;
	}

	/**
	 * @return the sdkVersion
	 */
	public String getSdkVersion() {
		return sdkVersion;
	}

	/**
	 * @deprecated
	 * @param sdkVersion
	 *            the sdkVersion to set
	 */
	public void setSdkVersion(String sdkVersion) {
		this.sdkVersion = sdkVersion;
	}

	/**
	 * @return the portName
	 */
	public String getPortName() {
		return portName;
	}

	/**
	 * @deprecated
	 * @param portName
	 *            the portName to set
	 */
	public void setPortName(String portName) {
		this.portName = portName;
	}

	public Map<String, String> getHeaderMap() throws OAuthException {
		if (headers == null) {
			headers = new HashMap<String, String>();
			if (credential instanceof SignatureCredential) {
				SignatureHttpHeaderAuthStrategy signatureHttpHeaderAuthStrategy = new SignatureHttpHeaderAuthStrategy(
						getEndPoint());
				headers = signatureHttpHeaderAuthStrategy
						.generateHeaderStrategy((SignatureCredential) credential);
			} else if (credential instanceof CertificateCredential) {
				CertificateHttpHeaderAuthStrategy certificateHttpHeaderAuthStrategy = new CertificateHttpHeaderAuthStrategy(
						getEndPoint());
				headers = certificateHttpHeaderAuthStrategy
						.generateHeaderStrategy((CertificateCredential) credential);
			}
			headers.putAll(getDefaultHttpHeadersNVP());
		}
		return headers;
	}

	public String getPayLoad() {
		// No processing necessary for NVP return the raw payload
		return rawPayLoad;
	}

	public String getEndPoint() {
		String endPoint = searchEndpoint();
		if (endPoint != null) {
			if (endPoint.endsWith("/")) {
				endPoint += serviceName + "/" + method;
			} else {
				endPoint += "/" + serviceName + "/" + method;
			}
		} else if ((Constants.SANDBOX.equalsIgnoreCase(this.configurationMap
				.get(Constants.MODE).trim()))) {
			endPoint = Constants.PLATFORM_SANDBOX_ENDPOINT + serviceName + "/"
					+ method;
		} else if ((Constants.LIVE.equalsIgnoreCase(this.configurationMap.get(
				Constants.MODE).trim()))) {
			endPoint = Constants.PLATFORM_LIVE_ENDPOINT + serviceName + "/"
					+ method;
		}
		return endPoint;
	}

	public ICredential getCredential() {
		return credential;
	}

	public void validate() throws ClientActionRequiredException {
		String mode = configurationMap.get(Constants.MODE);
		if (mode == null && searchEndpoint() == null) {
			// Mandatory Mode not specified.
			throw new ClientActionRequiredException(
					"mode[production/live] OR endpoint not specified");
		}
		if ((mode != null)
				&& (!mode.trim().equalsIgnoreCase(Constants.LIVE) && !mode
						.trim().equalsIgnoreCase(Constants.SANDBOX))) {
			// Mandatory Mode not specified.
			throw new ClientActionRequiredException(
					"mode[production/live] OR endpoint not specified");
		}
	}

	/*
	 * Search a valid endpoint in the configuration, returning null if not found
	 */
	private String searchEndpoint() {
		String endPoint = this.configurationMap.get(Constants.ENDPOINT + "."
				+ getPortName()) != null ? this.configurationMap
				.get(Constants.ENDPOINT + "." + getPortName())
				: (this.configurationMap.get(Constants.ENDPOINT) != null ? this.configurationMap
						.get(Constants.ENDPOINT) : null);
		if (endPoint != null && endPoint.trim().length() <= 0) {
			endPoint = null;
		}
		return endPoint;
	}

	private ICredential getCredentials() throws InvalidCredentialException,
			MissingCredentialException {
		ICredential returnCredential = null;
		CredentialManager credentialManager = new CredentialManager(
				this.configurationMap);
		returnCredential = credentialManager.getCredentialObject(apiUserName);
		if (accessToken != null && accessToken.length() != 0) {
			ThirdPartyAuthorization tokenAuth = new TokenAuthorization(
					accessToken, tokenSecret);
			if (returnCredential instanceof SignatureCredential) {
				SignatureCredential sigCred = (SignatureCredential) returnCredential;
				sigCred.setThirdPartyAuthorization(tokenAuth);
			} else if (returnCredential instanceof CertificateCredential) {
				CertificateCredential certCred = (CertificateCredential) returnCredential;
				certCred.setThirdPartyAuthorization(tokenAuth);
			}
		}
		return returnCredential;
	}

	private Map<String, String> getDefaultHttpHeadersNVP() {
		Map<String, String> returnMap = new HashMap<String, String>();
		returnMap.put(Constants.PAYPAL_APPLICATION_ID_HEADER,
				getApplicationId());
		returnMap.put(Constants.PAYPAL_REQUEST_DATA_FORMAT_HEADER,
				Constants.PAYLOAD_FORMAT_NVP);
		returnMap.put(Constants.PAYPAL_RESPONSE_DATA_FORMAT_HEADER,
				Constants.PAYLOAD_FORMAT_NVP);
		returnMap.put(Constants.PAYPAL_REQUEST_SOURCE_HEADER, sdkName + "-"
				+ sdkVersion);

		String sandboxEmailAddress = this.configurationMap
				.get(Constants.SANDBOX_EMAIL_ADDRESS);
		if (sandboxEmailAddress != null) {
			returnMap.put(Constants.PAYPAL_SANDBOX_EMAIL_ADDRESS_HEADER,
					sandboxEmailAddress);
		}
		return returnMap;
	}

	private String getApplicationId() {
		String applicationId = null;
		if (credential instanceof CertificateCredential) {
			applicationId = ((CertificateCredential) credential)
					.getApplicationId();
		} else if (credential instanceof SignatureCredential) {
			applicationId = ((SignatureCredential) credential)
					.getApplicationId();
		}
		return applicationId;
	}

	private void initCredential() throws InvalidCredentialException,
			MissingCredentialException {
		if (credential == null) {
			credential = getCredentials();
		}
	}

}
