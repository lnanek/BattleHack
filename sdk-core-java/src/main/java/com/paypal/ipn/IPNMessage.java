package com.paypal.ipn;

import java.net.URL;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.paypal.core.ConfigManager;
import com.paypal.core.ConnectionManager;
import com.paypal.core.Constants;
import com.paypal.core.HttpConfiguration;
import com.paypal.core.HttpConnection;
import com.paypal.core.LoggingManager;
import com.paypal.core.SDKUtil;

public class IPNMessage {

	private static final long serialVersionUID = -7187275404183441828L;
	private static final String ENCODING = "windows-1252";

	private Map<String, String> ipnMap = new HashMap<String, String>();
	private Map<String, String> configurationMap = null;
	private HttpConfiguration httpConfiguration = null;
	private String ipnEndpoint = Constants.EMPTY_STRING;
	private boolean isIpnVerified = false;
	private StringBuffer payload;

	/**
	 * Populates HttpConfiguration with connection specifics parameters
	 */
	private void initialize() {
		httpConfiguration = new HttpConfiguration();
		ipnEndpoint = getIPNEndpoint();
		httpConfiguration.setEndPointUrl(ipnEndpoint);
		httpConfiguration.setConnectionTimeout(Integer
				.parseInt(configurationMap
						.get(Constants.HTTP_CONNECTION_TIMEOUT)));
		httpConfiguration.setMaxRetry(Integer.parseInt(configurationMap
				.get(Constants.HTTP_CONNECTION_RETRY)));
		httpConfiguration.setReadTimeout(Integer.parseInt(configurationMap
				.get(Constants.HTTP_CONNECTION_READ_TIMEOUT)));
		httpConfiguration.setMaxHttpConnection(Integer
				.parseInt(configurationMap
						.get(Constants.HTTP_CONNECTION_MAX_CONNECTION)));
	}
	
	/**
	 * Constructs {@link IPNMessage} using the given {@link HttpServletRequest}
	 * to retrieve the name and value {@link Map}.
	 * 
	 * @param request
	 *            {@link HttpServletRequest} object received from PayPal IPN
	 *            call back.
	 */
	public IPNMessage(HttpServletRequest request) {
		this(request.getParameterMap());
	}

	/**
	 * Constructs {@link IPNMessage} using the given {@link Map} for name and
	 * values.
	 * 
	 * @param ipnMap
	 *            {@link Map} representing IPN name/value pair
	 */
	public IPNMessage(Map<String, String[]> ipnMap) {
		this(ipnMap, ConfigManager.getInstance().getConfigurationMap());
	}

	/**
	 * Constructs {@link IPNMessage} using the given {@link HttpServletRequest}
	 * to retrieve the name and value {@link Map} and a custom configuration
	 * {@link Map}
	 * 
	 * @param request
	 *            {@link HttpServletRequest} object received from PayPal IPN
	 *            call back.
	 * @param configurationMap
	 *            custom configuration {@link Map}
	 */
	public IPNMessage(HttpServletRequest request,
			Map<String, String> configurationMap) {
		this(request.getParameterMap(), configurationMap);
	}

	/**
	 * Constructs {@link IPNMessage} using the given {@link Map} for name and
	 * values and a custom configuration {@link Map}
	 * 
	 * @param ipnMap
	 *            {@link Map} representing IPN name/value pair
	 * @param configurationMap
	 */
	public IPNMessage(Map<String, String[]> ipnMap,
			Map<String, String> configurationMap) {
		this.configurationMap = SDKUtil.combineDefaultMap(configurationMap);
		initialize();
		payload = new StringBuffer("cmd=_notify-validate");
		if (ipnMap != null) {
			String[] encodingParam = ipnMap.get("charset");
			String encoding = encodingParam != null && encodingParam.length > 0 ?
				 encodingParam[0] : ENCODING;
			for (Map.Entry<String, String[]> entry : ipnMap.entrySet()) {
				String name = entry.getKey();
				String[] value = entry.getValue();
				try {
					this.ipnMap.put(name,
							 URLDecoder.decode(value[0], encoding));
					payload.append("&").append(name).append("=")
							.append(URLEncoder.encode(value[0], encoding));
				} catch (Exception e) {
					LoggingManager.debug(IPNMessage.class, e.getMessage());
				}
			}
		}
	}

	/**
	 * This method post back ipn payload to PayPal system for verification
	 */
	public boolean validate() {
		Map<String, String> headerMap = new HashMap<String, String>();
		URL url = null;
		String res = Constants.EMPTY_STRING;
		HttpConnection connection = ConnectionManager.getInstance()
				.getConnection();

		try {

			connection.createAndconfigureHttpConnection(httpConfiguration);
			url = new URL(this.ipnEndpoint);
			headerMap.put("Host", url.getHost());
			res = Constants.EMPTY_STRING;
			if (!this.isIpnVerified) {
				res = connection.execute(null, payload.toString(), headerMap);
			}

		} catch (Exception e) {
			LoggingManager.debug(IPNMessage.class, e.getMessage());
		}

		// check notification validation
		if (res.equals("VERIFIED")) {
			isIpnVerified = true;
		}

		return isIpnVerified;
	}

	/**
	 * @return Map of IPN name/value parameters
	 */
	public Map<String, String> getIpnMap() {
		return ipnMap;
	}

	/**
	 * @param ipnName
	 * @return IPN value for corresponding IpnName
	 */
	public String getIpnValue(String ipnName) {

		return this.ipnMap.get(ipnName);

	}

	/**
	 * @return Transaction Type (eg: express_checkout, cart, web_accept)
	 */
	public String getTransactionType() {
		return this.ipnMap.containsKey("txn_type") ? this.ipnMap
				.get("txn_type") : this.ipnMap.get("transaction_type");
	}

	private String getIPNEndpoint() {
		String ipnEPoint = null;
		ipnEPoint = configurationMap.get(Constants.IPN_ENDPOINT);
		if (ipnEPoint == null) {
			String mode = configurationMap.get(Constants.MODE);
			if (mode != null
					&& (Constants.SANDBOX.equalsIgnoreCase(configurationMap
							.get(Constants.MODE).trim()))) {
				ipnEPoint = Constants.IPN_SANDBOX_ENDPOINT;
			} else if (mode != null
					&& (Constants.LIVE.equalsIgnoreCase(configurationMap.get(
							Constants.MODE).trim()))) {
				ipnEPoint = Constants.IPN_LIVE_ENDPOINT;
			}
		}
		return ipnEPoint;
	}

}
