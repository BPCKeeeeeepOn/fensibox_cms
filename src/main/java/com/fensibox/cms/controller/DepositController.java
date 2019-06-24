package com.fensibox.cms.controller;

import com.fensibox.cms.common.ResponseResult;
import com.fensibox.cms.controller.vm.PageVM;
import com.fensibox.cms.controller.vm.order.OrderContentVM;
import com.fensibox.cms.service.DepositService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/deposit")
@Slf4j
public class DepositController {


    @Autowired
    private DepositService depositService;

    @GetMapping("/list")
    public ResponseResult list(OrderContentVM orderContentVM, PageVM pageVM) {
        return ResponseResult.success().body(depositService.list(orderContentVM, pageVM));
    }
}
