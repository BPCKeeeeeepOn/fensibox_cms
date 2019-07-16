package com.fensibox.cms.controller;

import com.fensibox.cms.common.ResponseResult;
import com.fensibox.cms.controller.vm.PageVM;
import com.fensibox.cms.controller.vm.product.ProductInVm;
import com.fensibox.cms.service.ProductService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/product")
@Slf4j
public class ProductController {


    @Autowired
    private ProductService productService;

    @GetMapping("/list")
    public ResponseResult list(String name, PageVM pageVM) {
        return ResponseResult.success().body(productService.list(pageVM, name));
    }

    @GetMapping("/detail/{id}")
    public ResponseResult detail(@PathVariable("id") Long id) {
        return ResponseResult.success().body(productService.detail(id));
    }

    @PostMapping("/price/update")
    public ResponseResult updatePrice(@RequestBody ProductInVm productInVm) {
        productService.updatePrice(productInVm.getId(), productInVm.getPrice());
        return ResponseResult.success();
    }

    @PostMapping("/member/update")
    public ResponseResult updateMemberPrice(@RequestBody ProductInVm productInVm) {
        productService.updateMemberPrice(productInVm.getId(), productInVm.getPrice());
        return ResponseResult.success();
    }

    @GetMapping("/discount")
    public ResponseResult selectDiscount() {
        return ResponseResult.success().body(productService.selectDiscount());
    }

    @PostMapping("/discount/update")
    public ResponseResult updateDiscount(@RequestBody Map<String, Object> map) {
        Double discount = (Double) map.get("discount");
        productService.updateDiscount(discount);
        return ResponseResult.success();
    }

    @GetMapping("/sync")
    public ResponseResult sync() {
        productService.syncProduct();
        return ResponseResult.success();
    }
}
