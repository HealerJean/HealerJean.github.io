<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>

<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>


<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    
    <title>My JSP 'message.jsp' starting page</title>
    
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">    
  </head>
  
  <body>
  		 <!--  结果 -->
  		<%--  ${pathString } --%>
  	<%-- 	${msg } --%>
  	<!-- 配置请求参数 -->
  	
  	<%--    id=${id }<br/>
  		name=${name} --%>
<!--   	person.id = {person.id}<br/>
  	person.name = {person.name} -->
  	
  	
  	<!-- 自定义类型转化器 -->
 <%--  	  ${birthday } --%>
 
<%-- 	 ${app}
   	${applicationScope.app} <br>
    ${sessionScope.ses}<br>
    ${requestScope.req}<br>
    ==============================<br/>
    <c:forEach items="${names}" var="name">
    	${name }<br/>
    </c:forEach>  --%>
    
    
    
<!--     上传文件 -->
<%-- 	${message } --%>

<!-- 自定义拦截器 -->
自定义文件拦截器
	${message }
  
  <br/>
  <s:debug></s:debug>
  
  </body>
</html>
