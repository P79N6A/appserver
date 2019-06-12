<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    
<%
String path = request.getContextPath();
String basePath = request.getScheme() + "://" 
		+ request.getServerName() + ":" + request.getServerPort()+ path + "/";
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
	<head>
		<script type="text/javaScript" src="<%=path%>/js/jquery-1.7.2.min.js"></script>
		
	</head>
<body>
	<h2>version:2015120901</h2>
	
	
	
	
	<h4>MC1：文件上传接口</h4>
	<h5>接口开发者:盛志鹏</h5>
	<h5>date: </h5>
	<form action="<%=basePath%>interface/service/common/commonBatchUploadFile" enctype="multipart/form-data" method="post">
        userId：<input name="userId" value="1485448" >
        mimeType：<input name="mimeType" value="jpg" >
        bizId：<input name="bizId" value="111" >
        bizType：<input name="bizType" value="clientSystemLog " >
         file：<input name="file" type="file" >
		<input id="sub2" type="submit" value="上传文件" />
	</form>
	<h5>
		tips:用户必须存在，usageType必须按规范填写<br>
	</h5>
	<hr>
	<h4>MC1：批量文件上传接口</h4>
	<h5>接口开发者:卢建平</h5>
	<h5>date: </h5>
	<form action="<%=basePath%>interface/user/commonBatchUploadFile" enctype="multipart/form-data" method="post">
        userId：<input name="userId" value="1485448" >
        mimeType：<input name="mimeType" value="jpg" >
         file：<input name="file" type="file" >
         file：<input name="file" type="file" >
         file：<input name="file" type="file" >
		<input id="sub2" type="submit" value="上传文件" />
	</form>
	<h5>
		tips:用户必须存在，usageType必须按规范填写<br>
	</h5>
	<hr>
	<%--
	<h2>Hello World!</h2>
	<hr>
	<h2><%=basePath%></h2>
	<h3>1,test restful get</h3>
	<h4>request:<%=basePath%>main/11</h4>
	<form action="<%=basePath%>main/11" method="get">
		<input type="submit" value="submit"/>
	</form>
	<hr>
	<h3>2,test restful post</h3>
	<h4>request:<%=basePath%>main/33/ljp</h4>
	<form action="<%=basePath%>main/33/ljp" method="post">
		<input type="submit" value="submit"/>
	</form>
	<hr>
	<h3>3,test general post</h3>
	<form action="<%=basePath%>main/index" method="post">
		id:<input type="text" name="id"/><br>
		name:<input type="text" name="name"/><br>
		<input type="submit" value="submit"/>
	</form>
	<hr>
	<h3>4,test service</h3>
	<a href="<%=basePath%>main/testService">测试服务层</a>
	<hr>
	<h3>4,测试数据库新增操作</h3>
		<form action="<%=basePath%>main/testDb" method="get">
		<!-- 
		id:<input type="text" name="id"/><br>
		 -->
		name:<input type="text" name="name"/><br>
		<input type="submit" value="submit"/>
	</form>
	<h3>5,测试不响应情况</h3>
		<form action="<%=basePath%>main/testReturn" method="post">
		<input type="submit" value="submit"/>
	</form>
	  --%>
	  
	<h3>6,更新购物车测试</h3>
	<h2>json: "{\"userId\":1179341045,\"goodsList\":[{\"shopId\":2147483647,\"goodsId\":1,\"goodsNumber\":2,\"goodsIndex\":1}]}";</h2>
		<form action="<%=basePath%>main/testReturn" method="post">
		<input id="sub2" type="button" value="提交json" />
	</form>
	<hr>
	<h3>6,下单测试</h3>
	<h2>json: "{\"userId\":1179341046,\"distributionType\":1,\"payTimeType\":0,\"orderTotalPrice\":9.99,\"oGoodsList\":[{\"shopId\":2147483647,\"goodsId\":1},{\"shopId\":2147483647,\"goodsId\":1}]}";</h2>
		<form action="<%=basePath%>main/testReturn" method="post">
		<input id="sub" type="button" value="提交json" />
	</form>
	<hr>
	<h3>7,更新订单测试</h3>
	<h2>json: "{\"userId\":1179341046,\"distributionType\":1,\"payTimeType\":0,\"orderTotalPrice\":9.99,\"oGoodsList\":[{\"shopId\":2147483647,\"goodsId\":1},{\"shopId\":2147483647,\"goodsId\":1}]}";</h2>
		<form action="<%=basePath%>main/testReturn" method="post">
		<input id="sub3" type="button" value="提交json" />
	</form>
	<hr>
	<h3>8,编辑用户基本资料测试</h3>
	<h2>json: "{\"userId\":1179341073,\"imgBig\":\"/user/img/002.png\",\"imgSmall\":\"/user/img/002_small.png\",\"nikeName\":\"贪吃羊2\",\"sex\":1,\"address\":\"深圳\",\"mobile\":\"13637589781\",\"name\":\"孙洪亮2\",\"idCard\":\"23230111111232\",\"bankCard\":\"23230111111232\",\"payPassword\":\"\"}";</h2>
		<form action="<%=basePath%>main/testReturn" method="post">
		<input id="sub4" type="button" value="提交json" />
	</form>
	<hr>
	<h3>9,手机号码更改测试</h3>
	<h2>json: "{\"userId\":1179341046,\"distributionType\":1,\"payTimeType\":0,\"orderTotalPrice\":9.99,\"oGoodsList\":[{\"shopId\":2147483647,\"goodsId\":1},{\"shopId\":2147483647,\"goodsId\":1}]}";</h2>
		<form action="<%=basePath%>main/testReturn" method="post">
		<input id="sub5" type="button" value="提交json" />
	</form>
	<hr>
	<h3>10,支付密码更改测试</h3>
	<h2>json: "{\"userId\":1179341046,\"distributionType\":1,\"payTimeType\":0,\"orderTotalPrice\":9.99,\"oGoodsList\":[{\"shopId\":2147483647,\"goodsId\":1},{\"shopId\":2147483647,\"goodsId\":1}]}";</h2>
		<form action="<%=basePath%>main/testReturn" method="post">
		<input id="sub6" type="button" value="提交json" />
	</form>
	<hr>
	<h3>11,用户预定商铺资源测试</h3>
	<h2>json: "{\"userId\":1179341046,\"distributionType\":1,\"payTimeType\":0,\"orderTotalPrice\":9.99,\"oGoodsList\":[{\"shopId\":2147483647,\"goodsId\":1},{\"shopId\":2147483647,\"goodsId\":1}]}";</h2>
		<form action="<%=basePath%>main/testReturn" method="post">
		<input id="sub7" type="button" value="提交json" />
	</form>
	<hr>
	<h3>12,用户注册post测试</h3>
	<h2>json: "{\"userId\":1179341046,\"distributionType\":1,\"payTimeType\":0,\"orderTotalPrice\":9.99,\"oGoodsList\":[{\"shopId\":2147483647,\"goodsId\":1},{\"shopId\":2147483647,\"goodsId\":1}]}";</h2>
		<form action="<%=basePath%>main/testReturn" method="post">
		<input id="sub8" type="button" value="提交json" />
	</form>
	<hr>
	
	 
	<h1>各位伙伴，以后新完成的接口就在下面一条条列出来吧，给联调的同事一些方便</h1>	
	<h4>格式，如：</h4>
	<h4>1,登录接口</h4>
	<h5>接口开发者:</h5>
	<h5>date: 20150313</h5>
	<h5>URL:</h5>
	<h5>tips:</h5>
	<hr>
	<h4>1,获取购物车商品列表</h4>
	<h5>接口开发者:卢建平</h5>
	<h5>date: 20150313</h5>
	<h5>
		<a href="interface/cart/getCart?userId=1179341045&pNo=1&pSize=10" target="_blank">
		URL:interface/cart/getCart?userId=1179341045&pNo=1&pSize=10
		</a>
	</h5>
	<h5>
		tips:
	</h5>
	<hr>
	<h4>U3,订单详情</h4>
	<h5>接口开发者:卢建平</h5>
	<h5>date: 20150415</h5>
	<h5> 
		<a href="interface/order/getOrderDetail?orderId=98765432315041515551956097858 " target="_blank">
		URL:<%=basePath%>interface/order/getOrderDetail?orderId=98765432315041515551956097858 
		</a>
	</h5>
	<h5>
		tips:
	</h5>
	<hr>
	<h4>3,更新购物车商品列表</h4>
	<h5>接口开发者:卢建平</h5>
	<h5>date: 20150313</h5>
	<h5>
		<a href="interface/cart/updateCart" target="_blank">
		URL:interface/cart/updateCart
		</a>
	</h5>
	<h5>
		tips:<br>
		以post方法提交json数据;<br>
		请求头,content-type=application/json;<br>
		提交数据data,"{\"userId\":1179341045,\"goodsList\":[{\"shopId\":2147483647,\"goodsId\":1,\"goodsNumber\":2,\"goodsIndex\":1}]}";
	</h5>
	<hr>
	<h4>4,获取所有城市接口</h4>
	<h5>接口开发者:朱海全</h5>
	<h5>date: 20150313</h5>
	<h5>
		<a href="interface/home/getAllCitis" target="_blank">
		URL:interface/home/getAllCitis
		</a>
	</h5>
	<h5>
		tips:<br>
	</h5>
	<hr>
	<h4>5,获取商铺中的商品分类接口</h4>
	<h5>接口开发者:朱海全</h5>
	<h5>date: 20150313</h5>
	<h5>
		<a href="interface/goods/getShopGoodsCategory?shopId=1" target="_blank">
		URL:interface/goods/getShopGoodsCategory?shopId=1
		</a>
	</h5>
	<h5>
		tips:<br>
	</h5>
	<hr>
	<h4>7,获取商铺资源分组</h4>
	<h5>接口开发者:许恒</h5>
	<h5>date: 20150313</h5>
	<h5>
		<a href="interface/goods/getShopResourceGroup?shopId=1" target="_blank">
		URL:interface/goods/getShopResourceGroup?shopId=1
		</a>
	</h5>
	<h5>
		tips:
	</h5>
	<hr>
	<h4>8,获取商铺中的服务时段</h4>
	<h5>接口开发者:许恒</h5>
	<h5>date: 20150313</h5>
	<h5>
		<a href="interface/goods/getShopTimeInteval?shopId=1" target="_blank">
		URL:interface/goods/getShopTimeInteval?shopId=1
		</a>
	</h5>
	<h5>
		tips:
	</h5>
	<hr>
	<h4>9,意见反馈</h4>
	<h5>接口开发者:许恒</h5>
	<h5>date: 20150313</h5>
	<h5>
		<a href="interface/commonconf/giveFeedback?userId=1&feedback=hello&createTime=2015-01-01%2012:00" target="_blank">
		URL:interface/commonconf/giveFeedback?userId=1&feedback=hello&createTime=2015-01-01%2012:00
		</a>
	</h5>
	<h5>
		tips:
	</h5>
	<hr>
	<h4>10,实名认证</h4>
	<h5>接口开发者:陈永鑫</h5>
	<h5>date: 20150313</h5>
	<h5>
	<a href="interface/user/authRealName?userId=1179341045&trueName=孙悟空&identityCardNo=88899919000101888" target="_blank">
	 	URL:interface/user/authRealName?userId=1179341045&trueName=孙悟空&identityCardNo=88899919000101888
	 </a>
		
	</h5>
	<h5>
		tips:<br>
	</h5>
	<hr>	
	<h4>11,获取用户银行卡列表</h4>
	<h5>接口开发者:卢建平</h5>
	<h5>date: 20150316</h5>
	<h5>
		<a href="interface/user/getUserBankCards?userId=1179341045" target="_blank"> 
		URL:interface/user/getUserBankCards?userId=1179341045
		</a>
	</h5>
	<h5>
		tips:<br>
	</h5>
	<hr>
	<h4>13,用户使用传奇宝支付接口</h4>
	<h5>接口开发者:朱海全</h5>
	<h5>date: 20150316</h5>
	<h5>
		<a href="interface/pay/payByLegender?userId=1179341045" target="_blank"> 
		URL:interface/pay/payByLegender?userId=1179341045
		</a>
	</h5>
	<h5>
		tips:<br>
	</h5>
	<hr>
	<h4>14,查询用户账单接口</h4>
	<h5>接口开发者:朱海全</h5>
	<h5>date: 20150316</h5>
	<h5>
		<a href="interface/user/getUserBill?userId=1179341045" target="_blank"> 
		URL:interface/user/getUserBill?userId=1179341045
		</a>
	</h5>
	<h5>
		tips:<br>
	</h5>
	<hr>
	<h4>15,查询传奇宝账户余额</h4>
	<h5>接口开发者:朱海全</h5>
	<h5>date: 20150316</h5>
	<h5>
		<a href="interface/user/getAccountMoney?userId=1179341045" target="_blank"> 
		URL:interface/user/getAccountMoney?userId=1179341045
		</a>
	</h5>
	<h5>
		tips:<br>
	</h5>
	<hr>	
	<h4>P5,使用红包支付接口</h4>
	<h5>接口开发者:聂久乾</h5>
	<h5>date: 20150316</h5>
	<h5>
		<a href="interface/pay/payByRedPacket?userId=1044775818&orderId=20060729915041709450973073683&redPacketId=1&orderPayType=0" target="_blank"> 
		URL:<%=basePath %>interface/pay/payByRedPacket?userId=1044775818&orderId=20060729915041709450973073683&redPacketId=1&orderPayType=0
		</a>
	</h5>
	<h5>
		tips:<br>
	</h5>
	<hr>	
	<h4>P6,用户获取红包接口</h4>
	<h5>接口开发者:聂久乾</h5>
	<h5>date: 20150316</h5>
	<h5>
		<a href="interface/pay/obtainRedPacket?userId=1&redPacketBatchNo=12345678901233" target="_blank"> 
		URL:<%=basePath %>interface/pay/obtainRedPacket?userId=1&redPacketBatchNo=12345678901233
		</a>
	</h5>
	<h5>
		tips:<br>
	</h5>
	<hr>	
	<h4>P7,查询用户红包接口</h4>
	<h5>接口开发者:聂久乾</h5>
	<h5>date: 20150316</h5>
	<h5>
		<a href="interface/pay/queryUserRedPacket?userId=1163084422&pNo=1&pSize=10" target="_blank"> 
		URL:<%=basePath %>interface/pay/queryUserRedPacket?userId=1163084422&pNo=1&pSize=10
		</a>
	</h5>
	<h5>
		tips:<br>
	</h5>
	<hr>
	<hr>
	<h4>19,查询用户会员卡列表</h4>
	<h5>接口开发者:陈永鑫</h5>
	<h5>date: 20150316</h5>
	<h5>
	<a href="interface/user/getUserMembershipCardList?userId=1179341044&pNo=1&pSize=10" target="_blank">
	 	URL:interface/user/getUserMembershipCardList?userId=1179341044&pNo=1&pSize=10
	 </a>
		
	</h5>
	<h5>
		tips:<br>
	</h5>	
	<hr>
	<h4>19,查询会员卡详细信息</h4>
	<h5>接口开发者:陈永鑫</h5>
	<h5>date: 20150316</h5>
	<h5>
	<a href="interface/user/getUserMembershipCardInfo?accountId=2" target="_blank">
	 	URL:interface/user/getUserMembershipCardInfo?accountId=2
	 </a>
		
	</h5>
	<h5>
		tips:<br>
	</h5>
	<hr>	
	<h4>S3,获取指定商铺经纬度信息</h4>
	<h5>接口开发者:聂久乾</h5>
	<h5>date: 20150312</h5>
	<h5>
		<a href="interface/shop/getShopXy?shopId=2147483647" target="_blank"> 
		URL:<%=basePath %>interface/shop/getShopXy?shopId=2147483647
		</a>
	</h5>
	<h5>
		tips:<br>
	</h5>
	<hr>	
	<h4>21,获取版本及配置服务器（B服务器）的地址列表(R1)</h4>
	<h5>接口开发者:聂久乾</h5>
	<h5>date: 20150313</h5>
	<h5>
		<a href="getaddress?softwareName=routerAPP1&currentVersion=1.1.3.2&SNID=00:40:05:5F:44:43" target="_blank"> 
		URL:<%=basePath %>interface/getaddress?softwareName=routerAPP1&currentVersion=1.1.3.2&SNID=00:40:05:5F:44:43
		</a>
	</h5>
	<h5>
		tips:<br>
	</h5>
	<hr>	
	<h4>22,查询会员卡余额</h4>
	<h5>接口开发者:许恒</h5>
	<h5>date: 20150316</h5>
	<h5>
	<a href="interface/user/getUserMembershipCardMoney?queryType=2&accountId=1" target="_blank">
	 	URL:interface/user/getUserMembershipCardMoney?queryType=2&accountId=1
	 </a>
	</h5>
	<h5>
		tips:<br>
	</h5>	
	<hr>
	<h4>23,查询用户会员卡账单</h4>
	<h5>接口开发者:许恒</h5>
	<h5>date: 20150316</h5>
	<h5>
	<a href="interface/user/getUserMembershipCardBill?shopId=1&userId=1179341045" target="_blank">
	 	URL:interface/user/getUserMembershipCardBill?shopId=1&userId=1179341045
	 </a>
	</h5>
	<h5>
		tips:<br>
	</h5>	
	<hr>
	<h4>U26,获取推荐码接口</h4>
	<h5>接口开发者:聂久乾</h5>
	<h5>date: 20150320</h5>
	<h5>
	<a href="interface/user/getRefeCode?userId=1179341046&referMobile=15899773751" target="_blank">
	 	URL:<%=basePath %>interface/user/getRefeCode?userId=1179341046&referMobile=15899773751
	 </a>
	</h5>
	<h5>
		tips:<br>
	</h5>		
	<h5>
		tips:<br>
	</h5>		
	<hr>
	<h4>U17,最新版本检测接口</h4>
	<h5>接口开发者:聂久乾</h5>
	<h5>date: 20150320</h5>
	<h5>
	<a href="interface/commonconf/checkVersion?userId=1179341046&appId=1&curVersion=1.0.1" target="_blank">
	 	URL:<%=basePath %>interface/commonconf/checkVersion?userId=1179341046&appId=1&curVersion=1.0.1
	 </a>
	</h5>
	<hr>
	<h4>U27,推荐码验证接口</h4>
	<h5>接口开发者:聂久乾</h5>
	<h5>date: 20150320</h5>
	<h5>
	<a href="interface/user/verifyRefeCode?refecode=123456&mobile=15899773751" target="_blank">
	 	URL:<%=basePath %>interface/user/verifyRefeCode?refecode=123456&mobile=15899773751
	 </a>
	</h5>	
	<h5>
		tips:<br>
	</h5>
	<hr>
	<h4>27,查询用户搜索历史记录接口</h4>
	<h5>接口开发者:陈永鑫</h5>
	<h5>date: 20150320</h5>
	<h5>
	<a href="interface/user/getSearchHistory?userId=1179341046&pNo=1&pSize=3" target="_blank">
	 	URL:<%=basePath %>interface/user/getSearchHistory?userId=1179341046&pNo=1&pSize=3
	 </a>
	</h5>
	<h5>
		tips:<br>
	</h5>
	<hr>
	<h4>28,清空用户搜索历史记录接口</h4>
	<h5>接口开发者:陈永鑫</h5>
	<h5>date: 20150320</h5>
	<h5>
	<a href="interface/user/clearSearchHistory?userId=1179341046" target="_blank">
	 	URL:<%=basePath %>interface/user/clearSearchHistory?userId=1179341046
	 </a>
	</h5>
	<h5>
		tips:<br>
	</h5>			
	<hr>
	<h4>29,更新订单接口</h4>
	<h5>接口开发者:卢建平</h5>
	<h5>date: 20150320</h5>
	<h5>
	<a href="interface/order/updateOrder" target="_blank">
	 	URL:<%=basePath %>interface/order/updateOrder
	 </a>
	</h5>
	<h5>
		tips:<br>
		1，使用post请求方法
		2，contentType: "application/json"
        3，data: "{\"orderId\":\"1426841229190\",\"userId\":1179341046,\"distributionType\":1,\"distributionTime\":\"2015-03-20\",\"orderServiceType\":1,\"serviceTimeFrom\":\"2015-03-20 14:22:22\",\"serviceTimeTo\":\"2015-04-20\",\"orderType\":1,\"prepay_money\":33.33,\"payTimeType\":0,\"addressId\":1,\"orderTotalPrice\":9.99,\"oGoodsList\":[{\"shopId\":123456789,\"goodsId\":2},{\"shopId\":123456789,\"goodsId\":2}]}" 
	</h5>			
	<hr>
	<h4>30,用户下订单接口</h4>
	<h5>接口开发者:卢建平</h5>
	<h5>date: 20150320</h5>
	<h5>
	<a href="interface/order/placeOrder" target="_blank">
	 	URL:<%=basePath %>interface/order/placeOrder
	 </a>
	</h5>
	<h5>
		tips:<br>
		1，使用post请求方法
		2，contentType: "application/json"
        3，data: "{\"userId\":1179341046,\"distributionType\":1,\"distributionTime\":\"2015-03-20\",\"orderServiceType\":1,\"serviceTimeFrom\":\"2015-03-20 12:22:22\",\"serviceTimeTo\":\"2015-03-20\",\"orderType\":1,\"prepay_money\":33.33,\"payTimeType\":0,\"addressId\":1,\"orderTotalPrice\":9.99,\"oGoodsList\":[{\"shopId\":2147483647,\"goodsId\":1},{\"shopId\":2147483647,\"goodsId\":1}]}" 
	</h5>			
	<hr>
	<h4>A1,获取广告信息列表接口</h4>
	<h5>接口开发者:卢建平</h5>
	<h5>date: 20150415</h5>
	<h5>
	<a href="interface/ad/getAd?cityId=3&adSpaceId=4&pNo=1&pSize=10" target="_blank">
	 	URL:<%=basePath %>interface/ad/getAd?cityId=3&adSpaceId=4&pNo=1&pSize=10
	 </a>
	</h5>
	<h5>
		tips:<br>
	</h5>	
	<hr>
	<h4>U21：我的订单列表接口</h4>
	<h5>接口开发者:盛志鹏</h5>
	<h5>date: 20150320</h5>
	<h5>
	<a href="interface/myorder/getMyOrders?pNo=1&pSize=10&userId=1179341046&status=0" target="_blank">
	 	URL:<%=basePath %>interface/myorder/getMyOrders?pNo=1&pSize=10&userId=1179341046&status=0
	 </a>
	</h5>
	<h5>
		tips:确保用户uerId存在<br>
	</h5>		
	<hr>
	<h4>U15：获取用户基本信息接口</h4>
	<h5>接口开发者:盛志鹏</h5>
	<h5>date: 20150321</h5>
	<h5>
	<a href="interface/user/getBaseInfo?userId=1179341046" target="_blank">
	 	URL:<%=basePath %>interface/user/getBaseInfo?userId=1179341046
	 </a>
	</h5>
	<h5>
		tips:确保用户uerId存在<br>
	</h5>			
	<hr>
	<h4>A2,获取首页栏目接口</h4>
	<h5>接口开发者:卢建平</h5>
	<h5>date: 20150415</h5>
	<h5>
	<a href="interface/column/getColumnInfo?pNo=1&pSize=10&cityId=8&parentColumnId=0" target="_blank">
	 	URL:<%=basePath %>interface/column/getColumnInfo?pNo=1&pSize=10&cityId=8&parentColumnId=0
	 </a>
	</h5>
	<h5>
		tips:<br>
	</h5>			
	<hr>
	<h4>A3,获取TOP商品接口</h4>
	<h5>接口开发者:卢建平</h5>
	<h5>date: 20150415</h5>
	<h5>
	<a href="interface/goods/getHotGoods?pNo=1&pSize=10&cityId=1" target="_blank">
	 	URL:<%=basePath %>interface/goods/getHotGoods?pNo=1&pSize=10&cityId=1
	 </a>
	</h5>
	<h5>
		tips:<br>
	</h5>			
	<hr>
	<h4>A4,获取TOP代金券</h4>
	<h5>接口开发者:卢建平</h5>
	<h5>date: 20150415</h5>
	<h5>
	<a href="interface/coupon/getTopCashCoupon?cityId=199" target="_blank">
	 	URL:<%=basePath %>interface/coupon/getTopCashCoupon?cityId=199
	 </a>
	</h5>
	<h5>
		tips:<br>
	</h5>			
	<hr>
	<h4>P8,获取用户的代金券接口</h4>
	<h5>接口开发者:卢建平</h5>
	<h5>date: 20150415</h5>
	<h5>
	<a href="interface/coupon/getUserCashCoupon?userId=1179341046" target="_blank">
	 	URL:<%=basePath %>interface/coupon/getUserCashCoupon?userId=1179341046
	 </a>
	</h5>
	<h5>
		tips:<br>
	</h5>			
	<hr>
	<h4>P9,用户领取代金券接口</h4>
	<h5>接口开发者:卢建平</h5>
	<h5>date: 20150415</h5>
	<h5>
	<a href="interface/coupon/grabCashCoupon?userId=1&cashCouponId=1" target="_blank">
	 	URL:<%=basePath %>interface/coupon/grabCashCoupon?userId=1&cashCouponId=1
	 </a>
	</h5>
	<h5>
		tips:<br>
	</h5>			
	<hr>
	<h4>P10,用户消费代金券接口</h4>
	<h5>接口开发者:卢建平</h5>
	<h5>date: 20150415</h5>
	<h5>
	<a href="interface/coupon/consumeCashCoupon?userId=1&orderId=1426839929962&uccId=40" target="_blank">
	 	URL:<%=basePath %>interface/coupon/consumeCashCoupon?userId=1&orderId=1426839929962&uccId=40
	 </a>
	</h5>
	<h5>
		tips:<br>
	</h5>			
	<hr>
	<h4>P23,查询用户抵用券汇总信息接口</h4>
	<h5>接口开发者:卢建平</h5>
	<h5>date: 20150415</h5>
	<h5>
	<a href="interface/coupon/getUserCoupon?userId=1179341046" target="_blank">
	 	URL:<%=basePath %>interface/coupon/getUserCoupon?userId=1179341046
	 </a>
	</h5>
	<h5>
		tips:<br>
	</h5>			
	<hr>
	<h4>S3,获取店铺详情接口</h4>
	<h5>接口开发者:卢建平</h5>
	<h5>date: 20150415</h5>
	<h5>
	<a href="interface/shop/getShopDetails?shopId=123456789" target="_blank">
	 	URL:<%=basePath %>interface/shop/getShopDetails?shopId=123456789
	 </a>
	</h5>
	<h5>
		tips:<br>
	</h5>			
	<hr>
	<h4>S14,获取商铺的优惠券接口</h4>
	<h5>接口开发者:卢建平</h5>
	<h5>date: 20150415</h5>
	<h5>
	<a href="interface/coupon/getShopCoupon?shopId=200607299" target="_blank">
	 	URL:<%=basePath %>interface/coupon/getShopCoupon?shopId=200607299
	 </a>
	</h5>
	<h5>
		tips:<br>
	</h5>			
	<hr>
	<h4>P24,用户领取优惠券接口</h4>
	<h5>接口开发者:卢建平</h5>
	<h5>date: 20150415</h5>
	<h5>
	<a href="interface/coupon/grabCoupon?userId=1179341046&couponId=2" target="_blank">
	 	URL:<%=basePath %>interface/coupon/grabCoupon?userId=1179341046&couponId=2
	 </a>
	</h5>
	<h5>
		tips:<br>
	</h5>			
	<hr>
	<h4>S4,获取商铺详情接口</h4>
	<h5>接口开发者:卢建平</h5>
	<h5>date: 20150415</h5>
	<h5>
	<a href="interface/shop/getShopDetails?shopId=123456789" target="_blank">
	 	URL:<%=basePath %>interface/shop/getShopDetails?shopId=123456789
	 </a>
	</h5>
	<h5>
		tips:<br>
	</h5>			
	<hr>
	<h4>35,获取HOT商品列表接口</h4>
	<h5>接口开发者:卢建平</h5>
	<h5>date: 20150415</h5>
	<h5>
	<a href="interface/goods/getHotGoods?pNo=1&pSize=10&cityId=1" target="_blank">
	 	URL:<%=basePath %>interface/goods/getHotGoods?pNo=1&pSize=10&cityId=1
	 </a>
	</h5>
	<h5>
		tips:<br>
	</h5>	
	<hr>
	<h4>U7：用户登录接口</h4>
	<h5>接口开发者:盛志鹏</h5>
	<h5>date: 20150324</h5>
	<h5>
	<a href="interface/user/login?mobile=18657981038&password=e10adc3949ba59abbe56e057f20f883e&regId=asdaa&osInfo=iOS_6.1.4" target="_blank">
	 	URL:<%=basePath %>interface/user/login?mobile=18657981038&password=e10adc3949ba59abbe56e057f20f883e&regId=asdaa&osInfo=iOS_6.1.4
	 </a>
	</h5>
	<h5>
		tips:请确保手机号和密码正确<br>
	</h5>	
	<hr>
	<h4>U8：用户注册接口</h4>
	<h5>接口开发者:盛志鹏</h5>
	<h5>date: 20150324</h5>
	<h5>
	<a href="interface/user/register?mobile=18657981038&password=e10adc3949ba59abbe56e057f20f883e&regId=asdaa" target="_blank">
	 	URL:<%=basePath %>interface/user/register?mobile=18657981038&password=e10adc3949ba59abbe56e057f20f883e&regId=asdaa
	 </a>
	</h5>
	<h5>
		tips:先确保手机号码未被注册，同时能接收到验证码，如果传递了验证码就必须要正确，该密码明文为123456<br>
	</h5>
		<hr>
	<h4>U11：重置密码接口</h4>
	<h5>接口开发者:盛志鹏</h5>
	<h5>date: 20150324</h5>
	<h5>
	<a href="interface/user/modifyPwd?mobile=18657981038&oldPassword=e10adc3949ba59abbe56e057f20f883e&password=96e79218965eb72c92a549dd5a330112&confPassword=96e79218965eb72c92a549dd5a330112" target="_blank">
	 	URL:<%=basePath %>interface/user/modifyPwd?mobile=18657981038&oldPassword=e10adc3949ba59abbe56e057f20f883e&password=96e79218965eb72c92a549dd5a330112&confPassword=96e79218965eb72c92a549dd5a330112
	 </a>
	</h5>
	<h5>
		tips:请确保旧密码正确，新密码明文为111111<br>
	</h5>	
		<hr>
	<h4>U12：编辑基本资料接口</h4>
	<h5>接口开发者:盛志鹏</h5>
	<h5>date: 20150324</h5>
	<h5>
	<a href="interface/user/modifyBaseInfo" target="_blank">
	 	URL:<%=basePath %>interface/user/modifyBaseInfo
	 </a>
	</h5>
	<h5>
		tips:<br>
		1，使用post请求方法
		2，contentType: "application/json"
        3，data: "{\"userId\":1179341046,\"imgBig\":\"001.png\",\"imgSmall\":\"001_small.png\",\"nikeName\":\"贪吃羊\",\"sex\":0,\"provinceId\":1,\"cityId\":1}" 
	</h5>		
   <hr>
	<h4>U13：手机号码更改接口</h4>
	<h5>接口开发者:盛志鹏</h5>
	<h5>date: 20150324</h5>
	<h5>
	<a href="interface/user/modifyMobile" target="_blank">
	 	URL:<%=basePath %>interface/user/modifyMobile
	 </a>
	</h5>
	<h5>
		tips:<br>
		1，使用post请求方法
		2，contentType: "application/json"
        3，data: "{\"userId\":1179341046,\"mobile\":\"13637589781\",\"password\":\"40986546i45io6j45o6j3o45\",\"newMobile\":"\13037589792\"}" 
	</h5>	
	<hr>
	<h4>P18：设定支付密码接口</h4>
	<h5>接口开发者:盛志鹏</h5>
	<h5>date: 20150326</h5>
	<h5>
	<a href="interface/user/setPayPwd" target="_blank">
	 	URL:<%=basePath %>interface/user/setPayPwd
	 </a>
	</h5>
	<h5>
		tips:<br>
		1，使用post请求方法
		2，contentType: "application/json"
        3，data: "{\"userId\":1179341046,\"payPwd\":\"40986546i45io6j45o6j3o45\",\"confPayPwd\":\"40986546i45io6j45o6j3o45\"}" 
	</h5>	
	<hr>
	<h4>U9,获取手机验证码</h4>
	<h5>接口开发者:聂久乾</h5>
	<h5>date: </h5>
	<h5>
	<a href="interface/common/getVeriCode?mobile=15899773751&usage=用户注册" target="_blank">
	 	URL:<%=basePath %>interface/common/getVeriCode?mobile=15899773751&usage=用户注册
	 </a>
	</h5>
	<h5>
		tips:<br>
	</h5>	
	<hr>
	<h4>U10,手机验证码校验</h4>
	<h5>接口开发者:聂久乾</h5>
	<h5>date: </h5>
	<h5>
	<a href="interface/common/checkVeriCode?mobile=15899773751&veriCode=524875" target="_blank">
	 	URL:<%=basePath %>interface/common/checkVeriCode?mobile=15899773751&veriCode=524875
	 </a>
	</h5>
	<h5>
		tips:<br>
	</h5>	
	<hr>
	<h4>S15,获取商铺限时折扣</h4>
	<h5>接口开发者:聂久乾</h5>
	<h5>date: </h5>
	<h5>
	<a href="interface/coupon/getShopTimedDiscount?shopId=123456789" target="_blank">
	 	URL:<%=basePath %>interface/coupon/getShopTimedDiscount?shopId=123456789
	 </a>
	</h5>
	<h5>
		tips:<br>
	</h5>	
	<hr>
	<h4>S16,获取限时折扣对应的商品列表</h4>
	<h5>接口开发者:聂久乾</h5>
	<h5>date: </h5>
	<h5>
	<a href="interface/coupon/getShopTimedDiscountGoods?discountId=1" target="_blank">
	 	URL:<%=basePath %>interface/coupon/getShopTimedDiscountGoods?discountId=1
	 </a>
	</h5>
	<h5>
		tips:<br>
	</h5>	
	<hr>
	<h4>47,收银机接口：初始化数据</h4>
	<h5>接口开发者:聂久乾</h5>
	<h5>date: </h5>
	<h5>
	<a href="interface/getShopData?shopId=660165405&token=4889599520150413201636" target="_blank">
	 	URL:<%=basePath %>interface/getShopData?shopId=660165405&token=4889599520150413201636
	 </a>
	</h5>
	<h5>
		tips:<br>
	</h5>	
	<hr>
	<h4>46,收银机接口：下发短信验证码</h4>
	<h5>接口开发者:聂久乾</h5>
	<h5>date: </h5>
	<h5>
	<a href="interface/captchaMember?shopId=660165405&token=4889599520150413201636&mobile=15899773751" target="_blank">
	 	URL:<%=basePath %>interface/captchaMember?shopId=660165405&token=4889599520150413201636&mobile=15899773751
	 </a>
	</h5>
	<h5>
		tips:<br>
	</h5>	
	<hr>
	<h4>G1：获取商品详情</h4>
	<h5>接口责任人:张鹏程</h5>
	<h5>date: </h5>
	<h5>
	<a href="interface/goods/getGoodsDetail?goodsId=30" target="_blank">
	 	URL:<%=basePath %>interface/goods/getGoodsDetail?goodsId=30
	 </a>
	</h5>
	<h5>
		tips:<br>
	</h5>	
	<hr>
	<h4>P33：查询银行接口列表</h4>
	<h5>接口责任人:张鹏程</h5>
	<h5>date: </h5>
	<h5>
	<a href="interface/pay/getBanks" target="_blank">
	 	URL:<%=basePath %>interface/pay/getBanks
	 </a>
	</h5>
	<h5>
		tips:<br>
	</h5>	
	</h5>
	
	<hr>
	<h4>U16：意见反馈接口</h4>
	<h5>接口开发者:盛志鹏</h5>
	<h5>date: </h5>
	<h5>
		<a href="interface/commonconf/giveFeedback?userId=1179341045&feedback=hello&createTime=2015-03-01 12:30:00" target="_blank">
	 	URL:<%=basePath %>interface/commonconf/giveFeedback?userId=1179341045&feedback=hello&createTime=2015-03-01 12:30:00
	 </a>
	</h5>
	<h5>
		tips:<br>
	</h5>
	
  	<hr>
	<h4>U31：消息系统RegID注册接口</h4>
	<h5>接口开发者:盛志鹏</h5>
	<h5>date: </h5>
	<h5>
	 <a href="interface/user/addRegId?regId=234234234343&osInfo=iOS_6.1.4" target="_blank">
	 	URL:<%=basePath %>interface/user/addRegId?regId=234234234343&osInfo=iOS_6.1.4
	 </a>
	</h5>
	<h5>
		tips:<br>
	</h5>
	
	<hr>
	<h4>U32：我的推荐用户列表接口</h4>
	<h5>接口开发者:盛志鹏</h5>
	<h5>date: </h5>
	<h5>
	<a href="interface/user/getMyRefUsers?pNo=1&pSize=10&userId=1179341046" target="_blank">
	 	URL:<%=basePath %>interface/user/getMyRefUsers?pNo=1&pSize=10&userId=1179341046
	 </a>
	</h5>
	<h5>
		tips:<br>
	</h5>
	<hr>
	<h4>P13：验证支付密码接口</h4>
	<h5>接口开发者:盛志鹏</h5>
	<h5>date: </h5>
	<h5>
	<a href="interface/pay/authPayPassword?mobile=18657981038&payPassword=40986546i45io6j45o6j3o45" target="_blank">
	 	URL:<%=basePath %>interface/pay/authPayPassword?mobile=18657981038&payPassword=e10adc3949ba59abbe56e057f20f883e
	 </a>
	</h5>
	<h5>
		tips:密码为123456，必须确保手机号码已存在<br>
	</h5>
	<hr>
	<h4>P14：支付密码更改接口</h4>
	<h5>接口开发者:盛志鹏</h5>
	<h5>date: </h5>
	<h5>
	<a href="interface/user/modifyPayPwd" target="_blank">
	 	URL:<%=basePath %>interface/user/modifyPayPwd
	 </a>
	</h5>
	<h5>
		tips:密码为123456，必须确保手机号码已存在<br>
		1，使用post请求方法
		2，contentType: "application/json"
        3，data: "{\"userId\":1179341046,\"payPassword\":\"3iu4oi5u3o5o3iu5o3453o3o3\",\"newPayPwd\":\"40986546i45io6j45o6j3o45\",\"confNewPayPwd\":\"40986546i45io6j45o6j3o45\"}" 
	</h5>
		<hr>
	<h4>P22：查询用户抵用券汇总信息接口</h4>
	<h5>接口开发者:盛志鹏</h5>
	<h5>date: </h5>
	<h5>
	<a href="interface/user/getUserVoucherInfo?userId=1485448" target="_blank">
	 	URL:<%=basePath %>interface/user/getUserVoucherInfo?userId=1485448
	 </a>
	</h5>
	<h5>
		tips:确保用户存在<br>
	</h5>
	<hr>
	<h4>P26：用户充值接口</h4>
	<h5>接口开发者:盛志鹏</h5>
	<h5>date: </h5>
	<h5>
	<a href="interface/pay/charge?userId=1485448&amount=11.11&terminalType=android客户端&3rdOrgName=支付宝&chargeType=1" target="_blank">
	 	URL:<%=basePath %>interface/pay/charge?userId=1485448&amount=11.11&terminalType=android客户端&3rdOrgName=支付宝&chargeType=1
	 </a>
	</h5>
	<h5>
		tips:确保用户存在<br>
	</h5>
	<hr>
	<h4>S17：点赞接口</h4>
	<h5>接口开发者:盛志鹏</h5>
	<h5>date: </h5>
	<a href="interface/shop/zan?zanType=1&bizId=30&userId=1179341046" target="_blank">
	 	URL:<%=basePath %>interface/shop/zan?zanType=1&bizId=30&userId=1179341046
	 </a>
	<h5>
		tips:用户必须存在<br>
	</h5>
	<hr>
	<h4>U4：获取用户地址接口</h4>
	<h5>接口开发者:盛志鹏</h5>
	<h5>date: </h5>
	<a href="interface/user/getUserAddresses?pNo=1&pSize=10&userId=1179341046" target="_blank">
	 	URL:<%=basePath %>interface/user/getUserAddresses?pNo=1&pSize=10&userId=1179341046
	 </a>
	<h5>
		tips:用户必须存在<br>
	</h5>
	
	<hr>
	<h4>U5：新增用户地址接口</h4>
	<h5>接口开发者:盛志鹏</h5>
	<h5>date: </h5>
	<a href="interface/user/addUserAddress?userId=1179341046&defaultFlag=1&userName=szp&mobile=18657981038&provinceId=19&cityId=123&districtId=3343&address=szdx" target="_blank">
	 	URL:<%=basePath %>interface/user/addUserAddress?userId=1179341046&defaultFlag=1&userName=szp&mobile=18657981038&provinceId=19&cityId=123&districtId=3343&address=szdx
	 </a>
	<h5>
		tips:用户必须存在<br>
	</h5>
		<hr>
	<h4>U6：删除用户地址接口</h4>
	<h5>接口开发者:盛志鹏</h5>
	<h5>date: </h5>
	<a href="interface/user/deleteUserAddress?userId=1179341046&addressId=3" target="_blank">
	 	URL:<%=basePath %>interface/user/deleteUserAddress?userId=1179341046&addressId=3
	 </a>
	<h5>
		tips:用户必须存在<br>
	</h5>
		<hr>
		<hr>
	<h4>P34：查询用户可用红包总额接口 </h4>
	<h5>接口开发者:盛志鹏</h5>
	<h5>date: </h5>
	<a href="interface/user/getRedPacketTotalMoney?userId=1179341046" target="_blank">
	 	URL:<%=basePath %>interface/user/getRedPacketTotalMoney?userId=1179341046
	 </a>
	<h5>
		tips:用户必须存在<br>
	</h5>
		<hr>
	<h4>P29：文件上传接口</h4>
	<h5>接口开发者:盛志鹏</h5>
	<h5>date: </h5>
	<form action="<%=basePath%>interface/user/uploadFile" enctype="multipart/form-data" method="post">
        userId：<input name="userId" value="1485448" >
        usageType：<input name="usageType" value="11" >
        bizId：<input name="bizId"  >
        mimeType：<input name="mimeType" value="jpg" >
         file：<input name="file" type="file" >
		<input id="sub2" type="submit" value="上传文件" />
	</form>
	<h5>
		tips:用户必须存在，usageType必须按规范填写<br>
	</h5>
	<hr>
	<h4>S19：获取商铺红包接口</h4>
	<h5>接口开发者:聂久乾</h5>
	<h5>date: </h5>
	<a href="interface/coupon/getShopRedPacket?shopId=741852963&pNo=1&pSize=10" target="_blank">
	 	URL:<%=basePath %>interface/coupon/getShopRedPacket?shopId=741852963&pNo=1&pSize=10
	 </a>
	<h5>
		tips:<br>
	</h5>
	<hr>
	<h4>S20：搜索商铺限时折扣</h4>
	<h5>接口开发者:聂久乾</h5>
	<h5>date: </h5>
	<a href="interface/shop/searchShopTimeDiscount?cityId=178&pNo=1&pSize=10" target="_blank">
	 	URL:<%=basePath %>interface/shop/searchShopTimeDiscount?cityId=178&pNo=1&pSize=10
	 </a>
	<h5>
		tips:<br>
	</h5>
	<hr>
	<h4>M3：获取帮助信息接口</h4>
	<h5>接口开发者:黄睿</h5>
	<h5>date: </h5>
	<a href="interface/help/getHelpInfo?categoryId=1&pNo=1&pSize=10" target="_blank">
	 	URL:<%=basePath %>interface/help/getHelpInfo?categoryId=1&pNo=1&pSize=10
	 </a>
	<h5>
		tips:<br>
	</h5>
	<hr>
	<h4>M4：获取帮助信息分类接口</h4>
	<h5>接口开发者:黄睿</h5>
	<h5>date: </h5>
	<a href="interface/help/getHelpInfoCategory?pNo=1&pSize=10" target="_blank">
	 	URL:<%=basePath %>interface/help/getHelpInfoCategory?pNo=1&pSize=10
	 </a>
	<h5>
		tips:<br>
	</h5>
</body>

<script type="text/javascript">
	//下单
	$("#sub1111").on("click",function(){
		console.log(333);
		var i = "{\"userId\":1179341046,\"distributionType\":1,\"payTimeType\":0,\"orderTotalPrice\":9.99,\"oGoodsList\":[{\"shopId\":2147483647,\"goodsId\":1},{\"shopId\":2147483647,\"goodsId\":1}]}";
		i = "{\"userId\":1179341046,\"distributionType\":1,\"distributionTime\":\"2015-03-20 12:22:22\",\"orderServiceType\":1,\"serviceTimeFrom\":\"2015-03-20 12:22:22\",\"serviceTimeTo\":\"2015-03-20 12:22:22\",\"orderType\":1,\"prepay_money\":33.33,\"payTimeType\":0,\"addressId\":1,\"orderTotalPrice\":9.99,\"goods\":[{\"shopId\":123456789,\"goodsId\":30},{\"shopId\":123456789,\"goodsId\":30}]}";
		var s = eval("("+i+")");
		$.ajax({
            type: "POST",
            contentType: "application/json",
            url: "interface/order/placeOrder",
            data: i,
            dataType: "json",
            success: function(data){
            	console.log(data);
                     }
        	});
	});
	//下单
	$("#sub").on("click",function(){
		console.log(333);
		var i = "{\"userId\":1,\"distributionType\":1,\"distributionTime\":\"2015-03-20 12:22:22\",\"orderServiceType\":1,\"serviceTimeFrom\":\"2015-03-20 12:22:22\",\"serviceTimeTo\":\"2015-03-20 12:22:22\",\"orderType\":1,\"prepay_money\":33.33,\"payTimeType\":0,\"addressId\":1,\"orderTotalPrice\":119.99,\"goods\":[{\"shopId\":987654323,\"goodsId\":36,\"goodsNumber\":1}]}";
		var s = eval("("+i+")");
		$.ajax({
            type: "POST",
            contentType: "application/json",
            url: "interface/order/placeOrder",
            data: i,
            dataType: "json",
            success: function(data){
            	console.log(data);
                     }
        	});
	});
	//更新订单
	$("#sub31112").on("click",function(){
		console.log(333);
		var i = "{\"orderId\":\"1426841229190\",\"userId\":1179341046,\"distributionType\":1,\"distributionTime\":\"2015-03-20 12:10:10\",\"orderServiceType\":1,\"serviceTimeFrom\":\"2015-03-20 14:22:22\",\"serviceTimeTo\":\"2015-04-20 12:20:20\",\"orderType\":1,\"prepay_money\":33.33,\"payTimeType\":0,\"addressId\":1,\"orderTotalPrice\":9.99,\"goods\":[{\"shopId\":123456789,\"goodsId\":30},{\"shopId\":123456789,\"goodsId\":30}]}";
		var s = eval("("+i+")");
		$.ajax({
            type: "POST",
            contentType: "application/json",
            url: "interface/order/updateOrder",
            data: i,
            dataType: "json",
            success: function(data){
            	console.log(data);
                     }
        	});
	});
	//更新订单
	$("#sub3").on("click",function(){
		console.log(333);
		var i = "{\"orderId\":\"1426841229190\",\"userId\":1,\"distributionType\":1,\"distributionTime\":\"2015-03-20 12:10:10\",\"orderServiceType\":1,\"serviceTimeFrom\":\"2015-03-20 14:22:22\",\"serviceTimeTo\":\"2015-04-20 12:20:20\",\"orderType\":1,\"prepay_money\":33.33,\"payTimeType\":0,\"addressId\":1,\"orderTotalPrice\":9.99,\"goods\":[{\"shopId\":987654323,\"goodsId\":36}]}";
		var s = eval("("+i+")");
		$.ajax({
            type: "POST",
            contentType: "application/json",
            url: "interface/order/updateOrder",
            data: i,
            dataType: "json",
            success: function(data){
            	console.log(data);
                     }
        	});
	});
	//更新购物车
	$("#sub2").on("click",function(){
		console.log(333);
		var i = "{\"userId\":1179341055,\"goodsList\":[{\"shopId\":123456789,\"goodsId\":31,\"goodsNumber\":2,\"goodsIndex\":1},{\"shopId\":123456789,\"goodsId\":30,\"goodsNumber\":2,\"goodsIndex\":1}]}";
		var s = eval("("+i+")");
		$.ajax({
            type: "POST",
            contentType: "application/json",
            url: "interface/cart/updateCart",
            data: i,
            dataType: "json",
            success: function(data){
            	console.log(data);
                     }
        	});
	});
	
	
	//编辑用户基本资料
	$("#sub4").on("click",function(){
		var i = "{\"userId\":11798989,\"imgBig\":\"/user/img/001.png\",\"imgSmall\":\"/user/img/001_small.png\",\"nikeName\":\"一点传奇\",\"sex\":0,\"provinceId\":0,\"cityId\":0}";
		var s = eval("("+i+")");
		$.ajax({
            type: "POST",
            contentType: "application/json",
            url: "interface/user/modifyBaseInfo",
            data: i,
            dataType: "json",
            success: function(data){
            	console.log(data);
                     }
        	});
	});
	
	
	//手机号码更改
	$("#sub5").on("click",function(){
		var i = "{\"userId\":11798989,\"mobile\":\"18657981038\",\"password\":\"21232f297a57a5a743894a0e4a801fc3\",\"newMobile\":\"18657981038\"}";
		var s = eval("("+i+")");
		$.ajax({
            type: "POST",
            contentType: "application/json",
            url: "interface/user/modifyMobile",
            data: i,
            dataType: "json",
            success: function(data){
            	console.log(data);
                     }
        	});
	});
	//支付密码更改
	$("#sub6").on("click",function(){
		var i = "{\"userId\":1179341046,\"payPassword\":\"111\",\"newPayPwd\":\"111\",\"confNewPayPwd\":\"\"}";
		var s = eval("("+i+")");
		$.ajax({
            type: "POST",
            contentType: "application/json",
            url: "interface/user/modifyPayPwd",
            data: i,
            dataType: "json",
            success: function(data){
            	console.log(data);
                     }
        	});
	});
	
	//用户预定商铺资源
	$("#sub17").on("click",function(){
		var i = "{\"orderId\":\"1426839929962\",\"shopId\":123456789,\"osrs\":[{\"groupId\":2,\"intevalId\":7,\"resourceNumber\":1,\"reserveResourceDate\":\"2015-08-08\"},{\"groupId\":3,\"intevalId\":2,\"resourceNumber\":1,\"reserveResourceDate\":\"2015-08-08\"}]}";
		var s = eval("("+i+")");
		$.ajax({
            type: "POST",
            contentType: "application/json",
            url: "interface/order/reserveShopResource",
            data: i,
            dataType: "json",
            success: function(data){
            	console.log(data);
                     }
        	});
	});
	//用户预定商铺资源
	$("#sub7").on("click",function(){
		var i = "{\"orderId\":\"1427529355460\",\"shopId\":123456789,\"osrs\":[{\"groupId\":2,\"intevalId\":1,\"resourceNumber\":2,\"reserveResourceDate\":\"2015-08-08\"}]}";
		var s = eval("("+i+")");
		$.ajax({
            type: "POST",
            contentType: "application/json",
            url: "interface/order/reserveShopResource",
            data: i,
            dataType: "json",
            success: function(data){
            	console.log(data);
                     }
        	});
	});
	//注册
	$("#sub8").on("click",function(){
		var i = {'mobile':18657981038,'password':'xxsdfjddafd','veriCode':457841};
// 		var s = eval("("+i+")");
		$.ajax({
            type: "POST",
            url: "interface/user/register",
            data: i,
            dataType: "json",
            success: function(data){
            	console.log(data);
                     }
        	});
	});
	
</script>
</html>
