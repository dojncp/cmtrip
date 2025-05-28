package com.sc2.cmtrip.repository;

import com.sc2.cmtrip.entity.CtMenu;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CtMenuRepository extends JpaRepository<CtMenu, Long> {

    /**
     * Automatically sort and return all menus according to the `sortOrder` field
     * 按照sortOrder字段，自动排序返回所有菜单
     * @return
     */
    List<CtMenu> findAllByOrderBySortOrder();
}
