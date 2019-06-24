package com.fensibox.cms.controller.vm.order;

import lombok.Data;

@Data
public class OrderOutVM {

    private String id;

    private String uid;

    private String username;

    private Long oid;

    private Integer status;

    private Integer num;

    private Integer tnum;

    private Integer startNum;

    private Integer nowNum;

    private Long gid;

    private String gname;

    private Long modelId;

    private Double orderPrice;

    private String remark;

    private String createdTime;

}
