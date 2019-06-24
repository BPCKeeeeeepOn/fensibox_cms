package com.fensibox.cms.controller;

import com.fensibox.cms.common.ResponseResult;
import com.fensibox.cms.controller.vm.PageVM;
import com.fensibox.cms.controller.vm.announcement.AnnouncementInVM;
import com.fensibox.cms.service.AnnouncementService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/announcement")
@Slf4j
public class AnnouncementController {


    @Autowired
    private AnnouncementService announcementService;

    @PostMapping("/add")
    public ResponseResult add(@RequestBody AnnouncementInVM announcementInVM) {
        announcementService.add(announcementInVM);
        return ResponseResult.success();
    }

    @GetMapping("/list")
    public ResponseResult list(PageVM pageVM) {
        return ResponseResult.success().body(announcementService.list(pageVM));
    }

    @GetMapping("/detail/{id}")
    public ResponseResult detail(@PathVariable("id") Long id) {

        return ResponseResult.success().body(announcementService.detail(id));
    }

    @PostMapping("/update/{id}")
    public ResponseResult update(@PathVariable("id") Long id, @RequestBody AnnouncementInVM announcementInVM) {
        announcementService.update(id, announcementInVM);
        return ResponseResult.success();
    }
}
