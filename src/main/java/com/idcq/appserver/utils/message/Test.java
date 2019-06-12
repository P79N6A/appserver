package com.idcq.appserver.utils.message;

import com.idcq.appserver.dto.shop.ShopDto;
import com.idcq.appserver.dto.user.UserDto;


public class Test {
	public static void main(String[] args) {
		ShopDto shopDto = new ShopDto();
		int notifyType = 1;
		Long bizId = 1321132L;
		int bizType = 13;
		String title = "XXX店铺满送红包";
		String content = "满100送20红包满200送50红包";
		String messageImage = "http://www.baidu.com/sfsdfsd.jpg";
		
		MessageControl.execute(title, content,messageImage, notifyType,bizType,bizId,shopDto);
		System.out.println();
		UserDto userDto = new UserDto();
		notifyType = 2;
		MessageControl.execute(title, content,messageImage, notifyType,bizType,bizId,userDto);
	}
}
