package com.fensibox.cms.repository;

import com.fensibox.cms.entity.CustomUser;
import com.fensibox.cms.entity.CustomUserExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface CustomUserMapper {
    long countByExample(CustomUserExample example);

    int deleteByExample(CustomUserExample example);

    int deleteByPrimaryKey(Long id);

    int insert(CustomUser record);

    int insertSelective(CustomUser record);

    List<CustomUser> selectByExample(CustomUserExample example);

    CustomUser selectByPrimaryKey(Long id);

    int updateByExampleSelective(@Param("record") CustomUser record, @Param("example") CustomUserExample example);

    int updateByExample(@Param("record") CustomUser record, @Param("example") CustomUserExample example);

    int updateByPrimaryKeySelective(CustomUser record);

    int updateByPrimaryKey(CustomUser record);
}