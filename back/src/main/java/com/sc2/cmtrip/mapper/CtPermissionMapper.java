package com.sc2.cmtrip.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.sc2.cmtrip.entity.CtPermission;
import io.lettuce.core.dynamic.annotation.Param;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;


public interface CtPermissionMapper extends BaseMapper<CtPermission> {

    @Select("<script>" +
            "SELECT * FROM ct_permission WHERE id IN " +
            "<foreach item='permissionId' collection='permissionIds' open='(' separator=',' close=')'>" +
            "#{permissionId}" +
            "</foreach>" +
            "</script>")
    List<CtPermission> findPermissionsByIds(@Param("permissionIds") List<Long> permissionIds);
}
