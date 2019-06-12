package com.idcq.appserver.dao.goods;

import com.idcq.appserver.dao.BaseDao;
import com.idcq.appserver.dto.goods.GoodsSoldOutDto;
import org.apache.ibatis.session.RowBounds;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 处理商品沽清
 * Created by Administrator on 2016/8/19 0019.
 */
@Repository
public class GoodsSoldOutDaoImpl extends BaseDao<GoodsSoldOutDto> implements IGoodsSoldOutDao{

    @Override
    public List<GoodsSoldOutDto> getItemsByCondition(GoodsSoldOutDto searchCondition, int pNo, int pSize) {
        pNo = pNo <= 0 ? 1 : pNo;
        RowBounds rowBounds = new RowBounds((pNo - 1) * pSize, pSize);
        return this.getSqlSession().selectList(generateStatement("getItemsByCondition"), searchCondition, rowBounds);
    }

    @Override
    public void batchInsert(List<GoodsSoldOutDto> goodsSoldOutDtos) {
        this.getSqlSession().insert(generateStatement("batchInsert"), goodsSoldOutDtos);
    }

    @Override
    public void batchDelete(List<GoodsSoldOutDto> goodsSoldOutDtos) {
        this.getSqlSession().delete("batchDelete", goodsSoldOutDtos);
    }
}
