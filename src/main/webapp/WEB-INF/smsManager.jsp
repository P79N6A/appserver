
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page isELIgnored="false"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";

%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link rel="stylesheet" href="<%=basePath %>css/table.css" type="text/css">
<title>短信查询列表</title>

</head>
<body>
<!-- 查询条件 -->
<div  class="demo">
<form  action="<%=basePath %>interface/common/querySmsList" method="get">
	<table>
		<tr>
			<td>手机号码：
			<input type="text"  name="mobile" value="${mobile }"/>
			<input type="submit" value="查询" />
			&nbsp;说明：查询24小时内数据</td>
		</tr>
	</table>
</form>
</div>

<!-- 查询结果 -->
 <div class="demo">
	<table class="bordered">
		<thead>
			<tr>
				<th>序号</th>        
				<th>手机号码</th>
				<th>发送内容</th>
				<th>发送时间</th>
				<th>状态</th>
			</tr>
		</thead>
		<tbody>
			<c:choose>
				<c:when test="${smsList == null || smsList.size() ==0}">
					<tr><td colspan="5">无记录</td></tr>
				</c:when>
				<c:otherwise>
					<c:forEach var="sms"  items="${smsList}" varStatus="status">
     			<tr>
     				<td>${status.index + 1}</td>
     				<td>${sms.sendMobile }</td>
     				<td>${sms.sendContent }</td>
     				<td><fmt:formatDate value="${sms.sendTime }" pattern="yyyy-MM-dd HH:mm:ss"/></td>
     				<td>
     					<c:choose>
       						<c:when test="${sms.sendStatus == 0 || sms.sendStatus ==1}">
           						 成功
      	 					</c:when>
      			 			<c:otherwise>
              					失败
       						</c:otherwise>
						</c:choose>
     				</td>
     			</tr>
			</c:forEach>
				</c:otherwise>
			</c:choose>
			
		</tbody>
	</table>
	</div> 
</body>
</html>