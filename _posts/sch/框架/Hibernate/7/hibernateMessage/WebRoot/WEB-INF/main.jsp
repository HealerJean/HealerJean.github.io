<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <base href="<%=basePath%>">
    
    <title>My JSP 'main.jsp' starting page</title>
    
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
    <a href="/hibernateMessage/message.do?flag=gopublish">发布信息</a> <a href="#">退出信息</a><br/>
   留言信息： <br/>
   <table border="1">
   <tr><td>发送人</td><td>时间</td><td>接收人</td><td>内容</td><td>附件</td></tr>
    <tr><td>发送人</td><td>时间</td><td>接收人</td><td>内容</td><td>附件</td></tr>
     <tr><td>发送人</td><td>时间</td><td>接收人</td><td>内容</td><td>附件</td></tr>
   </table>
  </body>
</html>
