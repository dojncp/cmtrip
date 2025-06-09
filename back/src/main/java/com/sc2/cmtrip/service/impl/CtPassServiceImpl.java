package com.sc2.cmtrip.service.impl;

import cn.dev33.satoken.stp.StpUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sc2.cmtrip.common.CtUtils;
import com.sc2.cmtrip.entity.CtPass;
import com.sc2.cmtrip.entity.CtPassEntity;
import com.sc2.cmtrip.mapper.CtPassEntityMapper;
import com.sc2.cmtrip.mapper.CtPassMapper;
import com.sc2.cmtrip.service.CtPassEntityService;
import com.sc2.cmtrip.service.CtPassService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class CtPassServiceImpl extends ServiceImpl<CtPassMapper, CtPass> implements CtPassService {

    @Autowired
    private CtPassMapper ctPassMapper;

    @Autowired
    private CtPassEntityService ctPassEntityService;

    @Autowired
    private CtPassEntityMapper ctPassEntityMapper;

    @Autowired
    private CtUtils ctUtils;

    /**
     * Return all valid passes
     * 查询系统中所有的有效通票
     * @return
     */
    @Override
    public List<CtPass> getAllPasses() {
        LambdaQueryWrapper<CtPass> lcp = new LambdaQueryWrapper<>();
        lcp.eq(CtPass::getIsDelete, false);
        if (this.count(lcp) > 0) {
            List<CtPass> passes = this.list(lcp);
            return passes;
        }
        return new ArrayList<>();
    }

    /**
     * Get pass entities by id 根据id查询通票实体
     * @param passId
     * @return
     */
    @Override
    public CtPass getPassById(Long passId) {
        LambdaQueryWrapper<CtPass> lcp = new LambdaQueryWrapper<>();
        lcp.eq(CtPass::getId, passId);
        if(this.count(lcp) > 0) {
            return ctPassMapper.selectById(passId);
        } else {
            return null;
        }
    }

    /**
     * Add a pass
     * 新增通票
     * @param ctPass
     */
    @Override
    public void addPass(CtPass ctPass) {
        if (!isPassNameUnique(ctPass.getPassName())) {
            throw new RuntimeException("Name of the pass already exists!");
        }
        // The currently logged-in user's ID 现在登录的用户id
        String loginId = (String) StpUtil.getLoginId();
        ctPass.setCreateBy(loginId);
        this.save(ctPass);
    }

    /**
     * Add a pass with an image
     * 新增带图片的通票
     * @param ctPass
     * @param image
     */
    @Override
    public void addPassWithImage(CtPass ctPass, MultipartFile image) {
        if (!isPassNameUnique(ctPass.getPassName())) {
            throw new RuntimeException("Name of the pass already exists!");
        }
        // Upload the image and obtain its virtual path 上传图片并获取其虚拟路径
        String imageProfileUrl = ctUtils.uploadImage(image);
        // Write the virtual path into the instance 将虚拟路径写入实例
        ctPass.setImgPath(imageProfileUrl);
        this.addPass(ctPass);
    }

    /**
     * Edit a pass
     * 修改通票
     * @param ctPass
     */
    @Override
    public void editPass(CtPass ctPass) {
        if (!this.isPassNameUniqueExceptSelf(ctPass.getPassName(), ctPass.getId())) {
            throw new RuntimeException("This pass name already exists!");
        }
        // The currently logged-in user's ID 现在登录的用户id
        Long loginId = Long.parseLong((String) StpUtil.getLoginId());
        ctPass.setUpdateBy(String.valueOf(loginId));
        ctPass.setUpdateTime(LocalDateTime.now());
        this.updateById(ctPass);
    }

    /**
     * Edit a pass with an image
     * 修改带图片的通票
     * @param ctPass
     * @param image
     */
    @Override
    public void editPassWithImage(CtPass ctPass, MultipartFile image) {
        if (!this.isPassNameUniqueExceptSelf(ctPass.getPassName(), ctPass.getId())) {
            throw new RuntimeException("This pass name already exists!");
        }
        // The virtual path of the old image 旧图片的虚拟路径
        String oldImageUrl = ctPass.getImgPath();
        // The virtual path of the new image 图片文件虚拟路径
        String imageProfileUrl = ctUtils.uploadImage(image);
        // Write the virtual path into the instance 将虚拟路径写入实例
        ctPass.setImgPath(imageProfileUrl);
        // Call the save method 调用保存方法
        this.editPass(ctPass);
        // If exists, delete the old image file 如果有，则删除旧图片文件
        try {
            if (StringUtils.hasText(oldImageUrl)) {
                ctUtils.deleteFile(oldImageUrl);
            }
        } catch (Exception e) {
            // Record to the logs 记录日志
            log.warn("Failed to delete old pass image: {," + oldImageUrl + "}, error: " + e.getMessage());
        }
    }

    /**
     * Delete a pass (Logical delete)
     * 删除通票（逻辑删除）
     * @param id
     */
    @Transactional
    @Override
    public void deletePass(Long id) {
        // Check whether there are entities belongs to the pass or not 判断是否有附属于待删除通票的实体，若有则不允许删除
        List<Long> passEntityIds = ctPassEntityService.getPassEntityIdsByPassId(id);
        if (!passEntityIds.isEmpty()) {
            throw new RuntimeException("There are pass entity(entities) of this pass had not been deleted yet!");
        }
        CtPass ctPass = ctPassMapper.selectById(id);
        if (ctPass.getIsDelete() == true) {
            throw new RuntimeException("The pass to be deleted was deleted!");
        }
        String imageUrlToDelete = ctPass.getImgPath();
        // Logical delete the pass 逻辑删除通票
        ctPass.setIsDelete(true);
        // Only delete the physical pass image file if an image path exists
        // 仅在有图片地址时，删除pass图片物理文件
        if (imageUrlToDelete != null && !imageUrlToDelete.isEmpty()) {
            try {
                ctUtils.deleteFile(imageUrlToDelete);
            } catch (Exception e) {
                log.warn("Failed to delete the file: " + imageUrlToDelete);
            }
        }
    }

    /**
     * Batch delete passes
     * 批量删除通票
     * @param ids
     */
    @Transactional
    @Override
    public void deletePasses(List<Long> ids) {
        if (ids == null || ids.isEmpty()) {
            return;
        }
        List<Long> passEntityIds = this.getPassEntityIdsByPassIds(ids);
        if (!passEntityIds.isEmpty()) {
            throw new RuntimeException("Some passes has associated entities and cannot be deleted!");
        }
        List<String> imageUrlsToDelete = new ArrayList<>();
        for (Long id : ids) {
            CtPass pass = ctPassMapper.selectById(id);
            // Batch get image urls 批量获取图片url
            if (pass != null && pass.getImgPath() != null && !pass.getImgPath().isEmpty()) {
                imageUrlsToDelete.add(pass.getImgPath());
            }
        }
        // Batch delete passes 批量逻辑删除pass
        LambdaUpdateWrapper<CtPass> lcp = new LambdaUpdateWrapper<>();
        lcp.in(CtPass::getId, ids)
                .set(CtPass::getUpdateTime, LocalDateTime.now())
                .set(CtPass::getUpdateBy, StpUtil.getLoginId())
                .set(CtPass::getIsDelete, 1);
        ctPassMapper.update(null, lcp);
        // Batch delete image files 批量删除图片
        for (String imageUrl : imageUrlsToDelete) {
            try {
                ctUtils.deleteFile(imageUrl);
            } catch (Exception e) {
                log.warn("Failed to delete the file: " + imageUrl);
            }
        }
    }

    /**
     * Get pass entity ids by pass id
     * 查询指定通票对应的实体id列表
     * @param passId
     * @return
     */
    private List<Long> getPassEntityIdsByPassId(Long passId) {
        LambdaQueryWrapper<CtPassEntity> lcpe = new LambdaQueryWrapper<>();
        lcpe.eq(CtPassEntity::getPassId, passId);
        if (ctPassEntityService.count(lcpe) > 0) {
            lcpe.select(CtPassEntity::getId);
            List<Long> passEntityIds = ctPassEntityService.list(lcpe).stream().map(CtPassEntity::getId).toList();
            return passEntityIds;
        }
        return new ArrayList<>();
    }

    /**
     * Get pass entity ids by pass ids
     * 查询指定多张通票对应的实体id列表
     * @param passIds
     * @return
     */
    private List<Long> getPassEntityIdsByPassIds(List<Long> passIds) {
        if (!(passIds == null || passIds.isEmpty())) {
            LambdaQueryWrapper<CtPassEntity> lcpe = new LambdaQueryWrapper<>();
            lcpe.in(CtPassEntity::getPassId, passIds);
            if (ctPassEntityService.count(lcpe) > 0) {
                lcpe.select(CtPassEntity::getId);
                List<Long> passEntityIds = ctPassEntityService.list(lcpe).stream().map(CtPassEntity::getId).toList();
                return passEntityIds;
            }
        }
        return new ArrayList<>();
    }

    /**
     * Check whether the pass name is unique. If it is, return true; otherwise, return false
     * 判断通票名称是否唯一，若唯一则输出true，否则输出false
     * @param passName
     * @return
     */
    private boolean isPassNameUnique(String passName) {
        LambdaQueryWrapper<CtPass> lcp = new LambdaQueryWrapper<>();
        lcp.eq(CtPass::getPassName, passName);
        if (this.count(lcp) > 0) {
            return false;
        } else {
            return true;
        }
    }

    /**
     * Check whether the pass name is unique when edited. If it is, return true; otherwise, return false
     * 编辑时，判断通票名称是否与其他既有通票冲突，若不冲突则输出true，否则输出false
     * @param newPassName
     * @param id
     * @return
     */
    private boolean isPassNameUniqueExceptSelf(String newPassName, Long id) {
        LambdaQueryWrapper<CtPass> lct = new LambdaQueryWrapper<>();
        lct.eq(CtPass::getPassName, newPassName)
                .ne(CtPass::getId, id);
        if (this.count(lct) > 0) {
            List<String> otherPassNames = this.list(lct).stream().map(CtPass::getPassName).toList();
            if (otherPassNames.contains(newPassName)) {
                return false;
            }
        }
        return true;
    }

    /**
     * Get the pass of an entity id
     * 通过通票实体的id查询其所属通票
     * @param entityId
     * @return
     */
    @Override
    public CtPass getPassOfTheEntityId(Long entityId) {
        CtPassEntity entity = ctPassEntityMapper.selectById(entityId);
        if (entity != null) {
            Long passId = entity.getPassId();
            CtPass pass = ctPassMapper.selectById(passId);
            if (pass != null) {
                return pass;
            }
        }
        return new CtPass();
    }

}
