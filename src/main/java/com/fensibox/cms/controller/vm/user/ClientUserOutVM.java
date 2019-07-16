package com.fensibox.cms.controller.vm.user;

import lombok.Data;

@Data
public class ClientUserOutVM {


    private String id;

    private String phone;

    private String username;

    private Integer role;

    private String inviteCode;

    private Double credit;

    private Integer enabled;

    private String createdTime;
}
