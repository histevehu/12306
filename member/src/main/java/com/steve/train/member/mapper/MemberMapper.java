package com.steve.train.member.mapper;

import com.steve.train.member.domain.Member;
import com.steve.train.member.domain.MemberExample;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface MemberMapper {
    long countByExample(MemberExample example);

    int deleteByExample(MemberExample example);

    int deleteByPrimaryKey(Long id);

    int insert(Member record);

    int insertSelective(Member record);

    List<Member> selectByExample(MemberExample example);

    Member selectByPrimaryKey(Long id);

    int updateByExampleSelective(@Param("record") Member record, @Param("example") MemberExample example);

    int updateByExample(@Param("record") Member record, @Param("example") MemberExample example);

    int updateByPrimaryKeySelective(Member record);

    int updateByPrimaryKey(Member record);
}