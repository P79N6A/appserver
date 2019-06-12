package com.idcq.appserver.dao.redpacket;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.idcq.appserver.dao.BaseDao;
import com.idcq.appserver.dto.redpacket.RedPacketDto;

@Repository
public class RedPacketDaoImpl extends BaseDao<RedPacketDto>implements IRedPacketDao{

	@Override
	public int addRedPacketDto(RedPacketDto redPacketDto) throws Exception {
		// TODO Auto-generated method stub
		return super.insert("addRedPacketDto",redPacketDto);
	}

	@Override
	public int delRedPacketDtoById(Long redPacketDtoId) throws Exception {
		// TODO Auto-generated method stub
		return super.delete("delRedPacketDtoById", redPacketDtoId);
	}

	@Override
	public int updateRedPacketDtoById(RedPacketDto redPacketDto)
			throws Exception {
		// TODO Auto-generated method stub
		return super.update("updateRedPacketDtoById",redPacketDto);
	}

	@Override
	public RedPacketDto getRedPacketDtoById(Long redPacketDtoId)
			throws Exception {
		// TODO Auto-generated method stub
		return (RedPacketDto)super.selectOne("getRedPacketDtoById", redPacketDtoId);
	}

	@Override
	public List<RedPacketDto> getRedPacketDtoList(RedPacketDto redPacketDto,
			int pageNo, int pageSize) throws Exception {
		// TODO Auto-generated method stub
		Map<String,Object> map = new HashMap<String, Object>();
		map.put("start",(pageNo - 1) * pageSize);
		map.put("limit",pageSize);
		map.put("redPacketDto", redPacketDto);
		return super.findList("getRedPacketDtoList",map);
	}
	
	/**
	 * 批量更新红包
	 * @Title: batchUpdateRedPacket 
	 * @param @param redPacketList
	 * @param @throws Exception  
	 * @throws
	 */
	public void batchUpdateRedPacket(List<RedPacketDto> redPacketList)
			throws Exception {
		Map<String,Object>paramMap=new HashMap<String,Object>();
		paramMap.put("redPacketList", redPacketList);
		super.update(generateStatement("batchUpdateRedPacket"),paramMap);
	}

    /* (non-Javadoc)
     * @see com.idcq.appserver.dao.redpacket.IRedPacketDao#getMemberRedPackets(java.util.Map)
     */
    @Override
    public List<Map<String, Object>> getMemberRedPackets(Map<String, Object> parms) throws Exception
    {
        return (List)super.findList("getMemberRedPackets",parms);
    }

    /* (non-Javadoc)
     * @see com.idcq.appserver.dao.redpacket.IRedPacketDao#getMemberRedPacketsCount(java.util.Map)
     */
    @Override
    public Integer getMemberRedPacketsCount(Map<String, Object> parms) throws Exception
    {
        return (Integer)super.selectOne("getMemberRedPacketsCount", parms);
    }

    /* (non-Javadoc)
     * @see com.idcq.appserver.dao.redpacket.IRedPacketDao#getRedPacketDetail()
     */
    @Override
    public Map<String, Object> getRedPacketDetail(Long redPacketId) throws Exception
    {
        return (Map<String, Object>)super.selectOne("getRedPacketDetail", redPacketId);
    }

    /* (non-Javadoc)
     * @see com.idcq.appserver.dao.redpacket.IRedPacketDao#getBusinessShopsById(java.util.Map)
     */
    @Override
    public List<Map<String, Object>> getBusinessShopsById(Long businessAreaActivityId) throws Exception
    {
        return (List)super.findList("getBusinessShopsById",businessAreaActivityId);
    }

	
	
}