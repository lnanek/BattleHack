package com.sample.adaptivepayments;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;

import com.paypal.exception.ClientActionRequiredException;
import com.paypal.exception.HttpErrorException;
import com.paypal.exception.InvalidCredentialException;
import com.paypal.exception.InvalidResponseDataException;
import com.paypal.exception.MissingCredentialException;
import com.paypal.exception.SSLConfigurationException;
import com.paypal.sdk.exceptions.OAuthException;
import com.paypal.svcs.services.AdaptivePaymentsService;
import com.paypal.svcs.types.ap.ConvertCurrencyRequest;
import com.paypal.svcs.types.ap.ConvertCurrencyResponse;
import com.paypal.svcs.types.ap.CurrencyCodeList;
import com.paypal.svcs.types.ap.CurrencyConversionList;
import com.paypal.svcs.types.ap.CurrencyList;
import com.paypal.svcs.types.common.CurrencyType;
import com.paypal.svcs.types.common.RequestEnvelope;
import com.sample.util.Configuration;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class BattleHackPaymentStatus extends HttpServlet {

	private static final long serialVersionUID = 1L;

	public BattleHackPaymentStatus() {
		super();
	}

	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {

		final String requestId = request.getParameter("requestId");
		final int requestIdParsed = Integer.parseInt(requestId);

		//response.setContentType("text/plain");
		//PrintWriter out = response.getWriter();
		//out.println("\n\nPayment requested!...");

		//out.println("\n\nChecking status...");

		try {

			Class.forName("org.hsqldb.jdbcDriver");
			Connection conn = DriverManager.getConnection("jdbc:hsqldb:test.db");

			PreparedStatement prep = conn.prepareStatement(
					"select * from people where request_id = ?;");
			prep.setInt(1, requestIdParsed);

			ResultSet rs = prep.executeQuery();
			rs.next();
			final String status = rs.getString("status");

			rs.close();

			//out.println("\n\nStatus is: " + status);
			
			request.setAttribute("status", status);
			
			getServletConfig().getServletContext().getRequestDispatcher("/status.jsp")
			.forward(request, response);

			conn.close();

		} catch (SQLException ex) {
			throw new RuntimeException(ex);
		} catch (ClassNotFoundException ex) {
			throw new RuntimeException(ex);
		}

		// TODO auto-refresh

	}

}
