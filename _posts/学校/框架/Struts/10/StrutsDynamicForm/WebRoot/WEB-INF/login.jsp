<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <base href="<%=basePath%>">
    
    <title>My JSP 'Login.jsp' starting page</title>
    
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">    
	<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
	<meta http-equiv="description" content="This is my page">
	<!--
	<link rel="stylesheet" type="text/css" href="styles.css">
	-->
<style type="text/css">
 #submit{width: 255px;height: 37px;font-size:18px}
</style>
  </head>
  
  <body>
   <body ><center>
  	<br/><br/><br/><br/>
    <h1 class="welcome"> 欢迎进入登录界面 </h1>

	<form method="post" action="/StrutsDynamicForm/login.do">
    	<table cellspacing="10">
    		
    	<tr>
    		<td>请输入入用户名</td> 
    		<td><input type="text" name="username"/></td> 
    	</tr>
    	<tr>
    		<td>请输入入密码</td> 
    		<td><input type="password" name="password"/> </td>
    	</tr> 
      	<tr>
    		<td>请输入email</td> 
    		<td><input type="text" name="email"/></td> 
    	</tr>  	
 

    		<tr></tr>
    		<tr >
    		<td colspan="2"align="center"><input id="submit"type="submit"value="注册"width="500" ></td>   
    		</tr>      		
   		 </table>
    </form> 
  </body>
</html>
