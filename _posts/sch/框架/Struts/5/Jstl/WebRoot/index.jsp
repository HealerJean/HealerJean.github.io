<%@page import="com.hlj.domain.User"%>
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <base href="<%=basePath%>">
    
    <title>My JSP 'index.jsp' starting page</title>
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">    
	<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
	<meta http-equiv="description" content="This is my page">
	<!--
	<link rel="stylesheet" type="text/css" href="styles.css">
	-->
  </head>
   
  <body> 
  
<%--     <jsp:forward page="/WEB-INF/c_out.jsp"></jsp:forward>--%>
    <%-- <jsp:forward page="/WEB-INF/c_set.jsp"></jsp:forward> --%>
   <%--  <jsp:forward page="/WEB-INF/c_remove.jsp"></jsp:forward> --%>
   <%--  <jsp:forward page="/WEB-INF/c_catch.jsp"></jsp:forward> 
  --%>
 	<!-- 条件标签  -->
 <%-- <jsp:forward page="/WEB-INF/c_if.jsp"></jsp:forward> --%>
  <!--   相当于swift	 -->
 <%-- <jsp:forward page="/WEB-INF/c_choose.jsp"></jsp:forward> --%>
  
  <%-- <jsp:forward page="/WEB-INF/c_forEach.jsp"></jsp:forward> --%>
    	
<%--     	<jsp:forward page="/WEB-INF/c_forToken.jsp"></jsp:forward>
 --%>  
<%--  <jsp:forward page="/WEB-INF/c_forEachMap.jsp"></jsp:forward>
 --%> 
 <%-- <jsp:forward page="/WEB-INF/c_redirect.jsp"></jsp:forward>
  --%>
   <jsp:forward page="/WEB-INF/c_import.jsp"></jsp:forward>
 
 </body>
</html>
