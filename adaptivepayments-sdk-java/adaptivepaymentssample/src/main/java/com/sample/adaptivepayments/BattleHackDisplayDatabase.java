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

public class BattleHackDisplayDatabase extends HttpServlet {

	private static final long serialVersionUID = 1L;

	public BattleHackDisplayDatabase() {
		super();
	}

	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
			
			
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();
        try {
                
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Pay Beam - Instant PayPal Payments on Google Glass</title>");  
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Pay Beam - Instant PayPal Payments on Google Glass</h1>");
            
            out.println("<h2>Recipients:</h2>");
            
            Class.forName("org.hsqldb.jdbcDriver");
            Connection conn = DriverManager.getConnection("jdbc:hsqldb:test.db");
            Statement stat = conn.createStatement();
            ResultSet rs = stat.executeQuery("select * from people;");
            
            while (rs.next()) {
                out.println("email = " + rs.getString("email") + "<br/>");
                out.println("amount = " + rs.getString("amount") + "<br/>");
                out.println("status = " + rs.getString("status") + "<br/>");
            }
            rs.close();

            out.println("<h2>Approvals:</h2>");
            
            Statement stat2 = conn.createStatement();
            ResultSet rs2 = stat2.executeQuery("select * from approvals;");
            
            while (rs2.next()) {
                out.println("email = " + rs2.getString("email") + "<br/>");
                out.println("preapprovalKey = " + rs2.getString("preapprovalKey") + "<br/>");
            }
            rs2.close();
            
           
            out.println("</body>");
            out.println("</html>");

        } catch (SQLException ex) {
        	throw new RuntimeException(ex);
        } catch (ClassNotFoundException ex) {
        	throw new RuntimeException(ex);
        } finally { 
            out.close();
        }			
	
	
	}

}
