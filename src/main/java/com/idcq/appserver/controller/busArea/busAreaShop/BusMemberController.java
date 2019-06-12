package com.idcq.appserver.controller.busArea.busAreaShop;

import java.io.InputStream;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartRequest;

import com.idcq.appserver.common.CodeConst;
import com.idcq.appserver.common.CommonConst;
import com.idcq.appserver.dto.common.PageModel;
import com.idcq.appserver.dto.shop.ShopMemberDto;
import com.idcq.appserver.dto.user.UserDto;
import com.idcq.appserver.exception.APIBusinessException;
import com.idcq.appserver.exception.APISystemException;
import com.idcq.appserver.exception.ServiceException;
import com.idcq.appserver.exception.ValidateException;
import com.idcq.appserver.service.busArea.shopMember.IShopMemberService;
import com.idcq.appserver.service.collect.ICollectService;
import com.idcq.appserver.service.member.IMemberServcie;
import com.idcq.appserver.service.shop.IShopServcie;
import com.idcq.appserver.utils.CommonValidUtil;
import com.idcq.appserver.utils.DateUtils;
import com.idcq.appserver.utils.JacksonUtil;
import com.idcq.appserver.utils.ProgramUtils;
import com.idcq.appserver.utils.RequestUtils;
import com.idcq.appserver.utils.ResultUtil;
import com.idcq.appserver.utils.excel.ParseShopMemberSource;
/**
 * 商圈店铺类
 *
 * @author Administrator
 */
@Controller
public class BusMemberController {
    private final Log logger = LogFactory.getLog(getClass());

    @Autowired
    private IShopMemberService shopMemberService;

    @Autowired
    private IShopServcie shopServcie;

    @Autowired
    private IMemberServcie memberService;

    @Autowired
    private ICollectService collectService;

    /**
     * PSM1：批量导入店铺会员接口
     *
     * @param request
     * @return
     * @throws Exception
     */
    @RequestMapping(value =
            {"/service/shopMember/importShopMember", "/token/shopMember/importShopMember",
                    "/session/shopMember/importShopMember"}, method = RequestMethod.POST, consumes = "application/json", produces = "application/json;charset=UTF-8")
    @ResponseBody
    public Object importShopMember(HttpEntity<String> entity, HttpServletRequest request) {

        logger.info("批量导入店铺会员接口-start");
        Map<String, Object> map = new HashMap<String, Object>();
        try {
            /**
             * 步骤一：初步解析验证并验证请求参数
             *
             * 1.流数据的提取； 2.是否防止数据恶意导入；
             */
            Map<String, String> requestMap = JacksonUtil.postJsonToMap(entity);
            Long shopId = null;
            String data = null;

            /* 验证shopId不为空，并转换相应类型 */
            String shopIdStr = requestMap.get("shopId");
            CommonValidUtil.validStrLongFmt(shopIdStr, CodeConst.CODE_PARAMETER_NOT_NULL,
                    CodeConst.MSG_REQUIRED_SHOPID);
            shopId = CommonValidUtil.validStrLongFmt(shopIdStr, CodeConst.CODE_PARAMETER_NOT_VALID,
                    CodeConst.MSG_FORMAT_ERROR_SHOP_ID);
            // 校验商铺是否存在
            collectService.checkShopExists(shopId);
            /* 取得data属性，并验证source即data属性不为空 */
            data = requestMap.get("members");// 会员列表
            CommonValidUtil.validStrNull(data, CodeConst.CODE_PARAMETER_NOT_NULL, CodeConst.MSG_MIS_MEMBERS);

            /**
             * 步骤二：解析会员元数据流
             *
             * 1.根据sourceType判断源数据类型； 2.校验模板协议； 3.检验数据类型是否正确； 4.根据类型调用解释策略；
             *
             */
            /* 解析json数据流 */
            List<ShopMemberDto> shopMemberDtos = ParseShopMemberSource.parseJson(data);

            /* 设置shopId */
            for (ShopMemberDto dto : shopMemberDtos) {
                dto.setShopId(shopId);
            }

            /**
             * 步骤三：保存会员信息
             *
             * 店内会员表，平台会员表
             * 1.检验数据的有效性（必须要有姓名,手机号等级等），并将无效的纳入错误信息（注意区别对特不同源导入的不同数据协议）；
             * 2.注意重复会员条件； 3.注意处理会员等级； 4.注意插入平台会员（是否要检验电话号码的合法性）； 5.注意导入时间；
             * 6.非预料异常的捕捉与恢复；
             */
            /* 添加会员 */
            List<Map<String, String>> tempresult = shopMemberService.addShopMembers(shopMemberDtos, 0);

            /**
             * 步骤四：返回处理结果
             *
             * 注意返回失败条数与原因；
             */
            /* 处理返回结果 */
            Integer totalNum = shopMemberDtos.size();
            Integer failureNum = 0;
            Integer code = CodeConst.CODE_SUCCEED;
            String msg = "批量导入店铺会员接口成功！";
            if (tempresult != null && tempresult.size() != 0) {
                failureNum = tempresult.size();
                if (tempresult.size() < totalNum) {
                    code = CodeConst.CODE_PARTIAL_SUCCESS;
                    msg = "部分导入成功！";
                } else {
                    msg = "导入失败！";
                    code = CodeConst.CODE_FAILURE;
                }
            }

            map.put("successNum", totalNum - failureNum);
            map.put("failureNum", failureNum);
            map.put("failList", tempresult);

            return ResultUtil.getResult(code, msg, map);
        } catch (ServiceException e) {
            throw new APIBusinessException(e);
        } catch (Exception e) {
            this.logger.error("批量导入店铺会员接口-系统异常", e);
            throw new APISystemException("批量导入店铺会员接口-系统异常", e);
        }
    }

    /*
     * @Test public void test(){ String s = null;
     * System.out.println(Integer.valueOf(s)); }
     */

    /**
     * PSM2：查询店铺内会员信息列表接口
     *
     * @param request
     * @return
     * @throws Exception
     */
    @RequestMapping(value =
            {"/service/shopMember/queryShopMemberList", "/token/shopMember/queryShopMemberList",
                    "/session/shopMember/queryShopMemberList"}, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public Object queryShopMemberList(HttpServletRequest request) {
        /**
         * 步骤一：校验查询参数
         *
         * 校验请求参数中的必填参数是否填写，校验请求参数的类型
         *
         * @return
         */

        /**
         * 步骤一：查询店铺会员信息列表
         *
         * 根据请求查询的参数查询店铺会员 其中searchKey为模糊搜索Key,用来全模糊匹配mobile，qq两个字段
         *
         * 数据表：1dcq_shop_member
         *
         * @return
         */

        logger.info("查询店铺内会员信息列表接口-start" + request.getQueryString());
        try {
            Map<String, Object> requestMap = checkParamValidForQueryShopMember(request);
            List<ShopMemberDto> resultList = shopMemberService.queryShopMemberList(requestMap);
            Long shopId = Long.valueOf(requestMap.get("shopId").toString());
            Map<String, Object> responseData = new HashMap<String, Object>();
            responseData.put("lst", resultList);
            responseData.put("rCount", shopMemberService.queryShopMemberCount(requestMap));
            responseData.put("pNo", requestMap.get("pNo"));
            return ResultUtil.getResult(CodeConst.CODE_SUCCEED, "查询店铺内会员信息列表接口成功！", responseData);
        } catch (ServiceException e) {
            throw new APIBusinessException(e);
        } catch (Exception e) {
            this.logger.error("查询店铺内会员信息列表接口-系统异常", e);
            throw new APISystemException("查询店铺内会员信息列表接口-系统异常", e);
        }
    }

    private Map<String, Object> checkParamValidForQueryShopMember(HttpServletRequest request) throws Exception {

        Map<String, Object> requestMap = new HashMap<String, Object>();

        String shopIdStr = RequestUtils.getQueryParam(request, "shopId");
        String userIdStr = RequestUtils.getQueryParam(request, "userId");
        String searchKey = RequestUtils.getQueryParam(request, "searchKey");
        String mobile = RequestUtils.getQueryParam(request, "mobile");
        String qq = RequestUtils.getQueryParam(request, "qq");
        String grade = RequestUtils.getQueryParam(request, "grade");
        String memberStatus = RequestUtils.getQueryParam(request, "memberStatus");
        String orderField = RequestUtils.getQueryParam(request, "orderField");
        String startTime = RequestUtils.getQueryParam(request, "startTime");
        String endTime = RequestUtils.getQueryParam(request, "endTime");
        String pNoStr = RequestUtils.getQueryParam(request, "pNo");
        String pSizeStr = RequestUtils.getQueryParam(request, "pSize");
        String existMobile = RequestUtils.getQueryParam(request, "existMoble");
        
        
        String birthdayStartDate = RequestUtils.getQueryParam(request, "birthdayStartDate");
        String birthdayEndDate = RequestUtils.getQueryParam(request, "birthdayEndDate");
        String shopMemberLevelId = RequestUtils.getQueryParam(request, "shopMemberLevelId");
        if(StringUtils.isNotBlank(shopMemberLevelId)){
        	requestMap.put("shopMemberLevelId", shopMemberLevelId);
        }
        
        if(StringUtils.isNotBlank(birthdayStartDate)){
        	requestMap.put("birthdayStartDate", birthdayStartDate);
        }
        
        if(StringUtils.isNotBlank(birthdayEndDate)){
        	requestMap.put("birthdayEndDate", birthdayEndDate);
        }
        
        
        if(StringUtils.isNotBlank(existMobile)){
            existMobile = existMobile.trim();
            if( (!"1".equals(existMobile)) &&  (!"0".equals(existMobile)))
            {
                throw new ValidateException(CodeConst.CODE_PARAMETER_NOT_VALID, "店铺Id类型错误,只能为null,0,1");
            }
        }

        Long shopId = CommonValidUtil.validStrLongFmt(shopIdStr, CodeConst.CODE_PARAMETER_NOT_VALID, "店铺Id类型错误");

        int flag = this.shopServcie.queryShopExists(Long.valueOf(shopId));
        CommonValidUtil.validPositInt(flag, CodeConst.CODE_PARAMETER_NOT_EXIST, "商铺不存在");

        requestMap.put("shopId", shopId);
        requestMap.put("existMobile", existMobile);
        if (userIdStr != null) {
            Long userId = CommonValidUtil.validStrLongFmt(userIdStr, CodeConst.CODE_PARAMETER_NOT_VALID, "userId类型错误");
            UserDto userDto = memberService.getUserByUserId(userId);
            if (userDto == null)
                throw new ValidateException(CodeConst.CODE_USER_NOT_EXIST, "用户不存在");

            requestMap.put("userId", userId);
        }

        if (StringUtils.isNotBlank(mobile)) {
            CommonValidUtil.validMobileStr(mobile, CodeConst.CODE_PARAMETER_NOT_VALID,
                    CodeConst.MSG_REQUIRED_MOBILE_VALID);

            requestMap.put("mobile", mobile);
        }

        if (StringUtils.isNotBlank(qq)) {
            requestMap.put("qq", qq);
        }

        if (StringUtils.isNotBlank(searchKey)) {
            requestMap.put("searchKey", searchKey);
        }

        if (StringUtils.isNotBlank(memberStatus)) {
            requestMap.put("memberStatus",
                    CommonValidUtil.validStrIntFmt(memberStatus, CodeConst.CODE_PARAMETER_NOT_VALID, "会员状态类型错误"));
        } 
        if (StringUtils.isNotBlank(orderField)) {
            requestMap.put("orderField",
                    CommonValidUtil.validStrIntFmt(orderField, CodeConst.CODE_PARAMETER_NOT_VALID, "排序字段类型错误"));
        }

        if (StringUtils.isNotBlank(grade)) {
            requestMap.put("grade", grade.split(","));
        }
        if (startTime != null) {
            CommonValidUtil.validDateTimeFormat(startTime, CodeConst.CODE_PARAMETER_NOT_VALID, "startTime格式错误");
            requestMap.put("startTime", startTime);
        }

        if (endTime != null) {
            CommonValidUtil.validDateTimeFormat(endTime, CodeConst.CODE_PARAMETER_NOT_VALID, "endTime格式错误");
            Calendar cal = Calendar.getInstance();
            cal.setTime(DateUtils.parse(endTime, DateUtils.DATE_FORMAT));
            cal.set(Calendar.HOUR_OF_DAY, 23);
            cal.set(Calendar.MINUTE, 59);
            cal.set(Calendar.SECOND, 59);
            requestMap.put("endTime", cal.getTime());
        }
        

        Integer pNo = PageModel.handPageNo(pNoStr);
        Integer pSize = PageModel.handPageSize(pSizeStr);
        requestMap.put("pNo", pNo);
        requestMap.put("n", (pNo - 1) * pSize);
        requestMap.put("m", pSize);

        return requestMap;
    }

    /**
     * PSM3：查询店铺会员详情
     *
     * @param request
     * @return
     * @throws Exception
     */
    @RequestMapping(value =
            {"/service/shopMember/getShopMemberDetail", "/token/shopMember/getShopMemberDetail",
                    "/session/shopMember/getShopMemberDetail"}, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public Object getShopMemberDetail(HttpServletRequest request) {
        /**
         * 步骤一：校验查询参数
         *
         * 校验请求参数中的必填参数是否填写，校验请求参数的类型
         *
         * @return
         */

        /**
         * 步骤一：查询店铺会员详情
         *
         * 根据店铺会员Id查询店铺会员详细信息
         *
         * 数据表：1dcq_shop_member
         *
         * @return
         */
        logger.info("查询店铺会员详情-start" + request.getQueryString());
        try {
            String memberIdStr = RequestUtils.getQueryParam(request, "memberId");
            String shopIdStr = RequestUtils.getQueryParam(request, "shopId");
            CommonValidUtil.validObjectNull(memberIdStr, CodeConst.CODE_PARAMETER_NOT_NULL, "店内会员Id不能空");
            Long memberId = CommonValidUtil.validStrLongFmt(memberIdStr, CodeConst.CODE_PARAMETER_NOT_VALID,
                    "店内会员Id类型错误");
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("memberId", memberId);
            if (shopIdStr != null && !shopIdStr.isEmpty()) {
                CommonValidUtil.validStrLongFmt(shopIdStr, CodeConst.CODE_PARAMETER_NOT_VALID, CodeConst.MSG_FORMAT_ERROR_SHOP_ID);
                map.put("shopId", shopIdStr);
            }
//            ShopMemberDto shopMember = shopMemberService.getShopMemberDetail(memberId);
            ShopMemberDto shopMember = shopMemberService.getShopMemberDetailByMap(map);
            return ResultUtil.getResultJson(CodeConst.CODE_SUCCEED, "查询店铺会员详情成功！", shopMember, DateUtils.DATE_FORMAT);
        } catch (ServiceException e) {
            throw new APIBusinessException(e);
        } catch (Exception e) {
            this.logger.error("查询店铺会员详情-系统异常", e);
            throw new APISystemException("查询店铺会员详情-系统异常", e);
        }
    }

    /**
     * PSM4：录入/编辑店铺会员信息接口
     *
     * @param request
     * @return
     * @throws Exception
     */
    @RequestMapping(value =
            {"/service/shopMember/updateShopMember", "/token/shopMember/updateShopMember",
                    "/session/shopMember/updateShopMember"}, method = RequestMethod.POST, consumes = "application/json;charset=UTF-8", produces = "application/json;charset=UTF-8")
    @ResponseBody
    public Object updateShopMember(HttpEntity<String> entity, HttpServletRequest request) {

        logger.info("录入/编辑店铺会员信息接口-start");
        try {
            Map<String, Object> map = new HashMap<String, Object>();

            /**
             * 步骤一：初步验证解析并验证请求参数
             *
             */
            /* 请求参数解析 */
            ShopMemberDto shopMemberDto = (ShopMemberDto) JacksonUtil.postJsonToObj(entity, ShopMemberDto.class,
                    DateUtils.DATE_FORMAT);
            CommonValidUtil.validLongNull(shopMemberDto.getShopId(), CodeConst.CODE_PARAMETER_NOT_VALID,
                    CodeConst.MSG_FORMAT_ERROR_SHOP_ID);
            // 校验商铺是否存在
            collectService.checkShopExists(shopMemberDto.getShopId());

            /**
             * 步骤二：更新/保存会员信息
             *
             * 1.校验会员姓名，手机号，等级，状态； 2.注意重复会员条件； 3.注意处理会员等级； 4.注意是否插入平台会员；
             */
            /* 根据memberId是否为空判断是添加店内会员还是更新店内会员 */
            /*
             * if (shopMemberDto.getMemberId() == null) { // 添加店内会员
             * logger.debug("增加会员"); ShopMemberDto resultDto = shopMemberService
             * .addShopMember(shopMemberDto, 1); memberId =
             * resultDto.getMemberId(); msg = "录入店铺会员信息接口成功！"; } else { //
             * 更新店内会员信息 logger.debug("更新会员");
             * shopMemberService.updateShopMember(shopMemberDto); memberId =
             * shopMemberDto.getMemberId(); msg = "编辑店铺会员信息接口成功！"; }
             */
            /* 处理并返回处理结果 */
            // name存在的话校验name
            /* 针对php的空串""校验 */
            CommonValidUtil.validLongNull(shopMemberDto.getMobile(), CodeConst.CODE_PARAMETER_NOT_NULL,
                    CodeConst.MSG_MOBILE_NOT_NULL);
            String name = shopMemberDto.getName();
            if (name != null && name.equals("")) {
                shopMemberDto.setName(null);
            }
            String address = shopMemberDto.getAddress();
            if (address != null && address.equals("")) {
                shopMemberDto.setAddress(null);
            }
            String birthdayDate = shopMemberDto.getBirthdayDate();
            if (birthdayDate != null && birthdayDate.equals("")) {
                shopMemberDto.setBirthdayDate(null);
            }
            String memberCardNo = shopMemberDto.getMemberCardNo();
            if (memberCardNo != null && memberCardNo.equals("")) {
                shopMemberDto.setMemberCardNo(null);
            }
            String memberDesc = shopMemberDto.getMemberDesc();
            if (memberDesc != null && memberDesc.equals("")) {
                shopMemberDto.setMemberDesc(null);
            }
            String rmobile = shopMemberDto.getReferMobile();
            if (rmobile != null && rmobile.equals("")) {
                shopMemberDto.setReferMobile(null);
            }
            String weixin = shopMemberDto.getWeixinId();
            if (weixin != null && weixin.equals("")) {
                shopMemberDto.setWeixinId(null);
            }
            String veriCode = shopMemberDto.getVeriCode();
            if (veriCode != null && veriCode.equals("")) {
                shopMemberDto.setVeriCode(null);
            }
            String weixinNo = shopMemberDto.getWeixinNo();
            if (weixinNo != null && weixinNo.equals("")) {
                shopMemberDto.setWeixinNo(null);
            }
            Map<String, String> re = shopMemberService.editShopMember(shopMemberDto);
            /**
             * 步骤三：返回处理结果
             */
            map.put("memberId", re.get("memberId"));
            return ResultUtil.getResult(CodeConst.CODE_SUCCEED, re.get("msg"), map);
        } catch (ServiceException e) {
            throw new APIBusinessException(e);
        } catch (Exception e) {
            this.logger.error("录入/编辑店铺会员信息接口-系统异常", e);
            throw new APISystemException("录入/编辑店铺会员信息接口-系统异常", e);
        }
    }

    /**
     * PSM5：操作店铺会员信息接口
     *
     * @param request
     * @return
     * @throws Exception
     */
    @RequestMapping(value =
            {"/service/shopMember/operateShopMemberInfo", "/token/shopMember/operateShopMemberInfo",
                    "/session/shopMember/operateShopMemberInfo"}, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public Object operateShopMemberInfo(HttpServletRequest request) {
        /**
         * 步骤一：校验查询参数
         *
         * 校验请求参数中的必填参数是否填写，校验请求参数的类型
         *
         * @return
         */

        /**
         * 步骤二：校验查询参数
         *
         * 根据操作类型对多个店铺会员执行相应的操作
         *
         * 数据表：1dcq_shop_member
         *
         * @return
         */
        logger.info("操作店铺会员信息接口-start" + request.getQueryString());
        try {

            Map<String, Object> requestMap = checkParamForOperateShopMember(request);
            Integer operateType = Integer.valueOf(requestMap.get("operateType").toString());
            if (operateType.intValue() == 1) {
                ProgramUtils.executeBeanByProgramConfigCode("operateProcessor_1_1", 1, requestMap);
            } else {
                throw new ValidateException(CodeConst.CODE_PARAMETER_NOT_VALID, "暂不支持其他操作方式");
            }
            return ResultUtil.getResult(CodeConst.CODE_SUCCEED, "操作店铺会员信息接口成功！", null);
        } catch (ServiceException e) {
            throw new APIBusinessException(e);
        } catch (Exception e) {
            this.logger.error("操作店铺会员信息接口-系统异常", e);
            throw new APISystemException("操作店铺会员信息接口-系统异常", e);
        }
    }

    private Map<String, Object> checkParamForOperateShopMember(HttpServletRequest request) throws Exception {

        Map<String, Object> requestMap = new HashMap<String, Object>();

        String memberIdStr = RequestUtils.getQueryParam(request, "memberId");
        String operateTypeStr = RequestUtils.getQueryParam(request, "operateType");

        CommonValidUtil.validObjectNull(memberIdStr, CodeConst.CODE_PARAMETER_NOT_NULL, "店内会员Id不能空");

        requestMap.put("memberId", memberIdStr.split(","));

        CommonValidUtil.validObjectNull(operateTypeStr, CodeConst.CODE_PARAMETER_NOT_NULL, "操作类型不能空");

        Integer operateType = CommonValidUtil.validStrIntFmt(operateTypeStr, CodeConst.CODE_PARAMETER_NOT_VALID,
                "操作类型错误");

        requestMap.put("operateType", operateType);

        return requestMap;
    }

    /**
     * PSM6：微信扫码绑定店铺会员接口
     *
     * @param request
     * @return
     * @throws Exception
     */
    @RequestMapping(value =
            {"/service/shopMember/weixinBindShopMember", "/token/shopMember/weixinBindShopMember",
                    "/session/shopMember/weixinBindShopMember"}, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public Object weixinBindShopMember(HttpServletRequest request) {
        /**
         * 步骤一：校验查询参数
         *
         * 校验请求参数中的必填参数是否填写，校验请求参数的类型
         * @return
         */

        /**
         * 步骤一：微信号绑定商铺会员
         *
         * 数据库：1dcq_shop_member
         * @return
         */
        logger.info("微信扫码绑定店铺会员接口-start" + request.getQueryString());
        try {
            String openId = RequestUtils.getQueryParam(request, "openId");
            String shopIdStr = RequestUtils.getQueryParam(request, "shopId");
            String nickName = RequestUtils.getQueryParam(request, "nickName");

            CommonValidUtil.validObjectNull(openId, CodeConst.CODE_PARAMETER_NOT_NULL, "微信openId不能空");

            CommonValidUtil.validObjectNull(shopIdStr, CodeConst.CODE_PARAMETER_NOT_NULL, "商铺Id不能空");

            Long shopId = CommonValidUtil.validStrLongFmt(shopIdStr, CodeConst.CODE_PARAMETER_NOT_VALID, "店铺Id类型错误");

            int flag = this.shopServcie.queryShopExists(Long.valueOf(shopId));
            CommonValidUtil.validPositInt(flag, CodeConst.CODE_PARAMETER_NOT_EXIST, "商铺不存在");

            CommonValidUtil.validObjectNull(nickName, CodeConst.CODE_PARAMETER_NOT_NULL, "微信昵称不能空");

            Map<String, Object> resultMap = shopMemberService.weixinBindShopMember(openId, shopId, nickName);
            return ResultUtil.getResult(CodeConst.CODE_SUCCEED, "微信扫码绑定店铺会员接口成功！", resultMap);
        } catch (ServiceException e) {
            throw new APIBusinessException(e);
        } catch (Exception e) {
            this.logger.error("微信扫码绑定店铺会员接口-系统异常", e);
            throw new APISystemException("微信扫码绑定店铺会员接口-系统异常", e);
        }
    }

    /**
     * PSM7：批量导入店铺会员接口
     *
     * @param request
     * @return
     * @throws Exception
     */
    @RequestMapping(value =
            {"/shopMember/importShopMemberByExcel", "/service/shopMember/importShopMemberByExcel",
                    "/token/shopMember/importShopMemberByExcel",
                    "/session/shopMember/importShopMemberByExcel"}, method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public Object importShopMemberByExcel(HttpServletRequest request, String shopId, String sourceName, Integer modelVersion) {

        logger.info("批量导入店铺会员接口-start");
        Map<String, Object> map = new HashMap<String, Object>();
        try {
            modelVersion = null == modelVersion ? 0 : modelVersion;
            /* 验证shopId不为空，并转换相应类型 */
            Long shopIdl = CommonValidUtil.validStrLongFmt(shopId, CodeConst.CODE_PARAMETER_NOT_VALID,
                    CodeConst.MSG_FORMAT_ERROR_SHOP_ID);
            // 校验商铺是否存在
            collectService.checkShopExists(shopIdl);

            MultipartRequest mrequest = (MultipartRequest) request;
            MultipartFile file = mrequest.getFile(sourceName);
            if (file == null) {
                file = mrequest.getFile(mrequest.getFileNames().next());
            }

            InputStream in = file.getInputStream();
            /* 解析json数据流 */
            List<ShopMemberDto> shopMemberDtos = ParseShopMemberSource.parseExcel(in, modelVersion);

            /* 设置shopId */
            for (ShopMemberDto dto : shopMemberDtos) {
                dto.setShopId(shopIdl);
            }

            /* 添加会员 */
            List<Map<String, String>> tempresult = shopMemberService.addShopMembers(shopMemberDtos, 1);

            /* 处理返回结果 */
            Integer totalNum = shopMemberDtos.size();
            Integer failureNum = 0;
            Integer code = CodeConst.CODE_SUCCEED;
            String msg = "批量导入店铺会员接口成功！";
            if (tempresult != null && tempresult.size() != 0) {
                failureNum = tempresult.size();
                if (tempresult.size() < totalNum) {
                    code = CodeConst.CODE_PARTIAL_SUCCESS;
                    msg = "部分导入成功！";
                } else {
                    msg = "导入失败！";
                    code = CodeConst.CODE_FAILURE;
                }
            }

            map.put("successNum", totalNum - failureNum);
            map.put("failureNum", failureNum);
            map.put("failList", tempresult);

            return ResultUtil.getResult(code, msg, map);
        } catch (ServiceException e) {
            throw new APIBusinessException(e);
        } catch (Exception e) {
            this.logger.error("批量导入店铺会员接口-系统异常", e);
            throw new APISystemException("批量导入店铺会员接口-系统异常", e);
        }
    }

    /**
     * PSM8：查询商铺内会员总数及一段时间内新增数量
     *
     * @param request
     * @return
     * @throws Exception
     */
    @RequestMapping(value =
            {"/shopMember/shopMemberStat", "/service/shopMember/shopMemberStat",
                    "/token/shopMember/shopMemberStat",
                    "/session/shopMember/shopMemberStat"}, method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public Object shopMemberStat(HttpServletRequest request, Long shopId, String startDate, String endDate) {
        try {
            // 校验商铺是否存在
            if (null == shopId) {
                throw new ValidateException(CodeConst.CODE_PARAMETER_NOT_VALID, "shopId不能为空");
            }
            //校验店铺是否存在
            collectService.checkShopExists(shopId);
            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            Date startDateD = null;
            Date endDateD = null;
            //如果截止日期没上传则默认当前天
            if(StringUtils.isBlank(endDate)){
                endDateD = new Date();
            }else{
                endDate = endDate.trim();
                Date tempDate = null;
                try {
                    tempDate = dateFormat.parse(endDate);
                } catch (ParseException e) {
                    logger.debug(e.getMessage(), e);
                    throw new ValidateException(CodeConst.CODE_PARAMETER_NOT_VALID, "endDate格式错误");
                }
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(tempDate);
                calendar.set(Calendar.DAY_OF_MONTH, calendar.get(Calendar.DAY_OF_MONTH) + 1);
                endDateD = calendar.getTime();
            }
//            dateFormat.parse(endDate.trim().substring(0,endDate.trim().length()) + );
            if(StringUtils.isBlank(startDate))
            {   //如果为空，则起始时间为当月第一天
                Calendar calendar = Calendar.getInstance();
                int curYear = calendar.get(Calendar.YEAR);
                int curMonth = calendar.get(Calendar.MONTH) + 1;
                startDateD = dateFormat.parse(curYear + "-" + curMonth + "-" +"01");
            }else {
                try {
                    startDateD = dateFormat.parse(startDate.trim());
                } catch (ParseException e) {
                    logger.debug(e.getMessage(), e);
                    throw new ValidateException(CodeConst.CODE_PARAMETER_NOT_VALID, "startDate格式错误");
                }
            }
            Map<String, Object> queryParams = new HashMap<String, Object>();
            queryParams.put("shopId", shopId);
            queryParams.put("startDate", startDateD);
            queryParams.put("endDate", endDateD);
            Map<String, Object> rs = shopMemberService.getShopMemberStat(queryParams);
            return ResultUtil.getResult(CodeConst.CODE_SUCCEED, "获取店铺会员统计信息成功！", rs);
        } catch (ServiceException e) {
            throw new APIBusinessException(e);
        } catch (Exception e) {
            this.logger.error("查询商铺内会员总数及一段时间内新增数量", e);
            throw new APISystemException("查询商铺内会员总数及一段时间内新增数量", e);
        }
    }
    /**
     * PSM9：查询商铺内会员总数及一段时间内新增数量
     *
     * @param request
     * @return
     * @throws Exception
     */
    @RequestMapping(value =
            {"/shopMember/shopMemberStatDetail", "/service/shopMember/shopMemberStatDetail",
                    "/token/shopMember/shopMemberStatDetail",
                    "/session/shopMember/shopMemberStatDetail"}, method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public Object shopMemberStatDetail(HttpServletRequest request, Long shopId, String startDate, String endDate, Integer dateType, Integer pNo, Integer pSize) {
        try {
            //dateType为必填
            if(null == dateType)
            {
                throw new ValidateException(CodeConst.CODE_PARAMETER_NOT_VALID, "dateType不能为空");
            }
            //startDate为必填
            if(null == startDate)
            {
                throw new ValidateException(CodeConst.CODE_PARAMETER_NOT_VALID, "startDate不能为空");
            }
            // 校验商铺是否存在
            if (null == shopId) {
                throw new ValidateException(CodeConst.CODE_PARAMETER_NOT_VALID, "shopId不能为空");
            }
            //校验店铺是否存在
            collectService.checkShopExists(shopId);
            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            Date startDateD = null;
            Date endDateD = null;
            if(StringUtils.isBlank(endDate)){
                endDateD = new Date();
            }else{
                try {
                    endDateD = dateFormat.parse(endDate.trim());
                } catch (ParseException e) {
                    logger.debug(e.getMessage(), e);
                    throw new ValidateException(CodeConst.CODE_PARAMETER_NOT_VALID, "startDate格式错误");
                }
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(endDateD);
                calendar.set(Calendar.DAY_OF_MONTH, calendar.get(Calendar.DAY_OF_MONTH) + 1);
                endDateD = calendar.getTime();
            }
            try {
                startDateD = dateFormat.parse(startDate);
            } catch (ParseException e) {
                logger.debug(e.getMessage(), e);
                throw new ValidateException(CodeConst.CODE_PARAMETER_NOT_VALID, "startDate格式错误");
            }
            pNo = ( null == pNo || pNo <= 0) ? 1 : pNo;
            pSize = null == pSize ? 10 : pSize;
            Map<String, Object> searchParams = new HashMap<String, Object>();
            searchParams.put("shopId", shopId);
            searchParams.put("startDate", startDateD);
            searchParams.put("endDate", endDateD);
            searchParams.put("dateType", dateType);
            searchParams.put("pNo", pNo);
            searchParams.put("pSize", pSize);
            Map<String, Object> rs = shopMemberService.getShopMemberStatDetail(searchParams);
            return ResultUtil.getResult(CodeConst.CODE_SUCCEED, "获取店铺会员统计信息成功！", rs);
        } catch (ServiceException e) {
            throw new APIBusinessException(e);
        } catch (Exception e) {
            this.logger.error("批量导入店铺会员接口-系统异常", e);
            throw new APISystemException("批量导入店铺会员接口-系统异常", e);
        }
    }
    /**
     * PSM11：根据指定条件查询/统计店内会员信息接口
     *
     * @param request
     * @return
     * @throws Exception
     */
    @RequestMapping(value =
            {"/shopMember/getShopMemberInfo", "/service/shopMember/getShopMemberInfo",
                    "/token/shopMember/getShopMemberInfo",
                    "/session/shopMember/getShopMemberInfo"}, method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public Object getShopMemberInfo(HttpServletRequest request, HttpEntity<String> entity) throws  Exception{
        logger.debug("开始查询会员情况：");
        Map<String, String> queryStr = JacksonUtil.postJsonToMap(entity);
        String shopIdStr = queryStr.get("shopId");
        // 校验商铺是否存在
        if (StringUtils.isBlank(shopIdStr)) {
            throw new ValidateException(CodeConst.CODE_PARAMETER_NOT_VALID, "shopId不能为空");
        }
        Long shopId = null;
        try{
            shopId = Long.parseLong(shopIdStr.trim());
        }catch (Exception e){
            throw new ValidateException(CodeConst.CODE_PARAMETER_NOT_VALID, "shopId格式不正确");
        }
        //校验店铺是否存在
        collectService.checkShopExists(shopId);
        String detailStr = queryStr.get("detail");
        Integer detail = null;
        try{
            detail = Integer.valueOf(detailStr.trim());
        }catch (Exception e){
            throw new ValidateException(CodeConst.CODE_PARAMETER_NOT_VALID, "detail格式不正确");
        }
        if(detail <= 0 || detail > 2)
        {
            throw new ValidateException(CodeConst.CODE_PARAMETER_NOT_VALID, "detail格式不正确");
        }
        List<Object> rs = shopMemberService.getShopMemberInfo(shopId,detail, queryStr.get("params"));

        return ResultUtil.getResult(CodeConst.CODE_SUCCEED, "查询成功!", rs);
    }

    /**
     * 4.9.10	PSM10：会员消费统计接口 
     *
     * @param request
     * @return
     * @throws Exception
     */
    @RequestMapping(value =
            {"/service/shopMember/getMemberConsumerStat",
             "/token/shopMember/getMemberConsumerStat",
             "/session/shopMember/getMemberConsumerStat"}, method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public Object getMemberConsumerStat(HttpServletRequest request) {
    	logger.info("PSM10：会员消费统计接口 -start" + request.getQueryString());
    	try {
    		
            String shopIdStr = RequestUtils.getQueryParam(request, "shopId");
            String orderStartTime = RequestUtils.getQueryParam(request, "orderStartTime");
            String orderEndTime = RequestUtils.getQueryParam(request, "orderEndTime");
            String memberJoinStartTime = RequestUtils.getQueryParam(request, "memberJoinStartTime");
            String memberJoinEndTime = RequestUtils.getQueryParam(request, "memberJoinEndTime");
            String keyword = RequestUtils.getQueryParam(request, "keyword");
            String searchCondition = RequestUtils.getQueryParam(request, "searchCondition");
            String orderBy = RequestUtils.getQueryParam(request, "orderBy");
            //orderBy为空默认设置为0 按消费次数排序
            if(orderBy ==null){
            	orderBy = "0";
            }
            String orderByMode = RequestUtils.getQueryParam(request, "orderByMode");
            String pNoStr = RequestUtils.getQueryParam(request, "pNo");
            String pSizeStr = RequestUtils.getQueryParam(request, "pSize");
            CommonValidUtil.validObjectNull(shopIdStr, CodeConst.CODE_PARAMETER_NOT_NULL, "店铺Id不能为空");
            Long shopId = CommonValidUtil.validStrLongFmt(shopIdStr, CodeConst.CODE_PARAMETER_NOT_VALID, "店铺Id类型错误");
            if(null != keyword){
            	CommonValidUtil.validObjectNull(searchCondition, CodeConst.CODE_PARAMETER_NOT_NULL, "搜索条件必填");
            }
            
            Integer pNo = PageModel.handPageNo(pNoStr);
            Integer pSize = PageModel.handPageSize(pSizeStr);
            
            Map<String, Object> searchParams = new HashMap<String, Object>();
            searchParams.put("shopId", shopId);
            searchParams.put("orderStartTime", orderStartTime);
            searchParams.put("orderEndTime", orderEndTime);
            searchParams.put("memberJoinStartTime", memberJoinStartTime);
            searchParams.put("memberJoinEndTime", memberJoinEndTime);
            searchParams.put("keyword", keyword);
            searchParams.put("searchCondition", searchCondition);
            searchParams.put("orderBy", orderBy);
            searchParams.put("searchCondition", searchCondition);
            searchParams.put("orderByMode", orderByMode);
            searchParams.put("pNo", (pNo-1)*pSize);
            searchParams.put("pSize", pSize);
            searchParams.put("orderStatus", CommonConst.ORDER_STATUS_FINISH);
            searchParams.put("memberStatus", CommonConst.MEMBER_STATUS_DELETE);
            Map<String, Object> resultMap = shopMemberService.getMemberConsumerStat(searchParams);
            resultMap.put("pNo", pNo);
            return ResultUtil.getResultJson(CodeConst.CODE_SUCCEED, "会员消费统计成功！", resultMap, DateUtils.DATETIME_FORMAT);
        } catch (ServiceException e) {
            throw new APIBusinessException(e);
        } catch (Exception e) {
            this.logger.error("会员消费统计接口-系统异常", e);
            throw new APISystemException("会员消费统计接口-系统异常", e);
        }
    }

   
    
    
}
