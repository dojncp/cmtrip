package com.sc2.cmtrip.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.sc2.cmtrip.entity.CtRolePermission;
import io.lettuce.core.dynamic.annotation.Param;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

//@Mapper
public interface CtRolePermissionMapper extends BaseMapper<CtRolePermission> {

    @Select("<script>" +
            "SELECT permission_id FROM ct_role_permission WHERE role_id IN " +
            "<foreach item='roleId' collection='roleIds' open='(' separator=',' close=')'>" +
            "#{roleId}" +
            "</foreach>" +
            "</script>")
    List<Long> findPermissionIdsByRoleIds(@Param("roleIds") List<Long> roleIds);
}
