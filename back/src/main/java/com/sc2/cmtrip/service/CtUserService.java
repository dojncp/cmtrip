package com.sc2.cmtrip.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.sc2.cmtrip.entity.CtUser;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

public interface CtUserService extends IService<CtUser> {

    public CtUser findByUserName(String userName);

    void registerUser(CtUser newUser);

    void deleteUser(Long id);

    Map<String, Object> doLogin(String userName, String password);

    void doLogout();

    List<CtUser> getUserList();

    List<String> getRoleNamesByUserId(Long userId);

    void assignRoleToUser(Long userId, Long roleId);

    void assignRolesToUser(Long userId, List<Long> roleIds);

    void removeRolesToUser(Long userId, List<Long> roleIds);

    void updateRolesToUser(Long userId, List<Long> assignRoleIds, List<Long> removeRoleIds);

    List<String> getPermissionNamesByUserId(Long userId);

    @Transactional
    void deleteTripsByUserId(Long userId);
}
