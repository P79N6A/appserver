package com.idcq.appserver.dao.goods;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.idcq.appserver.dao.BaseDao;
import com.idcq.appserver.dto.goods.ShopTechRefGoodsDto;
/**
 * 商铺商品关联技师数据访问层
 * @ClassName: ShopTechRefGoodsDaoImp 
 * @Description: TODO
 * @author 张鹏程 
 * @date 2015年7月30日 下午6:01:31 
 *
 */
@Repository
public class ShopTechRefGoodsDaoImp extends BaseDao<ShopTechRefGoodsDto> implements IShopTechRefGoodsDao{
		
	/**
	 * 查询技师根据商品族id
	 * @Title: queryListByGoodsGroupId 
	 * @param @param groupId
	 * @param @return
	 * @param @throws Exception  
	 * @throws
	 */
	public List<ShopTechRefGoodsDto> queryListByGoodsGroupId(Long groupId)
			throws Exception {
		Map<String,Object>params=new HashMap<String,Object>();
		params.put("groupId", groupId);
		return findList(generateStatement("queryListByGoodsGroupId"),params);
	}

}
