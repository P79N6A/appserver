/**
 * Copyright (C) 2016 Asiainfo-Linkage
 *
 *
 * @className:com.idcq.appserver.service.common.Processor
 * @description:TODO
 * 
 * @version:v1.0.0 
 * @author:ChenYongxin
 * 
 * Modification History:
 * Date         Author      Version     Description
 * -----------------------------------------------------------------
 * 2016年3月15日     ChenYongxin       v1.0.0        create
 *
 *
 */
package com.idcq.appserver.common.processor;

import java.util.Map;

public interface IProcessor
{
    /**
     * 
     * 
     * @Function: com.idcq.appserver.service.common.Processor.exective
     * @Description:
     *
     * @param params
     * @return
     *
     * @version:v1.0
     * @author:ChenYongxin
     * @date:2016年3月15日 上午10:58:32
     *
     * Modification History:
     * Date         Author      Version     Description
     * -----------------------------------------------------------------
     * 2016年3月15日    ChenYongxin      v1.0.0         create
     */
    public Object exective(Map<String,Object> params) throws Exception;
}
