package com.fensibox.cms.controller.vm.user;

import lombok.Data;

@Data
public class UserInVM {

    private String username;

    private String inviteCode;

    private Integer enabled;

}
