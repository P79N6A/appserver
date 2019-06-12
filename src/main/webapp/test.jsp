<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page isELIgnored="false"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">

<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link href="../css/css.css" rel="stylesheet" type="text/css" />
<title>测试页面</title>
</head>
<body id="cntlist">
	<div class="data_list">
	<table width="100%" width="100%" border="0" cellpadding="0" cellspacing="0">
		<thead>
			<tr>
				<th width="5%">编号</th>
				<th width="9%">接口名称</th>
				<th width="6%">优先级</th>
				<th width="10%">接口类型</th>
				<th width="7%">责任人</th>
				<th width="25%">接口地址</th>
				<th width="10%">接口数据</th>
				<th>响应结果</th>
			</tr>
		</thead>
		<tbody>
			<c:forEach items="${testList}" var="test">
				<tr>
					<td>${test.id}</td>
					<td>${test.name}</td>
					<td>${test.priority}</td>
					<td>${test.reqType}</td>
					<td>${test.person}</td>
					<td>${test.reqUrl}</td>
					<td>${test.dataStr}</td>
					<td>${test.content}</td>
				</tr>
			</c:forEach>
		</tbody>
	</table>
	</div>
</body>
</html>