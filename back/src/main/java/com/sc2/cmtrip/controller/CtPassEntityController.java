package com.sc2.cmtrip.controller;

import cn.dev33.satoken.annotation.SaCheckPermission;
import com.sc2.cmtrip.common.ApiResult;
import com.sc2.cmtrip.entity.CtPass;
import com.sc2.cmtrip.entity.CtPassEntity;
import com.sc2.cmtrip.service.CtPassEntityService;
import com.sc2.cmtrip.service.CtPassService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/pass-entity")
public class CtPassEntityController {

    @Autowired
    private CtPassEntityService ctPassEntityService;

    /**
     * Get pass entities of the current user
     * 获取当前用户的通票实体
     * @return
     */
    @SaCheckPermission("list-my-all-pass-entities")
    @GetMapping("/list-my-all")
    public ApiResult getMyPassEntities() {
        List<CtPassEntity> myEntities = ctPassEntityService.getMyPassEntities();
        return ApiResult.success(myEntities);
    }

    /**
     * Get pass entities of current user by a passId
     * 根据一个passId，先判断其是否有实体依附于本用户，再返回该passId下辖实体中属于本用户的实体列表
     * @param passId
     * @return
     */
    @SaCheckPermission("list-my-pass-entities-by-pass-id")
    @GetMapping("/list-my-all-by-pass-id")
    public ApiResult getMyPassEntitiesByPassId(Long passId) {
        List<CtPassEntity> myEntitiesOfPassId = ctPassEntityService.getMyPassEntitiesByPassId(passId);
        return ApiResult.success(myEntitiesOfPassId);
    }

    /**
     * Add a pass entity
     * 新增通票实体
     * @param ctPassEntity
     */
    @SaCheckPermission("add-pass-entity")
    @PostMapping("/add")
    public void addPassEntity(@RequestBody CtPassEntity ctPassEntity) {
        ctPassEntityService.addPassEntity(ctPassEntity);
    }

    /**
     * Edit a pass entity
     * 编辑通票实体
     * @param ctPassEntity
     */
    @SaCheckPermission("edit-pass-entity")
    @PostMapping("/edit")
    public void editPassEntity(@RequestBody CtPassEntity ctPassEntity) {
        ctPassEntityService.editPassEntity(ctPassEntity);
    }

    /**
     * Delete a pass entity
     * 删除通票实体
     * @param id
     */
    @SaCheckPermission("delete-pass-entity")
    @DeleteMapping("/delete")
    public void deletePassEntity(@RequestParam Long id) {
        ctPassEntityService.deletePassEntity(id);
    }

}
