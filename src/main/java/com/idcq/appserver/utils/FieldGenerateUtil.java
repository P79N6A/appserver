package com.idcq.appserver.utils;

import java.util.Date;
import java.util.UUID;

/**
 * 字段生成工具
 * 
 * @author Administrator
 * 
 * @date 2015年3月20日
 * @time 下午3:36:54
 */
public class FieldGenerateUtil {
	
	public  static long oInt5 = 100000;
	
	/**
	 * 订单号生成工具
	 * <p>
	 * 	订单ID的规则：商铺ID(不满10位，前面补零)+yyMMdd+HHmmss+自增值/随机数(5位)
	 * </p>
	 * @param shopId 订单中任意一个商铺的ID
	 * @return
	 * @throws Exception
	 */
	public static String generateOrderId(Long shopId) throws Exception{
		return generateOrderId(shopId, 5);
	}
	
	public static String generateOrderId(Long shopId, int num) throws Exception{
		String incrInt = UUID.randomUUID().toString();
		int incrLen = incrInt.length();
		if(incrLen < num) {
			num = 5;
		}
		String incr =incrInt.substring(incrLen-num,incrLen);//获取uuid的后num位字符串
		String timestamp = DateUtils.format(new Date(),DateUtils.MILLI_TIMESTAMP_FORMAT);
		return getShopIdStr(shopId)+timestamp+incr;
	}
	
	public static String generate32bitOrderId(Long shopId) throws Exception{
		String incrInt = UUID.randomUUID().toString();
		int incrLen = incrInt.length();
		String int5 =incrInt.substring(incrLen-7,incrLen);//获取uuid的后5位字符串
		String timestamp = DateUtils.format(new Date(),DateUtils.MILLI_TIMESTAMP_FORMAT);
		return getShopIdStr(shopId)+timestamp+int5;
	}
	/**
	 * 生成交易流水号
	 * 
	 * @Function: com.idcq.appserver.utils.FieldGenerateUtil.generate32bitOrderId
	 * @Description:
	 *
	 * @param shopId
	 * @return
	 * @throws Exception
	 *
	 * @version:v1.0
	 * @author:ChenYongxin
	 * @date:2015年11月24日 下午3:00:22
	 *
	 * Modification History:
	 * Date         Author      Version     Description
	 * -----------------------------------------------------------------
	 * 2015年11月24日    ChenYongxin      v1.0.0         create
	 */
    public static String generatebitOrderId(Long transactionId) throws Exception{
        String timestamp = DateUtils.format(new Date(),DateUtils.MILLI_TIMESTAMP_FORMAT);
        //return timestamp+getShopIdStr(transactionId);
        return timestamp+transactionId;
    }
    /**
     * 获取出入库单号
     * 
     * @Function: com.idcq.appserver.utils.FieldGenerateUtil.generatebitStorageNoId
     * @Description:
     *
     * @param storageType
     * @return
     * @throws Exception
     *
     * @version:v1.0
     * @author:ChenYongxin
     * @date:2016年4月6日 下午4:59:57
     *
     * Modification History:
     * Date         Author      Version     Description
     * -----------------------------------------------------------------
     * 2016年4月6日    ChenYongxin      v1.0.0         create
     */
    public static String generatebitStorageNoId(String storageType) throws Exception{
        String timestamp = DateUtils.format(new Date(),DateUtils.TIMESTAMP_FORMAT);
        return storageType+timestamp+UUID.randomUUID().toString().substring(0, 5);
    }
	
	
	public static synchronized long incrOInt5() {
		return ++oInt5; 
	}
	
	/**
	 * 获取10位的商铺ID
	 * <p>
	 * 	shopId若不满10位，则在前面补零
	 * </p>
	 * 
	 * @param shopId
	 * @return
	 * @throws Exception
	 */
	private static String getShopIdStr(Long shopId)throws Exception{
		//商铺ID不满10位，前面补零
		StringBuilder shopIdStr = new StringBuilder(shopId+"");
		int shopIdLen = shopIdStr.length();
		if(shopIdLen < 10){
			int differLen = 10 - shopIdLen;
			shopIdStr.reverse();
			for(int i=0;i<differLen;i++){
				shopIdStr.append("0");		
			}
			return shopIdStr.reverse().toString();
		}
		return shopId.toString();
	}
	
}
