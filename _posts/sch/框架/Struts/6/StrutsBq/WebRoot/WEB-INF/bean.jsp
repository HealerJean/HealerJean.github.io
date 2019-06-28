<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<%@taglib prefix="bean" uri="http://struts.apache.org/tags-bean" %>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <base href="<%=basePath%>">
    
    <title>My JSP 'bean.jsp' starting page</title>
    
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
  <h1>bean write 标签的使用</h1>
    <%
    	request.setAttribute("a", "zhe shi zhang yu jin");
    %>
    <bean:write name="a"/>
      <h1>bean  message标签的使用</h1>
     
    <bean:message key="key1" arg0 = "新内容，但是不改变"/>
    <bean:message key="erro" arg0 = "因为你是傻子" arg1="而且你是聪明"/>
     
    
  </body>
</html>
