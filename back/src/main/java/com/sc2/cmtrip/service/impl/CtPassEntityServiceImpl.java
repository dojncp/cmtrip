package com.sc2.cmtrip.service.impl;

import cn.dev33.satoken.stp.StpUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sc2.cmtrip.entity.CtActionPassEntity;
import com.sc2.cmtrip.entity.CtPass;
import com.sc2.cmtrip.entity.CtPassEntity;
import com.sc2.cmtrip.entity.CtUserPassEntity;
import com.sc2.cmtrip.mapper.CtPassEntityMapper;
import com.sc2.cmtrip.service.CtActionPassEntityService;
import com.sc2.cmtrip.service.CtPassEntityService;
import com.sc2.cmtrip.service.CtUserPassEntityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class CtPassEntityServiceImpl extends ServiceImpl<CtPassEntityMapper, CtPassEntity> implements CtPassEntityService {

    @Autowired
    private CtUserPassEntityService ctUserPassEntityService;

    @Autowired
    private CtActionPassEntityService ctActionPassEntityService;

    /**
     * Return all valid pass entities
     * 查询系统中所有的有效通票实体
     * @return
     */
    @Override
    public List<CtPassEntity> getAllPassEntities() {
        LambdaQueryWrapper<CtPassEntity> lcpe = new LambdaQueryWrapper<>();
        if (this.count(lcpe) > 0) {
            List<CtPassEntity> passEntities = this.list(lcpe);
            return passEntities;
        }
        return new ArrayList<>();
    }

    /**
     * Get pass entity list by pass id
     * 根据passId获取passEntityId
     * @param passId
     * @return
     */
    @Override
    public List<CtPassEntity> getPassEntitiesByPassId(Long passId) {
        LambdaQueryWrapper<CtPass> lct = new LambdaQueryWrapper<>();
        // Check whether the pass is deleted 判断传入的passId是否已经被删除
        lct.eq(CtPass::getIsDelete, true)
                .eq(CtPass::getId, passId);
        // Return valid list only when the passId is not deleted 当且仅当passId未被删除时才可作进一步的查询
        if (lct.isEmptyOfEntity()) {
            LambdaQueryWrapper<CtPassEntity> lcp = new LambdaQueryWrapper<>();
            lcp.eq(CtPassEntity::getPassId, passId);
            if (this.count(lcp) > 0) {
                List<CtPassEntity> passEntities = this.list(lcp);
                return passEntities;
            }
        }
        return new ArrayList<>();
    }

    /**
     * Get pass entity id list by pass id
     * 根据passId获取passEntityId
     * @param passId
     * @return
     */
    @Override
    public List<Long> getPassEntityIdsByPassId(Long passId) {
        LambdaQueryWrapper<CtPass> lct = new LambdaQueryWrapper<>();
        // Check whether the pass is deleted 判断传入的passId是否已经被删除
        lct.eq(CtPass::getIsDelete, true)
                .eq(CtPass::getId, passId);
        // Return valid list only when the passId is not deleted 当且仅当passId未被删除时才可作进一步的查询
        if (lct.isEmptyOfEntity()) {
            LambdaQueryWrapper<CtPassEntity> lcp = new LambdaQueryWrapper<>();
            lcp.eq(CtPassEntity::getPassId, passId);
            if (this.count(lcp) > 0) {
                List<Long> passEntities = this.list(lcp).stream().map(CtPassEntity::getId).toList();
                return passEntities;
            }
        }
        return new ArrayList<>();
    }

    /**
     * Get pass entities of the current user
     * 获取当前用户的通票实体
     * @return
     */
    @Override
    public List<CtPassEntity> getMyPassEntities() {
        Long userId = (Long) Long.parseLong((String) StpUtil.getLoginId());;
        LambdaQueryWrapper<CtUserPassEntity> lcupe = new LambdaQueryWrapper<>();
        lcupe.eq(CtUserPassEntity::getUserId, userId);
        if (ctUserPassEntityService.count(lcupe) > 0) {
            List<Long> passEntityIds = ctUserPassEntityService.list(lcupe).stream().map(CtUserPassEntity::getPassEntityId).toList();
            LambdaQueryWrapper<CtPassEntity> lcpe = new LambdaQueryWrapper<>();
            lcpe.in(CtPassEntity::getId, passEntityIds);
            if (this.count(lcpe) > 0) {
                List<CtPassEntity> passEntities = this.list(lcpe);
                return passEntities;
            }
        }
        return new ArrayList<>();
    }

    /**
     * Get pass entities of current user by a passId
     * 根据一个passId，先判断其是否有实体依附于本用户，再返回该passId下辖实体中属于本用户的实体列表
     * @param passId
     * @return
     */
    @Override
    public List<CtPassEntity> getMyPassEntitiesByPassId(Long passId) {
        List<CtPassEntity> allMyPassEntities = getMyPassEntities();
        List<Long> allMyPassEntityIds = allMyPassEntities.stream().map(CtPassEntity::getId).toList();
        Set<Long> allMyPassEntityIdsSet = new HashSet<>(allMyPassEntityIds);
        if (!allMyPassEntities.isEmpty()) {
            List<Long> myPassIds = allMyPassEntities.stream().map(CtPassEntity::getPassId).distinct().toList();
            if (myPassIds.contains(passId)) {
                List<CtPassEntity> myPassEntitiesOfThisPassId = this.getPassEntitiesByPassId(passId);
                List<CtPassEntity> filteredEntities = myPassEntitiesOfThisPassId.stream()
                        .filter(entity -> allMyPassEntityIdsSet.contains(entity.getId()))
                        .toList();
                return filteredEntities;
            }
        }
        return new ArrayList<>();
    }

    /**
     * Add a pass entity
     * 添加通票实例
     * @param ctPassEntity
     */
    @Transactional
    @Override
    public void addPassEntity(CtPassEntity ctPassEntity) {
        // Check the duplication status of new entity 检验新实体是否在当前用户相应通票下重复
        Long userId = Long.parseLong((String) StpUtil.getLoginId());
        boolean isThisEntityNameUnique = isEntityNameOfPassUnique(userId, ctPassEntity.getPassId(), ctPassEntity.getEntityName());
        if (!isThisEntityNameUnique) {
            throw new RuntimeException("Name of the entity already existed under your pass!");
        }
        // The following are the logic for insertion and writing to the database (both local and relational databases) 以下是新增和写库（本库、关系库）逻辑
        Long loginId = Long.parseLong((String) StpUtil.getLoginId());
        ctPassEntity.setCreateBy(String.valueOf(String.valueOf(loginId)));
        // Write to the database 写入数据库
        this.save(ctPassEntity);
        // Get the entity ID 获取实体id
        Long entityId = ctPassEntity.getId();
        // Create a new instance of the relation table 新建关系表实例
        CtUserPassEntity ctpe = new CtUserPassEntity();
        ctpe.setUserId(loginId);
        ctpe.setPassEntityId(entityId);
        ctpe.setCreateBy(String.valueOf(loginId));
        // Write to the relation table 写入关系表
        ctUserPassEntityService.save(ctpe);
    }

    /**
     * Edit a pass entity
     * 编辑通票实例
     * @param ctPassEntity
     */
    @Override
    public void editPassEntity(CtPassEntity ctPassEntity) {
        if (this.isPassEntityWithAnyActions(ctPassEntity.getId())) {
            throw new RuntimeException("This Entity has been connected to at least one action, please delete the connection(s) and then to edit the entity.");
        }
        Long userId = Long.parseLong((String) StpUtil.getLoginId());
        if (!isEntityNameOfPassUniqueExceptSelf(userId, ctPassEntity.getEntityName(), ctPassEntity.getId())) {
            throw new RuntimeException("Name of the action under the same trip already existed!");
        }
        ctPassEntity.setUpdateBy(String.valueOf(userId));
        this.updateById(ctPassEntity);
    }

    /**
     * Delete a pass entity
     * 删除通票实体
     * @param id
     */
    @Transactional
    @Override
    public void deletePassEntity(Long id) {
        // Delete the pass entity 删除当前通票实体
        this.removeById(id);
        if (this.isPassEntityWithAnyActions(id)) {
            throw new RuntimeException("This Entity has been connected to at least one action, please delete the connection(s) and then to delete the entity.");
        }
        // Delete user-entity relations 删除用户与通票实体间关系
        LambdaQueryWrapper<CtUserPassEntity> lcupe = new LambdaQueryWrapper<>();
        lcupe.eq(CtUserPassEntity::getPassEntityId, id);
        ctUserPassEntityService.remove(lcupe);
        // Delete action-entity relations 删除行程与通票实体间关系
        LambdaQueryWrapper<CtActionPassEntity> lcape = new LambdaQueryWrapper<>();
        lcape.eq(CtActionPassEntity::getPassEntityId, id);
        ctActionPassEntityService.remove(lcape);
    }

    /**
     * Check if there are duplicate pass entity names under a specific pass and a special user
     * 判断某一用户在某一通票下有无同名实体。以是否唯一的形式展现，若名称唯一，则返回true
     * @param passId
     * @param entityName
     * @return
     */
    private boolean isEntityNameOfPassUnique(Long userId, Long passId, String entityName) {
        LambdaQueryWrapper<CtUserPassEntity> lcupe = new LambdaQueryWrapper<>();
        lcupe.eq(CtUserPassEntity::getUserId, userId)
                .select(CtUserPassEntity::getPassEntityId);
        if (ctUserPassEntityService.count(lcupe) > 0) {
            List<Long> entityIds = ctUserPassEntityService.list(lcupe).stream().map(CtUserPassEntity::getPassEntityId).toList();
            LambdaQueryWrapper<CtPassEntity> lcpe = new LambdaQueryWrapper<>();
            lcpe.eq(CtPassEntity::getPassId, passId)
                    .in(CtPassEntity::getId, entityIds)
                    .eq(CtPassEntity::getEntityName, entityName);
            if (this.count(lcpe) > 0) {
                return false;
            }
        }
        return true;
    }

    /**
     * Check if there are duplicate pass entity names under the specific pass besides of original entity
     * 编辑时，判断某一用户的实体名称是否与通票下的其他实体相冲突。若实体名称唯一，则返回true。
     * @param newEntityName
     * @param id
     * @return
     */
    private boolean isEntityNameOfPassUniqueExceptSelf(Long userId, String newEntityName, Long id) {
        LambdaQueryWrapper<CtUserPassEntity> lcupe = new LambdaQueryWrapper<>();
        lcupe.eq(CtUserPassEntity::getUserId, userId)
                .select(CtUserPassEntity::getPassEntityId);
        if (ctUserPassEntityService.count(lcupe) > 0) {
            List<Long> entityIds = ctUserPassEntityService.list(lcupe).stream().map(CtUserPassEntity::getPassEntityId).toList();
            if (entityIds.contains(id)) {
                LambdaQueryWrapper<CtPassEntity> lcpe = new LambdaQueryWrapper<>();
                lcpe.eq(CtPassEntity::getId, id)
                        .select(CtPassEntity::getPassId);
                if (this.count(lcpe) == 1) {
                    Long passId = this.list(lcpe).stream().map(CtPassEntity::getPassId).toList().get(0);
                    LambdaQueryWrapper<CtPassEntity> nlcpe = new LambdaQueryWrapper<>();
                    nlcpe.eq(CtPassEntity::getPassId, passId)
                            .in(CtPassEntity::getId, entityIds)
                            .ne(CtPassEntity::getId, id)
                            .eq(CtPassEntity::getEntityName, newEntityName);
                    if (this.count(nlcpe) > 0) {
                        return false;
                    }
                }
            }
        }
        return true;
    }

    /**
     * Check if a pass entity has been connected to any actions
     * 检查一个通票实体是否与若干个行程相挂钩
     * @param entityId
     * @return
     */
    private boolean isPassEntityWithAnyActions(Long entityId) {
        LambdaQueryWrapper<CtActionPassEntity> lcape = new LambdaQueryWrapper<>();
        lcape.eq(CtActionPassEntity::getPassEntityId, entityId);
        if (ctActionPassEntityService.count(lcape) > 0) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * Get action id list by pass entity id
     * 根据通票实体id获取其对应的行程id的列表
     * @param entityId
     * @return
     */
    @Override
    public List<Long> getActionIdsByPassEntityId(Long entityId) {
        LambdaQueryWrapper<CtActionPassEntity> lcape = new LambdaQueryWrapper<>();
        lcape.eq(CtActionPassEntity::getPassEntityId, entityId);
        if (ctActionPassEntityService.count(lcape) > 0) {
            List<CtActionPassEntity> actionPassEntities = ctActionPassEntityService.list(lcape);
            List<Long> actionIds = actionPassEntities.stream().map(CtActionPassEntity::getActionId).toList();
            return actionIds;
        }
        return new ArrayList<>();
    }

}
