<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <title>My JSP 'index.jsp' starting page</title>
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">    
  </head>
  
  <body>
<%--    <form action="<%=request.getContextPath()%>/control/department/list_execute.action" method="post">
   		id:<input type="text" name="id"><br/>
   		name:<input type="text" name="name"><br/>
   		<input type="submit" value="发送"/>
   </form> --%>
     <form action="<%=request.getContextPath()%>/control/department/list_execute.action" method="post">
   		id:<input type="text" name="person.id"><br/>
   		name:<input type="text" name="person.name"><br/>
   		<input type="submit" value="发送"/>
   </form>
  </body>
</html>
