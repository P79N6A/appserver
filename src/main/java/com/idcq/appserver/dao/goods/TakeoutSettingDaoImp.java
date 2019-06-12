package com.idcq.appserver.dao.goods;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.idcq.appserver.dao.BaseDao;
import com.idcq.appserver.dto.goods.TakeoutSettingDto;
/**
 * 外卖设置
* @ClassName: TakeoutSettingDaoImp 
* @Description: TODO
* @author 张鹏程 
* @date 2015年7月15日 上午9:03:25 
*
 */
@Repository
public class TakeoutSettingDaoImp extends BaseDao<TakeoutSettingDto> implements ITakeoutSettingDao{

	@Override
	public List<TakeoutSettingDto> findTakeoutSettingByShopList(
			List<Long> shopIdList, Integer settingType) throws Exception {
		Map<String,Object>params=new HashMap<String,Object>();
		params.put("settingType", settingType);
		params.put("shopIdList", shopIdList);
		return super.findList(generateStatement("findTakeoutSettingByShopList"),params);
	}

}
