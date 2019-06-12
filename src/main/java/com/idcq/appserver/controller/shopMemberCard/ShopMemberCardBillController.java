package com.idcq.appserver.controller.shopMemberCard;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.idcq.appserver.common.CodeConst;
import com.idcq.appserver.dto.common.PageModel;
import com.idcq.appserver.dto.message.MessageListDto;
import com.idcq.appserver.dto.shopMemberCard.ShopMemberCardBillNewDto;
import com.idcq.appserver.exception.APIBusinessException;
import com.idcq.appserver.exception.APISystemException;
import com.idcq.appserver.exception.ServiceException;
import com.idcq.appserver.service.shopMemberCard.IShopMemberCardBillService;
import com.idcq.appserver.utils.DateUtils;
import com.idcq.appserver.utils.RequestUtils;
import com.idcq.appserver.utils.ResultUtil;
/**
 * 店铺会员卡账单
 * @ClassName: ShopMemberCardBillController 
 * @Description: TODO
 * @author 张鹏程 
 * @date 2016年1月14日 下午5:14:33 
 *
 */
@Controller
public class ShopMemberCardBillController {
	
	private Log logger = LogFactory.getLog(this.getClass());
	
	/**
	 * 店铺会员卡
	 */
	@Autowired
	private IShopMemberCardBillService shopMemberCardBillService;
	
	// last update time by dengjh 20160217
	@RequestMapping(value={"/token/pay/getShopMemberCardBills","/session/pay/getShopMemberCardBills","/service/pay/getShopMemberCardBills"},produces="application/json;charset=utf-8",method=RequestMethod.GET)
	public @ResponseBody String getShopMemberCardBills(HttpServletRequest request){
		try{
			String shopId=RequestUtils.getQueryParam(request,"shopId");
			String mobile=RequestUtils.getQueryParam(request, "mobile");
			String startDate=RequestUtils.getQueryParam(request, "startDate");
			String endDate=RequestUtils.getQueryParam(request, "endDate");
			String billType=RequestUtils.getQueryParam(request, "billType");
			String pNo=RequestUtils.getQueryParam(request, "pNo");
			String pSize=RequestUtils.getQueryParam(request, "pSize");
			String cardId=RequestUtils.getQueryParam(request, "cardId");
			String cardType=RequestUtils.getQueryParam(request, "cardType");
			String favoreeId=RequestUtils.getQueryParam(request, "favoreeId");
			String searchCondition=RequestUtils.getQueryParam(request, "searchCondition");
			//  增加支付类型条件   20160803 文震宇
			String payType = RequestUtils.getQueryParam(request, "payType");
			
			if(StringUtils.isEmpty(pNo)){
				pNo="1";
			}
			if(StringUtils.isEmpty(pSize)){
				pSize="10";
			}
			ShopMemberCardBillNewDto shopMemberCardBillDto=new ShopMemberCardBillNewDto();
			String userName = null;
			//处理模糊搜索条件
			 if(StringUtils.isNotEmpty(searchCondition)){
			        shopMemberCardBillDto.setIsSearch(1);
	                searchCondition = searchCondition.trim();
	                if(StringUtils.isEmpty(mobile)){
	                    try
                        {   
	                        //保证格式正确，待优化
	                        Long.parseLong(searchCondition);
                            mobile = searchCondition;
                        }
                        catch (Exception e)
                        {
                            logger.error(e.getMessage(), e);
                        }
	                }
	                if(StringUtils.isEmpty(cardId)){
	                    try
                        {   
                            //保证格式正确，待优化
                            Integer.parseInt(searchCondition);
                            cardId = searchCondition;
                        }
                        catch (Exception e)
                        {
                            logger.error(e.getMessage(), e);
                        }
	                }
	                userName = searchCondition;
	            }
			shopMemberCardBillDto.setUserName(userName);
			shopMemberCardBillDto.setMobile(mobile);
			shopMemberCardBillDto.setBillStartTime(startDate);
			shopMemberCardBillDto.setBillEndTime(endDate);
			if(!StringUtils.isEmpty(billType)){
				shopMemberCardBillDto.setBillType(Integer.parseInt(billType));
			}
		//  增加支付类型条件   20160803 文震宇
			if(!StringUtils.isEmpty(payType)){
				
				 try{
					 shopMemberCardBillDto.setPayType(Integer.parseInt(payType));
				    }catch(Exception e){
				        logger.error(e.getMessage(), e);
				    }
			}
			
			shopMemberCardBillDto.setShopId(Integer.parseInt(shopId));
			PageModel pageModel=new PageModel();
			pageModel.setToPage(Integer.parseInt(pNo));
			pageModel.setPageSize(Integer.parseInt(pSize));
			if(StringUtils.isNotEmpty(cardId)){
			    try{
			        shopMemberCardBillDto.setCardId(Integer.parseInt(cardId));
			    }catch(Exception e){
			        logger.error(e.getMessage(), e);
			    }
			}
			shopMemberCardBillDto.setCardType(cardType);
			shopMemberCardBillDto.setPageNo((pageModel.getToPage()-1)*pageModel.getPageSize());
			shopMemberCardBillDto.setPageSize(pageModel.getPageSize());
			Integer opertaterId = (favoreeId == null || favoreeId.trim().length() == 0) ? null : Integer.valueOf(favoreeId.trim());
			shopMemberCardBillDto.setOpertaterId(opertaterId);
			pageModel=shopMemberCardBillService.getShopMemberCardBillsNew(shopMemberCardBillDto, pageModel);
			MessageListDto msgList = new MessageListDto();
			msgList.setpNo(pageModel.getToPage());
			msgList.setpSize(pageModel.getPageSize());
			msgList.setLst(pageModel.getList());
			msgList.setrCount(pageModel.getTotalItem());
			List<MessageListDto> dataList = new ArrayList<MessageListDto>();
			dataList.add(msgList);
			return ResultUtil.getResultJson(CodeConst.CODE_SUCCEED, "查询店铺会员卡成功",
					msgList, DateUtils.DATETIME_FORMAT);
	} catch (ServiceException e){
		logger.error("查询店铺会员卡异常 ",e);
		throw new APIBusinessException(e);
	} catch (Exception e) {
		logger.error("查询店铺会员卡异常",e);
		throw new APISystemException("查询店铺会员卡异常", e);
	}
	}
	
}
