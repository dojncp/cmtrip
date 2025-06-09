package com.sc2.cmtrip.service.impl;

import cn.dev33.satoken.stp.StpUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sc2.cmtrip.entity.CtMenu;
import com.sc2.cmtrip.mapper.CtMenuMapper;
import com.sc2.cmtrip.repository.CtMenuRepository;
import com.sc2.cmtrip.service.CtMenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class CtMenuServiceImpl extends ServiceImpl<CtMenuMapper, CtMenu> implements CtMenuService {

    @Autowired
    private CtMenuRepository ctMenuRepository;

    /**
     * Return a hierarchical menu list
     * 返回有层次的菜单列表
     * @return
     */
    @Override
    public List<CtMenu> getMenuList() {
        // Determine the returned content based on the current user's role. Only users with the super administrator role can access the full menu routes
        // 根据当前用户的角色，决定返回的内容。仅超级管理员角色才可以获得完整菜单路由
        if (StpUtil.getRoleList().contains("superAdmin")) {
            List<CtMenu> menus = ctMenuRepository.findAllByOrderBySortOrder();
            List<CtMenu> menuTree = buildMenuTree(menus);
            return menuTree;
        } else {
            LambdaQueryWrapper<CtMenu> lcm = new LambdaQueryWrapper<>();
            // Exclude system management type menus 排除系统管理类型的菜单
            lcm.notLikeRight(CtMenu::getPath, "/system");
            // Exclude pass management type menus 排除通票管理类型的菜单
            // lcm.notLikeRight(CtMenu::getPath, "/pass-management");
            List<CtMenu> menus = this.list(lcm);
            List<CtMenu> menuTree = buildMenuTree(menus);
            return menuTree;
        }
    }

    /**
     * Build a hierarchical menu tree 构建菜单层次树
     * @param menus
     * @return
     */
    private List<CtMenu> buildMenuTree(List<CtMenu> menus) {
        Map<Long, CtMenu> menuMap = new HashMap<>();
        List<CtMenu> topMenus = new ArrayList<>();
        for (CtMenu menu: menus) {
            menu.setChildren(new ArrayList<>());
            menuMap.put(menu.getId(), menu);
        }
        for (CtMenu menu: menus) {
            Long parentId = menu.getParentId();
            // Only establish parent-child relationships when parentId is not null and greater than 0 只有当 parentId 不为 null 且大于 0 时，才尝试建立父子关系
            if (parentId != null && parentId > 0L) {
                CtMenu parent = menuMap.get(parentId);
                if (parent != null) {
                    parent.getChildren().add(menu);
                }
                // If parent is null, it means the parentId in the database points to a non-existent menu 如果 parent 为 null，说明数据库中的 parentId 指向了一个不存在的菜单
            } else {
                // Menus with a parentId of null or 0 are considered top-level menus parentId 为 null 或 0 的菜单认为是顶级菜单
                topMenus.add(menu);
            }
        }
        return topMenus;
    }

}
