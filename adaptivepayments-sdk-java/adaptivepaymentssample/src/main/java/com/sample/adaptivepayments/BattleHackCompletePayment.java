package com.sample.adaptivepayments;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.paypal.exception.ClientActionRequiredException;
import com.paypal.exception.HttpErrorException;
import com.paypal.exception.InvalidCredentialException;
import com.paypal.exception.InvalidResponseDataException;
import com.paypal.exception.MissingCredentialException;
import com.paypal.exception.SSLConfigurationException;
import com.paypal.sdk.exceptions.OAuthException;
import com.paypal.svcs.services.AdaptivePaymentsService;
import com.paypal.svcs.types.ap.FundingConstraint;
import com.paypal.svcs.types.ap.FundingTypeInfo;
import com.paypal.svcs.types.ap.FundingTypeList;
import com.paypal.svcs.types.ap.PayRequest;
import com.paypal.svcs.types.ap.PayResponse;
import com.paypal.svcs.types.ap.Receiver;
import com.paypal.svcs.types.ap.ReceiverList;
import com.paypal.svcs.types.common.RequestEnvelope;
import com.sample.util.Configuration;

public class BattleHackCompletePayment extends HttpServlet {
	private static final long serialVersionUID = 1L;

	public BattleHackCompletePayment() {
		super();
	}

	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		// TODO disable in production
		processRequest(request, response);
	}
	
	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		processRequest(request, response);
	}
	
	private void processRequest(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {

		final String recipient = request.getParameter("recipient");
		final String username = request.getParameter("username");
		final String amount = request.getParameter("amount");
				
        response.setContentType("text/plain");
        PrintWriter out = response.getWriter();
        out.println("Completing payment...");


		HttpSession session = request.getSession();
		URL currentURL = new URL(request.getRequestURL().toString());
		URL returnURL = new URL(currentURL, "index.jsp");
		URL cancelURL = new URL(currentURL, "index.jsp");

		final String preapprovalKey = getKeyFor(username);		

		completePayment(
				session, response, amount, recipient, username,
				preapprovalKey, cancelURL.toString(), returnURL.toString()
				);

        out.println("Done.");
        out.close();


	}
	
	protected void completePayment(
			final HttpSession session,
			final HttpServletResponse response,
			final String amount,
			final String receiverEmail,
			final String senderEmail,
			final String preapprovalKey,
			final String cancelUrl,
			final String returnUrl
			) throws ServletException, IOException {

		RequestEnvelope requestEnvelope = new RequestEnvelope("en_US");

		PayRequest req = new PayRequest();

		List<Receiver> receiver = new ArrayList<Receiver>();

		Receiver rec = new Receiver();
		/** (Required) Amount to be paid to the receiver */
		if (amount != "")
			rec.setAmount(Double.parseDouble(amount));
		/**
		 *  Receiver's email address. This address can be unregistered with paypal.com.
		 *  If so, a receiver cannot claim the payment until a PayPal account is linked
		 *  to the email address. The PayRequest must pass either an email address or a phone number. 
		 *  Maximum length: 127 characters 
		 */
			rec.setEmail(receiverEmail);

		/**
		 * (Optional) The transaction type for the payment. Allowable values are:
		    GOODS � This is a payment for non-digital goods
		    SERVICE � This is a payment for services (default)
		    PERSONAL � This is a person-to-person payment
		    CASHADVANCE � This is a person-to-person payment for a cash advance
		    DIGITALGOODS � This is a payment for digital goods
		    BANK_MANAGED_WITHDRAWAL � This is a person-to-person payment for bank withdrawals, available only with special permission.
		 */
		rec.setPaymentType("PERSONAL");
		
		
		receiver.add(rec);
		
		ReceiverList receiverlst = new ReceiverList(receiver);
		req.setReceiverList(receiverlst);
		req.setRequestEnvelope(requestEnvelope);

		/**
		 * (Optional) A note associated with the payment (text, not HTML). 
		 * Maximum length: 1000 characters, including newline characters 
		 */
		req.setMemo("PayBeam personal payment");
		
		/**  (Optional) Sender's email address. Maximum length: 127 characters */ 
		req.setSenderEmail(senderEmail);

		
		FundingConstraint fundingConstraint = new FundingConstraint();
		List<FundingTypeInfo> fundingTypeInfoList = new ArrayList<FundingTypeInfo>();
		
		FundingTypeList fundingTypeList = new FundingTypeList(
				fundingTypeInfoList);
		fundingConstraint.setAllowedFundingType(fundingTypeList);
		req.setFundingConstraint(fundingConstraint);
		
		/** Preapproval key for the approval set up between you and the sender */
			req.setPreapprovalKey(preapprovalKey);

		/**
		 * The action for this request. Possible values are:
		    PAY � Use this option if you are not using the Pay request in combination with ExecutePayment.
		    CREATE � Use this option to set up the payment instructions with SetPaymentOptions and then execute the payment at a later time with the ExecutePayment.
		    PAY_PRIMARY � For chained payments only, specify this value to delay payments to the secondary receivers; only the payment to the primary receiver is processed.
		 */
		req.setActionType("PAY");
		
		/**
		 * URL to redirect the sender's browser to after canceling the approval for a payment;
		 *  it is always required but only used for payments that require approval (explicit payments) 
		 */
		req.setCancelUrl(cancelUrl);
		/**
		 * The code for the currency in which the payment is made;
		 * you can specify only one currency, regardless of the number of receivers
		 */
		req.setCurrencyCode("USD");

		/**
		 * URL to redirect the sender's browser to after the sender has logged into PayPal and approved a payment; 
		 * it is always required but only used if a payment requires explicit approval 
		 */
		req.setReturnUrl(returnUrl);
		
		// Configuration map containing signature credentials and other required configuration.
		// For a full list of configuration parameters refer in wiki page
		// (https://github.com/paypal/sdk-core-java/wiki/SDK-Configuration-Parameters)
		Map<String,String> configurationMap =  Configuration.getAcctAndConfig();
		
		// Creating service wrapper object to make an API call by loading configuration map. 
		AdaptivePaymentsService service = new AdaptivePaymentsService(configurationMap);
		
		try {
			PayResponse resp = service.pay(req);
			response.setContentType("text/html");
			if (resp != null) {
				session.setAttribute("RESPONSE_OBJECT", resp);
				session.setAttribute("lastReq", service.getLastRequest());
				session.setAttribute("lastResp", service.getLastResponse());
				if (resp.getResponseEnvelope().getAck().toString()
						.equalsIgnoreCase("SUCCESS")) {
					Map<Object, Object> map = new LinkedHashMap<Object, Object>();
					map.put("Ack", resp.getResponseEnvelope().getAck());
					
					/**
					 * Correlation identifier. It is a 13-character, alphanumeric string 
					  (for example, db87c705a910e) that is used only by PayPal Merchant Technical Support.
						Note: You must log and store this data for every response you receive. 
						PayPal Technical Support uses the information to assist with reported issues. 
					 */
					map.put("Correlation ID", resp.getResponseEnvelope().getCorrelationId());
					
					/** 
					 * Date on which the response was sent, for example: 2012-04-02T22:33:35.774-07:00
					   Note: You must log and store this data for every response you receive. 
					   PayPal Technical Support uses the information to assist with reported issues. 
					 */
					map.put("Time Stamp", resp.getResponseEnvelope().getTimestamp());
					
					/**
					 * The pay key, which is a token you use in other Adaptive Payment APIs 
					 * (such as the Refund Method) to identify this payment. 
					 * The pay key is valid for 3 hours; the payment must be approved while the 
					 * pay key is valid. 
					 */
					map.put("Pay Key", resp.getPayKey());
					
					/**
					 * The status of the payment. Possible values are:
				    CREATED � The payment request was received; funds will be transferred once the payment is approved
				    COMPLETED � The payment was successful
				    INCOMPLETE � Some transfers succeeded and some failed for a parallel payment or, for a delayed chained payment, secondary receivers have not been paid
				    ERROR � The payment failed and all attempted transfers failed or all completed transfers were successfully reversed
				    REVERSALERROR � One or more transfers failed when attempting to reverse a payment
				    PROCESSING � The payment is in progress
				    PENDING � The payment is awaiting processing
				    */
					map.put("Payment Execution Status",resp.getPaymentExecStatus());
					if (resp.getDefaultFundingPlan() != null){
						/** Default funding plan.  */
						map.put("Default Funding Plan", resp.getDefaultFundingPlan().getFundingPlanId());
					}	
					
					map.put("Redirect URL",
							"<a href=https://www.sandbox.paypal.com/cgi-bin/webscr?cmd=_ap-payment&paykey="
									+ resp.getPayKey()
									+ ">https://www.sandbox.paypal.com/cgi-bin/webscr?cmd=_ap-payment&paykey="
									+ resp.getPayKey() + "</a>");
					session.setAttribute("map", map);
					response.sendRedirect("Response.jsp");
				} else {
					session.setAttribute("Error", resp.getError());
					response.sendRedirect("Error.jsp");
				}
			}
		} catch (SSLConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvalidCredentialException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (HttpErrorException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvalidResponseDataException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClientActionRequiredException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (MissingCredentialException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (OAuthException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	

	private String getKeyFor(final String username) throws ServletException, IOException {
						
        try {
                            
            Class.forName("org.hsqldb.jdbcDriver");
            Connection conn = DriverManager.getConnection("jdbc:hsqldb:test.db");
            
            PreparedStatement prep = conn.prepareStatement("select * from APPROVALS where email = ?;");
            prep.setString(1, "lnanek@gmail.com");

            ResultSet rs = prep.executeQuery();
            rs.next();            
            final String key = rs.getString("preapprovalKey");

            rs.close();
            
            return key;
            
        } catch (SQLException ex) {
        	throw new RuntimeException(ex);
        } catch (ClassNotFoundException ex) {
        	throw new RuntimeException(ex);
        } 			
	
	
	}

}
