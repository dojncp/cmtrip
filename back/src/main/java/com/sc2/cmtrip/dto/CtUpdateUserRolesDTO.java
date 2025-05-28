package com.sc2.cmtrip.dto;

import lombok.Data;

import java.util.List;

@Data
public class CtUpdateUserRolesDTO {
    private Long userId;
    private List<Long> assignRoleIds;
    private List<Long> removeRoleIds;
}
