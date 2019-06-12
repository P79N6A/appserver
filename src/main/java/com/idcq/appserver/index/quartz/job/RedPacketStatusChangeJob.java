package com.idcq.appserver.index.quartz.job;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.scheduling.quartz.QuartzJobBean;

import com.idcq.appserver.common.enums.RedPacketStatusEnum;
import com.idcq.appserver.dao.redpacket.IRedPacketDao;
import com.idcq.appserver.dao.user.IUserDao;
import com.idcq.appserver.dto.redpacket.RedPacketDto;
import com.idcq.appserver.dto.user.UserDto;
import com.idcq.appserver.utils.BeanFactory;
import com.idcq.appserver.utils.ProgramUtils;

/**
 * 红包状态切换
 * @ClassName: RedPacketStatusChange
 * @Description: TODO
 * @author 张鹏程
 * @date 2016年3月14日 上午11:08:00
 * 
 */
public class RedPacketStatusChangeJob extends QuartzJobBean
{

    @Override
    protected void executeInternal(JobExecutionContext context) throws JobExecutionException
    {
        /**
         * 步骤一:分页找出红包结束时间小于当前时间且状态为可用的
         * 
         */
        int toPage = 1;// 起始页
        int pageSize = 50;// 分页大小
        try
        {
            RedPacketDto redPacketDto = new RedPacketDto();
            redPacketDto.setStatus(RedPacketStatusEnum.USEABLE.getValue());// 红包状态为可用的
            IRedPacketDao redPacketDao = (IRedPacketDao) BeanFactory.getBean(IRedPacketDao.class);
            while (true)
            {
                List<RedPacketDto> redPacketDtoList = redPacketDao.getRedPacketDtoList(redPacketDto, toPage, pageSize);
                if (redPacketDtoList.size() == 0)
                {
                    break;
                }
                toPage++;
                dealRedPacket(redPacketDtoList, redPacketDao);
            }
            /**
             * 步骤二:将过期的红包状态进行变更
             * 
             */

            /**
             * 通知红包的持有者。红包过期了
             */
        }
        catch (Exception e)
        {

        }
    }

    /**
     * 处理红包
     * @Title: dealRedPacket
     * @param @param redPacketList
     * @param @param redPacketDao
     * @return void 返回类型
     * @throws
     */
    private void dealRedPacket(List<RedPacketDto> redPacketList, IRedPacketDao redPacketDao) throws Exception
    {
        List<RedPacketDto> needChangeStatusRedPacket = new ArrayList<RedPacketDto>();
        Date currentDate = new Date();
        List<Long> userIdList = new ArrayList<Long>();
        Map<String, Long> redPacketMap = new HashMap<String, Long>();
        for (RedPacketDto redPacketDto : redPacketList)
        {// 红包列表
            if (RedPacketStatusEnum.USEABLE.getValue() == redPacketDto.getStatus())
            {// 红包状态为可用
                if (currentDate.compareTo(redPacketDto.getEndDate()) > 0)
                {// 过了活动结束时间，应该变更活动的状态
                    redPacketDto.setStatus(RedPacketStatusEnum.EXPIRE.getValue());// 状态为已过期
                    needChangeStatusRedPacket.add(redPacketDto);
                    redPacketMap.put(redPacketDto.getRedPacketId() + "", redPacketDto.getUserId());
                }
            }
        }
        if (needChangeStatusRedPacket.size() > 0)
        {// 需要改变红包的状态
            redPacketDao.batchUpdateRedPacket(redPacketList);
        }
        IUserDao userDao = BeanFactory.getBean(IUserDao.class);
        if (userIdList.size() > 0)
        {// 向会员发送消息
            List<UserDto> userDtoList = userDao.queryUserByIdList(userIdList);
            Map<String, UserDto> userDtoMap = buildUserDtoMap(userDtoList);
            for (Entry<String, Long> userDtoItem : redPacketMap.entrySet())
            {
                Map<String, Object> params = new HashMap<String, Object>();
                params.put("userData", userDtoMap.get(userDtoItem.getValue() + ""));// 通知的会员
                params.put("redPacketId", userDtoItem.getKey());// 红包ID
                ProgramUtils.executeBeanByExecutePointCode("pushRedPacketUserPoint", 1, params);
                // MessageControl.execute(null,jsonContent.toString(), null,2,
                // BizTypeEnum.USER.getValue(), userDto.getUserId(), userDto);
            }
        }
    }

    /**
     * 构建用户map
     * @Title: buildUserDtoMap
     * @param @param userDtoList
     * @param @return
     * @return Map<String,UserDto> 返回类型
     * @throws
     */
    private Map<String, UserDto> buildUserDtoMap(List<UserDto> userDtoList)
    {
        Map<String, UserDto> userDtoMap = new HashMap<String, UserDto>();
        for (UserDto userDto : userDtoList)
        {
            userDtoMap.put(userDto.getUserId() + "", userDto);
        }
        return userDtoMap;
    }

}
