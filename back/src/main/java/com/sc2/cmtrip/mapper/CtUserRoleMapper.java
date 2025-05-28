package com.sc2.cmtrip.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.sc2.cmtrip.entity.CtUserRole;
import io.lettuce.core.dynamic.annotation.Param;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

//@Mapper
public interface CtUserRoleMapper extends BaseMapper<CtUserRole> {

    @Select("SELECT role_id FROM ct_user_role WHERE user_id = #{userId}")
    List<Long> findRoleIdsByUserId(@Param("userId") Long userId);
}
