<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8" name="google-signin-client_id" content="clientID">
<title>Start Google</title>
</head>
<body>
	<h1>START GOOGLE</h1>
	<fieldset>
		<label>로그인</label> <br>
		<div id="googleLoginBtn" style="cursor: pointer">
			<img id="googleLoginImg" src="./images/btn_google_signin_light_pressed_web.png">
		</div>
	</fieldset>
	
	
	<div class="g-signin2" data-onsuccess="onSignIn"></div>
	
	<!-- <script src="https://apis.google.com/js/platform.js" async defer></script> -->
	<script type="text/javascript" src="/resources/js/custom-google.js"></script>
</body>
</html>