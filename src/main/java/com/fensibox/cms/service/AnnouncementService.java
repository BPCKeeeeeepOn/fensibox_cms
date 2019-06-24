package com.fensibox.cms.service;

import com.fensibox.cms.common.GeneratorID;
import com.fensibox.cms.controller.vm.PageVM;
import com.fensibox.cms.controller.vm.announcement.AnnouncementInVM;
import com.fensibox.cms.controller.vm.announcement.AnnouncementOutVM;
import com.fensibox.cms.entity.Announcement;
import com.fensibox.cms.repository.AnnouncementMapper;
import com.fensibox.cms.repository.biz.CmsBizMapper;
import com.github.pagehelper.PageHelper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class AnnouncementService {

    @Autowired
    private CmsBizMapper cmsBizMapper;

    @Autowired
    private AnnouncementMapper announcementMapper;

    public int add(AnnouncementInVM announcementInVM) {
        Announcement announcement = new Announcement();
        announcement.setTitle(announcementInVM.getTitle());
        announcement.setContent(announcementInVM.getContent());
        announcement.setId(GeneratorID.getId());
        return announcementMapper.insertSelective(announcement);
    }

    public List<AnnouncementOutVM> list(PageVM pageVM) {
        List<AnnouncementOutVM> list;
        list = PageHelper.offsetPage(pageVM.getOffset(), pageVM.getLimit())
                .doSelectPage(() -> cmsBizMapper.selectAnnouncementList());
        return list;
    }

    public AnnouncementOutVM detail(Long id) {
        return cmsBizMapper.selectAnnouncementDetail(id);

    }

    public int update(Long id, AnnouncementInVM announcementInVM) {
        Announcement announcement = announcementMapper.selectByPrimaryKey(id);
        announcement.setTitle(announcementInVM.getTitle());
        announcement.setContent(announcementInVM.getContent());
        return announcementMapper.updateByPrimaryKeySelective(announcement);
    }
}
