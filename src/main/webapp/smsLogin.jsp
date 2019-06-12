<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link rel="stylesheet" href="<%=basePath %>css/table.css" type="text/css">
<script type="text/javaScript" src="js/jquery-1.7.2.min.js"></script>
<title>登录界面</title>
<style type="text/css">
 
</style>
</head>
<body>
<div class="demo" style="width:400px;">
	<form action="<%=basePath %>interface/common/login" method="post">
	<table class="bordered" >
		<tr>
			<td>用户名:</td>
			<td><input type="text" name="userName" ></td>
		</tr>
		
		<tr>
			<td>密码:</td>
			<td><input type="password" name="password"/></td>
		</tr>
		
		<tr>
			<td colspan="2" >
			   <input type="submit" value="提交">
			   <input type="reset" value="重置"/>
			 </td>
		</tr>
		
	</table>
	</form>	
		
		
</div>		
</body>
</html>