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
    
    <title>My JSP 'c_choose.jsp' starting page</title>
    
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
  <%
  	request.setAttribute("age", "22");
  %>
    <c:choose> 
    	<c:when test="${age == 10 }">
    	
    		<font color="red">年龄等于10</font>
    	</c:when>
    	
        <c:when test="${age < 10 }">
    	
    		<font color="blue">年龄xiao于10</font>
    	</c:when>	
    	<c:otherwise>
    	
    	<font color="blue">otherwise</font>
    	
    	</c:otherwise>
    </c:choose>
  </body>
</html>
