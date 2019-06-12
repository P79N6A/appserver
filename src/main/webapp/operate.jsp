<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
%>
<html>
<head>
<base href="<%=basePath %>"/>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>操作内存中的Properties</title>
<script type="text/javaScript" src="js/jquery-1.7.2.min.js"></script>
<style type="text/css">
	.input{width:250px;height: 25px;line-height: 25px;vertical-align: middle;}
	.select{width:100px;height: 30px;line-height: 30px;vertical-align: middle;}
	table tr{line-height: 40px;}
</style>
</head>
<body>
	<div align="center">
		<h3>设置内存中Properties的key的值为value</h3>
		<div>
			<label id="operateResultShow"></label>
		</div>
		<div id="operarte">
			<form id="operateForm">
				<table>
					<tr>
						<td align="left"><span style="color: red">*</span>key：</td>
						<td align="left"><input type="text" name="key"  class="input"/></td>
						<td align="left">Properties中对应的key，如：redis.switch</td>
					</tr>
					<tr>
						<td align="left">val：</td>
						<td align="left"><input type="text" name="val" class="input"/></td>
						<td align="left">需要设置的值，如：off</td>
					</tr>
					<tr>
						<td align="left"><span style="color: red">*</span>obj：</td>
						<td align="left">
							<input type="text" name="obj" class="input" value="redis"/>
							<select id="selectObj" class="select">
								<option value="redis">redis</option>
								<option value="jpush">jpush</option>
								<option value="solr">solr</option>
								<option value="cache">cache</option>
								<option value="threadPool">threadPool</option>
							</select>
						</td>
						<td align="left">指定Properties</td>
					</tr>
					<tr>
						<td colspan="3" align="center">
							<input type="button" value="提交" id="subBtn" style="width: 120px;height: 30px;vertical-align: middle;"/>
						</td>
					</tr>
				</table>
			</form>
		</div>
		<hr/>
		<h3>获取内存中Properties的key的value</h3>
		<div>
			<label id="qeuryResultShow"></label>
		</div>
		<div id="query">
			<form id="qeuryForm">
				<table>
					<tr>
						<td align="left"><span style="color: red">*</span>key：</td>
						<td align="left"><input type="text" name="key"  class="input"/></td>
						<td align="left">Properties中对应的key，如：redis.switch</td>
					</tr>
					<tr>
						<td align="left"><span style="color: red">*</span>obj：</td>
						<td align="left">
							<input type="text" name="obj" class="input" value="redis"/>
							<select id="selectObj" class="select">
								<option value="redis">redis</option>
								<option value="jpush">jpush</option>
								<option value="solr">solr</option>
								<option value="cache">cache</option>
								<option value="threadPool">threadPool</option>
							</select>
						</td>
						<td align="left">指定Properties</td>
					</tr>
					<tr>
						<td colspan="3" align="center">
							<input type="button" value="查询" id="queryBtn" style="width: 120px;height: 30px;vertical-align: middle;"/>
						</td>
					</tr>
				</table>
			</form>
		</div>
	</div>
	<script type="text/javascript">
		$(function(){
			$(".select").change(function(){
				var val = $(this).find("option:selected").text();
				$(this).siblings("input[name='obj']").val(val);
			});
			$("#subBtn").click(function(){
				var param = $("#operateForm").serialize();
				$.ajax({
					url:"interface/common/refreshAppconfig",
					type:"post",
					data:param,
					cache:false,
					dataType:"json",
					success:function(data){
						var re = "出错了";
						if(data){
							re = data.msg;
						}
						$("#operateResultShow").html(re);
						window.setTimeout("clearContent('operateResultShow')", 5000);
					},
					error:function(){
						$("#operateResultShow").html("异常鸟~~");
						window.setTimeout("clearContent('operateResultShow')", 5000);
					}
				});
			});
			$("#queryBtn").click(function(){
				var param = $("#qeuryForm").serialize();
				$.ajax({
					url:"interface/common/queryAppconfig",
					type:"post",
					data:param,
					cache:false,
					dataType:"json",
					success:function(data){
						var re = "出错了";
						if(data){
							if (data.code == 0) {
								if(data.data && data.data.value){
									re = "查询结果为："+data.data.value;
								}else{
									re = "没有查询到结果";
								}
							}else{
								re = data.msg;
							}
						}
						$("#qeuryResultShow").html(re);
						window.setTimeout("clearContent('qeuryResultShow')", 5000);
					},
					error:function(){
						$("#qeuryResultShow").html("异常鸟~~");
						window.setTimeout("clearContent('qeuryResultShow')", 5000);
					}
				});
			});
		});
		
		function clearContent(obj){
			$("#"+obj).html("");
		}
	</script>
</body>
</html>