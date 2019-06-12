package com.idcq.appserver.dao.shop;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.idcq.appserver.dao.BaseDao;
import com.idcq.appserver.dto.shop.ShopAccountDto;
import com.idcq.appserver.utils.NumberUtil;

/**
 * 商品账户dao
 * 
 * @author Administrator
 * 
 * @date 2015年3月3日
 * @time 下午5:10:44
 */
@SuppressWarnings("unchecked")
@Repository
public class ShopAccountDaoImpl extends BaseDao<ShopAccountDto> implements IShopAccountDao
{

    public ShopAccountDto getShopAccount(Long shopId) throws Exception
    {
        return (ShopAccountDto) super.selectOne(generateStatement("getShopAccount"), shopId);
    }

    public void updateShopAccountMoney(Long shopId, double amount) throws Exception
    {
        Map param = new HashMap();
        param.put("shopId", shopId);
        param.put("amount",  NumberUtil.formatDouble(amount,4));
        super.update("updateShopAccountMoney", param);
    }

    /**
     * 查询是否赠送过保证金 @Title: queryIsGiveDepositFlag @param @param
     * shopId @param @return @param @throws Exception @throws
     */
    public Integer queryIsGiveDepositFlag(Long shopId) throws Exception
    {
        return (Integer) super.selectOne(generateStatement("queryIsGiveDepositFlag"), shopId);
    }

    /**
     * 给商铺赠送保证金 @Title: giveShopDeposit @param @param shopId @param @param
     * amount @param @throws Exception @throws
     */
    public void giveShopDeposit(Long shopId, Double amount) throws Exception
    {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("shopId", shopId);
        params.put("amount",  NumberUtil.formatDouble(amount,4));
        super.update(generateStatement("giveShopDeposit"), params);
    }

    @Override
    public Map<String, Object> getShopAccountMoney(Map<String, Object> parms) throws Exception
    {
        return (Map<String, Object>) super.selectOne(generateStatement("getShopAccountMoney"), parms);
    }

    @Override
    public List<Map<String, Object>> getShopAwardList(Map<String, Object> map) throws Exception
    {
        return (List) super.findList(generateStatement("getShopAwardList"), map);
    }

    @Override
    public Integer getShopAwardCount(Map<String, Object> map) throws Exception
    {
        return (Integer) super.selectOne(generateStatement("getShopAwardCount"), map);
    }

    @Override
    public List<Map<String, Object>> getUserAwardList(Map<String, Object> map) throws Exception
    {
        return (List) super.findList(generateStatement("getUserAwardList"), map);
    }

    @Override
    public Integer getUserAwardCount(Map<String, Object> map) throws Exception
    {
        return (Integer) super.selectOne(generateStatement("getUserAwardCount"), map);
    }

    @Override
    public List<Map<String, Object>> getAllAwardList(Map<String, Object> map) throws Exception
    {
        return (List) super.findList(generateStatement("getAllAwardList"), map);
    }

    @Override
    public Integer getAllAwardCount(Map<String, Object> map) throws Exception
    {
        return (Integer) super.selectOne(generateStatement("getAllAwardCount"), map);
    }

    @Override
    public Map<String, Object> getShopAwardTotal(Map<String, Object> map) throws Exception
    {
        return (Map) super.selectOne(generateStatement("getShopAwardTotal"), map);
    }

    @Override
    public Map<String, Object> getUserAwardTotal(Map<String, Object> map) throws Exception
    {
        return (Map) super.selectOne(generateStatement("getUserAwardTotal"), map);
    }

    public Integer updateFreezeMoney(Long shopId, double freezeAmount) throws Exception
    {
        Map<String, Object> param = new HashMap<String, Object>();
        param.put("shopId", shopId);
        param.put("freezeAmount",  NumberUtil.formatDouble(freezeAmount,4));
        return super.update("updateShopFreezeMoney", param);
    }

    @Override
    public void updateShopAccount(Long shopId, Double changeAmount, Double changeOnlineAmount,
            Double changeRewardAmount, Double changeRewardTotal, Double changeDepositAmount, Double changeFreezeAmount,
            Integer arrearsFlag,Double marketAmount, Double marketTotal, Double legendTotal, Double marketRebateTotal, Double marketRebateMoney) throws Exception
    {
        Map<String, Object> param = new HashMap<String, Object>();
        param.put("shopId", shopId);
        param.put("changeAmount",  NumberUtil.formatDouble(changeAmount,4));
        param.put("changeOnlineAmount",  NumberUtil.formatDouble(changeOnlineAmount,4));
        param.put("changeRewardAmount",  NumberUtil.formatDouble(changeRewardAmount,4));
        param.put("changeRewardTotal",  NumberUtil.formatDouble(changeRewardTotal,4));
        param.put("changeDepositAmount",  NumberUtil.formatDouble(changeDepositAmount,4));
        param.put("changeFreezeAmount",  NumberUtil.formatDouble(changeFreezeAmount,4));
        param.put("arrearsFlag", arrearsFlag);
        param.put("marketAmount",  NumberUtil.formatDouble(marketAmount,4));
        param.put("marketTotal",  NumberUtil.formatDouble(marketTotal,4));
        param.put("legendTotal",  NumberUtil.formatDouble(legendTotal,4));
        param.put("marketRebateTotal",  NumberUtil.formatDouble(marketRebateTotal,4));
        param.put("marketRebateMoney",  NumberUtil.formatDouble(marketRebateMoney,4));
        super.update("updateShopAccount", param);
    }
    
    @Override
    public void updateShopAccount(Long shopId, Double changeAmount, Double changeOnlineAmount,
            Double changeRewardAmount, Double changeRewardTotal, Double changeDepositAmount, Double changeFreezeAmount,
            Integer arrearsFlag,Double marketAmount, Double marketTotal, Double legendTotal) throws Exception
    {
        Map<String, Object> param = new HashMap<String, Object>();
        param.put("shopId", shopId);
        param.put("changeAmount",  NumberUtil.formatDouble(changeAmount,4));
        param.put("changeOnlineAmount",  NumberUtil.formatDouble(changeOnlineAmount,4));
        param.put("changeRewardAmount",  NumberUtil.formatDouble(changeRewardAmount,4));
        param.put("changeRewardTotal",  NumberUtil.formatDouble(changeRewardTotal,4));
        param.put("changeDepositAmount",  NumberUtil.formatDouble(changeDepositAmount,4));
        param.put("changeFreezeAmount",  NumberUtil.formatDouble(changeFreezeAmount,4));
        param.put("arrearsFlag", arrearsFlag);
        param.put("marketAmount",  NumberUtil.formatDouble(marketAmount,4));
        param.put("marketTotal",  NumberUtil.formatDouble(marketTotal,4));
        param.put("legendTotal",  NumberUtil.formatDouble(legendTotal,4));
        super.update("updateShopAccount", param);
    }
    
    @Override
	public List<ShopAccountDto> getAllAccount(Integer limit, Integer pageSize) throws Exception {
		
		Map<String, Object> parms = new HashMap<String, Object>();
		parms.put("limit", limit);
		parms.put("pageSize", pageSize);
		
		return findList("getAllAccount",parms);
	}
}
