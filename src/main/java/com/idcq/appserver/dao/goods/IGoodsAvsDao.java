package com.idcq.appserver.dao.goods;

import java.util.List;

import com.idcq.appserver.dto.goods.GoodsAvsDto;

public interface IGoodsAvsDao {
	public List<GoodsAvsDto> getGoodsAvsList(GoodsAvsDto goodsAvs);
	public int getGoodsAvsCount(GoodsAvsDto goodsAvs);

	public int queryGoodsAvsCount(GoodsAvsDto goodsAvs);
	/**
	 * 插入增值服务
	 * 
	 * @Function: com.idcq.appserver.dao.goods.IGoodsAvsDao.insertGoodsAvsDto
	 * @Description:
	 *
	 * @param goodsAvs
	 * @return
	 *
	 * @version:v1.0
	 * @author:ChenYongxin
	 * @date:2016年9月20日 下午2:30:51
	 *
	 * Modification History:
	 * Date         Author      Version     Description
	 * -----------------------------------------------------------------
	 * 2016年9月20日    ChenYongxin      v1.0.0         create
	 */
	public int insertGoodsAvsDto(GoodsAvsDto goodsAvs);
	/**
	 * 更新增值服务
	 * 
	 * @Function: com.idcq.appserver.dao.goods.IGoodsAvsDao.updateGoodsAvsDto
	 * @Description:
	 *
	 * @param goodsAvs
	 * @return
	 *
	 * @version:v1.0
	 * @author:ChenYongxin
	 * @date:2016年9月20日 下午2:31:08
	 *
	 * Modification History:
	 * Date         Author      Version     Description
	 * -----------------------------------------------------------------
	 * 2016年9月20日    ChenYongxin      v1.0.0         create
	 */
	public int updateGoodsAvsDto(GoodsAvsDto goodsAvs);
	/**
	 * 删除增值服务
	 * 
	 * @Function: com.idcq.appserver.dao.goods.IGoodsAvsDao.deleteGoodsAvs
	 * @Description:
	 *
	 * @param goodsAvsId
	 * @return
	 *
	 * @version:v1.0
	 * @author:ChenYongxin
	 * @date:2016年9月20日 下午3:20:34
	 *
	 * Modification History:
	 * Date         Author      Version     Description
	 * -----------------------------------------------------------------
	 * 2016年9月20日    ChenYongxin      v1.0.0         create
	 */
	public int deleteGoodsAvs(Long goodsAvsId);

}
