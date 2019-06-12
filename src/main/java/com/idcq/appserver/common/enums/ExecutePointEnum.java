package com.idcq.appserver.common.enums;

import org.apache.commons.lang3.StringUtils;

import com.idcq.appserver.common.CodeConst;
import com.idcq.appserver.exception.ValidateException;

/**
 * 程序类型枚举类
 * 
 * @author Administrator
 * 
 * @date 2016年3月14日
 * @time 下午1:52:15
 */
public enum ExecutePointEnum {
	
	SINGLE_OPT_INTERFACE("单独操作接口","",1),
	SHOPMB_IMPORT_PROCESSOR("商铺内会员_批量导入的不同格式处理器","",200),
	NEW_MATCH_PROCESSOR("新旧匹配规则处理器","",201),
	MEMBER_CONVERT("会员转换接口","",202),
	GET_DYNAMIC_INTERFACE("获取动态配置接口","",300),
	BUSAREA_STATUS_CONVERT("活动状态切换","",301),
	BUSAREA_ACT_MSG_PUSH_USER("商圈活动消息推送（用户）","",302),
	REDPACKET_STATUS_CHANGE("红包状态变更","",303),
	NOTIFY_REDPACKET_EXPIRE("通知红包任务过期","",304),
	APPLY_BUSAREA("用户获取商圈活动资格","businessAreaApplyBusinessAreaPoint",307),
	SHARED_OPT_FB("商圈分享操作反馈","commonOperateFbPoint_13_3",308),
	RECEIVED_OPT_FB("消息收到操作反馈","commonOperateFbPointt_16_1",309),
	CLICKED_OPT_FB("消息点击操作反馈","commonOperateFbPoint_16_2",310),
	READ_OPT_FB("商圈阅读（点击）操作反馈","commonOperateFbPoint_13_2",311);
	
	
	private String name;
	private String code;
	private Integer value;
	
	private ExecutePointEnum(String name,String code,Integer value){
		this.name = name;
		this.code = code;
		this.value = value;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Integer getValue() {
		return value;
	}

	public void setValue(Integer value) {
		this.value = value;
	}
	
	public static String getNameByValue(Integer value){
		if(value == null){
			return null;
		}
		
		ExecutePointEnum en = null;
		switch(value){
		case 1:
			en = SINGLE_OPT_INTERFACE;
			break;
		case 200:
			en = SHOPMB_IMPORT_PROCESSOR;
			break;
		case 201:
			en = NEW_MATCH_PROCESSOR;
			break;
		case 202:
			en = MEMBER_CONVERT;
			break;
		case 301:
			en = BUSAREA_STATUS_CONVERT;
			break;
		case 302:
			en = BUSAREA_ACT_MSG_PUSH_USER;
			break;
		case 303:
			en = REDPACKET_STATUS_CHANGE;
			break;
		case 304:
			en = NOTIFY_REDPACKET_EXPIRE;
			break;
		default:
			throw new ValidateException(CodeConst.CODE_INVALID, CodeConst.MSG_INVALID_PROGRAM_TYPE);
			
		}
		return en.getName();
	}
	
	public static Integer getValueByName(String name){
		if(StringUtils.isBlank(name)){
			return null;
		}
		
		ExecutePointEnum en = null;
		switch(name){
		case "commonOperateFbPoint_13_3":
			en = SHARED_OPT_FB;
			break;
		case "commonOperateFbPoint_13_2":
			en = READ_OPT_FB;
			break;
		case "commonOperateFbPoint_16_1":
			en = RECEIVED_OPT_FB;
			break;
		case "commonOperateFbPoint_16_2":
			en = CLICKED_OPT_FB;
			break;
		default:
			throw new ValidateException(CodeConst.CODE_INVALID, CodeConst.MSG_INVALID_EXECUTE_POINT);
			
		}
		return en.getValue();
	}
	
	
	
}
