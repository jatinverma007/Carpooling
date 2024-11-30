package com.carpooling.ums.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RolesPermissionActionDTO {
    private Long id;
    private Long roleId;
    private Long permissionId;
    private String action;
}
