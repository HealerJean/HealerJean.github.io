<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<%@taglib prefix="html" uri="http://struts.apache.org/tags-html" %>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <base href="<%=basePath%>">
    
    <title>My JSP 'login.jsp' starting page</title>
    
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
  	<!-- property 就是之前button的name -->
    <html:button property="mybutton" value="提交"></html:button>
    
 <!--    下面这个是图片按钮 -->  
	<html:image src="images/a.jpg"> </html:image>
	<html:img src="/images/a.png"/> 
   	<html:form action="/login.do" method="post">
	 
		<html:text property="username"></html:text>
	 	<html:checkbox property="check" value="place">place123123</html:checkbox>
	 	<html:submit value="注册" ></html:submit>
	 	<html:cancel  value="重写" ></html:cancel>
	</html:form>
  </body>
</html>
