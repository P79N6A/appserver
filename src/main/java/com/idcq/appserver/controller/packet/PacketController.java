package com.idcq.appserver.controller.packet;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.idcq.appserver.common.CodeConst;
import com.idcq.appserver.dto.ResultDto;
import com.idcq.appserver.exception.APIBusinessException;
import com.idcq.appserver.exception.APISystemException;
import com.idcq.appserver.exception.ServiceException;
import com.idcq.appserver.exception.ValidateException;
import com.idcq.appserver.service.packet.IPacketService;
import com.idcq.appserver.utils.CommonValidUtil;
import com.idcq.appserver.utils.RequestUtils;
import com.idcq.appserver.utils.ResultUtil;
import com.idcq.appserver.utils.SmsVeriCodeUtil;
/**
 * 红包使用controller
 * @author Administrator
 *
 */
@Controller
@RequestMapping(value="/pay")
public class PacketController {
	private final Log logger = LogFactory.getLog(getClass());
	@Autowired
	public IPacketService packetService;
	
	@RequestMapping(value="/initialPacketData")
	@ResponseBody
	public String initialPacketData(){
		new Thread(){
			public void run() {
				logger.info("初始化红包数据开始");
				long startTime = System.currentTimeMillis();
				long batchNo = 299493129;
				List<Map> datas = null;
				Random r = new Random();
				for (int i = 0; i < 6000; i++) {
					datas = new ArrayList<Map>();
					batchNo++;
					long shopId = (r.nextInt(4999)+1000000000);
					for (int k = 0; k < 50; k++) {
						Map params = new HashMap();
						params.put("red_packet_batch_no", batchNo+"");
						params.put("obtain_time", "2015-04-27 15:12:23");
						params.put("use_flag", 0);
						params.put("obtain_desc", "红包来了"+r.nextInt(3000));
						params.put("start_time", "2015-04-27 15:12:23");
						params.put("stop_time", "2015-08-12 15:02:15");
						params.put("shop_id", shopId);
						params.put("amount", SmsVeriCodeUtil.getIntNum(1, 899));
						params.put("owner_id", (r.nextInt(20000)+2000000000));
						datas.add(params);
					}
					packetService.insertRedPacket(datas);
				}
				logger.info("初始化红包数据结束，共耗时："+(System.currentTimeMillis()-startTime));
			}
			
		}.start();
		return "success";
	}
	
	/**
	 * 用户使用红包接口
	 * <br><b>红包只能使用一次，使用过后将状态改为不可用</b>
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/payByRedPacket",produces="application/json;charset=UTF-8")
	@ResponseBody
	public ResultDto payByRedPacket(HttpServletRequest request){
		try {
			logger.info("用户使用红包-start");
			String userIdStr = RequestUtils.getQueryParam(request, "userId");
			String orderId = RequestUtils.getQueryParam(request, "orderId");
			String redPacketIds = RequestUtils.getQueryParam(request, "redPacketId");
			//订单支付类型 0-单个订单支付 1-多个订单支付
			String orderPayTypeStr = RequestUtils.getQueryParam(request, "orderPayType");
			//验证userId是否非空
			CommonValidUtil.validStrNull(userIdStr, CodeConst.CODE_PARAMETER_NOT_NULL, CodeConst.MSG_REQUIRED_USER_ID);
			//验证参数userId是否能转换为long
			Long userId = CommonValidUtil.validStrLongFmt(userIdStr, CodeConst.CODE_PARAMETER_NOT_VALID, CodeConst.MSG_FORMAT_ERROR_USERID);
			//验证订单编号是否非空
			CommonValidUtil.validStrNull(orderId, CodeConst.CODE_PARAMETER_NOT_NULL, CodeConst.MSG_REQUIRED_ORDERID);
			//验证红包编号是否非空
			CommonValidUtil.validStrNull(redPacketIds, CodeConst.CODE_PARAMETER_NOT_NULL, CodeConst.MSG_REQUIRED_REDPACKET_ID);
			//验证红包编号格式是否正确
			//Long redPacketId = CommonValidUtil.validStrLongFmt(redPacketIdStr, CodeConst.CODE_PARAMETER_NOT_VALID, CodeConst.MSG_FORMAT_ERROR_REDPACKET_ID);
			int orderPayType = 0;//订单状态 0-单个订单 1-订单组 默认单个订单
			if (!StringUtils.isBlank(orderPayTypeStr)) {
				orderPayType = CommonValidUtil.validStrIntFmt(orderPayTypeStr, CodeConst.CODE_PARAMETER_NOT_VALID, CodeConst.MSG_ERROR_ORDER_TYPE_FMT);
			}
			packetService.payByRedPacket(userId,orderId,redPacketIds,orderPayType);
			try {
				//获取红包成功后，需要将缓存中存放的红包信息清除
				packetService.delQueryUserREdPacketToRedis(userId);
			} catch (Exception e) {
				logger.error("用户使用红包，清除红包缓存异常",e);
			}
			return ResultUtil.getResult(0, "操作成功！", null);
		} catch (ServiceException e){
			logger.error("用户使用红包异常",e);
			throw new APIBusinessException(e);
		} catch (Exception e) {
			logger.error("用户使用红包异常",e);
			throw new APISystemException("用户使用红包异常", e);
		}
	}
	
	/**
	 * 用户获取红包接口<br/><b>将该红包的持有人修改为该用户</b>
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/obtainRedPacket",produces="application/json;charset=UTF-8")
	@ResponseBody
	public ResultDto obtainRedPacket(HttpServletRequest request){
		try {
			logger.info("用户获取红包接口-start");
			String userIdStr = RequestUtils.getQueryParam(request, "userId");
			String redPacketBatchNo = RequestUtils.getQueryParam(request, "redPacketBatchNo");
			//验证是否非空
			CommonValidUtil.validStrNull(userIdStr, CodeConst.CODE_PARAMETER_NOT_NULL, CodeConst.MSG_REQUIRED_USER_ID);
			//验证参数是否能转换为int
			Long userId = CommonValidUtil.validStrLongFmt(userIdStr, CodeConst.CODE_PARAMETER_NOT_VALID, CodeConst.MSG_FORMAT_ERROR_USERID);
			CommonValidUtil.validStrNull(redPacketBatchNo, CodeConst.CODE_PARAMETER_NOT_NULL, CodeConst.MSG_REQUIRED_RED_BATCH_NO);
			Map<String,Object> pModel = null;
			synchronized (this) {
				pModel = packetService.obtainRedPacket(userId,redPacketBatchNo);	
			}
			try {
				//获取红包成功后，需要将缓存中存放的红包信息清除
				packetService.delQueryUserREdPacketToRedis(userId);
			} catch (Exception e) {
				logger.error("用户获取红包，清除红包缓存异常",e);
			}
			return ResultUtil.getResult(0, "获取红包成功", pModel);
		} catch (ServiceException e){
			logger.error("用户获取红包异常",e);
			throw new APIBusinessException(e);
		} catch (Exception e) {
			logger.error("用户获取红包异常",e);
			throw new APISystemException("用户获取红包异常", e);
		}
	}
	
	/**
	 * 查询用户红包接口
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/queryUserRedPacket",produces="application/json;charset=UTF-8")
	@ResponseBody
	public Object queryUserRedPacket(HttpServletRequest request){
		try {
			logger.info("用户获取红包接口-start");
			long start = System.currentTimeMillis();
			String userIdStr = RequestUtils.getQueryParam(request, "userId");//用户编号
			//验证是否非空
			CommonValidUtil.validStrNull(userIdStr, CodeConst.CODE_PARAMETER_NOT_NULL, CodeConst.MSG_REQUIRED_USER_ID);
			//验证参数是否能转换为int
			Long userId = CommonValidUtil.validStrLongFmt(userIdStr, CodeConst.CODE_PARAMETER_NOT_VALID, CodeConst.MSG_FORMAT_ERROR_USERID);
			int pNo = CommonValidUtil.validCurrentPage(RequestUtils.getQueryParam(request, "pNo"));
			int pSize = CommonValidUtil.validPageSize(RequestUtils.getQueryParam(request, "pSize"));
			String result = packetService.queryUserRedPacket(userId, pSize,pNo);
			logger.debug("共耗时："+(System.currentTimeMillis()-start));
			if (null == result) {
				throw new ValidateException(CodeConst.CODE_PARAMETER_NOT_EXIST, "未查询到相关信息");
			}
			//return ResultUtil.getResultJson(0, "获取我的红包列表成功", pModel,DateUtils.DATETIME_FORMAT);
			return result;
		} catch (ServiceException e){
			logger.error("用户获取红包信息异常",e);
			throw new APIBusinessException(e);
		} catch (Exception e) {
			logger.error("用户获取红包信息异常",e);
			throw new APISystemException("用户获取红包信息异常", e);
		}
	}
		
}
