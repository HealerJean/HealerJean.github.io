<%@page import="com.hlj.form.UserForm"%>
<%@page import="org.apache.struts.apps.mailreader.dao.User"%>
<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<%@taglib prefix="logic" uri="http://struts.apache.org/tags-logic" %>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <base href="<%=basePath%>">
    
    <title>My JSP 'logic.jsp' starting page</title>
    
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
    		<h1>logic 标签</h1>
    		
    		<%UserForm userForm = new UserForm();
    		
    			
    		%> 
    		<logic:iterate id="wo"></logic:iterate>
    		<logic:notEmpty></logic:notEmpty>
  </body>
</html>
