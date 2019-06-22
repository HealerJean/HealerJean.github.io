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
    
    <title>My JSP 'c_forEachMap.jsp' starting page</title>
    
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
      Map map=new HashMap();
      //map.put("aa","admin");
    //  map.put("bb","scott");
    
      User u1=new User();
      u1.setUsername("xiaoming");
      u1.setPassword("xiaoming");
       User u2=new User();
      u2.setUsername("xiaoming");
      u2.setPassword("xiaoming");
      map.put("u1",u1);
      map.put("u2",u2); 
      request.setAttribute("person",map); 
      %>
      <c:forEach items="${person}" var="per">
      key:${per.key }值 name: ${per.value.username }值 password:${per.value.password }<br/>
      </c:forEach>

  </body>
</html>
