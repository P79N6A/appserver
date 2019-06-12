package com.idcq.appserver.service.shopcart;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.idcq.appserver.common.CodeConst;
import com.idcq.appserver.dao.goods.IGoodsDao;
import com.idcq.appserver.dao.shop.IShopDao;
import com.idcq.appserver.dao.shopcart.IShopCartItemDao;
import com.idcq.appserver.dao.user.IUserDao;
import com.idcq.appserver.dto.common.PageModel;
import com.idcq.appserver.dto.shopcart.ShopCartDto;
import com.idcq.appserver.dto.shopcart.ShopCartItemDto;
import com.idcq.appserver.dto.user.UserDto;
import com.idcq.appserver.exception.ValidateException;
import com.idcq.appserver.utils.CommonValidUtil;
import com.idcq.appserver.utils.NumberUtil;
/**
 * 购物车service
 * 
 * @author Administrator
 * 
 * @date 2015年3月8日
 * @time 下午3:34:51
 */
@Service
public class ShopCartServiceImpl implements IShopCartServcie{

	@Autowired
	public IShopCartItemDao shopCartItemDao;
	@Autowired
	public IUserDao userDao;
	@Autowired
	public IShopDao shopDao;
	@Autowired
	public IGoodsDao goodDao;

	public ShopCartDto getShopCartByUser(ShopCartDto shop, int page,
			int pageSize) throws Exception {
		return null;
	}

	public PageModel getGoodsListByCart(ShopCartItemDto item, int page,
			int pageSize) throws Exception {
		Long userId = item.getUserId();
		//常规验证
		if(!NumberUtil.isPositLong(userId)){
			throw new ValidateException(CodeConst.CODE_PARAMETER_NOT_NULL,CodeConst.MSG_REQUIRED_MEMBER);
		}
		//验证用户存在性
		UserDto pUser = this.userDao.getUserById(userId);
		if(pUser == null){
			throw new ValidateException(CodeConst.CODE_PARAMETER_NOT_EXIST, CodeConst.MSG_MISS_MEMBER);
		}
		//获取商品列表
		PageModel pm = new PageModel();
		pm.setTotalItem(this.shopCartItemDao.getItemListCountByUserId(item));
		pm.setList(this.shopCartItemDao.getItemListByUserId(item, page, pageSize));
		pm.setToPage(page);
		pm.setPageSize(pageSize);
		return pm;
	}

	public int updateCart(ShopCartDto item) throws Exception {
		Long userId = item.getUserId();
		//验证用户
		if(!NumberUtil.isPositLong(userId)){
			throw new ValidateException(CodeConst.CODE_PARAMETER_NOT_NULL,CodeConst.MSG_FORMAT_ERROR);
		}
		if(null==userId){
			throw new ValidateException(CodeConst.CODE_PARAMETER_NOT_NULL,
					CodeConst.MSG_REQUIRED_MEMBER);
		}
		// 验证int
		CommonValidUtil.validPositLong(userId, CodeConst.CODE_PARAMETER_NOT_VALID,
				CodeConst.MSG_FORMAT_ERROR_USERID);
		// 验证用户是否存在
		UserDto user = userDao.getUserById(userId);
		if (null == user) {
			throw new ValidateException(CodeConst.CODE_PARAMETER_NOT_EXIST,
					CodeConst.MSG_MISS_MEMBER);
		}
		//验证购物项
		List<ShopCartItemDto> itemList = item.getGoodsList();
		if(itemList == null || itemList.size() <= 0){
			throw new ValidateException(CodeConst.CODE_PARAMETER_NOT_NULL,CodeConst.MSG_REQUIRED_OGOODS);
		}
		for(ShopCartItemDto it : itemList){
			//增加userId
			it.setUserId(userId);
			if(null==it.getGoodsId()){
				throw new ValidateException(CodeConst.CODE_PARAMETER_NOT_NULL,CodeConst.MSG_MISS_GOOD_ID);
			}
			if(!NumberUtil.isPositLong(it.getGoodsId())){
				throw new ValidateException(CodeConst.CODE_PARAMETER_NOT_VALID,CodeConst.MSG_FORMAT_ERROR_GOODS_NUM);
			}
			if(null==it.getShopId()){
				throw new ValidateException(CodeConst.CODE_PARAMETER_NOT_NULL,CodeConst.MSG_MISS_SHOP_ID);
			}			
			if(!NumberUtil.isPositLong(it.getShopId())){
				throw new ValidateException(CodeConst.CODE_PARAMETER_NOT_VALID,CodeConst.MSG_FORMAT_ERROR_SHOP_ID);
			}
			if(null!=it.getGoodsNumber()){
				if(!NumberUtil.isPositInt(it.getGoodsNumber())){
					throw new ValidateException(CodeConst.CODE_PARAMETER_NOT_VALID,CodeConst.MSG_FORMAT_ERROR_GOODS_NUM);
				}
			}
			if(null!=it.getGoodsIndex()){
				if(!NumberUtil.isPositInt(it.getGoodsIndex())){
					throw new ValidateException(CodeConst.CODE_PARAMETER_NOT_VALID,CodeConst.MSG_FORMAT_ERROR_GOODS_INDEX);
				}
			}
			//验证商品和商铺是否存在
			ValidGoodAndShopISEXIST(it.getShopId(), it.getGoodsId());
		}
		//删除历史购物项列表
		this.shopCartItemDao.delCartItemsByUserId(item.getUserId());
		
		//保存新的购物项列表 TODO 需要新增加验证shopid、goodid是否存在接口
		for(ShopCartItemDto it2 : itemList){
			this.shopCartItemDao.saveCartItem(it2);
		}
		return 1;
	}
	/**
	 * 判断shopId和goodId是否存在
	 * @param shopId
	 * @param goodId
	 * @return
	 */
	public void ValidGoodAndShopISEXIST(Long shopId,Long goodId){
		Integer shopCount = shopDao.getCountByShopId(shopId);
		if(null==shopCount||0==shopCount){
			throw new ValidateException(CodeConst.CODE_PARAMETER_NOT_EXIST,
					CodeConst.MSG_MISS_SHOP);
		}
		Integer goodCount = goodDao.getCountByGoodId(goodId);
		if(null==goodCount||0==goodCount){
			throw new ValidateException(CodeConst.CODE_PARAMETER_NOT_EXIST,
					CodeConst.MSG_MISS_GOOD);
		}
	}
	
	
	
}
