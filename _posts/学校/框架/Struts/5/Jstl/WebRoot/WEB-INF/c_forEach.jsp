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
    
    <title>My JSP 'c_forEach.jsp' starting page</title>
    
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
  <h1>c:forEach 的使用</h1>
		   <%
		 ArrayList<User> al=new ArrayList<User>();
		 User u1=new User();
		 u1.setUsername("陈超");
		 u1.setPassword("tiger");
		  User u2=new User();
		 u2.setUsername("system");
		 u2.setPassword("manager");
		  al.add(u1); 
		  al.add(u2);
		request.setAttribute("alluser",al);
  %>
   
 <c:forEach items="${alluser}" var="u" begin="0" end="10">
    ${u.username}  
     ${u.password} <br/> 
</c:forEach> 
 <c:forEach  var="o"begin="1" end="10">
    ${o } --》
 
</c:forEach> 
<br/>
 <c:forEach  var="o"begin="1" end="10"step="3">
    ${o } --》
 
</c:forEach> 

  </body>
</html>
