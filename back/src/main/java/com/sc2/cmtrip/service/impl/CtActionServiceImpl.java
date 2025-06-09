package com.sc2.cmtrip.service.impl;

import cn.dev33.satoken.stp.StpUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sc2.cmtrip.common.CtUtils;
import com.sc2.cmtrip.entity.*;
import com.sc2.cmtrip.mapper.CtActionMapper;
import com.sc2.cmtrip.mapper.CtPassEntityMapper;
import com.sc2.cmtrip.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
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
    private CtPassService ctPassService;

    @Autowired
    private CtPassEntityService ctPassEntityService;

    @Autowired
    private CtPassEntityMapper ctPassEntityMapper;

    @Autowired
    private CtTripActionService ctTripActionService;

    @Autowired
    private CtActionPassEntityService ctActionPassEntityService;

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
     * 新增行程
     * @param ctAction
     */
    @Transactional
    @Override
    public void addAction(Long tripId, CtAction ctAction) {
        // Check whether there are duplicate names under the same trip 校验相同旅行下，是否有同名行程
        boolean isThisActionNameUnique = isActionNameUnique(tripId, ctAction.getActionName());
        if (!isThisActionNameUnique) {
            throw new RuntimeException("Name of the action under the same trip already existed under the trip!");
        }
        // The following are the logic for insertion and writing to the database (both local and relational databases) 以下是新增和写库（本库、关系库）逻辑
        Long loginId = Long.parseLong((String) StpUtil.getLoginId());
        ctAction.setCreateBy(String.valueOf(loginId));
        // Write to the database 写入数据库
        this.save(ctAction);
        // Get the action ID 获取行动id
        Long actionId = ctAction.getId();
        // Create a new instance of the relation table 新建关系表实例
        CtTripAction cta = new CtTripAction();
        cta.setTripId(tripId);
        cta.setActionId(actionId);
        cta.setCreateBy(String.valueOf(loginId));
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
        if (!isActionNameUniqueExceptSelf(ctAction.getActionName(), ctAction.getId())) {
            throw new RuntimeException("Name of the action under the same trip already existed!");
        }
        // ID of the currently logged-in user 现在登录的用户的id
        Long loginId = Long.parseLong((String) StpUtil.getLoginId());
        ctAction.setUpdateBy(String.valueOf(loginId));
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
        if (!isActionNameUniqueExceptSelf(ctAction.getActionName(), ctAction.getId())) {
            throw new RuntimeException("Name of the action under the same trip already existed!");
        }
        // Old virtual path 旧的虚拟路径
        String oldImageUrl = ctAction.getImgPath();
        // Image url 图片路径
        String imageProfileUrl = ctUtils.uploadImage(image);
        ctAction.setImgPath(imageProfileUrl);
        ctAction.setUpdateBy(String.valueOf(StpUtil.getLoginId()));
        // Write to the database 写入数据库
        this.editAction(ctAction);
        // If present, delete the old image 如果有，则删除旧图片
        try {
            if (StringUtils.hasText(oldImageUrl)) {
                ctUtils.deleteFile(oldImageUrl);
            }
        } catch (Exception e) {
            // Record the log 记录日志
            log.warn("Failed to delete old action image: { " + oldImageUrl + " }, error: " + e.getMessage());
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

    /**
     * Check whether the action name is unique when edited. If it is, return true; otherwise, return false
     * 编辑时，判断行程名称是否与其他既有行程冲突，若不冲突则输出true，否则输出false
     * @param newActionName
     * @param id
     * @return
     */
    private boolean isActionNameUniqueExceptSelf(String newActionName, Long id) {
        LambdaQueryWrapper<CtTripAction> lcta = new LambdaQueryWrapper<>();
        lcta.eq(CtTripAction::getActionId, id)
                .select(CtTripAction::getTripId);
        if (ctTripActionService.count(lcta) == 1) {
            Long tripId = ctTripActionService.list(lcta).stream().map(CtTripAction::getTripId).toList().get(0);
            List<Long> actionIdsOfThisTrip = ctTripActionService.getActionIdsByTripId(tripId);
            LambdaQueryWrapper<CtAction> lca = new LambdaQueryWrapper<>();
            if (!actionIdsOfThisTrip.isEmpty()) {
                lca.eq(CtAction::getActionName, newActionName)
                        .in(CtAction::getId, actionIdsOfThisTrip)
                        .ne(CtAction::getId, id);
                if (this.count(lca) > 0) {
                    List<String> otherActionNames = this.list(lca).stream().map(CtAction::getActionName).toList();
                    if (otherActionNames.contains(newActionName)) {
                        return false;
                    }
                }
            }
        }
        return true;
    }


    /**
     * Bind an action with a pass entity
     * 将一次行程与一个通票实体绑定
     * @param actionId
     * @param entityId
     */
    @Transactional
    @Override
    public void bindActionToPassEntity(Long actionId, Long entityId) {
        CtAction action = ctActionMapper.selectById(actionId);
        CtPassEntity passEntity = ctPassEntityMapper.selectById(entityId);
        if (action != null & passEntity != null) {
            // Check if the action needs to remove the previous entity first
            // 检验行程是否需要先移除之前的关系
            LambdaQueryWrapper<CtActionPassEntity> cape = new LambdaQueryWrapper<>();
            cape.eq(CtActionPassEntity::getActionId, actionId);
            if (ctActionPassEntityService.count(cape) > 0) {
                // Delete the previous relation 删除既有关系后再继续
                ctActionPassEntityService.remove(cape);
            }
            // Check if the currency of the action is same to the currency of the pass entity
            // 检验行程是否在币种上适用通票实体
            String actionCurrency = action.getFareCurrency();
            String passCurrency = ctPassService.getPassOfTheEntityId(entityId).getFareCurrency();
            boolean sameCurrency = actionCurrency.equals(passCurrency);
            // Check if the pass can be connected to the action or not
            // 检验行程是否在时间上适用通票
            LocalDateTime actionStartTime = action.getActionStartTime();
            LocalDate actionStartDate = actionStartTime.toLocalDate();
            LocalDateTime actionEndTime = action.getActionEndTime();
            LocalDate actionEndDate = actionEndTime.toLocalDate();
            LocalDate entityStartDate = passEntity.getPassStartDate();
            LocalDate entityEndDate = passEntity.getPassEndDate();
            boolean validTime = !actionStartDate.isBefore(entityStartDate) && !actionEndDate.isAfter(entityEndDate);
            if (sameCurrency && validTime) {
                CtActionPassEntity actionPassEntity = new CtActionPassEntity();
                actionPassEntity.setActionId(actionId);
                actionPassEntity.setPassEntityId(entityId);
                actionPassEntity.setCreateBy((String) StpUtil.getLoginId());
                ctActionPassEntityService.save(actionPassEntity);
            } else {
                throw new RuntimeException("The Period(or Currency) of the action does not match the period(or currency) of the pass entity, or !");
            }
        } else {
            throw new RuntimeException("Invalid action or pass entity!");
        }
    }

    /**
     * Remove the relation between an action and a pass entity
     * 移除一条行程对通票的依赖
     * @param actionId
     * @param entityId
     */
    @Override
    public void releaseActionFromPassEntity(Long actionId, Long entityId) {
        LambdaQueryWrapper<CtActionPassEntity> lcape = new LambdaQueryWrapper<>();
        lcape.eq(CtActionPassEntity::getActionId, actionId)
                .eq(CtActionPassEntity::getPassEntityId, entityId);
        if (ctActionPassEntityService.count(lcape) == 1) {
            ctActionPassEntityService.remove(lcape);
        } else {
            throw new RuntimeException("The relation between the action and the pass entity does not exist!");
        }
    }

    /**
     * Retrieve the bound pass entity of an action
     * 获取绑定在行程上的通票实体
     * @param actionId
     * @return
     */
    @Override
    public CtPassEntity getBoundPassEntity(Long actionId) {
        LambdaQueryWrapper<CtActionPassEntity> lcape = new LambdaQueryWrapper<>();
        lcape.eq(CtActionPassEntity::getActionId, actionId);
        if (ctActionPassEntityService.count(lcape) == 1) {
            CtActionPassEntity ape = ctActionPassEntityService.getOne(lcape, false);
            Long passEntityId = ape.getPassEntityId();
            LambdaQueryWrapper<CtPassEntity> lcpe = new LambdaQueryWrapper<>();
            lcpe.eq(CtPassEntity::getId, passEntityId);
            if (ctPassEntityService.count(lcpe) == 1) {
                CtPassEntity passEntity = ctPassEntityService.getOne(lcpe, false);
                return passEntity;
            }
        }
        return null;
    }

    /**
     * Calculate the cost saved by using a pass
     * 计算与某个通票实体绑定的行程总共可省下多少钱
     * @param entityId
     * @return
     */
    @Override
    public Integer getPassEntitySavedFare(Long entityId) {
        Integer passFare = 0;
        CtPass pass = ctPassService.getPassOfTheEntityId(entityId);
        if (pass == null || pass.getId() != null) {
            passFare = pass.getFare();
        } else {
            return null;
        }
        List<Long> actionIds = ctPassEntityService.getActionIdsByPassEntityId(entityId);
        if (!actionIds.isEmpty()) {
            Integer totalCost = 0;
            for (Long actionId: actionIds) {
                Integer fare = ctActionMapper.selectById(actionId).getFare();
                if (fare != null) {
                    totalCost = totalCost + fare;
                }
            }
            return totalCost - passFare;
        } else {
            return null;
        }
    }

}
