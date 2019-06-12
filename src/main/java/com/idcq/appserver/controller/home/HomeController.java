package com.idcq.appserver.controller.home;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.idcq.appserver.common.CodeConst;
import com.idcq.appserver.common.CommonConst;
import com.idcq.appserver.dto.city.CityDto;
import com.idcq.appserver.dto.column.ColumnDto;
import com.idcq.appserver.dto.common.PageModel;
import com.idcq.appserver.dto.help.HelpCategoryDto;
import com.idcq.appserver.dto.help.HelpDto;
import com.idcq.appserver.dto.message.MessageDto;
import com.idcq.appserver.dto.message.MessageListDto;
import com.idcq.appserver.dto.region.CitiesDto;
import com.idcq.appserver.dto.region.DistrictDto;
import com.idcq.appserver.dto.region.ProvinceDto;
import com.idcq.appserver.exception.APIBusinessException;
import com.idcq.appserver.exception.APISystemException;
import com.idcq.appserver.exception.ServiceException;
import com.idcq.appserver.exception.ValidateException;
import com.idcq.appserver.service.home.IHomeServcie;
import com.idcq.appserver.service.member.IMemberServcie;
import com.idcq.appserver.service.shop.IShopServcie;
import com.idcq.appserver.utils.CommonValidUtil;
import com.idcq.appserver.utils.DateUtils;
import com.idcq.appserver.utils.NumberUtil;
import com.idcq.appserver.utils.RequestUtils;
import com.idcq.appserver.utils.ResultUtil;

/**
 * 首页controller
 * 
 * @author Administrator
 * 
 * @date 2015年3月9日
 * @time 下午12:00:31
 */
@Controller
public class HomeController {

    private final Logger logger = Logger.getLogger(HomeController.class);

    @Autowired
    public IHomeServcie homeServcie;

    @Autowired
    public IMemberServcie memberService;
    @Autowired
    public IShopServcie shopService;

    /**
     * 分页获取城市信息列表
     * 
     * @param city
     * @param request
     * @return
     */
    @RequestMapping(value = "/home/getCities", produces = "application/json;charset=UTF-8")
    @ResponseBody
    public Object getOrderList(@ModelAttribute(value = "order")
    CityDto city, HttpServletRequest request) {
        try {
            long start = System.currentTimeMillis();
            logger.info("分页获取城市列表-start");
            String pageNO = RequestUtils.getQueryParam(request, "pageNo");
            String pageSize = RequestUtils.getQueryParam(request, "pageSize");
            /*
             * 首先检索符合条件的总记录数 然后检索数据列表 最后封装到pageModel
             */
            PageModel pageModel = this.homeServcie
                    .getCityList(city, Integer.valueOf(pageNO), Integer.valueOf(pageSize));

            MessageListDto msgList = new MessageListDto();
            msgList.setpNo(pageModel.getToPage());
            msgList.setpSize(pageModel.getPageSize());
            msgList.setrCount(pageModel.getTotalItem());
            msgList.setLst(pageModel.getList());
            logger.info("共耗时:" + (System.currentTimeMillis() - start));
            return ResultUtil.getResultJsonStr(CodeConst.CODE_SUCCEED, "获取城市列表成功", msgList);
        } catch (ServiceException e) {
            throw new APIBusinessException(e);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("获取城市列表-系统异常", e);
            throw new APISystemException("获取城市列表-系统异常", e);
        }
    }

    /**
     * 一点传奇APP--A2获取栏目的下级栏目接口 根据父栏目 ID 获取子栏目信息，可获取前 N 条栏目；默认按照序号排序
     * 
     * @param cloumn
     * @param request
     * @return
     */
    @RequestMapping(value = "/column/getColumnInfo", produces = "application/json;charset=UTF-8")
    @ResponseBody
    public Object getColumnList(HttpServletRequest request) {
        try {
            logger.info("获取栏目列表-start"+ request.getQueryString());
            String pageNO = RequestUtils.getQueryParam(request, CommonConst.PAGE_NO);
            String pageSize = RequestUtils.getQueryParam(request, CommonConst.PAGE_SIZE);
            String cityId = RequestUtils.getQueryParam(request, "cityId");
            String parentColumnId = RequestUtils.getQueryParam(request, "parentColumnId");
            String columnType = RequestUtils.getQueryParam(request, "columnType");
            String shopClassify = RequestUtils.getQueryParam(request, "shopClassify");
            ColumnDto column = new ColumnDto();
            if (!StringUtils.isBlank(parentColumnId)) {
                CommonValidUtil.validNumStr(parentColumnId, CodeConst.CODE_PARAMETER_NOT_VALID,
                        CodeConst.MSG_FORMAT_ERROR_PCOLUMNID);
                column.setParentColumnId(Long.valueOf(parentColumnId));
            }
            if (!StringUtils.isBlank(columnType)) {
                CommonValidUtil.validNumStr(columnType, CodeConst.CODE_PARAMETER_NOT_VALID,
                        CodeConst.MSG_FORMAT_ERROR_COLUMNTYPE);
                column.setColumnType(Integer.valueOf(columnType));
            }
            if (null != cityId) {
                CommonValidUtil.validPositLong(cityId, CodeConst.CODE_PARAMETER_NOT_VALID,
                        CodeConst.MSG_FORMAT_ERROR_CITYID);
                column.setCityId(Long.valueOf(cityId));
            }
            if (!StringUtils.isBlank(shopClassify)) {
                CommonValidUtil.validNumStr(shopClassify, CodeConst.CODE_PARAMETER_NOT_VALID,
                        CodeConst.MSG_FORMAT_ERROR_SHOPCLASSIFY);
                column.setShopClassify(Integer.valueOf(shopClassify));
            }
            /*
             * 首先检索符合条件的总记录数 然后检索数据列表 最后封装到pageModel
             */
            String result = this.homeServcie.getColumnListInCache(column, PageModel.handPageNo(pageNO),
                    PageModel.handPageSize(pageSize));
            return result;
        } catch (ServiceException e) {
            throw new APIBusinessException(e);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("获取栏目列表-系统异常", e);
            throw new APISystemException("获取栏目列表-系统异常", e);
        }
    }

    /**
     * 一点传奇APP--M2获取消息接口
     * 获取系统消息或者商户消息（商户喇叭）。
     * 根据消息类型获取相应类型的消息；可获取分页消息；消息按照时间倒排序
     * @param request
     * @return
     */
    @RequestMapping(value = "/msg/getMsg", produces = "application/json;charset=UTF-8")
    @ResponseBody
    public Object getMessages(HttpServletRequest request) {
        try {
            logger.info("分页获取消息列表-start");
            String msgTypeStr = RequestUtils.getQueryParam(request, "msgType");
            String pageNO = RequestUtils.getQueryParam(request, "pNo");
            String pageSize = RequestUtils.getQueryParam(request, "pSize");
            String shopId = RequestUtils.getQueryParam(request, "shopId");
            MessageDto message = new MessageDto();
            // msgType不为空时shopid必填
            if (StringUtils.isNotEmpty(msgTypeStr)) {
                Integer msgType = NumberUtil.strToNum(msgTypeStr, "msgType");
                message.setMsgType(msgType);
                // 1代表商铺消息
                if (1 == msgType) {
                    CommonValidUtil.validStrNull(shopId, CodeConst.CODE_PARAMETER_NOT_NULL,
                            CodeConst.MSG_REQUIRED_SHOPID);
                    // 1,验证商铺ID
                    CommonValidUtil.validPositLong(shopId, CodeConst.CODE_PARAMETER_NOT_NULL,
                            CodeConst.MSG_FORMAT_ERROR_SHOP_ID);
                    message.setShopId(Long.parseLong(shopId));
                }

            }
            /*
             * 首先检索符合条件的总记录数 然后检索数据列表 最后封装到pageModel
             */
            PageModel pageModel = this.homeServcie.getMessageList(message, PageModel.handPageNo(pageNO),
                    PageModel.handPageSize(pageSize));
            MessageListDto msgList = new MessageListDto();
            msgList.setpNo(pageModel.getToPage());
            msgList.setpSize(pageModel.getPageSize());
            msgList.setrCount(pageModel.getTotalItem());
            msgList.setLst(pageModel.getList());
            return ResultUtil.getResultJson(CodeConst.CODE_SUCCEED, CodeConst.MSG_SUCCEED_MESSAGE, msgList,
                    DateUtils.DATETIME_FORMAT);
        } catch (ServiceException e) {
            throw new APIBusinessException(e);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("获取消息列表-系统异常", e);
            throw new APISystemException("获取消息列表-系统异常", e);
        }
    }

    /**
     * 获取消息详情
     * @param request
     * @return
     */
    @RequestMapping(value = "/msg/getMsgDetail", produces = "application/json;charset=UTF-8")
    @ResponseBody
    public Object getMsgDetail(HttpServletRequest request) {
        try {
            logger.info("获取消息详情-start");
            String messageId = RequestUtils.getQueryParam(request, "messageId");
            CommonValidUtil.validObjectNull(messageId, CodeConst.CODE_PARAMETER_NOT_NULL, "messageId不能为空");
            CommonValidUtil.validNumStr(messageId, CodeConst.CODE_PARAMETER_NOT_VALID, "messageId数据格式错误");
            MessageDto messageDto = homeServcie.getMessageById(Long.valueOf(messageId));
            if (messageDto == null) {
                throw new ValidateException(CodeConst.CODE_PARAMETER_NOT_EXIST, "消息不存在");
            }
            logger.info("获取消息详情-end");
            return ResultUtil.getResultJson(CodeConst.CODE_SUCCEED, "获取消息成功", messageDto, DateUtils.DATETIME_FORMAT);
        } catch (ServiceException e) {
            throw new APIBusinessException(e);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("获取消息详情-系统异常");
            throw new APISystemException("获取消息详情-系统异常", e);
        }
    }

    /**
     * 一点传奇APP--S9获取区县列表接口 
     * 通过城市 ID，分页获取区县列表信息
     * @param request
     * @return
     */
    @RequestMapping(value = "/home/getDistricts", produces = "application/json;charset=UTF-8")
    @ResponseBody
    public Object getDistricts(HttpServletRequest request) {
        try {
            long start = System.currentTimeMillis();
            logger.info("获取区县信息-start:" + start);
            String pageNO = RequestUtils.getQueryParam(request, "pNo");
            String pageSize = RequestUtils.getQueryParam(request, "pSize");
            String cityId = RequestUtils.getQueryParam(request, "cityId");
            String cityName = RequestUtils.getQueryParam(request, "cityName");
            PageModel pageModel = null;
            if (cityId == null) {
                if (cityName == null) {
                    CommonValidUtil.validObjectNull(cityId, CodeConst.CODE_PARAMETER_NOT_NULL, "cityId,cityName不能均为空");
                } else {
                    int index = cityName.indexOf("市");
                    if (index != -1) {
                        cityName = cityName.substring(0, index);
                    }
                    pageModel = this.homeServcie.getCitis(null, cityName, 1, 1);
                    if (pageModel.getList() != null && pageModel.getList().size(
                            ) > 0) {
                        CitiesDto city = (CitiesDto) pageModel.getList().get(0);
                        pageModel = this.homeServcie.getDistricts(city.getCityId(), PageModel.handPageNo(pageNO),
                                PageModel.handPageSize(pageSize));
                    }
                }
            } else {
                CommonValidUtil.validNumStr(cityId, CodeConst.CODE_PARAMETER_NOT_VALID, "cityId参数不合法");
                pageModel = this.homeServcie.getDistricts(Long.valueOf(cityId), PageModel.handPageNo(pageNO),
                        PageModel.handPageSize(pageSize));
            }

            MessageListDto msgList = new MessageListDto();
            msgList.setpNo(pageModel.getToPage());
            msgList.setpSize(pageModel.getPageSize());
            msgList.setrCount(pageModel.getTotalItem());
            msgList.setLst(pageModel.getList());
            logger.info("共耗时:" + (System.currentTimeMillis() - start));
            return ResultUtil.getResultJsonStr(CodeConst.CODE_SUCCEED, "获取区县信息成功", msgList);
        } catch (ServiceException e) {
            throw new APIBusinessException(e);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("获取区县信息-系统异常", e);
            throw new APISystemException("获取区县信息-系统异常", e);
        }
    }
   
    /**
     * 一点传奇APP--S9获取区县列表接口 
     * 通过城市 ID，分页获取区县列表信息
     * @param request
     * @return
     * @throws Exception 
     */
    @RequestMapping(value = "/home/getAllDistricits", produces = "application/json;charset=UTF-8")
    @ResponseBody
    public Object getAllDistricits(HttpServletRequest request) throws Exception {
    	
            logger.info("获取县信息-start");
            
            List<DistrictDto> listDistricts  = homeServcie.getAllDistricits();
            
            logger.info("获取县信息-end" );
            
            return ResultUtil.getResultJsonStr(CodeConst.CODE_SUCCEED, "获取县信息成功", listDistricts);
    }

    /**
     * 获取所有省份
     * 
     * @param city
     * @param request
     * @return
     */
    @RequestMapping(value = "/home/getAllProvinces", produces = "application/json;charset=UTF-8")
    @ResponseBody
    public Object getAllProvinces(HttpServletRequest request) {
        try {
            long start = System.currentTimeMillis();
            logger.info("获取所有省份信息-start:" + start);
            MessageListDto msgList = new MessageListDto();
            List<ProvinceDto> list = this.homeServcie.getAllProvinces();
            msgList.setLst(list);
            msgList.setrCount(list.size());
            logger.info("共耗时:" + (System.currentTimeMillis() - start));
            return ResultUtil.getResultJsonStr(CodeConst.CODE_SUCCEED, "获取所有省份信息成功", msgList);
        } catch (ServiceException e) {
            throw new APIBusinessException(e);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("获取所有省份信息-系统异常", e);
            throw new APISystemException("获取所有省份信息-系统异常", e);
        }
    }

    /**
     * 获取所有城市
     * 
     * @param city
     * @param request
     * @return
     */
    @RequestMapping(value = "/home/getAllCitis", produces = "application/json;charset=UTF-8")
    @ResponseBody
    public Object getAllCitis(HttpServletRequest request) {
        try {
            long start = System.currentTimeMillis();
            logger.info("获取所有城市信息-start:" + start);
            MessageListDto msgList = new MessageListDto();
            List<Map> list = this.homeServcie.getAllCitis();
            msgList.setLst(list);
            msgList.setrCount(list.size());
            logger.info("共耗时:" + (System.currentTimeMillis() - start));
            return ResultUtil.getResultJsonStr(CodeConst.CODE_SUCCEED, "获取所有城市信息成功", msgList);
        } catch (ServiceException e) {
            throw new APIBusinessException(e);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("获取所有城市信息-系统异常", e);
            throw new APISystemException("获取所有城市信息-系统异常", e);
        }
    }

    /**
     * 一点传奇APP -- S10获取省内城市列表接口
     * 通过省份 ID，获取城市列表信息
     * @param city
     * @param request
     * @return
     */
    @RequestMapping(value = "/home/getCitis", produces = "application/json;charset=UTF-8")
    @ResponseBody
    public Object getCitis(HttpServletRequest request) {
        try {
            long start = System.currentTimeMillis();
            logger.info("获取城市信息-start:" + start);
            String pageNO = RequestUtils.getQueryParam(request, "pNo");
            String pageSize = RequestUtils.getQueryParam(request, "pSize");
            String provinceId = RequestUtils.getQueryParam(request, "provinceId");
            CommonValidUtil.validObjectNull(provinceId, CodeConst.CODE_PARAMETER_NOT_NULL, "provinceId不能为空");
            CommonValidUtil.validNumStr(provinceId, CodeConst.CODE_PARAMETER_NOT_VALID, "provinceId参数不合法");
            /*
             * 首先检索符合条件的总记录数 然后检索数据列表 最后封装到pageModel
             */
            PageModel pageModel = this.homeServcie.getCitis(Long.valueOf(provinceId), null, PageModel.handPageNo(pageNO),
                    PageModel.handPageSize(pageSize));
            MessageListDto msgList = new MessageListDto();
            msgList.setpNo(pageModel.getToPage());
            msgList.setpSize(pageModel.getPageSize());
            msgList.setrCount(pageModel.getTotalItem());
            msgList.setLst(pageModel.getList());
            logger.info("共耗时:" + (System.currentTimeMillis() - start));
            return ResultUtil.getResultJsonStr(CodeConst.CODE_SUCCEED, "获取城市信息成功", msgList);
        } catch (ServiceException e) {
            throw new APIBusinessException(e);
        } catch (Exception e) {
            logger.error("获取城市信息-系统异常", e);
            throw new APISystemException("获取城市信息-系统异常", e);
        }

    }

    /**
     * 一点传奇APP --  S24根据区县 ID获取街道列表接口
     * 通过区县 ID，获取街道（镇）列表信息
     * @param districtId
     * @param request
     * @return
     */
    @RequestMapping(value = "/home/getTowns", produces = "application/json;charset=UTF-8")
    @ResponseBody
    public Object getTowns(HttpServletRequest request) {
        try {
            long start = System.currentTimeMillis();
            logger.info("获取街道信息-start:" + start);
            Integer pageNO = PageModel.handPageNo(RequestUtils.getQueryParam(request, "pNo"));
            Integer pageSize = PageModel.handPageSize(RequestUtils.getQueryParam(request, "pSize"));
            String districtIdStr = RequestUtils.getQueryParam(request, "districtId");
            CommonValidUtil.validObjectNull(districtIdStr, CodeConst.CODE_PARAMETER_NOT_NULL, "districtId不能为空");
            CommonValidUtil.validNumStr(districtIdStr, CodeConst.CODE_PARAMETER_NOT_VALID, "districtId参数不合法");
            Long districtId = NumberUtil.strToLong(districtIdStr, "districtId");
            PageModel pageModel = this.homeServcie.getTowns(districtId, pageNO, pageSize);
            MessageListDto msgList = new MessageListDto();
            msgList.setpNo(pageModel.getToPage());
            msgList.setpSize(pageModel.getPageSize());
            msgList.setrCount(pageModel.getTotalItem());
            msgList.setLst(pageModel.getList());
            logger.info("共耗时:" + (System.currentTimeMillis() - start));
            return ResultUtil.getResultJsonStr(CodeConst.CODE_SUCCEED, "获取街道信息成功", msgList);
        } catch (ServiceException e) {
            throw new APIBusinessException(e);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("获取街道信息-系统异常", e);
            throw new APISystemException("获取街道信息-系统异常", e);
        }

    }

    @RequestMapping(value = "/home/getCityInfo", produces = "application/json;charset=UTF-8")
    @ResponseBody
    public Object getCityInfo(HttpServletRequest request) {
        try {
            long start = System.currentTimeMillis();
            logger.info("获取城市信息-start:" + start);
            String cityName = RequestUtils.getQueryParam(request, "cityName");
            CommonValidUtil.validObjectNull(cityName, CodeConst.CODE_PARAMETER_NOT_NULL, "cityName不能为空");
            Map<String, Object> map = this.homeServcie.getCityInfoByName(cityName);
            logger.info("共耗时:" + (System.currentTimeMillis() - start));
            return ResultUtil.getResultJsonStr(CodeConst.CODE_SUCCEED, "获取城市信息成功", map);
        } catch (ServiceException e) {
            throw new APIBusinessException(e);
        } catch (Exception e) {
            logger.error("获取城市信息-系统异常", e);
            throw new APISystemException("获取城市信息-系统异常", e);
        }
    }
    
    @RequestMapping(value = "/home/getOpenedCitis", produces = "application/json;charset=UTF-8")
    @ResponseBody
    public Object getOpenedCitis(HttpServletRequest request) {
        try {
            //http://localhost:8080/appServer/interface/home/getOpenedCitis
            logger.info("S34：获取已开通城市列表-start");
            String pNoStr = RequestUtils.getQueryParam(request, "pNo");
            String pSizeStr = RequestUtils.getQueryParam(request, "pSize");
            String provinceIdStr = RequestUtils.getQueryParam(request, "provinceId");
            //参数map
            Map<String, Object> paramMap = new HashMap<String, Object>();
            /*****参数校验******/
            if(StringUtils.isNotBlank(provinceIdStr)){
                Integer provinceId = NumberUtil.strToNum(provinceIdStr, "provinceId");
                paramMap.put("provinceId", provinceId);
            }
            
            // 页码
            Integer pNo = PageModel.handPageNo(pNoStr);
            Integer pSize = PageModel.handPageSize(pSizeStr);
            Map<String, Object> resultMap = new HashMap<String, Object>();
            paramMap.put("pSize", pSize);
            paramMap.put("limit", (pNo - 1) * pSize);
            
            PageModel pageModel = shopService.getOpenedCitis(paramMap);
            resultMap.put("lst", pageModel.getList());
            resultMap.put("pNo", pNo);
            resultMap.put("rcount", pageModel.getTotalItem());
            return ResultUtil.getResult(CodeConst.CODE_SUCCEED, "获取城市信息成功！", resultMap);
        } catch (ServiceException e) {
            throw new APIBusinessException(e);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("S34：获取已开通城市列表-系统异常", e);
            throw new APISystemException("S34：获取已开通城市列表-系统异常", e);
        }
    }


    /**
     * 获取帮助信息
     * 
     * @param help
     * @param request
     * @return
     */
    @RequestMapping(value = "/help/getHelpInfo", produces = "application/json;charset=UTF-8")
    @ResponseBody
    public Object getHelpList(HttpServletRequest request) {
        try {
            logger.info("获取帮助信息列表-start");
            String pageNO = RequestUtils.getQueryParam(request, CommonConst.PAGE_NO);
            String pageSize = RequestUtils.getQueryParam(request, CommonConst.PAGE_SIZE);
            String categoryId = RequestUtils.getQueryParam(request, "categoryId");
            /*20160705修改接口描述，添加获取对象（mode:0-app段；1-商铺端，默认为0）*/
            String mode = RequestUtils.getQueryParam(request, "mode");
            String infoTitle = RequestUtils.getQueryParam(request, "infoTitle");

            HelpDto help = new HelpDto();
            if(!StringUtils.isBlank(mode)){
            	help.setMode(Integer.parseInt(mode));;
            }
            if (!StringUtils.isBlank(categoryId)) {
                CommonValidUtil.validNumStr(categoryId, CodeConst.CODE_PARAMETER_NOT_VALID,
                        CodeConst.HELP_CATEGORY_FORMAT_ERROR);
                help.setCategoryId(Long.valueOf(categoryId));
            }
            if(infoTitle != null){
            	help.setInfoTitle(infoTitle);
            }
            /*
             * 首先检索符合条件的总记录数 然后检索数据列表 最后封装到pageModel
             */
            PageModel pageModel = this.homeServcie.getHelpList(help, PageModel.handPageNo(pageNO),
                    PageModel.handPageSize(pageSize));
            MessageListDto msgList = new MessageListDto();
            msgList.setpNo(pageModel.getToPage());
            msgList.setpSize(pageModel.getPageSize());
            msgList.setrCount(pageModel.getTotalItem());
            msgList.setLst(pageModel.getList());
            return ResultUtil.getResultJson(CodeConst.CODE_SUCCEED, "获取帮助信息成功", msgList, DateUtils.DATETIME_FORMAT);
        } catch (ServiceException e) {
            throw new APIBusinessException(e);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("获取帮助信息列表-系统异常", e);
            throw new APISystemException("获取帮助信息列表-系统异常", e);
        }
    }
    
    /**
     * 获取帮助信息
     * 
     * @param help
     * @param request
     * @return
     */
    @RequestMapping(value = {"session/help/helpInfoBy1dsxy","service/help/helpInfoBy1dsxy","token/help/helpInfoBy1dsxy"}, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public Object helpInfoBy1dsxy(HttpServletRequest request) {
        try {
            logger.info("获取一点商学院帮助信息列表-start");
            String pageNOStr = RequestUtils.getQueryParam(request, "pageNo");
            String pageSizeStr = RequestUtils.getQueryParam(request, "pageSize");
            String helpTitle = RequestUtils.getQueryParam(request, "helpTitle");
            String columnId = RequestUtils.getQueryParam(request, "columnId");
            String columnNo = RequestUtils.getQueryParam(request, "columnNo");
            Map<String,Object> param = new HashMap<String, Object>();
            Integer pageNo  = PageModel.handPageNo(pageNOStr);
            Integer pageSize = PageModel.handPageSize(pageSizeStr);
            param.put("pageNo", (pageNo-1)*pageSize);
            param.put("pageSize", pageSize);
            param.put("helpTitle", helpTitle);
            param.put("columnId", columnId);
            param.put("columnNo", columnNo);
            PageModel pageModel = this.homeServcie.getHelpOfYDSXYList(param);
            MessageListDto msgList = new MessageListDto();
            msgList.setpNo(pageNo);
            msgList.setpSize(pageSize);
            msgList.setrCount(pageModel.getTotalItem());
            msgList.setLst(pageModel.getList());
            return ResultUtil.getResultJson(CodeConst.CODE_SUCCEED, "获取一点商学院帮助信息列表成功", msgList, DateUtils.DATETIME_FORMAT);
        } catch (ServiceException e) {
            throw new APIBusinessException(e);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("获取一点商学院帮助信息列表-系统异常", e);
            throw new APISystemException("获取一点商学院帮助信息列表-系统异常", e);
        }
    }

    /**
     * 获取帮助信息
     * 
     * @param help
     * @param request
     * @return
     */
    @RequestMapping(value = "/help/getHelpInfoCategory", produces = "application/json;charset=UTF-8")
    @ResponseBody
    public Object getHelpCategoryList(HttpServletRequest request) {
        try {
            logger.info("获取帮助信息分类列表-start");
            String pageNO = RequestUtils.getQueryParam(request, CommonConst.PAGE_NO);
            String pageSize = RequestUtils.getQueryParam(request, CommonConst.PAGE_SIZE);
            String parentCategoryId = RequestUtils.getQueryParam(request, "parentCategoryId");

            HelpCategoryDto helpCategory = new HelpCategoryDto();
            if (!StringUtils.isBlank(parentCategoryId)) {
                CommonValidUtil.validNumStr(parentCategoryId, CodeConst.CODE_PARAMETER_NOT_VALID,
                        CodeConst.HELP_CATEGORY_PARENT_FORMAT_ERROR);
                helpCategory.setParentCategoryId(Long.valueOf(parentCategoryId));
            }

            /*
             * 首先检索符合条件的总记录数 然后检索数据列表 最后封装到pageModel
             */
            PageModel pageModel = this.homeServcie.getHelpCategoryList(helpCategory, PageModel.handPageNo(pageNO),
                    PageModel.handPageSize(pageSize));
            MessageListDto msgList = new MessageListDto();
            msgList.setpNo(pageModel.getToPage());
            msgList.setpSize(pageModel.getPageSize());
            msgList.setrCount(pageModel.getTotalItem());
            msgList.setLst(pageModel.getList());
            return ResultUtil.getResultJsonStr(CodeConst.CODE_SUCCEED, "获取帮助的分类信息成功", msgList);
        } catch (ServiceException e) {
            throw new APIBusinessException(e);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("获取帮助信息分类列表-系统异常", e);
            throw new APISystemException("获取帮助信息分类列表-系统异常", e);
        }
    }
    
}
