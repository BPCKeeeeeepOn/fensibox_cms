package com.fensibox.cms.service.cache;

import com.fensibox.cms.entity.CustomUser;
import com.fensibox.cms.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

/**
 * 用户信息缓存
 *
 * @author USER
 */
@Component
public class UserInfoCacheService {

    @Autowired
    private UserService userService;

    @Cacheable(cacheNames = "fensibox_cms_user_prod", key = "#userName")
    public CustomUser getUserCache(String userName) {
        CustomUser user = userService.selectUserByUserName(userName);
        return user;
    }

    /**
     * 删除缓存
     *
     * @param userName
     */
    @CacheEvict(cacheNames = "fensibox_cms_user_prod", key = "#userName")
    public void removeCache(String userName) {
        return;
    }
}
