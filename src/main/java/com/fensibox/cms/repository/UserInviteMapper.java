package com.fensibox.cms.repository;

import com.fensibox.cms.entity.UserInvite;
import com.fensibox.cms.entity.UserInviteExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface UserInviteMapper {
    long countByExample(UserInviteExample example);

    int deleteByExample(UserInviteExample example);

    int deleteByPrimaryKey(Long id);

    int insert(UserInvite record);

    int insertSelective(UserInvite record);

    List<UserInvite> selectByExample(UserInviteExample example);

    UserInvite selectByPrimaryKey(Long id);

    int updateByExampleSelective(@Param("record") UserInvite record, @Param("example") UserInviteExample example);

    int updateByExample(@Param("record") UserInvite record, @Param("example") UserInviteExample example);

    int updateByPrimaryKeySelective(UserInvite record);

    int updateByPrimaryKey(UserInvite record);
}