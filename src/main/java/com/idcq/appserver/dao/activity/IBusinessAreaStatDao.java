package com.idcq.appserver.dao.activity;

import com.idcq.appserver.dto.activity.BusinessAreaStatDto;

/**
 * 商圈统计dao接口
 * @author Administrator
 * 
 * @date 2016年3月12日
 * @time 下午1:42:24
 */
public interface IBusinessAreaStatDao {
    /**
     * 根据主键查询商圈统计信息
     * 
     * @Function: com.idcq.appserver.dao.activity.IBusinessAreaStatDao.getBusinessAreaStatByCompKey
     * @Description:
     *
     * @param businessAreaStatDto
     * @return
     *
     * @version:v1.0
     * @author:ChenYongxin
     * @date:2016年3月12日 下午10:01:01
     *
     * Modification History:
     * Date         Author      Version     Description
     * -----------------------------------------------------------------
     * 2016年3月12日    ChenYongxin      v1.0.0         create
     */
    BusinessAreaStatDto getBusinessAreaStatByCompKey(BusinessAreaStatDto businessAreaStatDto);
    
    /**
     * 统计指定的商圈统计数量
     * 
     * @Function: com.idcq.appserver.dao.activity.IBusinessAreaStatDao.getCountCompKey
     * @Description:
     *
     * @param businessAreaStatDto
     * @return
     *
     * @version:v1.0
     * @author:LuJianPing
     * @date:2016年3月16日 下午7:19:01
     *
     * Modification History:
     * Date         Author      Version     Description
     * -----------------------------------------------------------------
     * 2016年3月12日    LuJianPing      v1.0.0         create
     */
    int getCountCompKey(BusinessAreaStatDto businessAreaStatDto);
    /**
     * 插入商圈统计
     * 
     * @Function: com.idcq.appserver.dao.activity.IBusinessAreaStatDao.addBusinessAreaStat
     * @Description:
     *
     * @param businessAreaStatDto
     * @return
     *
     * @version:v1.0
     * @author:ChenYongxin
     * @date:2016年3月12日 下午10:02:09
     *
     * Modification History:
     * Date         Author      Version     Description
     * -----------------------------------------------------------------
     * 2016年3月12日    ChenYongxin      v1.0.0         create
     */
    int addBusinessAreaStat(BusinessAreaStatDto businessAreaStatDto);
    /**
     * 更新商圈统计信息
     * 
     * @Function: com.idcq.appserver.dao.activity.IBusinessAreaStatDao.updateBusinessAreaStatByCompKey
     * @Description:
     *
     * @param businessAreaStatDto
     * @return
     *
     * @version:v1.0
     * @author:ChenYongxin
     * @date:2016年3月12日 下午10:09:49
     *
     * Modification History:
     * Date         Author      Version     Description
     * -----------------------------------------------------------------
     * 2016年3月12日    ChenYongxin      v1.0.0         create
     */
    int updateBusinessAreaStatByCompKey(BusinessAreaStatDto businessAreaStatDto);
    
    /**
     * 递增分享次数
     * 
     * @Function: com.idcq.appserver.dao.activity.IBusinessAreaStatDao.addUpSharedNumByCompKey
     * @Description:
     *
     * @param businessAreaStatDto
     * @return
     *
     * @version:v1.0
     * @author:ChenYongxin
     * @date:2016年3月12日 下午10:09:49
     *
     * Modification History:
     * Date         Author      Version     Description
     * -----------------------------------------------------------------
     * 2016年3月12日    ChenYongxin      v1.0.0         create
     */
    int addUpSharedNumByCompKey(BusinessAreaStatDto businessAreaStatDto);
    
    /**
     * 递增阅读次数
     * 
     * @Function: com.idcq.appserver.dao.activity.IBusinessAreaStatDao.addUpSharedNumByCompKey
     * @Description:
     *
     * @param businessAreaStatDto
     * @return
     *
     * @version:v1.0
     * @author:ChenYongxin
     * @date:2016年3月12日 下午10:09:49
     *
     * Modification History:
     * Date         Author      Version     Description
     * -----------------------------------------------------------------
     * 2016年3月12日    ChenYongxin      v1.0.0         create
     */
    int addUpReadNumByCompKey(BusinessAreaStatDto businessAreaStatDto);
    
    /**
     * 递增转发次数
     * 
     * @Function: com.idcq.appserver.dao.activity.IBusinessAreaStatDao.addUpSharedNumByCompKey
     * @Description:
     *
     * @param businessAreaStatDto
     * @return
     *
     * @version:v1.0
     * @author:ChenYongxin
     * @date:2016年3月12日 下午10:09:49
     *
     * Modification History:
     * Date         Author      Version     Description
     * -----------------------------------------------------------------
     * 2016年3月12日    ChenYongxin      v1.0.0         create
     */
    int addUpTransmitNumByCompKey(BusinessAreaStatDto businessAreaStatDto);
    
    /**
     * 删除商圈统计信息
     * 
     * @Function: com.idcq.appserver.dao.activity.IBusinessAreaStatDao.delBusinessAreaStatByCompKey
     * @Description:
     *
     * @param businessAreaStatDto
     * @return
     *
     * @version:v1.0
     * @author:ChenYongxin
     * @date:2016年3月12日 下午10:10:56
     *
     * Modification History:
     * Date         Author      Version     Description
     * -----------------------------------------------------------------
     * 2016年3月12日    ChenYongxin      v1.0.0         create
     */
    int delBusinessAreaStatByCompKey(BusinessAreaStatDto businessAreaStatDto);

}
