<%@page import="java.util.Properties"%>
<%@page import="com.idcq.appserver.common.CommonConst"%>
<%@page import="java.util.Map"%>
<%@ page isELIgnored="false" %>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<style type="text/css">
.input {
	width: 250px;
	height: 25px;
	line-height: 25px;
	vertical-align: middle;
}

.select {
	width: 100px;
	height: 30px;
	line-height: 30px;
	vertical-align: middle;
}

table tr {
	line-height: 40px;
}
</style>
<%
	Map<String,Properties> propMap = CommonConst.PROP_MAP;
%>
<body>
	
		<div id="operarte" class="table">
		<span class="title">设置内存中Properties的key的值为value</span>
		<div>
			<label id="operateResultShow"></label>
		</div>
		
			<form id="operateForm">
				<table>
					<tr>
						<td align="left"><span style="color: red">*</span>key：</td>
						<td align="left"><input type="text" name="key" class="input" /></td>
						<td align="left">Properties中对应的key，如：redis.switch</td>
					</tr>
					<tr>
						<td align="left">val：</td>
						<td align="left"><input type="text" name="val" class="input" /></td>
						<td align="left">需要设置的值，如：off</td>
					</tr>
					<tr>
						<td align="left"><span style="color: red">*</span>obj：</td>
						<td align="left"><input type="text" name="obj" class="input"
							value="redis" /> <select id="selectObj" class="select">
								<c:forEach items="<%=propMap %>" var="prop">
									<option value="${prop.key}">${prop.key}</option>
								</c:forEach>
								<%-- 
								<option value="redis">redis</option>
								<option value="jpush">jpush</option>
								<option value="solr">solr</option>
								<option value="cache">cache</option>
								<option value="threadPool">threadPool</option>
								--%>
						</select></td>
						<td align="left">指定Properties</td>
					</tr>
					<tr>
						<td colspan="3" align="center"><input type="button"
							value="提交" id="subBtn"
							style="width: 120px; height: 30px; vertical-align: middle;" /></td>
					</tr>
				</table>
			</form>
		</div>
		
		<div id="query" class="table">
		<span class="title">获取内存中Properties的key的value</span>
		<div>
			<label id="qeuryResultShow"></label>
		</div>
	
			<form id="qeuryForm">
				<table>
					<tr>
						<td align="left"><span style="color: red">*</span>key：</td>
						<td align="left"><input type="text" name="key" class="input" /></td>
						<td align="left">Properties中对应的key，如：redis.switch</td>
					</tr>
					<tr>
						<td align="left"><span style="color: red">*</span>obj：</td>
						<td align="left"><input type="text" name="obj" class="input"
							value="redis" /> <select id="selectObj" class="select">
								<c:forEach items="<%=propMap %>" var="prop">
									<option value="${prop.key}">${prop.key}</option>
								</c:forEach>
								<%-- 
								<option value="redis">redis</option>
								<option value="jpush">jpush</option>
								<option value="solr">solr</option>
								<option value="cache">cache</option>
								<option value="threadPool">threadPool</option>
								--%>
						</select></td>
						<td align="left">指定Properties</td>
					</tr>
					<tr>
						<td colspan="3" align="center"><input type="button"
							value="查询" id="queryBtn"
							style="width: 120px; height: 30px; vertical-align: middle;" /></td>
					</tr>
				</table>
			</form>
		</div>

	<script type="text/javascript">
		$(function() {
			$(".select").change(function() {
				var val = $(this).find("option:selected").text();
				$(this).siblings("input[name='obj']").val(val);
			});
			$("#subBtn").click(
					function() {
						var param = $("#operateForm").serialize();
						$.ajax({
							url : "interface/common/refreshAppconfig",
							type : "post",
							data : param,
							cache : false,
							dataType : "json",
							success : function(data) {
								var re = "出错了";
								if (data) {
									re = data.msg;
								}
								$("#operateResultShow").html(re);
								window.setTimeout(
										"clearContent('operateResultShow')",
										5000);
							},
							error : function() {
								$("#operateResultShow").html("异常鸟~~");
								window.setTimeout(
										"clearContent('operateResultShow')",
										5000);
							}
						});
					});
			$("#queryBtn")
					.click(
							function() {
								var param = $("#qeuryForm").serialize();
								$
										.ajax({
											url : "interface/common/queryAppconfig",
											type : "post",
											data : param,
											cache : false,
											dataType : "json",
											success : function(data) {
												var re = "出错了";
												if (data) {
													if (data.code == 0) {
														if (data.data
																&& data.data.value) {
															re = "查询结果为："
																	+ data.data.value;
														} else {
															re = "没有查询到结果";
														}
													} else {
														re = data.msg;
													}
												}
												$("#qeuryResultShow").html(re);
												window
														.setTimeout(
																"clearContent('qeuryResultShow')",
																5000);
											},
											error : function() {
												$("#qeuryResultShow").html(
														"异常鸟~~");
												window
														.setTimeout(
																"clearContent('qeuryResultShow')",
																5000);
											}
										});
							});
		});

		function clearContent(obj) {
			$("#" + obj).html("");
		}
	</script>