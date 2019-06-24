package com.fensibox.cms.repository;

import com.fensibox.cms.entity.MemberDiscount;
import com.fensibox.cms.entity.MemberDiscountExample;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MemberDiscountMapper {
    long countByExample(MemberDiscountExample example);

    int deleteByExample(MemberDiscountExample example);

    int deleteByPrimaryKey(Long id);

    int insert(MemberDiscount record);

    int insertSelective(MemberDiscount record);

    List<MemberDiscount> selectByExample(MemberDiscountExample example);

    MemberDiscount selectByPrimaryKey(Long id);

    int updateByExampleSelective(@Param("record") MemberDiscount record, @Param("example") MemberDiscountExample example);

    int updateByExample(@Param("record") MemberDiscount record, @Param("example") MemberDiscountExample example);

    int updateByPrimaryKeySelective(MemberDiscount record);

    int updateByPrimaryKey(MemberDiscount record);
}