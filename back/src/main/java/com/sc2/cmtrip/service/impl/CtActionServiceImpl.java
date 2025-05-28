package com.sc2.cmtrip.service.impl;

import cn.dev33.satoken.stp.StpUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sc2.cmtrip.common.CtUtils;
import com.sc2.cmtrip.entity.CtAction;
import com.sc2.cmtrip.entity.CtTrip;
import com.sc2.cmtrip.entity.CtTripAction;
import com.sc2.cmtrip.mapper.CtActionMapper;
import com.sc2.cmtrip.mapper.CtTripActionMapper;
import com.sc2.cmtrip.service.CtActionService;
import com.sc2.cmtrip.service.CtTripActionService;
import com.sc2.cmtrip.service.CtTripService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CtActionServiceImpl extends ServiceImpl<CtActionMapper, CtAction> implements CtActionService {

    @Autowired
    private CtTripService ctTripService;

    @Autowired
    private CtActionMapper ctActionMapper;

    @Autowired
    private CtTripActionService ctTripActionService;

    @Autowired
    private CtUtils ctUtils;

    /**
     * Retrieve all actions for the specified `tripId` of the current user
     * 获取当前用户指定tripId的所有行动
     * @param tripId
     * @return
     */
    @Override
    public List<CtAction> getMyActions(Long tripId) {
        List<CtTrip> myTrips = ctTripService.getTripsByTripId(tripId);
        if (!myTrips.isEmpty()) {
            List<Long> myTripIds = myTrips.stream().map(CtTrip::getId).collect(Collectors.toList());
            List<Long> myActionIds = ctTripActionService.getActionIdsByTripIds(myTripIds);
            if (!myActionIds.isEmpty()) {
                List<CtAction> myActions = ctActionMapper.selectBatchIds(myActionIds);
                return myActions;
            }
        }
        return new ArrayList<>();
    }

    /**
     * Add an action
     * 新增一条行程
     * @param ctAction
     */
    @Transactional
    @Override
    public void addAction(Long tripId, CtAction ctAction) {
        // Check whether there are duplicate names under the same trip 校验相同旅行下，是否有同名行程
        boolean isThisActionNameUnique = isActionNameUnique(tripId, ctAction.getActionName());
        if (!isThisActionNameUnique) {
            throw new RuntimeException("Name of action already existed in the trip!");
        }
        // The following are the logic for insertion and writing to the database (both local and relational databases) 以下是新增和写库（本库、关系库）逻辑
        Long loginId = Long.parseLong((String) StpUtil.getLoginId());
        ctAction.setCreateBy(String.valueOf(loginId));
        ctAction.setCreateTime(LocalDateTime.now());
        // Write to the database 写入数据库
        this.save(ctAction);
        // Get the action ID 获取行动id
        Long actionId = ctAction.getId();
        // Create a new instance of the relation table 新建关系表实例
        CtTripAction cta = new CtTripAction();
        cta.setTripId(tripId);
        cta.setActionId(actionId);
        cta.setCreateBy(String.valueOf(loginId));
        cta.setCreateTime(LocalDateTime.now());
        // Write to the relation table 写入关系表
        ctTripActionService.save(cta);
    }

    /**
     * Add an action with an image
     * 新增带图片的行动
     * @param tripId
     * @param ctAction
     * @param image
     */
    @Override
    public void addActionWithImage(Long tripId, CtAction ctAction, MultipartFile image) {
        // Check whether there are duplicate names under the same trip 校验行动名唯一性
        if (!isActionNameUnique(tripId, ctAction.getActionName())) {
            throw new RuntimeException("Name of the action under the same trip already existed!");
        }
        // Upload image and obtain the virtual path 上传图片并获取虚拟路径
        String imageProfileUrl = ctUtils.uploadImage(image);
        // Write the virtual path into the instance 将虚拟路径写入实例
        ctAction.setImgPath(imageProfileUrl);
        // Save 保存
        this.addAction(tripId, ctAction);
    }

    /**
     * Edit an action
     * 修改行动
     * @param ctAction
     */
    @Override
    public void editAction(CtAction ctAction) {
        // ID of the currently logged-in user 现在登录的用户的id
        Long loginId = Long.parseLong((String) StpUtil.getLoginId());
        ctAction.setUpdateBy(String.valueOf(loginId));
        ctAction.setUpdateTime(LocalDateTime.now());
        this.updateById(ctAction);
    }

    /**
     * Edit an action with an image
     * 修改一条带图片的行动
     * @param ctAction
     * @param image
     */
    @Override
    public void editActionWithImage(CtAction ctAction, MultipartFile image) {
        // Old virtual path 旧的虚拟路径
        String oldImageUrl = ctAction.getImgPath();
        // Do not check whether there are duplicate names under the same trip 不校验行动名唯一性
        String imageProfileUrl = ctUtils.uploadImage(image);
        ctAction.setImgPath(imageProfileUrl);
        // Write to the database 写入数据库
        this.editAction(ctAction);
        // If present, delete the old image 如果有，则删除旧图片
        try {
            if (StringUtils.hasText(oldImageUrl)) {
                ctUtils.deleteFile(oldImageUrl);
            }
        } catch (Exception e) {
            // Record the log 记录日志
            log.warn("Failed to delete old image: { " + oldImageUrl + " }, error: " + e.getMessage());
        }
    }


    /**
     * Delete an action
     * 删除单条行动
     * @param id
     */
    @Transactional
    @Override
    public void deleteAction(Long id) {
        // Path of the image to be deleted 待删图片路径
        String imageUrlToDelete = ctActionMapper.selectById(id).getImgPath();
        // Delete the action record itself 删除action记录
        this.removeById(id);
        // Query and delete the `trip-action` relation record corresponding to the current action (single entry) 查询并删除当前行动（单条）对应的trip-action关系表记录
        LambdaQueryWrapper<CtTripAction> lcta = new LambdaQueryWrapper<>();
        lcta.eq(CtTripAction::getTripId, id);
        ctTripActionService.remove(lcta);
        // Delete the physical image file only if the image path exists 仅在有图片地址时，删除图片物理文件
        if (imageUrlToDelete != null && !imageUrlToDelete.isEmpty()) {
            // Try to delete the image 尝试删除图片
            try {
                ctUtils.deleteFile(imageUrlToDelete);
            } catch (Exception e) {
                // Record the log 记录日志
                log.warn("Failed to delete the image: { " + imageUrlToDelete + " }, error: " + e.getMessage());
            }
        }
    }

    /**
     * Batch delete actions
     * 批量删除行动
     * @param ids
     */
    @Transactional
    @Override
    public void deleteActions(List<Long> ids) {
        // List of image paths to be deleted 待删除图片路径列表
        List<String> imageUrls = new ArrayList<>();
        for (Long id : ids) {
            String imagePath = ctActionMapper.selectById(id).getImgPath();
            if (!(imagePath == null || imagePath == "")) {
                // Continue only if the image path exists 仅在路径实际存在时才继续
                imageUrls.add(imagePath);
            }
        }
        // Batch delete action records 批量删除action记录
        this.removeBatchByIds(ids);
        // Query and batch delete `trip-action` relation table records 查询并批量删除trip-action关系表记录
        LambdaQueryWrapper<CtTripAction> lcta = new LambdaQueryWrapper<>();
        lcta.in(CtTripAction::getActionId, ids);
        ctTripActionService.remove(lcta);
        // Call the function to batch delete image files 调用函数批量删除图片文件
        try {
            ctUtils.deleteFiles(imageUrls);
        } catch (Exception e) {
            // Record the log 记录日志
            log.warn("Failed to delete the image: " + imageUrls);
        }
    }


    /**
     * Check if there are duplicate action names under a specific trip
     * 查询某个旅行下，是否有重名的行动
     * @param tripId
     * @param actionName
     * @return
     */
    private boolean isActionNameUnique(Long tripId, String actionName) {
        List<Long> actionIdsOfThisTrip = ctTripActionService.getActionIdsByTripId(tripId);
        if (!actionIdsOfThisTrip.isEmpty()) {
            List<CtAction> actions = ctActionMapper.selectBatchIds(actionIdsOfThisTrip);
            List<String> actionNames = actions.stream().map(CtAction::getActionName).collect(Collectors.toList());
            if (actionNames.contains(actionName)) {
                return false;
            }
        }
        return true;
    }

}
