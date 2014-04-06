package com.sample.adaptivepayments;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
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
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.PreparedStatement;
import java.sql.Types;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class BattleHackRequestPayment extends HttpServlet {
	private static final long serialVersionUID = 1L;

	public BattleHackRequestPayment() {
		super();
	}

	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {

		final String email = request.getParameter("email");
		final String amount = request.getParameter("amount");
				

        try {
            Class.forName("org.hsqldb.jdbcDriver");
            Connection conn = DriverManager.getConnection("jdbc:hsqldb:test.db");
            
            // Remove any previous request for this email.
            String deleteSQL = "delete from people where email = ?";
            PreparedStatement preparedStatement = conn.prepareStatement(deleteSQL);
            preparedStatement.setString(1, email);
            preparedStatement.executeUpdate();
            
            // Insert the new request for payment.
            PreparedStatement prep = conn.prepareStatement(
            		"insert into people values (?,?,?,?);");
            prep.setString(1, email);
            prep.setString(2, amount);
            prep.setString(3, "PENDING");
            prep.setNull(4, Types.INTEGER);
            prep.executeUpdate();

            // Get the ID so we can check on the status.
            PreparedStatement prep2 = conn.prepareStatement("CALL IDENTITY()");
            ResultSet generatedKeys = prep2.executeQuery();
            if (null != generatedKeys && generatedKeys.next()) {
                 Long primaryKey = generatedKeys.getLong(1);
                 
                 //out.println("\n\nRequest number " + primaryKey + " added!");
                 
                 response.sendRedirect("BattleHackPaymentStatus?requestId=" + primaryKey);
            }

            conn.close();
            return;
            
        } catch (SQLException ex) {
        	throw new RuntimeException(ex);
        } catch (ClassNotFoundException ex) {
        	throw new RuntimeException(ex);
        }

	}

}
