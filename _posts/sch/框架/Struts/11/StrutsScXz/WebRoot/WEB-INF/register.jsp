<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <base href="<%=basePath%>">
    
    <title>My JSP 'register.jsp' starting page</title>
    
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">    
	<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
	<meta http-equiv="description" content="This is my page">
	<!--
	<link rel="stylesheet" type="text/css" href="styles.css">
	-->

  </head>
  
  <body><center>
    	<form action="/StrutsScXz/register.do" method="post" enctype="MULTIPART/FORM-DATA">
    	<table>
    	<tr> 
    		名字：<input type="text" name="username">
    	</tr> 
    	<tr>
    		头像 </p><input type="file" name="myphoto" ><br/>
    		<input type="submit" value="注册">
    	</tr>
    	</table>	
    		
    	</form>
  </center></body>
  
</html>
