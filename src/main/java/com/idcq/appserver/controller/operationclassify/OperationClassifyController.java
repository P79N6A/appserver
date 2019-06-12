package com.idcq.appserver.controller.operationclassify;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.idcq.appserver.common.CodeConst;
import com.idcq.appserver.dto.common.PageModel;
import com.idcq.appserver.dto.message.MessageListDto;
import com.idcq.appserver.exception.APIBusinessException;
import com.idcq.appserver.exception.APISystemException;
import com.idcq.appserver.exception.ServiceException;
import com.idcq.appserver.service.operationclassify.IOperationClassifyService;
import com.idcq.appserver.utils.CommonValidUtil;
import com.idcq.appserver.utils.DateUtils;
import com.idcq.appserver.utils.FdfsUtil;
import com.idcq.appserver.utils.RequestUtils;
import com.idcq.appserver.utils.ResultUtil;

/**
 * 运营分类控制器
 * @ClassName: OperationClassifyController
 * @Description: TODO
 * @author 张鹏程
 * @date 2015年7月27日 下午1:30:58
 * 
 */
@Controller
public class OperationClassifyController
{

    @Autowired
    private IOperationClassifyService operationClassifyService;

    /**
     * 获取运营分类的下级分类
     * @Title: getOperationClassify
     * @param @param request
     * @param @return
     * @return String 返回类型
     * @throws
     */
    @RequestMapping(value = "operationclassify/getOperationClassify", produces = "application/json;charset=UTF-8")
    public @ResponseBody
    String getOperationClassify(HttpServletRequest request)
    {
        try
        {
            String cityId = RequestUtils.getQueryParam(request, "cityId");
            String parentClassifyId = RequestUtils.getQueryParam(request, "parentClassifyId");// 父运营分类
            String pNo = RequestUtils.getQueryParam(request, "pNo");
            String pSize = RequestUtils.getQueryParam(request, "pSize");
            String orderStr = RequestUtils.getQueryParam(request, "order");
            Integer order = null;
            //默认升序排列
            if(orderStr == null || orderStr.trim().length() == 0){
                order = 1;
            }else{
                order = Integer.parseInt(orderStr.trim());  
            }
            if (!StringUtils.isEmpty(cityId))
            {
                CommonValidUtil.validNumStr(cityId, CodeConst.CODE_PARAMETER_NOT_VALID, "cityId格式错误");
            }
            if (!StringUtils.isEmpty(parentClassifyId))
            {
                CommonValidUtil.validNumStr(parentClassifyId, CodeConst.CODE_PARAMETER_NOT_VALID,
                        "parentClassifyId格式错误");
            }
            if (StringUtils.isEmpty(pNo))
            {
                pNo = "1";
            }
            if (StringUtils.isEmpty(pSize))
            {
                pSize = "30";
            }
            PageModel pageModel = operationClassifyService.getOperationClassify(cityId, parentClassifyId, pNo, pSize, order);
            MessageListDto msgList = new MessageListDto();
            msgList.setrCount(pageModel.getTotalItem());
            msgList.setLst(updateResultList(pageModel));

            return ResultUtil.getResultJson(CodeConst.CODE_SUCCEED, "获取运营分类成功", msgList, DateUtils.DATETIME_FORMAT);

        }
        catch (ServiceException e)
        {
            throw new APIBusinessException(e);
        }
        catch (Exception e)
        {
            e.printStackTrace();
            throw new APISystemException("查询运营分类异常", e);
        }
    }

    private List<Map<String, Object>> updateResultList(PageModel pageModel) throws Exception
    {

        List<Map<String, Object>> newList = new ArrayList<Map<String, Object>>();
        List<Map<String, Object>> resultList = pageModel.getList();

        if (CollectionUtils.isNotEmpty(resultList))
        {
            for (Map<String, Object> operationClassify : resultList)
            {

                String classifyImgUrl = operationClassify.get("classifyImgUrl") == null ? "" : (String) operationClassify
                        .get("classifyImgUrl");
                operationClassify.put("classifyImgUrl", FdfsUtil.getFileProxyPath(classifyImgUrl));
                newList.add(operationClassify);
            }
        }
        return resultList;
    }
}
