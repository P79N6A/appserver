package com.idcq.appserver.dao.redpacket;

import java.util.List;
import java.util.Map;

import com.idcq.appserver.dto.redpacket.RedPacketDto;
/**
 * 红包dao接口
 * @author Administrator
 * 
 * @date 2016年3月12日
 * @time 下午1:42:24
 */
public interface IRedPacketDao {
	
	/**
	 * 新增红包
	 * @param redPacketDto
	 * @return
	 * @throws Exception
	 */
	int addRedPacketDto(RedPacketDto redPacketDto) throws Exception;
	
	/**
	 * 删除指定的红包
	 * @param redPacketDtoId
	 * @return
	 * @throws Exception
	 */
	int delRedPacketDtoById(Long redPacketDtoId) throws Exception;
	
	/**
	 * 修改指定的红包
	 * @param redPacketDto
	 * @return
	 * @throws Exception
	 */
	int updateRedPacketDtoById(RedPacketDto redPacketDto) throws Exception;
	
	/**
	 * 获取指定的红包
	 * @param redPacketDtoId
	 * @return
	 * @throws Exception
	 */
	RedPacketDto getRedPacketDtoById(Long redPacketDtoId) throws Exception;
	/**
	 * 获取会员红包接口
	 * 
	 * @Function: com.idcq.appserver.dao.redpacket.IRedPacketDao.getMemberRedPackets
	 * @Description:
	 *
	 * @param parms
	 * @throws Exception
	 *
	 * @version:v1.0
	 * @author:ChenYongxin
	 * @date:2016年3月16日 上午10:40:49
	 *
	 * Modification History:
	 * Date         Author      Version     Description
	 * -----------------------------------------------------------------
	 * 2016年3月16日    ChenYongxin      v1.0.0         create
	 */
	List<Map<String, Object>> getMemberRedPackets(Map<String, Object> parms) throws Exception;
	/**
	 * 根据商圈id获取商圈商铺
	 * 
	 * @Function: com.idcq.appserver.dao.redpacket.IRedPacketDao.getBusinessShopsById
	 * @Description:
	 *
	 * @param parms
	 * @return
	 * @throws Exception
	 *
	 * @version:v1.0
	 * @author:ChenYongxin
	 * @date:2016年3月16日 下午6:52:15
	 *
	 * Modification History:
	 * Date         Author      Version     Description
	 * -----------------------------------------------------------------
	 * 2016年3月16日    ChenYongxin      v1.0.0         create
	 */
	List<Map<String, Object>> getBusinessShopsById(Long businessAreaActivityId) throws Exception;
	/**
	 * 获取红包详情
	 * 
	 * @Function: com.idcq.appserver.dao.redpacket.IRedPacketDao.getRedPacketDetail
	 * @Description:
	 *
	 * @return
	 * @throws Exception
	 *
	 * @version:v1.0
	 * @author:ChenYongxin
	 * @date:2016年3月16日 下午4:21:07
	 *
	 * Modification History:
	 * Date         Author      Version     Description
	 * -----------------------------------------------------------------
	 * 2016年3月16日    ChenYongxin      v1.0.0         create
	 */
	Map<String, Object> getRedPacketDetail(Long redPacketId) throws Exception;
	
    /**
     * 获取会员红包数量
     * 
     * @Function: com.idcq.appserver.dao.redpacket.IRedPacketDao.getMemberRedPackets
     * @Description:
     *
     * @param parms
     * @throws Exception
     *
     * @version:v1.0
     * @author:ChenYongxin
     * @date:2016年3月16日 上午10:40:49
     *
     * Modification History:
     * Date         Author      Version     Description
     * -----------------------------------------------------------------
     * 2016年3月16日    ChenYongxin      v1.0.0         create
     */
    Integer getMemberRedPacketsCount(Map<String, Object> parms) throws Exception;
	
	/**
	 * 获取红包列表
	 * @param redPacketDto
	 * @return
	 * @throws Exception
	 */
	List<RedPacketDto> getRedPacketDtoList(RedPacketDto redPacketDto,int pageNo,int pageSize) throws Exception;
	
	/**
	 * 批量更新红包
	 * @Title: batchUpdateRedPacket 
	 * @param @param redPacketList
	 * @param @throws Exception
	 * @return void    返回类型 
	 * @throws
	 */
	void batchUpdateRedPacket(List<RedPacketDto> redPacketList)throws Exception;
}
