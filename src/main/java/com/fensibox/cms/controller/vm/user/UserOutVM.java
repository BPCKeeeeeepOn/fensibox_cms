package com.fensibox.cms.controller.vm.user;

import lombok.Data;

@Data
public class UserOutVM {


    private String username;

    private Integer role;

    private String inviteCode;

    private String createdTime;
}
