package com.fensibox.cms.repository.biz;

import com.fensibox.cms.common.DateTimeRange;
import com.fensibox.cms.controller.vm.user.UserOutVM;
import com.fensibox.cms.controller.vm.announcement.AnnouncementOutVM;
import com.fensibox.cms.controller.vm.deposit.DepositListOutVM;
import com.fensibox.cms.controller.vm.order.OrderContentVM;
import com.fensibox.cms.controller.vm.order.OrderOutVM;
import com.fensibox.cms.controller.vm.product.ProductListOutVM;
import com.fensibox.cms.entity.ClientUser;
import com.fensibox.cms.entity.MemberDiscount;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface CmsBizMapper {


    /**
     * 查询不同权限下的后台用户
     *
     * @param userId
     * @param role
     * @return
     */
    List<UserOutVM> selectCustomUserList(@Param("userId") Long userId, @Param("role") Integer role);

    /**
     * 查询不同权限下的会员
     *
     * @param userId
     * @param role
     * @return
     */
    List<ClientUser> selectClientUserList(@Param("userId") Long userId, @Param("role") Integer role);

    /**
     * 根据不同日期 查询业绩
     * @param userId
     * @param role
     * @param range
     * @return
     */
    int selectClientUserCount(@Param("userId") Long userId, @Param("role") Integer role, @Param("range") DateTimeRange<LocalDateTime> range);

    /**
     * 查看所有商品
     *
     * @return
     */
    List<ProductListOutVM> selectProductList(@Param("pname") String pname);

    MemberDiscount selectDiscount();

    List<AnnouncementOutVM> selectAnnouncementList();

    AnnouncementOutVM selectAnnouncementDetail(@Param("id") Long id);

    List<OrderOutVM> selectOrderList(@Param("orderContentVM") OrderContentVM orderContentVM, @Param("role") Integer role, @Param("uid") Long uid);

    OrderOutVM selectOrderDetail(@Param("id") Long id);

    List<DepositListOutVM> selectDepositList(@Param("orderContentVM") OrderContentVM orderContentVM, @Param("uid") Long uid, @Param("role") Integer role);

}
