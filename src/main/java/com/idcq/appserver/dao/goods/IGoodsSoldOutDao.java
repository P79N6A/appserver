package com.idcq.appserver.dao.goods;

import com.idcq.appserver.dto.goods.GoodsSoldOutDto;

import java.util.List;

/**
 * Created by Administrator on 2016/8/19 0019.
 */
public interface IGoodsSoldOutDao {
    /**
     * 以存在条件交集的方式取符合条件的记录
     * @param shopId
     * @param goodsId
     * @return
     */
    List<GoodsSoldOutDto> getItemsByCondition(GoodsSoldOutDto searchCondition, int pNo, int pSize);

    void batchInsert(List<GoodsSoldOutDto> goodsSoldOutDtos);

    void batchDelete(List<GoodsSoldOutDto> goodsSoldOutDtos);
}
