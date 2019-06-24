package com.fensibox.cms.controller.vm.deposit;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class DepositListOutVM {

    private String id;

    private String username;

    private String phone;

    private String money;

    private Integer status;

    @JsonProperty("deposit_type")
    private Integer depositType;

    @JsonProperty("created_time")
    private String createdTime;
}
