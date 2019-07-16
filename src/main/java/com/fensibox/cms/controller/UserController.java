package com.fensibox.cms.controller;

import com.fensibox.cms.common.ResponseResult;
import com.fensibox.cms.controller.vm.PageVM;
import com.fensibox.cms.controller.vm.user.UserCreditInVM;
import com.fensibox.cms.controller.vm.user.UserInVM;
import com.fensibox.cms.service.UserService;
import com.fensibox.cms.utils.CurrentUserUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/user")
@Slf4j
public class UserController {


    @Autowired
    private UserService userService;

    @Autowired
    private CurrentUserUtils currentUserUtils;

    @PostMapping("/add")
    public ResponseResult add(@RequestBody UserInVM userInVM) {
        userService.addUser(userInVM);
        return ResponseResult.success();
    }

    @GetMapping("/detail/{id}")
    public ResponseResult detail(@PathVariable("id") Long id) {
        return ResponseResult.success().body(userService.detail(id));
    }

    @PutMapping("/update/{id}")
    public ResponseResult updateCustomUser(@PathVariable("id") Long id, @RequestBody UserInVM userInVM) {
        userService.updateCustomUser(id, userInVM);
        return ResponseResult.success();
    }

    @PutMapping("/update/credit/{id}")
    public ResponseResult updateCredit(@PathVariable("id") Long id, @RequestBody UserCreditInVM userCreditInVM) {
        userService.updateCredit(id, userCreditInVM.getCredit());
        return ResponseResult.success();
    }

    @GetMapping("/info")
    public ResponseResult info() {
        return ResponseResult.success().body(currentUserUtils.getCurrUser());
    }

    @GetMapping("/userList")
    public ResponseResult selectCustomUserList(PageVM pageVM) {
        return ResponseResult.success().body(userService.selectCustomUserList(pageVM));
    }

    @GetMapping("/memberList")
    public ResponseResult selectClientUserList(PageVM pageVM) {
        return ResponseResult.success().body(userService.selectClientUserList(pageVM));
    }

    @PostMapping("/editPwd")
    public ResponseResult editPassword(@RequestBody Map<String, String> params) {
        String password = params.get("password");
        String newPassword = params.get("new_password");
        ;
        userService.editPassword(password, newPassword);
        return ResponseResult.success();
    }

    @GetMapping("/achievement/list")
    public ResponseResult achievementList() {
        return ResponseResult.success().body(userService.selectAchievementList());
    }

    @GetMapping("/achievement/detail")
    public ResponseResult achievementDetail() {
        return ResponseResult.success().body(userService.selectAchievementDetail());
    }
}
