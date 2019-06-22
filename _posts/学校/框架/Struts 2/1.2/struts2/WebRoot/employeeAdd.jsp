<%@ page language="java" import="java.util.*,java.net.URLDecoder" pageEncoding="UTF-8"%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <title>My JSP 'employeeAdd.jsp' starting page</title>
    
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">
  </head>
  
  <body>
  <h1>定向转发</h1>
<%--    <%= URLDecoder.decode(new String(request.getParameter("username").getBytes("ISO8859-1"),"UTF-8"),"UTF-8") %>
    <form action="/xxx">
    	姓名:<input type="text" name="xxx">
    </form>   --%>
 
    <!-- 上传下载 -->
<%--         <form action="${pageContext.request.contextPath}/control/department/list_execute.action" enctype="multipart/form-data" method="post">
    	文件:<input type="file" name="image">
    	<input type="submit" value="上传"/>
    </form> --%>
    
    
    <!-- 多文件上传 -->
        <form action="${pageContext.request.contextPath}/control/department/list_execute.action" enctype="multipart/form-data" method="post">
    	文件1:<input type="file" name="image"><br/>
    	文件2:<input type="file" name="image"><br/>
    	文件3:<input type="file" name="image"><br/>
    	<input type="submit" value="上传"/>
    </form>
  </body>
</html>
