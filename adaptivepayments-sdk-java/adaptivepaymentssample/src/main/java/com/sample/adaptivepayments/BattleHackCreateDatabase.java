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
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class BattleHackCreateDatabase extends HttpServlet {
	private static final long serialVersionUID = 1L;

	public BattleHackCreateDatabase() {
		super();
	}

	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {

        response.setContentType("text/plain");
        PrintWriter out = response.getWriter();
        out.println("Creating DB...");



        try {
            Class.forName("org.hsqldb.jdbcDriver");
            Connection conn = DriverManager.getConnection("jdbc:hsqldb:test.db");
            //System.out.println(conn.getClientInfo());
            Statement stat = conn.createStatement();
            stat.executeUpdate("drop table if exists people;");
            stat.executeUpdate("create table people (name varchar(45), occupation varchar(45));");
            PreparedStatement prep = conn.prepareStatement("insert into people values (?,?);");

            prep.setString(1, "Gandhi");
            prep.setString(2, "politics");
            prep.addBatch();
            prep.setString(1, "Turing");
            prep.setString(2, "computers");
            prep.addBatch();
            prep.setString(1, "Wittgenstein");
            prep.setString(2, "smartypants");
            prep.addBatch();

            conn.setAutoCommit(false);
            prep.executeBatch();
            conn.setAutoCommit(true);

            conn.close();
        } catch (SQLException ex) {
        	throw new RuntimeException(ex);
        } catch (ClassNotFoundException ex) {
        	throw new RuntimeException(ex);
        }


        out.println("Created.");
				out.close();


	}

}
