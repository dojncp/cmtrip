package com.sc2.cmtrip.dto;

import lombok.Data;

import java.util.List;

@Data
public class CtUpdateRolePermissionsDTO {
    private Long roleId;
    private List<Long> assignPermissionIds;
    private List<Long> removePermissionIds;
}
