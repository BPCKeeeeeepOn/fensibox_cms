package com.fensibox.cms.service;

import com.fensibox.cms.controller.vm.PageVM;
import com.fensibox.cms.controller.vm.deposit.DepositListOutVM;
import com.fensibox.cms.controller.vm.order.OrderContentVM;
import com.fensibox.cms.entity.CustomUser;
import com.fensibox.cms.repository.biz.CmsBizMapper;
import com.fensibox.cms.utils.CurrentUserUtils;
import com.github.pagehelper.PageHelper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class DepositService {

    @Autowired
    private CurrentUserUtils currentUserUtils;

    @Autowired
    private CmsBizMapper cmsBizMapper;

    public List<DepositListOutVM> list(OrderContentVM orderContentVM, PageVM pageVM) {
        CustomUser user = currentUserUtils.getCurrUser();
        Long uid = user.getId();
        Integer role = user.getRole();
        List<DepositListOutVM> depositListOutVMList;
        depositListOutVMList = PageHelper.offsetPage(pageVM.getOffset(), pageVM.getLimit())
                .doSelectPage(() -> cmsBizMapper.selectDepositList(orderContentVM, uid, role));
        return depositListOutVMList;
    }


}
