package com.fensibox.cms.controller;

import com.fensibox.cms.common.ResponseResult;
import com.fensibox.cms.controller.vm.PageVM;
import com.fensibox.cms.controller.vm.order.OrderContentVM;
import com.fensibox.cms.service.OrderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/order")
@Slf4j
public class OrderController {


    @Autowired
    private OrderService orderService;

    @GetMapping("/list")
    public ResponseResult list(OrderContentVM orderContentVM, PageVM pageVM) {
        return ResponseResult.success().body(orderService.list(orderContentVM, pageVM));
    }

    @GetMapping("/detail/{id}")
    public ResponseResult detail(@PathVariable("id") Long id) {
        return ResponseResult.success().body(orderService.detail(id));
    }
}
