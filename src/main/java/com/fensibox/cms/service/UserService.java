package com.fensibox.cms.service;

import com.fensibox.cms.common.*;
import com.fensibox.cms.controller.vm.PageVM;
import com.fensibox.cms.controller.vm.user.UserAchievementOneVM;
import com.fensibox.cms.controller.vm.user.UserAchievementOutVM;
import com.fensibox.cms.controller.vm.user.UserInVM;
import com.fensibox.cms.controller.vm.user.UserOutVM;
import com.fensibox.cms.entity.ClientUser;
import com.fensibox.cms.entity.CustomUser;
import com.fensibox.cms.entity.CustomUserExample;
import com.fensibox.cms.exception.BizException;
import com.fensibox.cms.repository.ClientUserMapper;
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
import java.util.ArrayList;
import java.util.List;


@Service
@Slf4j
public class UserService {


    @Autowired
    private CustomUserMapper customUserMapper;

    @Autowired
    private ClientUserMapper clientUserMapper;

    @Autowired
    private CmsBizMapper cmsBizMapper;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private CurrentUserUtils currentUserUtils;


    public CustomUser selectUserByUserName(String username) {
        CustomUserExample ctue = new CustomUserExample();
        ctue.createCriteria().andUsernameEqualTo(username).andEnabledEqualTo(1);
        List<CustomUser> userList = customUserMapper.selectByExample(ctue);
        return CollectionUtils.isEmpty(userList) ? null : userList.get(0);
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
     * 更新管理员/商务信息
     *
     * @param id
     * @param userInVM
     * @return
     */
    public int updateCustomUser(Long id, UserInVM userInVM) {
        String username = userInVM.getUsername();
        String inviteCode = userInVM.getInviteCode();
        Integer enabled = userInVM.getEnabled();
        if (selectUserByUserName(username) != null)
            throw new BizException(ResponseResult.fail(ResultCode.USER_EXISZTS));

        if (StringUtils.isNotBlank(inviteCode)) {
            CustomUserExample customUserExample = new CustomUserExample();
            customUserExample.createCriteria().andInviteCodeEqualTo(inviteCode);
            if (customUserMapper.selectByExample(customUserExample).size() > 0)
                throw new BizException(ResponseResult.fail(ResultCode.INVITE_CODE_ERROR));
        }
        CustomUser customUser = customUserMapper.selectByPrimaryKey(id);
        if (customUser == null) {
            throw new BizException(ResponseResult.fail(ResultCode.NOT_USER));
        }
        customUser.setEnabled(enabled);
        customUser.setUsername(username);
        customUser.setInviteCode(inviteCode);
        return customUserMapper.updateByPrimaryKeySelective(customUser);
    }

    /**
     * 修改用户积分
     *
     * @param id
     * @param credit
     * @return
     */
    public int updateCredit(Long id, Double credit) {
        ClientUser clientUser = clientUserMapper.selectByPrimaryKey(id);
        if (clientUser == null) {
            throw new BizException(ResponseResult.fail(ResultCode.NOT_USER));
        }
        Double old = clientUser.getCredit();
        log.info("updateCredit userId:{} old credit: {}", id, old);
        clientUser.setCredit(credit);
        return clientUserMapper.updateByPrimaryKeySelective(clientUser);
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
     * 查询后台用户详情
     *
     * @param id
     * @return
     */
    public UserOutVM detail(Long id) {
        return cmsBizMapper.selectCustomUser(id);
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
     * 查询业绩列表
     *
     * @return
     */
    public List<UserAchievementOutVM> selectAchievementList() {
        //获取当前登录用户信息
        CustomUser currentCustomUser = currentUserUtils.getCurrUser();
        Long userId = currentCustomUser.getId();
        Integer role = currentCustomUser.getRole();
        //返回前端结果
        List<UserAchievementOutVM> userAchievementOutVMList = new ArrayList<>();
        int lastDayCount, monthCount, lastMonthCount, totalCount;
        double lastDayMoney, monthMoney, lastMonthMoney, totalMoney;
        UserAchievementOneVM userAchievementOneVM;
        LocalDateTime start, end;
        List<UserOutVM> list = cmsBizMapper.selectCustomUserList(userId, role);
        for (UserOutVM user : list) {
            UserAchievementOutVM userAchievementOutVM = new UserAchievementOutVM();
            Long uId = Long.valueOf(user.getId());
            Integer urole = user.getRole();
            String username = user.getUsername();
            //查询昨日
            start = LocalDateTime.of(LocalDate.now(), LocalTime.MIN).minusDays(1);
            end = start.plusDays(1);
            userAchievementOneVM = cmsBizMapper.selectClientUserCount(uId, urole, DateTimeRange.between(start, end));
            lastDayCount = userAchievementOneVM.getCount();
            lastDayMoney = userAchievementOneVM.getTotalMoney();
            //查询本月
            start = LocalDateTime.of(LocalDateTime.now().getYear(), LocalDateTime.now().getMonth(), 1, 0, 0, 0);
            end = LocalDateTime.now();
            userAchievementOneVM = cmsBizMapper.selectClientUserCount(uId, urole, DateTimeRange.between(start, end));
            monthCount = userAchievementOneVM.getCount();
            monthMoney = userAchievementOneVM.getTotalMoney();
            //查询上月
            start = LocalDateTime.of(LocalDateTime.now().getYear(), LocalDateTime.now().getMonth(), 1, 0, 0, 0).minusMonths(1l);
            end = LocalDateTime.of(LocalDateTime.now().getYear(), LocalDateTime.now().getMonth(), 1, 0, 0, 0);
            userAchievementOneVM = cmsBizMapper.selectClientUserCount(uId, urole, DateTimeRange.between(start, end));
            lastMonthCount = userAchievementOneVM.getCount();
            lastMonthMoney = userAchievementOneVM.getTotalMoney();
            //查询总数
            start = null;
            end = null;
            userAchievementOneVM = cmsBizMapper.selectClientUserCount(uId, urole, DateTimeRange.between(start, end));
            totalCount = userAchievementOneVM.getCount();
            totalMoney = userAchievementOneVM.getTotalMoney();
            userAchievementOutVM.setUid(String.valueOf(uId));
            userAchievementOutVM.setUsername(username);
            userAchievementOutVM.setRole(urole);
            userAchievementOutVM.setLastDayCount(lastDayCount);
            userAchievementOutVM.setLastDayMoney(lastDayMoney);
            userAchievementOutVM.setLastMonthCount(lastMonthCount);
            userAchievementOutVM.setLastMonthMoney(lastMonthMoney);
            userAchievementOutVM.setMonthCount(monthCount);
            userAchievementOutVM.setMonthMoney(monthMoney);
            userAchievementOutVM.setTotalCount(totalCount);
            userAchievementOutVM.setTotalMoney(totalMoney);
            userAchievementOutVMList.add(userAchievementOutVM);
        }
        return userAchievementOutVMList;
    }

    /**
     * 查询个人业绩（超管/管理/商务）
     *
     * @return
     */
    public UserAchievementOutVM selectAchievementDetail() {
        //获取当前登录用户信息
        CustomUser currentCustomUser = currentUserUtils.getCurrUser();
        Long userId = currentCustomUser.getId();
        Integer role = currentCustomUser.getRole();
        UserAchievementOutVM userAchievementOutVM = new UserAchievementOutVM();
        int lastDayCount, monthCount, lastMonthCount, totalCount;
        double lastDayMoney, monthMoney, lastMonthMoney, totalMoney;
        UserAchievementOneVM userAchievementOneVM;
        LocalDateTime start, end;
        //查询昨日
        start = LocalDateTime.of(LocalDate.now(), LocalTime.MIN).minusDays(1);
        end = start.plusDays(1);
        userAchievementOneVM = cmsBizMapper.selectClientUserCount(userId, role, DateTimeRange.between(start, end));
        lastDayCount = userAchievementOneVM.getCount();
        lastDayMoney = userAchievementOneVM.getTotalMoney();
        //查询本月
        start = LocalDateTime.of(LocalDateTime.now().getYear(), LocalDateTime.now().getMonth(), 1, 0, 0, 0);
        end = LocalDateTime.now();
        userAchievementOneVM = cmsBizMapper.selectClientUserCount(userId, role, DateTimeRange.between(start, end));
        monthCount = userAchievementOneVM.getCount();
        monthMoney = userAchievementOneVM.getTotalMoney();
        //查询上月
        start = LocalDateTime.of(LocalDateTime.now().getYear(), LocalDateTime.now().getMonth(), 1, 0, 0, 0).minusMonths(1l);
        end = LocalDateTime.of(LocalDateTime.now().getYear(), LocalDateTime.now().getMonth(), 1, 0, 0, 0);
        userAchievementOneVM = cmsBizMapper.selectClientUserCount(userId, role, DateTimeRange.between(start, end));
        lastMonthCount = userAchievementOneVM.getCount();
        lastMonthMoney = userAchievementOneVM.getTotalMoney();
        //查询总数
        start = null;
        end = null;
        userAchievementOneVM = cmsBizMapper.selectClientUserCount(userId, role, DateTimeRange.between(start, end));
        totalCount = userAchievementOneVM.getCount();
        totalMoney = userAchievementOneVM.getTotalMoney();
        userAchievementOutVM.setLastDayCount(lastDayCount);
        userAchievementOutVM.setLastDayMoney(lastDayMoney);
        userAchievementOutVM.setLastMonthCount(lastMonthCount);
        userAchievementOutVM.setLastMonthMoney(lastMonthMoney);
        userAchievementOutVM.setMonthCount(monthCount);
        userAchievementOutVM.setMonthMoney(monthMoney);
        userAchievementOutVM.setTotalCount(totalCount);
        userAchievementOutVM.setTotalMoney(totalMoney);
        return userAchievementOutVM;
    }

}
