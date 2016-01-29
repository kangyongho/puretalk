<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<title>JSP Redirect</title>
</head>
<body>
	<!-- 나중에 PureTalk Down load page로 이동시키기 -->
	<% response.sendRedirect("http://www.google.com"); %>
	
</body>
</html>