package com.idcq.appserver.dto.shop;

/**
 * Created by Administrator on 2016/8/6 0006.
 */
public class ShopMemberStatInfo {


    private String date;

    private Integer memberAddNum;

    public void setDate(String date) {
        this.date = date;
    }

    public void setMemberAddNum(Integer memberAddNum) {
        this.memberAddNum = memberAddNum;
    }
    public String getDate() {
        return date;
    }

    public Integer getMemberAddNum() {
        return memberAddNum;
    }
}
