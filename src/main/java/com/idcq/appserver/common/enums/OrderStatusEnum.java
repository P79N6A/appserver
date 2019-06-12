package com.idcq.appserver.common.enums;

import com.idcq.appserver.common.CodeConst;
import com.idcq.appserver.exception.ValidateException;

/**
 * 订单状态枚举
 * 
 * @author Administrator
 * 
 * @date 2015年5月27日
 * @time 下午4:00:13
 */
public enum OrderStatusEnum {
	
	BOOKED("已预定",0),
	OPEN("已开单",1),
	SEND("派送中",2),
	SETTLE("已完成",3),
	STOP("退单中",4),
	STOPED("已退单",5),
	SELFORDER("自助下单",8),
	TOBECONFIREED("待确认",9);
	
    private String name;
    private Integer value;
    
    private OrderStatusEnum(String name,Integer value){
        this.name = name;
        this.value = value;
    }
    
    public String getName(){
        return this.name;
    }
    
    public Integer getValue(){
    	return this.value;
    }
    
    public static void main(String[] args) throws Exception {
    	System.out.println(OrderStatusEnum.getStatusName(4));
	}
    
    public static String getStatusName(Integer i) throws Exception{
    	OrderStatusEnum ce = null;
    	switch(i){
    	case 0:
    		ce = BOOKED;
    		break;
    	case 1:
    		ce = OPEN;
    		break;
    	case 2:
    		ce = SEND;
    		break;
    	case 3:
    		ce = SETTLE;
    		break;
    	case 4:
    		ce = STOP;
    		break;
    	case 5:
    		ce = STOPED;
    		break;
    	case 8:
    		ce = SELFORDER;
    		break;
    	case 9:
    		ce = TOBECONFIREED;
    		break;
    	default:
    		throw new ValidateException(CodeConst.CODE_INVALID, CodeConst.MSG_INVALID_ODDER_STATUS);
    	}
    	return ce.getName();
    }

}
