<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ page import="java.net.URL"%>
<%
	URL currentURL = new URL(request.getRequestURL().toString());
	URL returnURL = new URL(currentURL, "index.html");
%>
<!DOCTYPE html>
<html>
<head>
<meta charset="utf-8">
<title>PayBeam - Instant PayPal Payments on Google Glass</title>
<style>
	body{
		font-family: "Arial", sans-serif;
		margin: 0;
		padding: 0;
		background-color: #fff;
	}
	.main{
		width: 1160px;
		margin: 0 auto;
	}
	.hero h1{
		font-size: 4em;
		margin-top: 0;
		padding-top: 180px;
		font-style: italic;

	}
	.hero h2{
		font-size: 3em;
	}
	div.hero{
		height: 380px;
		width: 100%;
		color: #ffffff;
		background-color: #458FDA;
	}
	.left{
		display: inline-block;
		width:48%;
		/*float:left;*/
	}
	.right{
		display: inline-block;
		width:48%;
	}
	input{
		line-height: 2em;
		width: 90%;
		border-radius: 4px;
		font-size: 1.5em;

	}
	button{

	}
	footer .main{
		text-align: center;
		margin: 2em;
	}
</style>
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
	<h3>Request a Payment</h3>
	<form action="BattleHackRequestPayment" method="post">
		Receive Money At: <input type="text" name="email" /><br />
		
		Amount: $<input type="text" name="amount" /><br />
		
		<input type="submit" value="Make Request" />
	</form>
	</div>
	<div class="right">
	<h2>Google Glass User</h2>	
	<h3>Approve Instant Payments for Later</h3>
	<form action="Preapproval" method="post">	
		
		<input type="hidden" name="currencyCode" value="USD" />
		
		Start Date: <input type="text" name="startingDate" value="2014-04-05" /><br />		
		
		<input type="hidden" name="endingDate" value="" />		
		<input type="hidden" name="ipnNotificationURL" value="" />
		
		<input type="hidden" name="cancelURL" value="<%=returnURL%>" />
		<input type="hidden" name="returnURL" value="<%=returnURL%>" />

		<input type="hidden" name="feesPayer" value="" />
		<input type="hidden" name="pinType" value="" />
		<input type="hidden" name="memo" value="" />

		<!-- TODO lookup automatically from Google login -->
		Account E-mail: <input type="text" name="senderEmail" value="" /><br />						

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