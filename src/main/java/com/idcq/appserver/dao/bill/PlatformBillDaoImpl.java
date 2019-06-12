package com.idcq.appserver.dao.bill;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.idcq.appserver.dao.BaseDao;
import com.idcq.appserver.dto.bill.PlatformBillDto;
import com.idcq.appserver.utils.NumberUtil;

/**
 * 会员dao
 * 
 * @author Administrator
 * 
 * @date 2015年3月3日
 * @time 下午5:10:44
 */
@Repository
public class PlatformBillDaoImpl extends BaseDao<PlatformBillDto> implements IPlatformBillDao{

	@Override
	public int insertPlatformBill(PlatformBillDto platformBillDto) throws Exception {
        Double money = NumberUtil.formatDouble(platformBillDto.getMoney(), 4);
        platformBillDto.setMoney(money);
        if (money == 0) {
            return 0;
        }
        platformBillDto.setMoney(money);
		return super.insert("insertPlatformBill", platformBillDto);
	}

	public int updatePlatformBill(PlatformBillDto platformBillDto)
			throws Exception {
		return super.update("updatePlatformBill", platformBillDto);
	}

    public int insertPlatformBillMiddle(PlatformBillDto platformBillDto) throws Exception
    {
        Double money = NumberUtil.formatDouble(platformBillDto.getMoney(), 4);
        platformBillDto.setMoney(money);
        if (money == 0) {
            return 0;
        }
        platformBillDto.setMoney(money);
        return super.insert("insertPlatformBillMiddle", platformBillDto);
    }

    public List<PlatformBillDto> getPlatformBillMiddleByOrderId(String orderId) throws Exception
    {
        return super.findList("getPlatformBillMiddleByOrderId", orderId);
    }

    public void deletePlatformBillMiddle(String orderId) throws Exception
    {
        super.delete("deletePlatformBillMiddle", orderId);
    }

    public List<PlatformBillDto> getPlatformBillByOrderId(String orderId) throws Exception
    {
        return super.findList("getPlatformBillByOrderId", orderId);
    }

}
