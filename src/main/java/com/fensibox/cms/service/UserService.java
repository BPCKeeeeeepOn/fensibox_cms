package com.fensibox.cms.service;

import com.fensibox.cms.common.*;
import com.fensibox.cms.controller.vm.PageVM;
import com.fensibox.cms.controller.vm.user.UserAchievementOutVM;
import com.fensibox.cms.controller.vm.user.UserInVM;
import com.fensibox.cms.controller.vm.user.UserOutVM;
import com.fensibox.cms.entity.ClientUser;
import com.fensibox.cms.entity.CustomUser;
import com.fensibox.cms.entity.CustomUserExample;
import com.fensibox.cms.exception.BizException;
import com.fensibox.cms.repository.CustomUserMapper;
import com.fensibox.cms.repository.biz.CmsBizMapper;
import com.fensibox.cms.utils.CommonUtils;
import com.fensibox.cms.utils.CurrentUserUtils;
import com.github.pagehelper.PageHelper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;


@Service
@Slf4j
public class UserService {


    @Autowired
    CustomUserMapper customUserMapper;

    @Autowired
    CmsBizMapper cmsBizMapper;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    private CurrentUserUtils currentUserUtils;


    public CustomUser selectUserByUserName(String username) {
        CustomUserExample ctue = new CustomUserExample();
        ctue.createCriteria().andUsernameEqualTo(username).andEnabledEqualTo(1);
        List<CustomUser> userList = customUserMapper.selectByExample(ctue);
        return CollectionUtils.isEmpty(userList) ? null : userList.get(0);
    }

    public int add() {
        CustomUser customUser = new CustomUser();
        customUser.setId(GeneratorID.getId());
        customUser.setUsername("bpc_");
        customUser.setRole(3);
        customUser.setEnabled(1);
        customUser.setInviteCode("123456");
        customUser.setUpperUserId(590183316561555456L);
        customUser.setPassword(passwordEncoder.encode(CmsConstants.DEFAULT_PASSWORD));
        return customUserMapper.insertSelective(customUser);
    }

    /**
     * 新增管理员/商务
     *
     * @param userInVM
     * @return
     */
    public int addUser(UserInVM userInVM) {
        //获取当前登录用户信息
        CustomUser currentCustomUser = currentUserUtils.getCurrUser();
        Long upperUserId = currentCustomUser.getId();
        Integer loginRole = currentCustomUser.getRole();
        String username = userInVM.getUsername();
        if (selectUserByUserName(username) != null)
            throw new BizException(ResponseResult.fail(ResultCode.USER_EXISZTS));
        CustomUser customUser = new CustomUser();
        customUser.setId(GeneratorID.getId());
        customUser.setUsername(username);
        customUser.setEnabled(1);
        if (1 == loginRole) {
            customUser.setRole(2);
        }
        if (2 == loginRole) {
            customUser.setRole(3);
            customUser.setUpperUserId(upperUserId);
            customUser.setInviteCode(CommonUtils.randomNum());
        }
        customUser.setPassword(passwordEncoder.encode(CmsConstants.DEFAULT_PASSWORD));
        return customUserMapper.insertSelective(customUser);
    }

    /**
     * 查询不同权限下的后台用户
     *
     * @return
     */
    public List<UserOutVM> selectCustomUserList(PageVM pageVM) {
        CustomUser currentCustomUser = currentUserUtils.getCurrUser();
        Long userId = currentCustomUser.getId();
        Integer role = currentCustomUser.getRole();
        return PageHelper.offsetPage(pageVM.getOffset(), pageVM.getLimit())
                .doSelectPage(() -> cmsBizMapper.selectCustomUserList(userId, role));
    }


    /**
     * 查询不同权限下的会员
     *
     * @return
     */
    public List<ClientUser> selectClientUserList(PageVM pageVM) {
        CustomUser currentCustomUser = currentUserUtils.getCurrUser();
        Long userId = currentCustomUser.getId();
        Integer role = currentCustomUser.getRole();
        return PageHelper.offsetPage(pageVM.getOffset(), pageVM.getLimit())
                .doSelectPage(() -> cmsBizMapper.selectClientUserList(userId, role));
    }

    /**
     * 修改密码
     *
     * @param password
     * @param newPassword
     * @return
     */
    public int editPassword(String password, String newPassword) {
        if (StringUtils.isAnyBlank(password, newPassword)) {
            throw new BizException(ResponseResult.fail(ResultCode.PARAMS_ERROR));
        }
        long userId = currentUserUtils.getCurrUserId();
        CustomUser customUser = customUserMapper.selectByPrimaryKey(userId);
        String oldPassword = customUser.getPassword();
        if (passwordEncoder.matches(password, oldPassword)) {
            throw new BizException(ResponseResult.fail(ResultCode.OLD_PASSWORD_ERROR));
        }
        customUser.setPassword(passwordEncoder.encode(newPassword));
        return customUserMapper.updateByPrimaryKeySelective(customUser);
    }

    /**
     * 查询本月新增业绩
     *
     * @return
     */
    public UserAchievementOutVM selectAchievement() {
        //获取当前登录用户信息
        CustomUser currentCustomUser = currentUserUtils.getCurrUser();
        Long userId = currentCustomUser.getId();
        Integer role = currentCustomUser.getRole();
        UserAchievementOutVM userAchievementOutVM = new UserAchievementOutVM();
        int lastDay, month, lastMonth, total;
        LocalDateTime start, end;
        //查询昨日
        start = LocalDateTime.of(LocalDate.now(), LocalTime.MIN).minusDays(1);
        end = start.plusDays(1);
        lastDay = cmsBizMapper.selectClientUserCount(userId, role, DateTimeRange.between(start, end));
        //查询本月
        start = LocalDateTime.of(LocalDateTime.now().getYear(), LocalDateTime.now().getMonth(), 1, 0, 0, 0);
        end = LocalDateTime.now();
        month = cmsBizMapper.selectClientUserCount(userId, role, DateTimeRange.between(start, end));
        //查询上月
        start = LocalDateTime.of(LocalDateTime.now().getYear(), LocalDateTime.now().getMonth(), 1, 0, 0, 0).minusMonths(1l);
        end = LocalDateTime.of(LocalDateTime.now().getYear(), LocalDateTime.now().getMonth(), 1, 0, 0, 0);
        lastMonth = cmsBizMapper.selectClientUserCount(userId, role, DateTimeRange.between(start, end));
        //查询总数
        start = null;
        end = null;
        total = cmsBizMapper.selectClientUserCount(userId, role, DateTimeRange.between(start, end));
        userAchievementOutVM.setLastDay(lastDay);
        userAchievementOutVM.setLastMonth(lastMonth);
        userAchievementOutVM.setMonth(month);
        userAchievementOutVM.setTotal(total);
        return userAchievementOutVM;
    }

}
