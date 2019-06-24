package com.fensibox.cms.service;

import com.fensibox.cms.common.ResponseResult;
import com.fensibox.cms.common.ResultCode;
import com.fensibox.cms.controller.vm.PageVM;
import com.fensibox.cms.controller.vm.order.OrderContentVM;
import com.fensibox.cms.controller.vm.order.OrderOutVM;
import com.fensibox.cms.entity.CustomUser;
import com.fensibox.cms.exception.BizException;
import com.fensibox.cms.repository.biz.CmsBizMapper;
import com.fensibox.cms.utils.CurrentUserUtils;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrderService {


    @Autowired
    private CurrentUserUtils currentUserUtils;

    @Autowired
    private CmsBizMapper cmsBizMapper;

    public List<OrderOutVM> list(OrderContentVM orderContentVM, PageVM pageVM) {
        //获取当前用户信息
        CustomUser user = currentUserUtils.getCurrUser();
        if (user == null) {
            throw new BizException(ResponseResult.fail(ResultCode.INVALID_AUTHTOKEN));
        }
        Long uid = user.getId();
        Integer role = user.getRole();
        List<OrderOutVM> list;
        list = PageHelper.offsetPage(pageVM.getOffset(), pageVM.getLimit())
                .doSelectPage(() -> cmsBizMapper.selectOrderList(orderContentVM, role, uid));
        return list;
    }

    public OrderOutVM detail(Long id) {
        return cmsBizMapper.selectOrderDetail(id);
    }


}
