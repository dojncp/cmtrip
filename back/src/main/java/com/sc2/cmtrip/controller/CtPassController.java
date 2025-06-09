package com.sc2.cmtrip.controller;

import cn.dev33.satoken.annotation.SaCheckLogin;
import cn.dev33.satoken.annotation.SaCheckPermission;
import cn.dev33.satoken.annotation.SaIgnore;
import com.sc2.cmtrip.common.ApiResult;
import com.sc2.cmtrip.entity.CtPass;
import com.sc2.cmtrip.entity.CtTrip;
import com.sc2.cmtrip.service.CtPassService;
import com.sc2.cmtrip.service.CtTripService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/pass")
public class CtPassController {

    @Autowired
    private CtPassService ctPassService;

    /**
     * Retrieve all valid passes
     * 获取系统中所有的有效通票
     * @return
     */
    @SaCheckPermission("list-all-passes")
    @GetMapping("/list")
    public ApiResult getAllPasses() {
        List<CtPass> passes = ctPassService.getAllPasses();
        return ApiResult.success(passes);
    }

    /**
     * Add a pass
     * 新增通票
     * @param ctPass
     */
    @SaCheckPermission("add-pass")
    @PostMapping("/add")
    public void addTrip(@RequestBody CtPass ctPass) {
        ctPassService.addPass(ctPass);
    }

    /**
     * Add a pass with an image
     * 新增带图片的通票
     * @param ctPass
     * @param image
     */
    @SaCheckPermission("add-pass")
    @PostMapping("/add-image")
    public void addPassWithImage(@RequestPart("passForm") CtPass ctPass,
                                 @RequestPart("image") MultipartFile image) {
        ctPassService.addPassWithImage(ctPass, image);
    }

    /**
     * Edit a pass
     * 编辑通票
     * @param ctPass
     */
    @SaCheckPermission("edit-pass")
    @PostMapping("/edit")
    public void editPass(@RequestBody CtPass ctPass) {
        ctPassService.editPass(ctPass);
    }

    /**
     * Edit a pass with an image
     * 编辑带图片的通票
     * @param ctPass
     * @param image
     */
    @SaCheckPermission("edit-pass")
    @PostMapping("/edit-image")
    public void editPassWithImage(@RequestPart("passForm") CtPass ctPass,
                                 @RequestPart("image") MultipartFile image) {
        ctPassService.editPassWithImage(ctPass, image);
    }

    /**
     * Delete a pass
     * 删除通票
     * @param id
     */
    @SaCheckPermission("delete-pass")
    @DeleteMapping("/delete")
    public void deletePass(@RequestParam Long id) {
        ctPassService.deletePass(id);
    }

}
