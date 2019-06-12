package com.idcq.appserver.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.idcq.appserver.common.CodeConst;
import com.idcq.appserver.common.enums.ExecutePointExecTypeEnum;
import com.idcq.appserver.common.processor.IProcessor;
import com.idcq.appserver.dao.programconfig.IExecutePointDao;
import com.idcq.appserver.dao.programconfig.IProgramConfigDao;
import com.idcq.appserver.dto.programconfig.ExecutePointDto;
import com.idcq.appserver.dto.programconfig.ProgramConfigDto;
import com.idcq.appserver.exception.ValidateException;

public class ProgramUtils {
    
    private final Log logger = LogFactory.getLog(getClass());
    
    
    /**
     * 通过切入点执行单个程序
     * 
     * @Function: com.idcq.appserver.utils.ProgramUtils.executeBeanByExecutePointCode
     * @Description:
     *
     * @param code 切入点code
     * @param pointType 切入点类型
     * @param params 参数map 例如：{"name",name}
     * @return
     * @throws Exception 获取bean失败
     *
     * @version:v1.0
     * @author:ChenYongxin
     * @date:2016年3月15日 下午2:58:17
     *
     * Modification History:
     * Date         Author      Version     Description
     * -----------------------------------------------------------------
     * 2016年3月15日    ChenYongxin      v1.0.0         create
     */
    public static Object executeBeanByExecutePointCode(String code, Integer pointType, Map<String, Object> params) throws Exception
    {
        Object rslt = null;
        
        IExecutePointDao executePointDao = BeanFactory.getBean(IExecutePointDao.class);
        IProgramConfigDao programConfigDao = BeanFactory.getBean(IProgramConfigDao.class);

        //获取插入点
        ExecutePointDto point = executePointDao.getExecutePointByCode(code,pointType);
        if(point!=null){
        	if(ExecutePointExecTypeEnum.INSTEAD.getValue() == point.getPointExecuteType()){
                //获取程序
            	ProgramConfigDto programs = programConfigDao.getProgramConfigById(point.getConfigId());
                if(null != programs){
                    // 替代执行
                    rslt = executeBeanByProgram(programs, params);
                }
                else{
                    throw new ValidateException(CodeConst.CODE_BEAN_IS_NULL, CodeConst.MSG_BEAN_IS_NULL);
                }
            }
        	
        }
        else{
            throw new ValidateException(CodeConst.CODE_POINGT_IS_NULL, CodeConst.MSG_POINT_IS_NULL);
        }
        return rslt;
    }
    
    /**
     * 通过切入点批量执行程序
     * 
     * @Function: com.idcq.appserver.utils.ProgramUtils.batchExecutePoint
     * @Description:
     *
     * @param code 切入点code
     * @param pointType 切入点类型
     * @param params 参数map 例如：{"name",name}
     * @return
     * @throws Exception 获取bean失败
     *
     * @version:v1.0
     * @author:ChenYongxin
     * @date:2016年3月15日 下午3:03:03
     *
     * Modification History:
     * Date         Author      Version     Description
     * -----------------------------------------------------------------
     * 2016年3月15日    ChenYongxin      v1.0.0         create
     */
    public static Object batchExecutePoint(String code, Integer pointType, Map<String, Object> params) throws Exception
    {
        List<Object> rslt = new ArrayList<Object>();
        
        IExecutePointDao executePointDao = BeanFactory.getBean(IExecutePointDao.class);
        IProgramConfigDao programConfigDao = BeanFactory.getBean(IProgramConfigDao.class);
        //获取程序配置
        ExecutePointDto point = executePointDao.getExecutePointByCode(code,pointType);
        
        if(point!=null){
            if(ExecutePointExecTypeEnum.PARALLEL.getValue() == point.getPointExecuteType()){//非替代执行 
                
                //获取切入点下所有程序
                List<ProgramConfigDto> programs = programConfigDao.getProgramConfigListByExecPointId(point.getExecutePointId());
                for (ProgramConfigDto program : programs){
                    Object obj = executeBeanByProgram(program, params);
                    rslt.add(obj);
                }
           }
        }
        else{
            throw new ValidateException(CodeConst.CODE_POINGT_IS_NULL, CodeConst.MSG_POINT_IS_NULL);
        }
        
        return rslt;
    }
    /**
     * 
     * 
     * @Function: com.idcq.appserver.utils.ProgramUtils.executeBeanByProgramConfigCode
     * @Description:
     *
     * @param code 程序code
     * @param 程序类型
     * @param params 参数map 例如：{"name",name}
     * @return
     * @throws Exception 获取bean失败
     *
     * @version:v1.0
     * @author:ChenYongxin
     * @date:2016年3月15日 下午3:03:23
     *
     * Modification History:
     * Date         Author      Version     Description
     * -----------------------------------------------------------------
     * 2016年3月15日    ChenYongxin      v1.0.0         create
     */
    public static Object executeBeanByProgramConfigCode(String code, Integer programType, Map<String, Object> params)
            throws Exception
    {
        Object rslt = null;
        
        IProgramConfigDao programConfigDao = BeanFactory.getBean(IProgramConfigDao.class);
        
        //参数
        ProgramConfigDto programConfigPra = new ProgramConfigDto();
        programConfigPra.setProgramCode(code);
        programConfigPra.setProgramType(programType);
        
        IProcessor processor = null;
        //获取程序配置
        ProgramConfigDto programConfigDto = programConfigDao.getBeanByProgramConfigCode(programConfigPra);
        if(null != programConfigDto){
            String springBean = programConfigDto.getSpringBean();
            //获取springBean
            processor = (IProcessor)BeanFactory.getBean(springBean);
            
            // 判断是否取到，未取到的报异常。
            if(processor==null){
                throw new ValidateException(CodeConst.CODE_BEAN_IS_NULL, CodeConst.MSG_BEAN_IS_NULL);
            }
            
            rslt = processor.exective(params);

        }
        else{
            throw new ValidateException(CodeConst.CODE_BEAN_IS_NULL, CodeConst.MSG_BEAN_IS_NULL);
        }
        
        return rslt;
    }
    /**
     * 
     * 
     * @Function: com.idcq.appserver.utils.ProgramUtils.executeBeanByProgramConfigCode
     * @Description:
     *
     * @param programConfigDto程序实体
     * @param params 参数map 例如：{"name",name}
     * @return
     * @throws Exception 获取bean失败
     *
     * @version:v1.0
     * @author:ChenYongxin
     * @date:2016年3月15日 下午3:03:23
     *
     * Modification History:
     * Date         Author      Version     Description
     * -----------------------------------------------------------------
     * 2016年3月15日    ChenYongxin      v1.0.0         create
     */
    public static Object executeBeanByProgram(ProgramConfigDto programConfigDto, Map<String, Object> params)
            throws Exception
    {
        Object rslt = null;
        IProcessor processor = null;
        
        if(null != programConfigDto){
            String springBean = programConfigDto.getSpringBean();
            //获取springBean
            processor = (IProcessor)BeanFactory.getBean(springBean);
            
            // 判断是否取到，未取到的报异常。
            if(processor==null){
                throw new ValidateException(CodeConst.CODE_BEAN_IS_NULL, CodeConst.MSG_BEAN_IS_NULL);
            }
            rslt = processor.exective(params);

        }
        
        return rslt;
    }

}
