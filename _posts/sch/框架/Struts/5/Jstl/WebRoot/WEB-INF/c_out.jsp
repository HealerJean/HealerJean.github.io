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
    
    <title>My JSP 'c_out.jsp' starting page</title>
    
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
    <h1>c:out的使用</h1>
    	<%request.setAttribute("hello", "你好");
  		session.setAttribute("hello", "你好");
  		application.setAttribute("hello", "你好");
  		pageContext.setAttribute("hello", "你好");
  	//使用嵌套html内容
  	pageContext.setAttribute("html","pageContext你们<a href='http://www.baidu.com'>百度</a>");	
  	 %> 
  	   	 <%=request.getAttribute("hello").toString() %>
  	
    <c:out value="hello world"></c:out> 
 
    <h1>如何输出request/session/application/pageContext的内容</h1>
        ${hello} 
    <c:out value="${hello}"  ></c:out>   
            
    <c:out value="${no} " default="没有值" ></c:out>  
   <!--  escapeXml用于表示是否按照html内容显示，true表示 文本内容，false表示html内容 -->
    <c:out value="${html} " default="没有值" escapeXml="false"></c:out>  
    <%
    	 
    	User u=new User();
 	 u.setUsername("admin");
 	 u.setPassword("admin");
 	 request.setAttribute("user",u);
    	
     %> 
        <c:out value="${user.password} " default="没有值" escapeXml="false"></c:out>  
 
  </body>
</html>
