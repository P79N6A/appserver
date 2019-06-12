package com.idcq.appserver.dao.shop;

import java.util.Date;
import java.util.List;
import java.util.Map;

import com.idcq.appserver.dto.common.PageModel;
import com.idcq.appserver.dto.shop.ShopDto;
import com.idcq.appserver.dto.shop.ShopEmployeeDto;
import com.idcq.appserver.dto.shop.ShopIncomeStatDto;
import com.idcq.idianmgr.dto.shop.ShopBean;

public interface IShopDao {
	
	/**
	 * 调用存储过程定时统计商铺销售数量
	 * 
	 * @param startTime
	 * @param endTime
	 * @return
	 * @throws Exception
	 */
	int statisShopSoldExecute(Date startTime,Date endTime) throws Exception;
	public ShopDto getShopFromDbById(Long primaryKeyId) throws Exception;
	public void updateShopPoint(ShopBean shopBean);
	 /**
     * 根据商铺ID获取正常商铺信息和扩展信息
     * 
     * @param shopId
     * @return
     * @throws Exception
     */
	ShopDto getShopExtendByIdAndStatus(Long shopId, Integer shopStatus)throws Exception;
	List<Long> getIdListByPrincipalId(Long principalId) throws Exception;
	/**
	 * 获取商铺列表
	 * 
	 * @param Shop
	 * @return
	 * @throws Exception
	 */
	List<ShopDto> getList(ShopDto shop,int page,int pageSize) throws Exception ;
	
	/**
	 * 搜索店铺
	 * 
	 * @param param
	 * @return
	 * @throws Exception
	 */
	List<ShopDto> searchShop(Map param) throws Exception ;
	/**
	 * @param paramshopId	int		是	商铺ID
	 * @param paramstartTime	Date		否	起始时间，不填为本月第一天
	 * @param paramendTime	Date		否	结束时间，不填为当前天
	 * @param paramsortBy	string		否	排序字段。1销售金额、2销售笔数
	 * @param paramorderBy	int		否	-1降序，1升序
	 * 
	 * @Function: com.idcq.appserver.dao.shop.IShopDao.getSalerPerformanceList
	 * @Description:
	 *
	 * @return
	 * @throws Exception
	 *
	 * @version:v1.0
	 * @author:ChenYongxin
	 * @date:2016年9月22日 下午2:38:19
	 *
	 * Modification History:
	 * Date         Author      Version     Description
	 * -----------------------------------------------------------------
	 * 2016年9月22日    ChenYongxin      v1.0.0         create
	 */
	List<Map<String, Object>> getSalerPerformanceList(Map<String, Object> param) throws Exception;
	
	int getSalerPerformanceCount(Map<String, Object> param) throws Exception;

	
	Integer searchShopTotal(Map param) throws Exception;
	
	/**
	 * 获取指定商铺的经纬度信息
	 * @param shopId
	 * @return
	 * @throws Exception
	 */
	Map getShopXyById(Long shopId) throws Exception;
	/**
	 * 根据shopId查询商铺数量
	 * @return
	 */
	int getCountByShopId(Long shopId);
	int getShopPoint(Long shopId);
	
	/**
	 * 获取商铺的会员折扣
	 * 
	 * @param shopId
	 * @return
	 * @throws Exception
	 */
	Double getMemberDiscount(Long shopId) throws Exception;
	
	/**
	 * 根据id列表批量查询商铺信息
	 * @param idList
	 * @return
	 */
	List<ShopDto>getListByShopIds(List<Long>idList);
	
	/**
	 * 分页查找
	 * @param pageModel
	 * @param shopDto
	 * @return
	 */
	public PageModel getShopPageModel(PageModel pageModel,ShopDto shopDto,int... queryTotalCount);
	
	/**
	 * 更新商铺点赞次数，每执行一次加1
	 * @param shopId
	 * @return
	 */
	int updateShopZan (Long shopId) throws Exception;
	
	/**
	 * 根据商铺id查询商铺点赞数
	 * @param shopId
	 * @return
	 * @throws Exception
	 */
	Integer getShopZanById(Long shopId) throws Exception;
	
	/**
	 * 根据商铺编号查找商铺负责人
	 * @param shopId
	 * @return
	 */
	Long getUserIdByShopIed(Long shopId);

	/**
	 * 验证正常的商铺存在性
	 * 
	 * @param shopId
	 * @return
	 * @throws Exception
	 */
	int queryNormalShopExists(Long shopId) throws Exception;
	
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
	 * 
	* @Title: callTemporyIndexProcedure 
	* @Description: TODO(这里用一句话描述这个方法的作用) 
	* @param @param lastUpdateTime
	* @param @throws Exception
	* @return void    返回类型 
	* @throws
	 */
	public void callTemporyIndexProcedure(String lastUpdateTime)throws Exception;
	
	/**
	 * 增加商铺的下订单次数
	 * 
	 * @param shopId
	 * @param soldNum
	 * @return
	 * @throws Exception
	 */
	int incrShopSoldNum(Long shopId ,Integer soldNum) throws Exception;
	
	
	long getShopTotalCount()throws Exception;
	
	List<ShopDto>getShopByPageAndLastUpdateTime(String lastUpdateTime,PageModel pageModel)throws Exception;
	
	/**
	 * 获取商铺需要索引的数据通过商铺关联的第三方表
	* @Title: getShopMarketInfoByParam 
	* @Description: TODO
	* @param @param lastUpdateTime
	* @param @param pageModel
	* @param @return
	* @param @throws Exception
	* @return List<ShopDto>    返回类型 
	* @throws
	 */
	List<ShopDto>getShopMarketInfoByParam(String lastUpdateTime,PageModel pageModel)throws  Exception;
	
	
	ShopDto getShopEssentialInfo(Long shopId)throws Exception;
	
	/**
	 * 获取正常的商铺信息
	 * @param shopId
	 * @return
	 * @throws Exception
	 */
	ShopDto getNormalShopById(Long shopId)throws Exception;
	
	/**
	 * 获取商铺信息
	 * <p>不过滤商铺状态
	 * @param shopId
	 * @return
	 * @throws Exception
	 */
	ShopDto getShopById(Long shopId)throws Exception;
	
	/**
	 * 查询商铺信息，如果存在，则将商铺状态返回
	 * @param shopId
	 * @return
	 * @throws Exception
	 */
	Map queryShopStatus(Long shopId)throws Exception;
	/**
	 * 更新商铺评分
	 * @param shopDto
	 * @return
	 */
	Integer updateShopGrade(ShopDto shopDto) throws Exception;
	/**
	 * 根据商铺状态过滤查询商铺id集合
	 * @param shopDto
	 * @return
	 */
	List<Long> getIdListByStatus(Integer limit,Integer pSize,Integer shopStatus)throws Exception;
	
	/**
	 * 根据商铺编号查询商铺名称
	 * @param shopId
	 * @return
	 */
	String getShopNameById(Long shopId);
	
	/**
	 * 更新商铺状态
	 * @param shopDto
	 * @return
	 * @throws Exception
	 */
	Integer updateShopStatus(ShopDto shopDto) throws Exception;
	/**
	 * 获取少量商铺基本信息
	 * @param minPage
	 * @param maxPage
	 * @return
	 */
	public List<Map<String, Object>> getBaseShopList(Integer minPage, Integer maxPage) throws Exception;
	
	/**
	 * 根据店铺的管理者ID 获取商铺
	 */
	public ShopDto getShopByPrincipalId(Long principalId) throws Exception;
	
	
	Double getDepositByPrincipalId(Long principalId) throws Exception;
	/**
	 * 查询商铺账务统计
	 * 
	 * @Function: com.idcq.appserver.dao.shop.IShopDao.getAccountingStat
	 * @Description:
	 *
	 * @param principalId
	 * @return
	 * @throws Exception
	 *
	 * @version:v1.0
	 * @author:ChenYongxin
	 * @date:2015年9月21日 上午11:33:37
	 *
	 * Modification History:
	 * Date         Author      Version     Description
	 * -----------------------------------------------------------------
	 * 2015年9月21日    ChenYongxin      v1.0.0         create
	 */
	List<Map<String, Object>> getAccountingStat(Map<String, Object> map) throws Exception;
	/**
	 * 获取财务统计账号总数
	 * 
	 * @Function: com.idcq.appserver.dao.shop.IShopDao.getAccountingStatCount
	 * @Description:
	 *
	 * @param map
	 * @return
	 * @throws Exception
	 *
	 * @version:v1.0
	 * @author:ChenYongxin
	 * @date:2015年9月21日 下午1:52:47
	 *
	 * Modification History:
	 * Date         Author      Version     Description
	 * -----------------------------------------------------------------
	 * 2015年9月21日    ChenYongxin      v1.0.0         create
	 */
	List<Map<String, Object>> getOrderList(Map<String, Object> map)
			throws Exception;
	/**
	 * 获取订单列表-收银机
	 * 
	 * @Function: com.idcq.appserver.dao.shop.IShopDao.getOrderListCount
	 * @Description:
	 *
	 * @param map
	 * @return
	 * @throws Exception
	 *
	 * @version:v1.0
	 * @author:ChenYongxin
	 * @date:2015年9月21日 下午3:13:52
	 *
	 * Modification History:
	 * Date         Author      Version     Description
	 * -----------------------------------------------------------------
	 * 2015年9月21日    ChenYongxin      v1.0.0         create
	 */
	Integer getOrderListCount(Map<String, Object> map) throws Exception;
	/**
	 * 	  获取订单列表总数-收银机
	 * 
	 * @Function: com.idcq.appserver.dao.shop.IShopDao.getAccountingStatCount
	 * @Description:
	 *
	 * @param map
	 * @return
	 * @throws Exception
	 *
	 * @version:v1.0
	 * @author:ChenYongxin
	 * @date:2015年9月21日 下午3:14:17
	 *
	 * Modification History:
	 * Date         Author      Version     Description
	 * -----------------------------------------------------------------
	 * 2015年9月21日    ChenYongxin      v1.0.0         create
	 */
	public Integer getAccountingStatCount(Map<String, Object> map)
			throws Exception;
	
	/**
	 * 赠送商铺保证金
	* @Title: giveShopDeposit 
	* @param @param shopId
	* @param @param depositAmount
	* @return void    返回类型 
	* @throws
	 */
	void giveShopDeposit(Long shopId, Double depositAmount);
	/**
	 * 根据shopid查询商铺保障金
	 * @param shopId
	 * @return
	 * @throws Exception
	 */
	Double getDepositByShopId(Long shopId) throws Exception;
	/**
	 * 查询商铺支付密码
	 * @param shopId
	 * @return
	 * @throws Exception
	 */
	String getShopPasswordById(Long shopId) throws Exception;
	
	/**
	 * 查询订单状态各种类型数量
	 * @Title: getShopOrderCount 
	 * @param @param map
	 * @param @return
	 * @param @throws Exception
	 * @return Map<String, Object>   返回类型 
	 * @throws
	 */
	Map<String, Object> shopOrderCount(Map<String, Object> map)throws Exception;
	/**
	 * 查询订单各种支付类型的金额总数
	 * @Title: getOrderAmountCount 
	 * @param @param map
	 * @param @return
	 * @param @throws Exception
	 * @return Map<String, Object>   返回类型 
	 * @throws
	 */
	Map<String, Object> getOrderAmountCount(Map<String, Object> map)throws Exception;
	/**
	 * 查询图片logo
	 * @param map
	 * @return
	 * @throws Exception
	 */
	List<Map<String, Object>> getBizLogo(Map<String, Object> map)throws Exception;
	/**
	 * 查询图片logo总数
	 * @param map
	 * @return
	 * @throws Exception
	 */
	Integer getBizLogoCount(Map<String, Object> map)throws Exception;
	
	/**
	 * 查询场地类预定资源列表
	 * @param paramMap
	 * @return
	 * @throws Exception
	 */
	List<Map<String, Object>> getPlaceGoods(Map<String, Object> paramMap) throws Exception;

	/**
	 * 判断资源是否被占
	 * @param paramMap
	 * @return
	 */
	boolean isUsedResource(Map<String, Object> paramMap) throws Exception;
	
	/**
	 * 根据shopId或者userId获取店铺列表
	 * 
	 * @Function: com.idcq.appserver.dao.shop.IShopDao.queryNormalShopListBy
	 * @Description:
	 *
	 * @param bizId   店铺id或者用户id
	 * @param bizType id的类型 1表示店铺 2表示用户
	 * @return
	 * @throws Exception
	 *
	 * @version:v1.0
	 * @author:shengzhipeng
	 * @date:2015年8月3日 下午2:24:41
	 *
	 * Modification History:
	 * Date            Author       Version     Description
	 * -----------------------------------------------------------------
	 * 2015年8月3日    shengzhipeng       v1.0.0         create
	 */
	List<Map> queryNormalShopListBy(Long bizId, Integer bizType) throws Exception;
	
	/**
	 * 根据用户名和店铺id查询雇员信息
	 * 
	 * @Function: com.idcq.appserver.dao.shop.IShopDao.queryShopEmployee
	 * @Description:
	 *
	 * @param mobile 雇员用户名 
	 * @param shopId 店铺id，当为空不指定店铺
	 * @return
	 * @throws Exception
	 *
	 * @version:v1.0
	 * @author:shengzhipeng
	 * @date:2015年10月10日 下午3:32:30
	 *
	 * Modification History:
	 * Date            Author       Version     Description
	 * -----------------------------------------------------------------
	 * 2015年10月10日    shengzhipeng       v1.0.0         create
	 */
	Map queryShopEmployee(String mobile, Long shopId)throws Exception;
	/**
	 * 查询状态为3,5的订单的总记录数，用于后台统计总订单数
	 * 
	 * @Function: com.idcq.appserver.dao.shop.IShopDao.getOrderTotalCount
	 * @Description:
	 *
	 * @param paramMap
	 * @return
	 * @throws Exception
	 *
	 * @version:v1.0
	 * @author:ChenYongxin
	 * @date:2015年8月4日 下午4:31:10
	 *
	 * Modification History:
	 * Date         Author      Version     Description
	 * -----------------------------------------------------------------
	 * 2015年8月4日    ChenYongxin      v1.0.0         create
	 */
	Integer getOrderTotalCount(Map<String, Object> paramMap)throws Exception;
	/**
	 * 根据shopid获取店老板userid
	 * 
	 * @Function: com.idcq.appserver.dao.shop.IShopDao.getUserByShopId
	 * @Description:
	 *
	 * @param shopId
	 * @return
	 *
	 * @version:v1.0
	 * @author:ChenYongxin
	 * @date:2015年8月6日 上午11:14:15
	 *
	 * Modification History:
	 * Date         Author      Version     Description
	 * -----------------------------------------------------------------
	 * 2015年8月6日    ChenYongxin      v1.0.0         create
	 */
	Long getUserByShopId(Long shopId)throws Exception;
	/**
	 * 4.2.16MS16：店铺预约开关设置接口
	 * 
	 * @Function: com.idcq.appserver.dao.shop.IShopDao.bookSwitch
	 * @Description:
	 *
	 * @param map
	 * @return
	 * @throws Exception
	 *
	 * @version:v1.0
	 * @author:ChenYongxin
	 * @date:2015年8月17日 上午9:55:27
	 *
	 * Modification History:
	 * Date         Author      Version     Description
	 * -----------------------------------------------------------------
	 * 2015年8月17日    ChenYongxin      v1.0.0         create
	 */
	Integer bookSwitch(Map<String, Object> map)throws Exception;
	
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
	 * 获取外卖标志
	 * @param shopId
	 * @return
	 * @throws Exception
	 */
	Integer getTakeoutFlag(Long shopId) throws Exception;
	/**
	 * 查询商铺服务人员信息
	 * 
	 * @Function: com.idcq.appserver.dao.shop.IShopDao.queryShopServerList
	 * @Description:
	 *
	 * @return
	 * @throws Exception
	 *
	 * @version:v1.0
	 * @author:ChenYongxin
	 * @date:2015年9月21日 下午5:43:23
	 *
	 * Modification History:
	 * Date         Author      Version     Description
	 * -----------------------------------------------------------------
	 * 2015年9月21日    ChenYongxin      v1.0.0         create
	 */
	List<Map<String, Object>> queryShopServerList(Map<String, Object> map) throws Exception;
	/**
	 * 增加员工信息
	 * 
	 * @Function: com.idcq.appserver.dao.shop.IShopDao.insertEmployee
	 * @Description:
	 *
	 * @param shopEmployeeDto
	 * @return
	 * @throws Exception
	 *
	 * @version:v1.0
	 * @author:ChenYongxin
	 * @date:2015年9月22日 上午9:53:57
	 *
	 * Modification History:
	 * Date         Author      Version     Description
	 * -----------------------------------------------------------------
	 * 2015年9月22日    ChenYongxin      v1.0.0         create
	 */
	Integer insertEmployee(ShopEmployeeDto shopEmployeeDto)throws Exception;
	/**
	 * 更新员工信息
	 * 
	 * @Function: com.idcq.appserver.dao.shop.IShopDao.updateEmployee
	 * @Description:
	 *
	 * @param shopEmployeeDto
	 * @return
	 * @throws Exception
	 *
	 * @version:v1.0
	 * @author:ChenYongxin
	 * @date:2015年9月22日 上午9:54:11
	 *
	 * Modification History:
	 * Date         Author      Version     Description
	 * -----------------------------------------------------------------
	 * 2015年9月22日    ChenYongxin      v1.0.0         create
	 */
	Integer updateEmployee(ShopEmployeeDto shopEmployeeDto)throws Exception;
	/**
	 * 删除员工信息
	 * 
	 * @Function: com.idcq.appserver.dao.shop.IShopDao.deleteEmployee
	 * @Description:
	 *
	 * @param shopEmployeeDto
	 * @return
	 * @throws Exception
	 *
	 * @version:v1.0
	 * @author:ChenYongxin
	 * @date:2015年9月22日 上午9:54:23
	 *
	 * Modification History:
	 * Date         Author      Version     Description
	 * -----------------------------------------------------------------
	 * 2015年9月22日    ChenYongxin      v1.0.0         create
	 */
	Integer deleteEmployee(ShopEmployeeDto shopEmployeeDto)throws Exception;
	/**
	 * 根据shopid和编号查询员工数
	 * 
	 * @Function: com.idcq.appserver.dao.shop.IShopDao.queryEmployeeByCodeAndShopId
	 * @Description:
	 *
	 * @param shopEmployeeDto
	 *
	 * @version:v1.0
	 * @author:ChenYongxin
	 * @date:2015年9月22日 上午10:31:06
	 *
	 * Modification History:
	 * Date         Author      Version     Description
	 * -----------------------------------------------------------------
	 * 2015年9月22日    ChenYongxin      v1.0.0         create
	 */
	Long queryEmployeeByCodeAndShopId(ShopEmployeeDto shopEmployeeDto)throws Exception;
	/**
	 * 插入商铺账务统计信息
	 * 
	 * @Function: com.idcq.appserver.dao.shop.IShopDao.insertAccountingStat
	 * @Description:
	 *
	 * @param shopIncomeStat
	 * @return
	 * @throws Exception
	 *
	 * @version:v1.0
	 * @author:ChenYongxin
	 * @date:2015年9月22日 下午3:24:13
	 *
	 * Modification History:
	 * Date         Author      Version     Description
	 * -----------------------------------------------------------------
	 * 2015年9月22日    ChenYongxin      v1.0.0         create
	 */
	Integer insertAccountingStat(ShopIncomeStatDto shopIncomeStat)throws Exception;

	Long getVantagesBy(Long shopId, Long userId) throws Exception;
	
	/**
	 * 新增商铺
	 * @param shopDto
	 * @throws Exception
	 */
	void saveShop(ShopDto shopDto) throws Exception;

	/**
	 * 更新商铺
	 * @param shopBean
	 */
	void updateShop(ShopBean shopBean);

	/**
	 * 新增商铺账户
	 * @param shopAccount
	 */
	void saveShopAccount(Map<String, Object> shopAccount) throws Exception;

	/**
	 * 修改商铺支付密码
	 * @param shopId
	 * @param payPwd
	 * @throws Exception
	 */
	void updateShopPayPwd(Long shopId, String payPwd) throws Exception;

	/**
	 * 查询商铺相关信息
	 * @param shopId
	 * @return
	 * @throws Exception
	 */
	Map<String, Object> getPayPassWordById(Long shopId) throws Exception;

	/**
	 * 查询会员是否店铺服务人员
	 * @param userId
	 * @return
	 * @throws Exception
	 */
	Integer queryUserIsShopServer(Long userId) throws Exception;
	
	List<Map> getPromotionShop(Long cityId, String queryDate, int pageNo, int pageSize) throws Exception;
	
	int getPromotionShopCount(Long cityId, String queryDate) throws Exception;
	
	Map getPromotionShopDetail(Long shopId, String actCode) throws Exception;
    /**
     * 获取运营商发展的商铺列表接口
     * @return
     * @throws Exception
     */
    List<Map<String, Object>> getOperateShopList(Map<String, Object> pMap) throws Exception;
    /**
     * 获取运营商发展的商铺总数
     * @return
     * @throws Exception
     */
    Integer getOperateShopCount(Map<String, Object> pMap) throws Exception;
    
    Integer getOperateShopNumOfAgent(Map<String, Object> pMap) throws Exception;
    
    Integer getOperateDeviceShopNumOfAgent(Map<String, Object> pMap) throws Exception;
    
    Map<String, Object> getShopResource(Long shopId, Long resourceId)throws Exception;

	Map getTotalMember(Long shopId, String startTime, String endTime);

	Integer getShopRefTotalMembers(Map<String, Object> map);

	List<Map<String, Object>> getShopRefMembers(Map<String, Object> map);
	/**
	 * 获取已开通城市列表接口
	 * 
	 * @Function: com.idcq.appserver.dao.shop.IShopDao.getOpenedCitis
	 * @Description:
	 *
	 * @param map
	 * @return
	 * @throws Exception
	 *
	 * @version:v1.0
	 * @author:ChenYongxin
	 * @date:2016年3月2日 上午11:31:52
	 *
	 * Modification History:
	 * Date         Author      Version     Description
	 * -----------------------------------------------------------------
	 * 2016年3月2日    ChenYongxin      v1.0.0         create
	 */
	List<Map<String, Object>> getOpenedCitis(Map<String, Object> pMap)throws Exception;
    /**
     * 获取已开通城市总数
     * 
     * @Function: com.idcq.appserver.dao.shop.IShopDao.getOpenedCitis
     * @Description:
     *
     * @param map
     * @return
     * @throws Exception
     *
     * @version:v1.0
     * @author:ChenYongxin
     * @date:2016年3月2日 上午11:31:52
     *
     * Modification History:
     * Date         Author      Version     Description
     * -----------------------------------------------------------------
     * 2016年3月2日    ChenYongxin      v1.0.0         create
     */
    Integer getOpenedCitisCount(Map<String, Object> pMap)throws Exception;
	/**
	 * PCS41：获取商铺日营收
	 * @param shopId
	 * @return
	 * @throws Exception
	 */
	List<Map<String, Object>> shopIncomeStatDetail(Map<String, Object> map) throws Exception;
	/**
	 * PCS41：获取商铺日营收数
	 * @param shopId
	 * @return
	 * @throws Exception
	 */
	Integer shopDayIncomeStatDetailCount(Map<String, Object> pMap)throws Exception;
	/**
	 * PCS41：获取商铺日营收
	 */
	List<Map<String, Object>> shopDayIncomeStatDetail(Map<String, Object> map) throws Exception;
	/**
	 * 获取商铺营收统计数量
	 * @param shopId
	 * @return
	 * @throws Exception
	 */
	Integer shopIncomeStatDetailCount(Map<String, Object> map) throws Exception;


	
	/**
	 * 获取总店所属分店列表
	 * @param shopId
	 * @return
	 * @throws Exception
	 */
	List<ShopDto> getShopListByHeadShopId(Long shopId)throws Exception;

	List<Map<String, Object>> getShopSalestatisticsByCashier(Map<String, Object> map);
	
	List<Map<String, Object>> getShopSalestatisticsByPayWay(Map<String, Object> map);
	
	List<Map<String, Object>> getShopSalestatisticsByCashierId(Map<String, Object> map);

	int verifyCardNo(Map<String, Object> map);

	Map getUserInfoByUserId(Long userId);
	
	void batchUpdateShopMarketing(List<ShopDto>shopDtoList)throws Exception;

	List<ShopDto> getShopListByParams(Map<String, Object> params)throws Exception;

	Integer getShopListByParamsCount(Map<String, Object> params)throws Exception;

	int getShopByShopIdAndShopName(Map<String, Object> map);

	List<Map<String, Object>> getShopSalestatisticsByIsMember(
			Map<String, Object> map);
	
	ShopDto getShopByIdWithoutCache(Long shopId);

	List<Map<String, String>> queryShopEmployeeByMap(Map<String, String> requestMap);

	int getShopCountByMap(Map<String, String> requestMap);

	int updateEmployeePwd(Map<String, Object> map);

	List<Map<String, Object>> getAllShopEmployeeByMap(Map<String, Object> paramMap);

	List<Map<String, Object>> getAllShopBossesByMap(Map<String, Object> paramMap);

	List<ShopEmployeeDto> queryEmployeeListByMap(Map<String, String> param);

	Long updateEmployeeByDto(ShopEmployeeDto shopEmployeeDto);
	
	public Map<String, Object> getAttachmentInfo(Map<String, Object> map)
            throws Exception;
	
	public List<Map<String, Object>> getAttachmentInfoAnd(Map<String, Object> map)
	        throws Exception;
	
	public Integer deleteShopIncomeStat(String orderId)
	        throws Exception;
}
