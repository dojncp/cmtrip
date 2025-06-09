package com.sc2.cmtrip.service.impl;

import cn.dev33.satoken.stp.StpUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sc2.cmtrip.common.CtUtils;
import com.sc2.cmtrip.entity.CtTrip;
import com.sc2.cmtrip.entity.CtUserTrip;
import com.sc2.cmtrip.mapper.CtTripMapper;
import com.sc2.cmtrip.service.CtTripActionService;
import com.sc2.cmtrip.service.CtTripService;
import com.sc2.cmtrip.service.CtUserTripService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class CtTripServiceImpl extends ServiceImpl<CtTripMapper, CtTrip> implements CtTripService {

    @Autowired
    private CtTripMapper ctTripMapper;

    @Autowired
    private CtUserTripService ctUserTripService;

    @Autowired
    private CtTripActionService ctTripActionService;

    @Autowired
    private CtUtils ctUtils;

    /**
     * Query the list of trips based on the trip name (regardless of user)
     * 根据旅行名查询旅行列表（不分用户）
     * @param tripName
     * @return
     */
    @Override
    public List<CtTrip> getTripsByTripName(String tripName) {
        LambdaQueryWrapper<CtTrip> lct = new LambdaQueryWrapper<>();
        lct.eq(CtTrip::getTripName, tripName);
        if (this.count(lct) > 0) {
            return ctTripMapper.selectList(lct);
        } else {
            return new ArrayList<>();
        }
    }

    /**
     * Query the list of trips based on trip IDs (regardless of user)
     * 根据旅行id查询旅行列表（不分用户）
     * @param tripId
     * @return
     */
    @Override
    public List<CtTrip> getTripsByTripId(Long tripId) {
        LambdaQueryWrapper<CtTrip> lct = new LambdaQueryWrapper<>();
        lct.eq(CtTrip::getId, tripId);
        if (this.count(lct) > 0) {
            return ctTripMapper.selectList(lct);
        } else {
            return new ArrayList<>();
        }
    }

    /**
     * Get the list of all trips of the current user
     * 获取当前用户所有旅行的列表
     * @return
     */
    @Override
    public List<CtTrip> getMyTrips() {
        Long myUserId = Long.parseLong((String) StpUtil.getLoginId());
        return getTripsByUserId(myUserId);
    }

    /**
     * Query the trips of the corresponding user based on the user ID
     * 根据用户id查询相应用户旅行
     * @param userId
     * @return
     */
    @Override
    public List<CtTrip> getTripsByUserId(Long userId) {
        List<Long> tripIds = ctUserTripService.getTripIdsByUserId(userId);
        if (!tripIds.isEmpty()) {
            LambdaQueryWrapper<CtTrip> lct = new LambdaQueryWrapper<>();
            lct.in(CtTrip::getId, tripIds);
            if (this.count(lct) > 0) {
                return ctTripMapper.selectList(lct);
            }
        }
        return new ArrayList<>();
    }

    /**
     * add a trip
     * 新增旅行
     * @param ctTrip
     */
    @Transactional
    @Override
    public void addTrip(CtTrip ctTrip) {
        // Validate the uniqueness of the username 校验用户名唯一性
        if (!isTripNameUnique(ctTrip.getTripName())) {
            throw new RuntimeException("Name of the trip already existed!");
        }
        // The currently logged-in user's ID 现在登录的用户id
        Long loginId = Long.parseLong((String) StpUtil.getLoginId());
        ctTrip.setCreateBy(String.valueOf(loginId));
        ctTrip.setCreateTime(LocalDateTime.now());
        // Write the trip record 写入trip记录
        this.save(ctTrip);
        // Get the trip ID 获取trip的id
        Long tripId = ctTrip.getId();
        // Create a new instance of the relationship table 新建关系表实例
        CtUserTrip cut = new CtUserTrip();
        cut.setTripId(tripId);
        cut.setUserId(loginId);
        cut.setCreateBy(String.valueOf(loginId));
        cut.setCreateTime(LocalDateTime.now());
        // Write to the relationship table 写入关系表
        ctUserTripService.save(cut);
    }

    /**
     * Add a trip with an image
     * 新增带图片的旅行
     * @param ctTrip
     * @param image
     */
    @Override
    public void addTripWithImage(CtTrip ctTrip, MultipartFile image) {
        // Validate the uniqueness of the username 校验用户名唯一性
        if (!isTripNameUnique(ctTrip.getTripName())) {
            throw new RuntimeException("Name of the trip already existed!");
        }
        // Upload the image and obtain its virtual path 上传图片并获取其虚拟路径
        String imageProfileUrl = ctUtils.uploadImage(image);
        // Write the virtual path into the instance 将虚拟路径写入实例
        ctTrip.setImgPath(imageProfileUrl);
        // Call the save method 调用保存方法
        this.addTrip(ctTrip);
    }

    /**
     * Edit a trip
     * 修改旅行记录
     * @param ctTrip
     */
    @Override
    public void editTrip(CtTrip ctTrip) {
        // 检查其他trip的名字中是否有重复
        if (!isTripNameUniqueExceptSelf(ctTrip.getTripName(), ctTrip.getId())) {
            throw new RuntimeException("This trip name already exists!");
        }
        // The currently logged-in user's ID 现在登录的用户id
        Long loginId = Long.parseLong((String) StpUtil.getLoginId());
        ctTrip.setUpdateBy(String.valueOf(loginId));
        ctTrip.setUpdateTime(LocalDateTime.now());
        this.updateById(ctTrip);
    }

    /**
     * Edit a trip with an image
     * 修改带图片的旅行
     * @param ctTrip
     * @param image
     */
    @Override
    public void editTripWithImage(CtTrip ctTrip, MultipartFile image) {
        // Check whether the new trip name is unique 检查其他trip的名字中是否有重复
        if (!isTripNameUniqueExceptSelf(ctTrip.getTripName(), ctTrip.getId())) {
            throw new RuntimeException("This trip name already exists!");
        }
        // The virtual path of the old image 旧图片的虚拟路径
        String oldImageUrl = ctTrip.getImgPath();
        // The virtual path of the new image 图片文件虚拟路径
        String imageProfileUrl = ctUtils.uploadImage(image);
        // Write the virtual path into the instance 将虚拟路径写入实例
        ctTrip.setImgPath(imageProfileUrl);
        // Call the save method 调用保存方法
        this.editTrip(ctTrip);
        // If exists, delete the old image file 如果有，则删除旧图片文件
        try {
            if (StringUtils.hasText(oldImageUrl)) {
                ctUtils.deleteFile(oldImageUrl);
            }
        } catch (Exception e) {
            // Record to the logs 记录日志
            log.warn("Failed to delete old trip image: {," + oldImageUrl + "}, error: " + e.getMessage());
        }
    }

    /**
     * Delete a trip
     * 删除旅行
     * @param id
     */
    @Transactional
    @Override
    public void deleteTrip(Long id) {
        // Query the number of actions under the current item; if it's not zero, deletion is not allowed
        // 查询名下action数量，如果不为0则不允许删除
        List<Long> actionIds = ctTripActionService.getActionIdsByTripId(id);
        if (!actionIds.isEmpty()) {
            throw new RuntimeException("There are action(s) of this trip had not been deleted!");
        }
        // Path of the image to be deleted 待删图片路径
        String imageUrlToDelete = ctTripMapper.selectById(id).getImgPath();
        // Delete the trip 删除trip记录
        this.removeById(id);
        // Query and delete the records in the user-trip relation table
        // 查询并删除user-trip关系表记录
        LambdaQueryWrapper<CtUserTrip> lcut = new LambdaQueryWrapper<>();
        lcut.eq(CtUserTrip::getTripId, id);
        ctUserTripService.remove(lcut);
        // Only delete the physical trip image file if an image path exists
        // 仅在有图片地址时，删除trip图片物理文件
        if (imageUrlToDelete != null && !imageUrlToDelete.isEmpty()) {
            // Try to delete the image 尝试删除图片
            try {
                ctUtils.deleteFile(imageUrlToDelete);
            } catch (Exception e) {
                // Record the log 记录日志
                log.warn("Failed to delete the file: " + imageUrlToDelete);
            }
        }
    }

    /**
     * Batch delete trips
     * 批量删除旅行
     * @param ids
     */
    @Transactional
    @Override
    public void deleteTrips(List<Long> ids) {
        if (ids == null || ids.isEmpty()) {
            return;
        }
        for (Long id : ids) {
            List<Long> actionIds = ctTripActionService.getActionIdsByTripId(id);
            if (!actionIds.isEmpty()) {
                throw new RuntimeException("Some trips has associated actions and cannot be deleted!");
            }
        }
        List<String> imageUrlsToDelete = new ArrayList<>();
        for (Long id : ids) {
            CtTrip trip = ctTripMapper.selectById(id);
            if (trip != null && trip.getImgPath() != null && !trip.getImgPath().isEmpty()) {
                imageUrlsToDelete.add(trip.getImgPath());
            }
        }
        // Batch delete trips 批量删除旅行
        this.removeByIds(ids);
        // Batch delete user-trip relation table records 批量删除user-trip关系记录
        LambdaQueryWrapper<CtUserTrip> lcut = new LambdaQueryWrapper<>();
        lcut.in(CtUserTrip::getTripId, ids);
        ctUserTripService.remove(lcut);
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
     * Check whether the trip name is unique. If it is, return true; otherwise, return false
     * 判断旅行名称是否唯一，若唯一则输出true，否则输出false
     * @param tripName
     * @return
     */
    private boolean isTripNameUnique(String tripName) {
        LambdaQueryWrapper<CtTrip> lct = new LambdaQueryWrapper<>();
        lct.eq(CtTrip::getTripName, tripName);
        if (this.count(lct) > 0) {
            return false;
        } else {
            return true;
        }
    }

    /**
     * Check whether the trip name is unique when edited. If it is, return true; otherwise, return false
     * 编辑时，判断旅行名称是否与其他既有旅行冲突，若不冲突则输出true，否则输出false
     * @param newTripName
     * @param id
     * @return
     */
    private boolean isTripNameUniqueExceptSelf(String newTripName, Long id) {
        LambdaQueryWrapper<CtTrip> lct = new LambdaQueryWrapper<>();
        lct.eq(CtTrip::getTripName, newTripName)
                .ne(CtTrip::getId, id);
        if (this.count(lct) > 0) {
            List<String> otherTripNames = this.list(lct).stream().map(CtTrip::getTripName).toList();
            if (otherTripNames.contains(newTripName)) {
                return false;
            }
        }
        return true;
    }

}
