<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page isELIgnored="false" %>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>搜索节点状态管理</title>
<style type="text/css">
	div{
		width:800px;
		 margin: 0 auto;
	}
	table {
	 border-width: 1px;
	 border-collapse: collapse;
	 border-color: gray;
	 width:1200px;
	 margin: 0 auto;
}
</style>
<script type="text/javascript" src="<%=basePath %>js/jquery-1.7.2.min.js"></script>
</head>
<body>
	<div>
	<h1>solr节点管理</h1>
		 <table border="1">
				<tr><td>ip地址</td><td>状态</td><td>操作</td><td>实时信息</td></tr>
				<c:forEach items="${solrNodes}" var="entry">
					<tr>
						<td>
						<a href="${entry.key}" target="blank">${entry.key}</a>
							
						</td>
						<td>
							<c:choose>
								<c:when test="${entry.value.status==1 }">
									正常
								</c:when>
								<c:when test="${entry.value.status==2||entry.value.status==3 }">
									从集群中移除（异常产生时间:${entry.value.happendTime}）
								</c:when>
								<c:when test="${entry.value.status==4}">
									正在重建索引
								</c:when>
								<c:when test="${entry.value.status==5 }">
									索引结束
								</c:when>
							</c:choose>
						</td>	
						<td>
						
							<c:choose>
								<c:when test="${entry.value.status==1 }">
									<a href="javaScript:removeFromLvs('${entry.key }')">从LVS中剔除</a>
								</c:when>
								<c:when test="${entry.value.status==2||entry.value.status==3 }">
									<a href="javaScript:rebuildSingleNodeIndex('${entry.key }','node${entry.value.tag}')">
										重建索引
									</a>
									&nbsp;&nbsp;
									<a href="javaScript:rebuildSingleNodeIndex('${entry.key }','node${entry.value.tag}','${entry.value.happendTime}')">
										从异常发生时间开始重建
									</a>
								</c:when>
								<c:when test="${entry.value.status==4||entry.value.status==5 }">	
									<a href="javaScript:recoveryFromLvs('${entry.key}')">从LVS中恢复</a>
								</c:when>
							</c:choose>
						</td>
						<td id="node${entry.value.tag}">
						
						</td>
					</tr>
				</c:forEach>
			</table>
			<hr>
			<h1>添加Solr节点</h1>
			<form action="<%=basePath %>interface/addSolrNode" method="post">
				solr节点ip:<input type="text" name="solrIp"/><br/>
				<input type="submit" />					
			</form>
	</div>		
</body>

<script>
	var loadIndexCircle;
	var nodeIp;
	var nodeTag;
	function rebuildSingleNodeIndex(nodeKey,id,lastUpdateTime)
	{
		nodeTag=id;
		$.ajax({
			url:'<%=basePath %>interface/rebuildSingleNodeIndex?nodeKey='+nodeKey,
			dataType:'json',
			success:function(data){
				if(data.success==true)
				{
					alert(nodeKey+"开始重建索引了");
					nodeIp=nodeKey; 
					loadIndexCircle=setInterval("loadIndexInfo()",500);
				}
			}
		});		
	}
	
	//实时加载索引信息
	function loadIndexInfo()
	{
		$.ajax({
			url:'<%=basePath %>interface/loadIndexInfo',
			data:{
				nodeKey:nodeIp
			},
			dataType:'json',
			success:function(data){
				var target=$("#"+nodeTag);
				console.log(data.indexdNum);
				target.html("总条数:"+data.totalNum+"/已索引条数:"+data.indexdNum);
				console.log(target.html());
				if(parseInt(data.totalNum)==parseInt(data.indexdNum))
				{
					clearInterval(loadIndexCircle);
				}
			}
		});
	}
	
	function removeFromLvs(nodeKey)
	{
		$.ajax({
			url:'<%=basePath %>/interface/removeFromLvs',
			data:{
				ip:nodeKey
			},
			dataType:'json',
			success:function(data){
				alert("已通知LVS移除节点"+nodeKey);
				window.location.reload();
			}
		});
	}
	//将节点恢复
	function recoveryFromLvs(nodeKey)
	{
		$.ajax({
			url:'<%=basePath %>/interface/recoveryFromLvs',
			data:{
				ip:nodeKey
			},
			dataType:'json',
			success:function(data){
				alert("已通知lvs恢复节点");
				window.location.reload();
			}
		});
	}
</script>
</html>
		