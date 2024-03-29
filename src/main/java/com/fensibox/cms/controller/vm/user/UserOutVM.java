package com.fensibox.cms.controller.vm.user;

import lombok.Data;

@Data
public class UserOutVM {

    private String id;

    private String username;

    private Integer role;

    private String inviteCode;

    private Integer enabled;

    private String createdTime;
}
