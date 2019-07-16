package com.fensibox.cms.result;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fensibox.cms.entity.Order;
import lombok.Data;

@Data
public class QueryOrderOut {

    private int id;

    private int status;

    private int num;

    private int tnum;

    @JsonProperty("start_num")
    private int startNum;

    @JsonProperty("now_num")
    private int nowNum;

    private int gid;

    @JsonProperty("model_id")
    private int modelId;

    private String value1;

    private String value2;

    private String value3;

    private String value4;

    private String value5;

    private String value6;

    private String value7;

    private String value8;

    private String value9;

    private String value10;

    private String value11;

    private String value12;

    private String remark;

    @JsonProperty("created_at")
    private String createdAt;

    public Order buildOrder(){
        Order order = new Order();
        order.setGid(Long.valueOf(gid));
        order.setNum(num);
        order.setNowNum(nowNum);
        order.setOid(Long.valueOf(id));
        order.setModelId(Long.valueOf(modelId));
        order.setRemark(remark);
        order.setStartNum(startNum);
        order.setStatus(status);
        order.setTnum(tnum);
        return order;
    }
}
