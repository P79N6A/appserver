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
<title>搜索管理登录界面</title>
</head>
<body>
<div>
		<form action="<%=basePath %>interface/solr/loginIn" method="post">
			用户名:<input type="text" name="userName" ><br/>
			密码: <input type="password" name="password"/><br/>
			<input type="submit" value="提交">
			<input type="reset" value="重置"/>
		</form>
</div>		
</body>
</html>