package com.idcq.appserver.service.shop;

import java.util.List;
import java.util.Map;

import com.idcq.appserver.dto.common.PageModel;
import com.idcq.appserver.dto.goods.PlaceGoodsDto;
import com.idcq.appserver.dto.order.OrderDto;
import com.idcq.appserver.dto.shop.ShopDetailDto;
import com.idcq.appserver.dto.shop.ShopDto;
import com.idcq.appserver.dto.shop.ShopEmployeeDto;
import com.idcq.appserver.dto.user.UserDto;

public interface IShopServcie {

	
	/**
	 * 根据shopId进行查找
	* @Title: getShopById 
	* @param @param shopId
	* @param @return
	* @param @throws Exception
	* @return ShopDto    返回类型 
	* @throws
	 */
	ShopDto getShopById(Long shopId)throws Exception;
	
	ShopDto getShopExtendByIdAndStatus(Long shopId, Integer shoStatus)throws Exception;
	
	/**
	 * 获取指定的商铺信息
	 * 
	 * @param shopId
	 * @return
	 * @throws Exception
	 */
	ShopDto getShopMainOfCacheById(Long shopId)throws Exception;
	
	/**
	 * 获取店铺详情
	 * 
	 * @param shopId
	 * @return
	 * @throws Exception
	 */
	ShopDetailDto getShopDetailById(Long shopId) throws Exception ;
	/**
	 * @param paramshopId	int		是	商铺ID
	 * @param paramstartTime	Date		否	起始时间，不填为本月第一天
	 * @param paramendTime	Date		否	结束时间，不填为当前天
	 * @param paramsortBy	string		否	排序字段。1销售金额、2销售笔数
	 * @param paramorderBy	int		否	-1降序，1升序
	 * 
	 */
	public List<Map<String, Object>> getSalerPerformanceList(
			Map<String, Object> param) throws Exception;
	public int getSalerPerformanceCount(Map<String, Object> param)
			throws Exception;
	
	/**
	 * 获取商铺列表
	 * 
	 * @param shop
	 * @param page
	 * @param pageSize
	 * @return
	 * @throws Exception
	 */
	PageModel getShopList(ShopDto shop,int page,int pageSize) throws Exception ;
	
	/**
	 * 获取商铺评论
	 * 
	 * @param shopId
	 * @param page
	 * @param pageSize
	 * @return
	 * @throws Exception
	 */
	PageModel getShopComments(Long shopId,int page,int pageSize) throws Exception ;
	
	/**
	 * 搜索店铺
	 * 
	 * @param param
	 * @return
	 * @throws Exception
	 */
	PageModel searchShop(Map param) throws Exception ;
	
	/**
	 * 获取指定商铺经纬度信息
	 * @param shopId
	 * @return
	 * @throws Exception
	 */
	Map getShopXyById(Long shopId) throws Exception;
	
	/**
	 * 获得商铺 列表通过idlist
	 * @param shopIdList
	 * @return
	 */
	List<ShopDto>getShopListByIds(List<Long>shopIdList);
	
	/**
	 * 获得商铺通过分页工具
	 * @param pageModel
	 * @return
	 */
	PageModel getShopByPage(PageModel pageModel,int... queryTotalCount);
	
	/**
	 * 点赞接口
	 * @param zanType 点赞类型
	 * @param bizId 对应的商品或者商铺id
	 * @param userId 用户id 
	 * @return
	 */
	Map<String, Integer> praise(String zanType, String bizId, String userId) throws Exception;
	
	/**
	 * 
	 * @param shopId
	 * @param queryDate
	 * @return
	 * @throws Exception
	 */
	public List<Map> queryShopResourceSnapshot(Long shopId) throws Exception;
	
	/**
	 * 验证商铺存在性
	 * 
	 * @param shopId
	 * @return
	 * @throws Exception
	 */
	int queryShopExists(Long shopId) throws Exception;
	
	/**
	 * 验证商铺员工的存在性
	 * 
	 * @param shopId
	 * @param employeeId
	 * @return
	 * @throws Exception
	 */
	int queryShopEmplExists(Long shopId,Long employeeId) throws Exception;
	
	/**
	 * 根据商铺编号找到商铺负责人编号
	 * @param shopId
	 * @return
	 * @throws Exception
	 */
	Long getUserIdByShopId(Long shopId)throws Exception;
	
	/** 
	 * 根据shopId查找shop的基本信息
	 * @Title: getShopEssentialInfoById 
	 * @Description: TODO
	 * @param @param shopId
	 * @param @return
	 * @return ShopDto    返回类型 
	 * @throws 
	 */
	ShopDto getShopEssentialInfoById(Long shopId)throws Exception;
	/**
	 * 获取通用预定设置费用接口
	 * @param shopId
	 * @param pSize
	 * @param pNo
	 * @return
	 * @throws Exception
	 */
	PageModel getShopBooktFeeSetting(Long shopId,Integer pSize,Integer pNo) throws Exception;
	
	/**
	 * 赠送保证金金额
	* @Title: giveShopDeposit 
	* @param @param shopId
	* @param @param depositAmount
	* @param @throws Exception
	* @return void    返回类型 
	* @throws
	 */
	void giveShopDeposit(Long shopId,Double depositAmount)throws Exception;
	/**
	 * 查询商铺账户基本信息
	 * @return
	 * @throws Exception
	 */
	Map<String, Object> getShopAccountMoney(Map<String, Object> parms) throws Exception;
	/**
	 * 查询商铺账单列表
	 * @param map
	 * @return
	 * @throws Exception
	 */
	public PageModel getShopBill(Map<String, Object> map) throws Exception;
	/**
	 * 查询商铺奖励列表
	 * @param map
	 * @return
	 * @throws Exception
	 */
	public PageModel getShopAward(Map<String, Object> map) throws Exception;
	/**
	 * 获取商铺所有奖励
	 * @param map
	 * @return
	 * @throws Exception
	 */
	public Map<String, Object> getShopAwardTotal(Map<String, Object> map) throws Exception;
	/**
	 * 查询商铺支付密码
	 * @param shopId
	 * @return
	 * @throws Exception
	 */
	public String getShopPasswordById(Long shopId) throws Exception;
	
	/**
	 * 查询商铺订单数接口
	 * @param map
	 * @return
	 * @throws Exception
	 */
	public Map<String, Object> queryShopOrderCount(Map<String, Object> map)
			throws Exception;
	/**
	 * 查询图片logo
	 * @param map
	 * @return
	 * @throws Exception
	 */
	public PageModel getBizLogo(Map<String, Object> map)
			throws Exception;


	/**
	 * 获取场地类的预定资源列表
	 * @param paramMap
	 * @return
	 */
	List<PlaceGoodsDto> getPlaceGoods(Map<String, Object> paramMap) throws Exception;
	
	/**
	 * 获取商铺接单时间标志
	 * @param shopId
	 * @return
	 * @throws Exception
	 */
	Integer getShopConfirmMinute(Long shopId) throws Exception; 
	
	/**
	 * 获取指定商铺预约上门服务标志
	 * @param shopId
	 * @return
	 * @throws Exception
	 */
	Integer getIsHomeService(Long shopId) throws Exception; 
	
	/**
	 * 获取指定商铺预约到店服务标志
	 * @param shopId
	 * @return
	 * @throws Exception
	 */
	Integer getBookFlag(Long shopId) throws Exception; 
	/**
	 * 4.2.16MS16：店铺预约开关设置接口
	 * 
	 * @Function: com.idcq.appserver.service.shop.IShopServcie.bookSwitch
	 * @Description:
	 *
	 * @param mapParams
	 * @return
	 * @throws Exception
	 *
	 * @version:v1.0
	 * @author:ChenYongxin
	 * @date:2015年8月17日 上午9:57:29
	 *
	 * Modification History:
	 * Date         Author      Version     Description
	 * -----------------------------------------------------------------
	 * 2015年8月17日    ChenYongxin      v1.0.0         create
	 */
	public Integer bookSwitch(Map<String, Object> mapParams) throws Exception;
	/**
	 * 商品账务统计查询
	 * 
	 * @Function: com.idcq.appserver.service.shop.IShopServcie.getAccountingStat
	 * @Description:
	 *
	 * @param map
	 * @return
	 * @throws Exception
	 *
	 * @version:v1.0
	 * @author:ChenYongxin
	 * @date:2015年9月21日 下午1:46:43
	 *
	 * Modification History:
	 * Date         Author      Version     Description
	 * -----------------------------------------------------------------
	 * 2015年9月21日    ChenYongxin      v1.0.0         create
	 */
	public PageModel getAccountingStat(Map<String, Object> map) throws Exception;
	/**
	 * 收银机-获取订单列表
	 * 
	 * @Function: com.idcq.appserver.service.shop.IShopServcie.getOrderList
	 * @Description:
	 *
	 * @param map
	 * @return
	 * @throws Exception
	 *
	 * @version:v1.0
	 * @author:ChenYongxin
	 * @date:2015年9月21日 下午3:18:02
	 *
	 * Modification History:
	 * Date         Author      Version     Description
	 * -----------------------------------------------------------------
	 * 2015年9月21日    ChenYongxin      v1.0.0         create
	 */
	public PageModel getOrderList(Map<String, Object> map) throws Exception;
	/**
	 * 获取商铺雇员
	 * 
	 * @Function: com.idcq.appserver.service.shop.IShopServcie.queryShopServerList
	 * @Description:
	 *
	 * @param map
	 * @return
	 * @throws Exception
	 *
	 * @version:v1.0
	 * @author:ChenYongxin
	 * @date:2015年9月21日 下午5:50:34
	 *
	 * Modification History:
	 * Date         Author      Version     Description
	 * -----------------------------------------------------------------
	 * 2015年9月21日    ChenYongxin      v1.0.0         create
	 */
	public List<Map<String, Object>> queryShopServerList(Map<String, Object> map) throws Exception;
	/**
	 * 操作员工信息
	 * 
	 * @Function: com.idcq.appserver.service.shop.IShopServcie.opeShopEmployeeInfo
	 * @Description:
	 *
	 * @param shopEmployee
	 * @throws Exception
	 *
	 * @version:v1.0
	 * @author:ChenYongxin
	 * @date:2015年9月22日 上午10:11:07
	 *
	 * Modification History:
	 * Date         Author      Version     Description
	 * -----------------------------------------------------------------
	 * 2015年9月22日    ChenYongxin      v1.0.0         create
	 */
	Map opeShopEmployeeInfo(ShopEmployeeDto shopEmployee) throws Exception;
	
	 /**
	  * 后台收银接口业务处理方法，主要处理下单、支付、结算
	  * 
	  * @Function: com.idcq.appserver.service.order.IOrderServcie.handleShopBackOrder
	  * @Description:
	  *
	  * @param order 订单对象
	  * @param payType 支付方式
	  * @param veriCode 验证码
	  * @param isUseRedPacket 是否使用红包1=使用，0=不使用
	  * @return
	  * @throws Exception
	  *
	  * @version:v1.0
	  * @author:shengzhipeng
	  * @date:2015年10月10日 下午1:41:55
	  *
	  * Modification History:
	  * Date            Author       Version     Description
	  * -----------------------------------------------------------------
	  * 2015年10月10日    shengzhipeng       v1.0.0         create
	  */
	 Map handleShopBackOrder(OrderDto order, String payType, String veriCode, String isUseRedPacket) throws Exception;
	 
	 /**
	  * 验证短信支付验证码处理方法，如果验证码通过余额充足返回正常，如果余额不足需要返回异常
	  * 
	  * @Function: com.idcq.appserver.service.shop.IShopServcie.checkSmsPayVeriCode
	  * @Description:
	  *
	  * @param shopId 店铺id
	  * @param mobile 手机号码
	  * @param veriCode 验证码
	  * @param usage 短信模板 可为空
	  * @param settlePrice 结算价格
	  * @param username 管理员名称 
	  * @throws Exception
	  *
	  * @version:v1.0
	  * @author:shengzhipeng
	  * @date:2015年10月10日 下午4:10:36
	  *
	  * Modification History:
	  * Date            Author       Version     Description
	  * -----------------------------------------------------------------
	  * 2015年10月10日    shengzhipeng       v1.0.0         create
	  */
	 void checkSmsPayVeriCode(Long shopId, String mobile, String veriCode, String usage, Double settlePrice, String username)throws Exception;
	 /**
	  * 校验是否为会员
	  * 
	  * @Function: com.idcq.appserver.service.shop.IShopServcie.checkMember
	  * @Description:
	  *
	  * @param shopId
	  * @param mobile 手机号码
	  * @param username
	  * @throws Exception
	  *
	  * @version:v1.0
	  * @author:shengzhipeng
	  * @date:2015年10月10日 下午4:47:10
	  *
	  * Modification History:
	  * Date            Author       Version     Description
	  * -----------------------------------------------------------------
	  * 2015年10月10日    shengzhipeng       v1.0.0         create
	  */
	 void checkMember(Long shopId, String mobile, String username)throws Exception;
	 
	 /**
	  * 支付宝扫码预下单
	  * @Title: preOrderForScanCode 
	  * @param @param orderDto
	  * @param @throws Exception
	  * @return void    返回类型 
	  * @throws
	  */
	 public void preOrderForScanCode(OrderDto orderDto)throws Exception;
	 
	 /**
	  * 获取一点管家的账务流水
	  * @Title: getIdgjShopBill 
	  * @param @param map
	  * @param @return
	  * @param @throws Exception
	  * @return PageModel    返回类型 
	  * @throws
	  */
	 public PageModel getIdgjShopBill(Map<String, Object> map) throws Exception;
	 
	 
	 /**
	  * 一点管家账务统计
	  * @Title: getIdcqBillStatistics 
	  * @param @param shopId
	  * @param @param startTime
	  * @param @param endTime
	  * @param @return
	  * @return PageModel    返回类型 
	  * @throws
	  */
	 Map<String,Object> getIdcqBillStatistics(Map<String,Object>params)throws Exception;


	 /**
	  * CS19：查询店铺账户账单统计接口
	  * @param map
	  * @return
	  */
	List<Map<String, Object>> getBillStat(Map<String, Object> map) throws Exception;

	/** 
	 * CS20：查询店铺账户账单明细接口
	 * @param map
	 * @return
	 */
	PageModel getBillDetail(Map<String, Object> map) throws Exception;
    /**
     * 商铺转充接口
     * 
     * @Function: com.idcq.appserver.service.shop.IShopServcie.fillBail
     * @Description:
     *
     * @param shopId
     * @param onlineIncomeMoney
     * @param rewardMoney
     * @return
     * @throws Exception
     *
     * @version:v1.0
     * @author:ChenYongxin
     * @date:2015年12月2日 下午2:55:51
     *
     * Modification History:
     * Date         Author      Version     Description
     * -----------------------------------------------------------------
     * 2015年12月2日    ChenYongxin      v1.0.0         create
     */
    public Map<String, Object>  fillBail(Long shopId,Double onlineIncomeMoney,Double rewardMoney) throws Exception;
	 
    double getShopRewardTotalBy(Map<String, Object> map) throws Exception;
    
    PageModel getPromotionShop(Long cityId, String queryDate, int pageNo, int pageSize)throws Exception;
    
    Map getPromotionShopDetail(Long shopId, String actCode)throws Exception;
    
    
    /**
     * 微信扫码支付预下单
     * @Title: preOrderForWxScanCode 
     * @param @param orderDto
     * @param @throws Exception
     * @return void    返回类型 
     * @throws
     */
    public void preOrderForWxScanCode(OrderDto orderDto)throws Exception;
    /**
     * 查询商铺商品销售统计接口
     * @return
     * @throws Exception
     */
    PageModel getGoodsSalesStat(Map<String, Object> pMap) throws Exception;
    
    /**
     * 查询商铺分类商品销售统计接口
     * @return
     * @throws Exception
     */
    PageModel getGoodsCategorySalesStat(Map<String, Object> pMap) throws Exception;
	/**
	 * 获取商铺营收统计详情
	 * 
	 */
    PageModel shopIncomeStatDetail(Map<String, Object> map) throws Exception;
	/**
	 * 获取商铺营收统计详情
	 * 
	 */
    PageModel shopDayIncomeStatDetail(Map<String, Object> map) throws Exception;
    /**
     * 获取运营商发展的商铺列表接口
     * @return
     * @throws Exception
     */
    PageModel getOperateShopList(Map<String, Object> pMap) throws Exception;
    
    Map<String, Object> getOperateShopStat(Map<String, Object> requestMap) throws Exception;

	Map getTotalMember(Long shopId, String startTime, String endTime);
    
    Map<String, Object> getShopResource(Long shopId, Long resourceId)throws Exception;

	PageModel getShopRefMembers(Map<String, Object> map);


	PageModel getShopSalestatistics(Long shopId, Integer cashierId,
			Integer queryMode, String memberTypeStr, String startTime, String endTime, Integer pNo,Integer pSize);

	boolean verifyCardNo(Map<String, Object> map);

	Map getUserInfoByUserId(Long userId);
    

	/**
	 * 获取已开通城市
	 * 
	 * @Function: com.idcq.appserver.service.shop.IShopServcie.getOpenedCitis
	 * @Description:
	 *
	 * @param pMap
	 * @return
	 * @throws Exception
	 *
	 * @version:v1.0
	 * @author:ChenYongxin
	 * @date:2016年3月2日 下午1:52:28
	 *
	 * Modification History:
	 * Date         Author      Version     Description
	 * -----------------------------------------------------------------
	 * 2016年3月2日    ChenYongxin      v1.0.0         create
	 */
    public PageModel getOpenedCitis(Map<String, Object> pMap) throws Exception;

    /**
     * 查询商铺列表信息
     * @param params
     * @return
     * @throws Exception
     */
	List<ShopDto> getShopList(Map<String, Object> params) throws Exception;    
	
    /**
     * 查询商铺列表信息总数
     * @param params
     * @return
     * @throws Exception
     */
	Integer getShopListCount(Map<String, Object> params) throws Exception;

	Map<String, Object> getShopMemberStatisticsInfo(Map<String, Object> map) throws Exception;
	
	/**
	 * 获取提现校准信息接口
	 * @param withdrawId 提现id
	 * @return
	 * <p>withdrawId	int		是	提现id<p>
	 * <p>standardMoney	double		是	基准余额<p>
	 * <p>incomeTotalMoney	double		是	收入总和<p>
	 * <p>withdrawTotalMoney	double		是	提现总额<p>
	 * <p>withdrawMaxMoney	double		是	可提现余额<p>
	 * <p>auditResult	int		是	审核结果。成功：1，失败：0<p>
	 * <p> withdrawTime	datetime		是	提现时间<p>
	 * @throws Exception
	 */
	Map<String, Object> getShopAuditInfo(Long withdrawId) throws Exception;
	
	/**
	 * 根据店铺的管理者ID 获取商铺
	 */
	public ShopDto getShopByPrincipalId(Long principalId) throws Exception;

	UserDto getShopPrincipalInfoByShopId(Long shopId);
	
	/**
	 * 获取总店所属分店列表
	 * @param shopId
	 * @return
	 * @throws Exception
	 */
	List<ShopDto> getShopListByHeadShopId(Long shopId)throws Exception;
	
	Map getBillDetailById(Long billId, Long shopId) throws Exception;

	PageModel shopIncomeStatByTimePeriod(Map<String, Object> searchParams);


}
