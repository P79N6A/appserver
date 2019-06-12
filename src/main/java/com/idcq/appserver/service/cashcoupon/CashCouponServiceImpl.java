package com.idcq.appserver.service.cashcoupon;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;





import com.idcq.appserver.dao.cashcoupon.ICashCouponDao;
import com.idcq.appserver.dto.cashcoupon.CashCouponDto;
import com.idcq.appserver.dto.cashcoupon.UserCashCouponDto;
import com.idcq.appserver.dto.common.PageModel;
import com.idcq.appserver.utils.FdfsUtil;
import com.idcq.appserver.utils.NumberUtil;

@Service
public class CashCouponServiceImpl implements ICashCouponService {
	@Autowired
	private ICashCouponDao cashCouponDao;
	
	public PageModel getTopCashCouponList(long cityId, int pageNo, int pageSize)
			throws Exception {
		PageModel page = new PageModel();
		page.setTotalItem(cashCouponDao.getCashCouponListCount(cityId));
		page.setPageSize(pageSize);
		page.setToPage(pageNo);
		List<CashCouponDto>  list = cashCouponDao.getCashCouponList(cityId, pageNo, pageSize);
		//设置图片全路径2015.6.8
		String imgUrl = null;
		for(CashCouponDto cashCouponDto : list){
			imgUrl = cashCouponDto.getCashCouponImg();
			if(!StringUtils.isBlank(imgUrl)){
				cashCouponDto.setCashCouponImg(FdfsUtil.getFileProxyPath(imgUrl));
			}
		}
		page.setList(list);
		return page;
	}

	public PageModel getUserCashCouponList(long userId, int pageNo, int pageSize)
			throws Exception {
		PageModel page = new PageModel();
		page.setTotalItem(cashCouponDao.getUserCashCouponListCount(userId));
		page.setToPage(pageNo);
		page.setPageSize(pageSize);
		List<UserCashCouponDto> userCCoupons = new ArrayList<UserCashCouponDto>();
		List<CashCouponDto> pCashCouponList = cashCouponDao.getUserCashCouponList(userId, pageNo, pageSize);
		BigDecimal bigCashCouponValue ;
		BigDecimal bigCashCouponUsedPrice ;
		// 代金券转换到用户代金券dto
		String imgUrl = null;
		if(pCashCouponList != null && pCashCouponList.size() > 0){
			for(CashCouponDto cc : pCashCouponList){
				UserCashCouponDto uc = new UserCashCouponDto();
				PropertyUtils.copyProperties(uc, cc);
				// 面值
				bigCashCouponValue = new BigDecimal(cc.getPrice()+"");
				// 已经使用的金额
				bigCashCouponUsedPrice = new BigDecimal(cc.getUsedPrice());
				uc.setCashCouponAmount(NumberUtil.fmtDouble(bigCashCouponValue.subtract(bigCashCouponUsedPrice).doubleValue(), 2));
				uc.setUccPrice(NumberUtil.fmtDouble(Double.valueOf(cc.getValue()), 2));
				imgUrl = cc.getCashCouponImg();
				if(!StringUtils.isBlank(imgUrl)){
					uc.setCashCouponImg(FdfsUtil.getFileProxyPath(imgUrl));
				}
				userCCoupons.add(uc);
			}
		}
		page.setList(userCCoupons);
		return page;
	}

	public CashCouponDto getCouponDtoById(long cashCouponId) throws Exception {
		
		return cashCouponDao.getCouponDto(cashCouponId);
	}

	public int updateAvaliableNum(long cashCouponId) throws Exception {
		return cashCouponDao.updateAvaliableNum(cashCouponId);
	}

	public PageModel getShopCoupon(Long shopId, Integer queryType,Integer pageNo, Integer pageSize) throws Exception {
		PageModel page = new PageModel();
		Map param=new HashMap();
		param.put("shopId", shopId);
		param.put("queryType", queryType);
		param.put("pageNo", pageNo);
		param.put("pageSize", pageSize);
		page.setTotalItem(cashCouponDao.getShopCouponTotal(param));
		page.setPageSize(pageSize);
		page.setToPage(pageNo);
		page.setList(cashCouponDao.getShopCouponList(param));
		return page;
	}

	public CashCouponDto getCouponDtoByIdForGrab(long cashCouponId) throws Exception {
		return cashCouponDao.getCouponDtoForGrab(cashCouponId);
	}

	/** 
	* @Title: getCashCouponByShopId 
	* @Description: TODO(根据商铺id查找代金卷) 
	* @param @param shopId
	* @param @return
	* @param @throws Exception  
	* @throws 
	*/
	public List<CashCouponDto> getCashCouponByShopId(Long shopId)
			throws Exception {
		return cashCouponDao.getCashCouponByShopId(shopId);
	}

	
	
}
