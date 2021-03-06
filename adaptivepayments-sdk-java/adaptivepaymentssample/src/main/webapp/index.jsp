<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ page import="java.net.URL"%>
<%@ page import="java.text.SimpleDateFormat"%>
<%@ page import="java.util.Date"%>
<%
	URL currentURL = new URL(request.getRequestURL().toString());
	URL returnURL = new URL(currentURL, "index.jsp");
	
	SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
	final String today = dateFormat.format(new Date());
	
%>
<!DOCTYPE html>
<html>
<head>
<meta charset="utf-8">
<title>PayBeam - Instant PayPal Payments on Google Glass</title>
<link rel="stylesheet" type="text/css" href="theme.css">
</head>
<body>
<div class="hero">
<div class="main">
	<h1>PayBeam</h1><h2>Instant PayPal Payments on Google Glass</h2>
	</div>
	</div>
	<div class="main">
	<div class="left">
	<h2>Anyone</h2>
	<h3>Request a Payment from Google Glass</h3>
	<form action="BattleHackRequestPayment" method="post">
		Receive Money At E-mail: <input type="text" name="email" /><br />
		
		Amount: $<input type="text" name="amount" /><br />
		
		<input type="submit" value="Make Request" />
	</form>
	</div>
	<div class="right">
	<h2>Google Glass User</h2>	
	
	<h3>
		<a href="PayBeamGlassware.apk">Google Glass app</a>
		<a href="http://www.gosphero.com/how-to-load-apps-on-google-glass/">(install instructions)</a>
	</h3>
	
	<h3>Approve Instant Payments for Later</h3>
	<form action="Preapproval" method="post">	
		
		<input type="hidden" name="currencyCode" value="USD" />
		<input type="hidden" name="startingDate" value="<%=  today  %>" />		
		<input type="hidden" name="endingDate" value="" />		
		<input type="hidden" name="ipnNotificationURL" value="" />
		
		<input type="hidden" name="cancelURL" value="<%=returnURL%>" />
		<input type="hidden" name="returnURL" value="<%=returnURL%>" />

		<input type="hidden" name="feesPayer" value="" />
		<input type="hidden" name="pinType" value="" />
		<input type="hidden" name="memo" value="" />

		<!-- TODO lookup automatically from Google login -->
		Account E-mail (PayPal SandBox): <input type="text" name="senderEmail" value="" /><br />						

		<input type="hidden" name="dateOfMonth" value="" />
		<input type="hidden" name="dayOfWeek" value="NO_DAY_SPECIFIED" />

		Amount Allowed: $<input type="text" name="maxAmountPerPayment" value="25" /><br />
		Number of Payments: <input type="text" name="maxNumberOfPayments" value="2" /><br />
		
		<input type="hidden" name="totalAmountOfAllPayments" value="" />
		<input type="hidden" name="maxNumberOfPaymentsPerPeriod" value="" />
		<input type="hidden" name="paymentPeriod" value="" />
		<input type="hidden" name="displayMaxTotalAmount" value="" />
												
		<input type="submit" name="PreapprovalBtn" value="Pre-Approve Amount" />
		
	</form>
	</div>
</div>
<footer>
<div class="main">
Built for BattleHack 2014 San Francisco, CA
</div>
</footer>
   
</body>
</html>