package com.sc2.cmtrip.controller;

import cn.dev33.satoken.annotation.SaCheckPermission;
import com.sc2.cmtrip.common.ApiResult;
import com.sc2.cmtrip.common.CtUtils;
import com.sc2.cmtrip.entity.CtAction;
import com.sc2.cmtrip.service.CtActionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/action")
public class CtActionController {

    @Autowired
    private CtActionService ctActionService;

    /**
     * Get the id of the specified trip for the currently logged-in user.
     * 获取当前登录用户指定trip对应的id
     * @param tripId
     * @return
     */
    @SaCheckPermission("list-actions-by-trip-id")
    @GetMapping("/list-by-trip-id")
    public ApiResult getMyActionsByTripId(@RequestParam Long tripId) {
        List<CtAction> actions = ctActionService.getMyActions(tripId);
        return ApiResult.success(actions);
    }

    /**
     * Add an action
     * 添加一条行动
     * @param tripId
     * @param ctAction
     */
    @SaCheckPermission("add-action")
    @PostMapping("/add")
    // `@RequestParam` corresponds to `params` (key-value pairs) in JavaScript, while `@RequestBody` corresponds to `data`
    // @RequestParam 对应js中的params（键值对），而@RequestBody对应js中的data
    public void addAction(@RequestParam Long tripId, @RequestBody CtAction ctAction) {
        ctActionService.addAction(tripId, ctAction);
    }

    /**
     * Add an action with an image
     * 添加一条带图片的行动
     * @param tripId
     * @param ctAction
     * @param image
     */
    @SaCheckPermission("add-action")
    @PostMapping("/add-image")
    public void addActionWithImage(
            @RequestParam("tripId") Long tripId,             // URL parameter URL参数：tripId
            @RequestPart("actionForm") CtAction ctAction,    // JSON data JSON数据：CtAction entity CtAction实体类
            @RequestPart("image") MultipartFile image        // file data 文件数据：image to be uploaded 要上传的图片
    ) {
        ctActionService.addActionWithImage(tripId, ctAction, image);
    }

    /**
     * Edit an action
     * 编辑行动
     * @param ctAction
     */
    @SaCheckPermission("edit-action")
    @PostMapping("/edit")
    public void editAction(@RequestBody CtAction ctAction) {
        ctActionService.editAction(ctAction);
    }

    /**
     * Edit an action with an image
     * 修改一条带图片的行动
     * @param ctAction
     * @param image
     */
    @SaCheckPermission("edit-action")
    @PostMapping("/edit-image")
    public void editActionWithImage(@RequestPart("actionForm") CtAction ctAction,
                                    @RequestPart("image") MultipartFile image) {
        ctActionService.editActionWithImage(ctAction, image);
    }

    /**
     * Delete an image
     * 删除一次行动
     * @param id
     */
    @SaCheckPermission("delete-action")
    @DeleteMapping("/delete")
    public void deleteAction(@RequestParam Long id) {
        ctActionService.deleteAction(id);
    }

    /**
     * Bind an action with a pass entity
     * 将一次行程与一个通票实体绑定
     * @param actionId
     * @param entityId
     */
    @SaCheckPermission("bind-action-and-pass-entity")
    @PostMapping("/bind-pass-entity")
    public void bindActionToPassEntity(@RequestParam Long actionId,
                                       @RequestParam Long entityId) {
        ctActionService.bindActionToPassEntity(actionId, entityId);
    }

    /**
     * Remove the relation between an action and a pass entity
     * 移除一条行程对通票的依赖
     * @param actionId
     * @param entityId
     */
    @SaCheckPermission("release-pass-entity-from-action")
    @PostMapping("/release-pass-entity")
    public void releaseActionFromPassEntity(@RequestParam Long actionId,
                                            @RequestParam Long entityId) {
        ctActionService.releaseActionFromPassEntity(actionId, entityId);
    }

    /**
     * Retrieve the bound pass entity of an action
     * 获取绑定在行程上的通票实体
     * @param actionId
     * @return
     */
    @SaCheckPermission("get-bound-pass-entity-of-action")
    @GetMapping("/get-bound-pass-entity")
    public ApiResult getBoundPassEntity(@RequestParam Long actionId) {
        return ApiResult.success(ctActionService.getBoundPassEntity(actionId));
    }

    /**
     * Calculate the cost saved by using a pass
     * 计算与某个通票实体绑定的行程总共可省下多少钱
     * @param entityId
     * @return
     */
    @SaCheckPermission("get-pass-entity-saved-fare")
    @GetMapping("/get-pass-entity-saved-fare")
    public ApiResult getPassEntitySavedFare(@RequestParam Long entityId) {
        return ApiResult.success(ctActionService.getPassEntitySavedFare(entityId));
    }

}
