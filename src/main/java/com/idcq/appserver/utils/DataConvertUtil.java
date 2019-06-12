package com.idcq.appserver.utils;

import java.lang.reflect.Field;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.lang.math.NumberUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.format.annotation.DateTimeFormat;

import com.idcq.appserver.dto.goods.POSOrderDetailGoodsDto;
import com.idcq.appserver.dto.order.OrderGoodsDto;
import com.idcq.appserver.dto.pay.POSOrderDetailPayDto;
import com.idcq.appserver.dto.pay.PayDto;
/**
 * 数据转换工具
* @ClassName: DataConvertUtil 
* @Description: TODO(这里用一句话描述这个类的作用) 
* @author 张鹏程 
* @date 2015年3月28日 下午5:06:51 
*
 */
/**
 * 数据转换工具
* @ClassName: DataConvertUtil 
* @Description: TODO(这里用一句话描述这个类的作用) 
* @author 张鹏程 
* @date 2015年3月28日 下午5:06:51 
*
 */
public class DataConvertUtil {
	private final Log logger = LogFactory.getLog(getClass());
	/**
	 * 将 formObj类中的属性设置到toObj类中去
	 * @param fromObj
	 * @param toObj
	 * @param propsMap
	 */
	public static void propertyConvert(Object fromObj,Object toObj,Map<String,String>propsMap)
	{
		for(Entry<String,String>entry:propsMap.entrySet())
		{
			Object value=ReflectionUtils.getFieldValue(fromObj, entry.getKey());//获得fromObj中的属性值
			ReflectionUtils.setFieldValue(toObj, entry.getValue(), value);
		}
	}
	
	/**
	 * 将formObj类中的属性设置到toObj类中
	 * 除此之外,fromObj和toObj中的相同属性名会自动转换
	 * @param fromObj
	 * @param toObj
	 * @param propsMap
	 */
	public static void propertyConvertIncludeDefaultProp(Object fromObj,Object toObj,Map<String,String>propsMap)
	{
		for(Field field:fromObj.getClass().getDeclaredFields())
		{
			ReflectionUtils.makeAccessible(field);
			String fieldName=field.getName();
			if(!ReflectionUtils.checkFieldIsExist(toObj.getClass(), fieldName))//检测在toObj中属性是否存在
			{
				String mappingFieldName=propsMap.get(fieldName);
				if(mappingFieldName!=null)
				{
					Object value=ReflectionUtils.getFieldValue(fromObj, fieldName);
					if(value!=null)
					{	
						DateTimeFormat dateTimeFormat=field.getAnnotation(DateTimeFormat.class);
						if(dateTimeFormat!=null)
						{
							String dateTimePattern=dateTimeFormat.pattern();
							DateUtils.format((Date)value,dateTimePattern);
						}
						Field toField=ReflectionUtils.getDeclaredField(toObj, mappingFieldName);
						dateTimeFormat=toField.getAnnotation(DateTimeFormat.class);
						if(dateTimeFormat!=null)
						{
							String dateTimePattern=dateTimeFormat.pattern();
							value=DateUtils.parse(value+"", dateTimePattern);
						}
						ReflectionUtils.setFieldValue(toObj, mappingFieldName, ReflectionUtils.convertValue(value,toField.getType() ));
					}	
				}
			}
			else
			{
				Object value=ReflectionUtils.getFieldValue(fromObj, fieldName);
				if(value!=null)
				{	
					DateTimeFormat dateTimeFormat=field.getAnnotation(DateTimeFormat.class);
					if(dateTimeFormat!=null)
					{
						String dateTimePattern=dateTimeFormat.pattern();
						value=DateUtils.format((Date)value,dateTimePattern);
					}
					Field toField=ReflectionUtils.getDeclaredField(toObj, fieldName);
					dateTimeFormat=toField.getAnnotation(DateTimeFormat.class);
					if(dateTimeFormat!=null)
					{
						String dateTimePattern=dateTimeFormat.pattern();
						value=DateUtils.parse(value+"", dateTimePattern);
					}
					if("class java.lang.Double".equals(toField.getType()+""))
					{
						value=NumberUtil.formatDoubleForSolr(Double.parseDouble(value+""), 2);
						ReflectionUtils.setFieldValue(toObj, fieldName, ReflectionUtils.convertValue(value,toField.getType() ));
					}
					else if("class java.lang.Float".equals(toField.getType()+""))
					{
						value=NumberUtil.formatFloat(Float.parseFloat(value+""), 2);
						ReflectionUtils.setFieldValue(toObj, fieldName,ReflectionUtils.convertValue(value,toField.getType() ));
					}
					else
					{	
						if("class java.math.BigDecimal".equals(toField.getType()+""))
						{   
							value=NumberUtil.formatDoubleForSolr(Double.parseDouble(value+""), 2);
						}
						ReflectionUtils.setFieldValue(toObj, fieldName, ReflectionUtils.convertValue(value,toField.getType() ));
					}
				}
			}
		}
	}
	
	/**
	 * 将list转换为List<map>
	 * @param sourceObj
	 * @param fields
	 * @return
	 */
	public static List<Map<String,Object>>convertCollectionToListMap(List sourceObj,Collection<String>fields)
	{
		if(sourceObj==null)
		{
			throw new RuntimeException("被转换的List不能为空");
		}
		if(fields==null)
		{
			throw new RuntimeException("需要转换的fields不能为空");
		}
		List<Map<String, Object>>mapList=new ArrayList<Map<String,Object>>();
		for(int i=0;i<sourceObj.size();i++)
		{
			Object obj=sourceObj.get(i);
			Map<String,Object>record=new HashMap<String,Object>();
			for(String field:fields)
			{
				if(ReflectionUtils.checkFieldIsExist(obj.getClass(), field))
				{
					record.put(field, ReflectionUtils.getFieldValue(obj, field));
				}
			}
			mapList.add(record);
		}
		return mapList;
	}
	
	/**
	 * 将对象转换为map
	 * @param sourceObj
	 * @param fields
	 * @return
	 */
	public static Map<String,Object>convertObjToMap(Object sourceObj,Collection<String>fields)
	{
		Map<String,Object>record=new HashMap<String,Object>();
		for(String field:fields)
		{
			if(!ReflectionUtils.checkFieldIsExist(sourceObj.getClass(), field))
			{
				record.put(field, null);
			}
			else
			{	
				record.put(field, ReflectionUtils.getFieldValue(sourceObj, field));
			}
		}
		return record;
	}
	
	/**
	 * 将list转化为List<Map>
	* @Title: convertListObjToMap 
	* @Description: TODO(这里用一句话描述这个方法的作用) 
	* @param @param sourceObjList
	* @param @param fields
	* @param @return
	* @return Map<String,Object>    返回类型 
	* @throws
	 */
	public static List<Map<String,Object>>convertListObjToMap(List sourceObjList,Collection<String>fields)
	{
		if(sourceObjList!=null)
		{	
			List<Map<String,Object>>resultMapList=new ArrayList<Map<String,Object>>();
			for(Object obj:sourceObjList)
			{
				resultMapList.add(convertObjToMap(obj, fields));
			}
			return resultMapList;
		}
		return null;
	}
	
	/**
	 * 将字符串转为list
	 * @Title: convertStrToList 
	 * @param @param sourceStr
	 * @param @param splitStr
	 * @param @return
	 * @return List<String>    返回类型 
	 * @throws
	 */
	public static List<String>convertStrToList(String sourceStr,String splitStr)
	{
		List<String>convertedList=new ArrayList<String>();
		if(!StringUtils.isEmpty(sourceStr))
		{
			String array[]=sourceStr.split(splitStr);
			for(String str:array)
			{
				convertedList.add(str);
			}
		}
		return convertedList;
	}
	
	public static void main(String[] args) {
		System.out.println(Double.parseDouble("79.52"));
	}
	

	/**
	 * 订单商品列表转换
	 * @param orderGoodsList
	 * @return
	 */
	public static List<POSOrderDetailGoodsDto> odgdsList2POSOdgdsList(List<OrderGoodsDto> orderGoodsList){
		List<POSOrderDetailGoodsDto> posOdgdsList = null;
		POSOrderDetailGoodsDto posOdgds= null;
		if(orderGoodsList != null && orderGoodsList.size() > 0){
			posOdgdsList = new ArrayList<POSOrderDetailGoodsDto>();
			for(OrderGoodsDto e : orderGoodsList){
				posOdgds = new POSOrderDetailGoodsDto();
				posOdgds.setGoodsId(e.getGoodsId());
				posOdgds.setGoodsName(e.getGoodsName());
				posOdgds.setGoodsIndex(e.getGoodsIndex());
				posOdgds.setGoodsNumber(e.getGoodsNumber());
				posOdgds.setGoodsRequiredPrice(e.getGoodsRequiredPrice());
				posOdgds.setGoodsSettleFlag(e.getGoodsSettleFlag());
				posOdgds.setGoodsSettlePrice(e.getGoodsSettlePrice());
				posOdgds.setOrderGoodsDiscount(e.getOrderGoodsDiscount());
				posOdgds.setSettleUnitPrice(e.getSettleUnitPrice());
				posOdgds.setStandardPrice(e.getUnitPrice());
				posOdgds.setUnitName(e.getUnitName());
				posOdgds.setSpecsDesc(e.getSpecsDesc());
				posOdgds.setIsCancle(e.getIsCancle());
				posOdgds.setAvsSpecs(e.getAvsSpecs());
				posOdgds.setGoodsType(e.getGoodsType());
				posOdgds.setGoodsSetId(e.getGoodsSetId());
				posOdgds.setIsOrderDiscount(e.getIsOrderDiscount());
				posOdgds.setSetGoodsGroup(e.getSetGoodsGroup());
				posOdgds.setIsConsumeOuter(e.getIsConsumeOuter());
				posOdgdsList.add(posOdgds);
			}
			
		}
		return posOdgdsList;
	}
	
	/**
	 * 订单支付列表转换
	 * @param payList
	 * @return
	 */
	public static List<POSOrderDetailPayDto> odpayList2POSOdpayList(List<PayDto> payList){
		List<POSOrderDetailPayDto> posOdpayList = null;
		POSOrderDetailPayDto posOdpay= null;
		if(payList != null && payList.size() > 0){
			posOdpayList = new ArrayList<POSOrderDetailPayDto>();
			for(PayDto e : payList){
				posOdpay = new POSOrderDetailPayDto();
				posOdpay.setOddChange(e.getOddChange());
				if(e.getOrderPayTime() != null){
					posOdpay.setOrderPayTime(DateUtils.parse(e.getOrderPayTime(),DateUtils.DATETIME_FORMAT));
				}
				posOdpay.setPayAmount(e.getPayAmount());
				posOdpay.setPayChannel(e.getPayChannel());
				posOdpay.setPayIndex(e.getPayIndex());
				posOdpay.setPayType(e.getPayType());
				posOdpay.setRealCharges(e.getRealCharges());
				posOdpay.setPayStatus(e.getPayStatus());
				if(e.getUserPayTime() != null){
					posOdpay.setUserPayTime(DateUtils.parse(e.getUserPayTime(),DateUtils.DATETIME_FORMAT));
				}
				posOdpayList.add(posOdpay);
					
			}
			
		}
		return posOdpayList;
	}
}
