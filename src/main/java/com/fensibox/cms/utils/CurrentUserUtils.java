package com.fensibox.cms.utils;

import com.fensibox.cms.common.ResponseResult;
import com.fensibox.cms.common.ResultCode;
import com.fensibox.cms.entity.CustomUser;
import com.fensibox.cms.exception.BizException;
import com.fensibox.cms.service.cache.UserInfoCacheService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
public class CurrentUserUtils {

    @Autowired
    private UserInfoCacheService userInfoCacheService;

    private CurrentUserUtils() {
    }

    /**
     * 获取当前登录用户的用户名
     *
     * @return
     */
    public String getCurrentLoginUserName() {
        if ((String) SecurityContextHolder.getContext().getAuthentication().getPrincipal() == null) {
            throw new BizException(ResponseResult.fail(ResultCode.INVALID_AUTHTOKEN));
        }
        return (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }

    /**
     * 获取当前登录用户的userId
     *
     * @return
     */
    public long getCurrUserId() {
        return getCurrUser().getId();
    }

    /**
     * 获取当前登录用户的用户信息
     *
     * @return
     */
    public CustomUser getCurrUser() {
        return userInfoCacheService.getUserCache(getCurrentLoginUserName());
    }
}
