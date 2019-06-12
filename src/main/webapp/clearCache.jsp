<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
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
<link rel="stylesheet" href="./css/ui.css" type="text/css">
<script type="text/javaScript" src="js/jquery-1.7.2.min.js"></script>

<title>手动清除缓存</title>
<style type="text/css">
	body{
		margin: 50px;
	}
	#resultShow{
		text-align: center;
		color: #cc9966;
		font-weight: bold ;
	}
	
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

div{
	margin: 20px;
	float: left;
	verflow:auto;
	width:80%;
	
}
table{
 margin-left: 10px;
}
table#dataTable{
margin: auto; 
margin-bottom: 10px;
border: 1px solid #00BFFF;
}

div.table{
	border:1px solid #00BFFF; 
	display: table-cell;
	text-align: center;
	vertical-align: middle; 
	margin-top:0px;
	
}
span.title{
	background-color:#00BFFF; 
	display:block; 
	width:100%;
	margin-bottom: 10px;
	height: 30px;
	color: #ffffff;
	font-size: 20px;
	font: bold;
	
}
	
</style>

<script type="text/javascript">


	function getRedisStatus(){
		$.ajax({
			url:"interface/cache/setObject",
			type:"get",
			data:{'key':'mytest','value':'mytest'},
			cache:false,
			dataType:"json",
			success:function(data){
				var re = "0";
				if(data){
					re = data.code;
				}
				if(re == "0"){
					$("#redisStatus").css("color","green").html("可用");
				}else{
					$("#redisStatus").css("color","red").html("不可用");
				}
				
				//window.setTimeout("getRedisStatus()", 10000);
			},
			error:function(){
				$("#redisStatus").html("不能使用");
				//window.setTimeout("getRedisStatus()", 10000);
			}
		});
	}
		
		$(function(){
			
			getRedisStatus();
			
			// 刷新全部缓存
			$("#clearAllButton").click(function(){
				$.ajax({
					url:"interface/cache/refresh",
					type:"get",
					data:"",
					cache:false,
					dataType:"json",
					success:function(data){
						var re = "出错了";
						if(data){
							re = data.msg;
						}
						$("#resultShow").html(re);
						window.setTimeout("clearContent('resultShow')", 5000);
					},
					error:function(){
						$("#resultShow").html("异常鸟~~");
						window.setTimeout("clearContent('resultShow')", 5000);
					}
				});
			});
			
			// 刷新部分缓存
			$("#clearPart").click(function(){
				if($("#key1").val() == ''){
					alert('请输入缓存key');
					return;
				}
				var param = $("#clearPartForm").serialize();
				$.ajax({
					url:"interface/cache/refreshByPattern",
					type:"get",
					data:param,
					cache:false,
					dataType:"json",
					success:function(data){
						var re = "出错了";
						if(data){
							re = data.msg;
						}
						$("#resultShow").html(re);
						window.setTimeout("clearContent('resultShow')", 5000);
					},
					error:function(){
						$("#resultShow").html("异常鸟~~");
						window.setTimeout("clearContent('resultShow')", 5000);
					}
				});
			});
			
			// 刷新指定缓存
			$("#assignCache").click(function(){
				if($("#key2").val() == ''){
					alert('请输入缓存key');
					return;
				}
				var param = $("#assignCacheForm").serialize();
				$.ajax({
					url:"interface/cache/refresh",
					type:"get",
					data:param,
					cache:false,
					dataType:"json",
					success:function(data){
						var re = "出错了";
						if(data){
							re = data.msg;
						}
						$("#resultShow").html(re);
					},
					error:function(){
						$("#resultShow").html("异常鸟~~");
					}
				});
			});
			
			
			// 查询缓存
			$("#queryButton").click(function(){
				if($("#inputKey").val() == ''){
					alert('请输入缓存key');
					return;
				}
				$("#queryResult").html("");
				var url="interface/cache/getObject";
				var param = $("#inputKey").val();
				var type = $("#queryType").val();
				if(type == 2){
					url ="interface/cache/getObjects";
				}
				$.ajax({
					url:url,
					type:"get",
					data:{'key':param},
					cache:false,
					dataType:"json",
					success:function(data){
						if(data){
							
							if(type == 2){
								handleList(data.data);
							}else {
								handleObj(param,data.data);
							}
						}
						
						
					},
					error:function(){
						$("#queryResult").html("异常鸟~~");
					}
				});
			});
			
			
			// 展示key说明
			$("#showCacheKey").click(function(){
				if($("#contentDiv").css("display") == 'none'){
					$("#contentDiv").css("display","block");
					$("#showCacheKey").val("隐藏key说明");
				}else{
					$("#contentDiv").css("display","none");
					$("#showCacheKey").val("展示key说明");
				}
			});

	
		});
		
		// 处理返回对象
		function handleObj(k,data){
			var table=$('<table id="dataTable" border="1">');
			table.appendTo($("#queryResult"));
			var tr=$("<tr></tr>");
			tr.appendTo(table);
			
			fillData(tr,k,data);
				
			
			$("#queryResult").append("</table>");
		}
		
		// 处理返回列表
		function handleList(data){
			var keyTemp='';
			
			var table=$('<table id="dataTable" border="1">');
			table.appendTo($("#queryResult"));
			for(var i=0,l=data.length;i<l;i++){
				var tr=$("<tr></tr>");
				tr.appendTo(table);
				for(var key in data[i]){
					
					//key
					if(key == 'key'){
						keyTemp =data[i][key];
					}
					//value
					if(key == 'value'){
						fillData(tr,keyTemp,data[i][key]);
					}
				 }
				
				keyTemp = '';
			 }
			
			
			$("#queryResult").append("</table>");
		}
		
		// 回填数据
		function fillData(tr,key,value){
			
			var str="key["+key+"]";
			
			var td=$("<td>"+str+"</td>");
			td.appendTo(tr);
			
			for(var p in value){
				tr.append($("<td>"+p+":"+value[p]+"</td>"));
			}
			
			
		}
		
		
		function clearContent(obj){
			$("#"+obj).html("");
		}
		
		
</script>
</head>
<body>
<div class="table">
<span class="title">缓存运行情况</span>
<table border="1" width="78%" id="dataTable">
	<tr>
		<td style="font-weight: bold;">缓存组件</td>
		<td style="font-weight: bold;">状态</td>	
	</tr>
	<tr>
		<td>Redis</td>
		<td id="redisStatus" style="color: green">可用</td>
	</tr>
</table>
</div>

<div class="table">
<span class="title">缓存查询</span>
<table>
	<tr>
		<td>
			<select id="queryType">
				<option value="1">精确查询</option>
				<option value="2">模糊查询</option>
		</select>
		</td>
		<td>请输入缓存key:</td>
		<td><input type="text" id="inputKey" /> </td>
		<td><input type="button" value="查询" id="queryButton"/></td>
		<td>&nbsp;&nbsp;说明：精确查询是指按key找到指定的缓存值；模糊查询是指按key的前缀查询满足条件的一类缓存值</td>
	</tr>
</table>

<div id="queryResult" ></div>
</div>



<div class="table">
<span class="title">清除缓存操作</span>
	<table>
		<tr>
			<td>
				清空所有缓存数据操作：
			</td>
			<td>
				<input type="button" id="clearAllButton" value="清空全部缓存" style="width: 100px">
			</td>
		</tr>
	</table>


<form id="clearPartForm">
	<table>
		<tr>
			<td>
				清空部分缓存操作：
			</td>
			<td>
				Key前缀:
			</td>
			<td>
				<input id= "key1" name="key" value="" class="input"/>
			</td>
			<td>
				<input type="button" id="clearPart" value="清空" >
			</td>
			<td>
				说明：输入缓存key的前缀即可，例如，我想删除所有用户的缓存，用户的缓存前缀是user，则输入user提交就会清空所有用户缓存
			</td>
			
		</tr>
	</table>
</form>

<form id="assignCacheForm">
	<table>
		<tr>
			<td>
				清空指定key缓存操作：
			</td>
			<td>
				指定Key:
			</td>
			<td>
				<input id= "key2" name="key" value=""  class="input"/>
			</td>
			<td>
				<input type="button" id="assignCache" value="清空" >
			</td>
		</tr>
	</table>
</form>
</div>

<div id="resultShow"></div>


<div><input type="button" value="展示key说明" id="showCacheKey"></div>
<div id="contentDiv" style="display:none">
	<table id="dataTable" border="1">
		<tr>
			<th>key举例</th> <th>key命名规则</th> <th>备注</th>
		</tr>
		<tr>
			<td>UserDto:1001</td>
			<td>" UserDto:" + userId</td>
			<td>用户缓存</td>
		</tr>
		<tr>
			<td>Attachment:10011</td>
			<td>"Attachment:" + attachmentId</td>
			<td>附件缓存</td>
		</tr>
		<tr>
			<td>CitiesDto:110100</td>
			<td>"CitiesDto:" + cityId</td>
			<td>城市缓存</td>
		</tr>
		<tr>
			<td>DistrictDto:110101</td>
			<td>"DistrictDto:" + districtId</td>
			<td>区县缓存</td>
		</tr>
		<tr>
			<td>GoodsDto:1001</td>
			<td> "GoodsDto:" + goodsId</td>
			<td>商品缓存</td>
		</tr>
		
		<tr>
			<td>GoodsCategoryDto:1001</td>
			<td>"GoodsCategoryDto:" + goodsCategoryId</td>
			<td>商品分类缓存</td>
		</tr>
		<tr>
			<td>Unit:1001</td>
			<td>"Unit:" + unitId</td>
			<td>计量单位缓存</td>
		</tr>
		
		
		<tr>
		
			<td>ProvinceDto:1001</td>
			<td>"ProvinceDto:" + provinceId</td>
			<td>省缓存</td>
		</tr>
		<tr>
			<td>ShopDto:1001</td>
			<td>"ShopDto:" + shopId</td>
			<td>商铺缓存</td>
		</tr>
		
		<tr>
			<td>TownDto:110101</td>
			<td>"TownDto:" + townId</td>
			<td>街道缓存</td>
		</tr>
		<tr>
			<td>ADS:cityId:110100:adSpaceCode:index:pNo:1:pSize:10</td>
			<td>"ADS:cityId:" + cityId + ":adSpaceCode:" + adSpaceCode + ":pNo:" + page + ":pSize:" + pageSize</td>
			<td>首页广告分页列表缓存</td>
		</tr>
		<tr>
			<td>adKeyList:cityId:110100:adSpaceCode:index</td>
			<td>"adKeyList:cityId:"+cityId+":adSpaceCode:"+adSpaceCode</td>
			<td>首页广告列表缓存</td>
		</tr>
		<tr>
			<td>COLUMN:cityId:110100:parentColumnId:0:columnType:200:pNo:1:pSize:10</td>
			<td>"COLUMN:cityId:" + cityId + ":parentColumnId:" + parentColumnId +":columnType:"+columnType+ ":pNo:" + page + ":pSize:" + pageSize</td>
			<td>栏目下级栏目列表</td>
		</tr>
		<tr>
			<td>userRedPacket:1001:1:10</td>
			<td>"userRedPacket:" + userId + ":" + pNo+ ":" + pSize</td>
			<td>用户红包分页列表</td>
		</tr>
		<tr>
			<td>qrRedPacketKeys:1001</td>
			<td>"qrRedPacketKeys:" + userId</td>
			<td>红包列表缓存</td>
		</tr>
		<tr>
			<td>smsParam:eftime</td>
			<td>smsParam:eftime</td>
			<td>短信验证码失效时间缓存</td>
		</tr>
		<tr>
			<td>smsParam:smsUrl</td>
			<td>smsParam:smsUrl</td>
			<td>短信平台URL缓存</td>
		</tr>
		<tr>
			<td>smsParam:smsSn</td>
			<td>smsParam:smsSn</td>
			<td>短信平台用户名缓存</td>
		</tr>
		<tr>
			<td>smsParam:smsPwd</td>
			<td>smsParam:smsPwd</td>
			<td>短信平台密码缓存</td>
		</tr>
		
		<tr>
			<td>smsParam:smsGeneral</td>
			<td>smsParam:smsGeneral</td>
			<td>短信通用模板缓存</td>
		</tr>
		<tr>
			<td>smsParam:sms_channel</td>
			<td>smsParam:sms_channel</td>
			<td>短信发送通道 </td>
		</tr>	
		<tr>
			<td>veriCode:13645454545</td>
			<td>"veriCode:" + mobile"</td>
			<td>手机验证码缓存</td>
		</tr>
		<tr>
			<td>smsIp:192.168.1.120:136454545455</td>
			<td>"smsIp:" + requestIP + ":"+ mobile</td>
			<td>访问IP缓存</td>
		</tr>
		<tr>
			<td>system:message:1:10</td>
			<td>"system:message:" + page + ":" + pageSize</td>
			<td>系统消息分页列表缓存</td>
		</tr>
		<tr>
			<td>shop:message:1:10: 987654357</td>
			<td>"shop:message:" + page +":" + pageSize + ":" + shopId</td>
			<td>店铺消息分页列表缓存</td>
		</tr>
		<tr>
			<td>all:message:1:10</td>
			<td>"all:message:" + page + ":" + pageSize</td>
			<td>所有消息分页列表缓存</td>
		</tr>
		<tr>
			<td>shopDevice:1001</td>
			<td>"shopDevice:" + shopId</td>
			<td>收银机token和regId缓存</td>
		</tr>
		<tr>
			<td>mobile:136454545451</td>
			<td>"mobile:" + mobile</td>
			<td>手机号码和用户id映射关系缓存</td>
		</tr>
		
	
		<tr>
			<td>classifikey:1881</td>
			<td>"classifikey:" + bizId</td>
			<td>运营分类关键字</td>
		</tr>
		<tr>
			<td>goodsPicture:1001</td>
			<td>"goodsPicture:" + bizId</td>
			<td>商品缩略图缓存</td>
		</tr>
		<tr>
			<td>goodsGroupPicture:1001</td>
			<td>"goodsGroupPicture:" + bizId</td>
			<td>商品族缩略图缓存</td>
		</tr>
		<tr>
			<td>shopTopGoods:1001</td>
			<td>"shopTopGoods:" + shopId</td>
			<td>获取商铺销量最高的商品或商品族</td>
		</tr>
		<tr>
			<td>1dcqSetting_user_service_agreement</td>
			<td>"1dcqSetting_" + settingKey</td>
			<td>系统设置信息</td>
		</tr>
		<tr>
			<td>cityOperationClasses:_100010_1_1_10</td>
			<td>"cityOperationClasses:_" + cityId + "_" + parentClassifyId + "_" + pNo + "_" + pSize</td>
			<td>获取运营分类的下级运营分类分页列表</td>
		</tr>
		
	</table>
</div>
<hr>
<div>
<jsp:include page="inlcude_operate.jsp"></jsp:include>
</div>
</body>
</html>