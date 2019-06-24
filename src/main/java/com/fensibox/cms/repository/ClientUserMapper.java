package com.fensibox.cms.repository;

import com.fensibox.cms.entity.ClientUser;
import com.fensibox.cms.entity.ClientUserExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface ClientUserMapper {
    long countByExample(ClientUserExample example);

    int deleteByExample(ClientUserExample example);

    int deleteByPrimaryKey(Long id);

    int insert(ClientUser record);

    int insertSelective(ClientUser record);

    List<ClientUser> selectByExample(ClientUserExample example);

    ClientUser selectByPrimaryKey(Long id);

    int updateByExampleSelective(@Param("record") ClientUser record, @Param("example") ClientUserExample example);

    int updateByExample(@Param("record") ClientUser record, @Param("example") ClientUserExample example);

    int updateByPrimaryKeySelective(ClientUser record);

    int updateByPrimaryKey(ClientUser record);
}