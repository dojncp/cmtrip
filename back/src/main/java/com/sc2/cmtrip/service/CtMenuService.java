package com.sc2.cmtrip.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.sc2.cmtrip.entity.CtMenu;

import java.util.List;

public interface CtMenuService extends IService<CtMenu> {

    List<CtMenu> getMenuList();
}
