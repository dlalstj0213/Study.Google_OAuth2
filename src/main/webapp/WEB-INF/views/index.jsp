<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
<head>
	<title>Home</title>
</head>
<body>
<h1>
	Hello world!  
</h1>

</body>
	<div>
		<h3>
			<a href="google_api/">Google Token</a>
		</h3>
		<c:if test="${!empty sessionScope.token}">
		<h4>토큰 받기 완료</h4>
		<p>${ sessionScope.token }</p>
		</c:if>
	</div>
	
	<div>
		<h3>
			<a href="google_api/session_remove"> 세션 지우기 </a>
		</h3>
	</div>
	
	<div>
		<h3>
			<a href="google_api/revoke_token">
				토큰 삭제
			</a>
		</h3>
	</div>
</html>
