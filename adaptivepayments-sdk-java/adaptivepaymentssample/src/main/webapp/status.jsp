<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
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
			<h1>PayBeam</h1>
			<h2>Instant PayPal Payments on Google Glass</h2>
		</div>
	</div>
	<div class="main">
		<div class="left">
			<h2>Payment requested!</h2>

			<h2>Ask Google Glass user to complete.</h2>

			<h2>Status is: <%= request.getAttribute("status") %></h2>
		</div>
	</div>
	<footer>
		<div class="main">Built for BattleHack 2014 San Francisco, CA</div>
	</footer>

</body>
</html>