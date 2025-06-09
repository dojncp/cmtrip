package com.sc2.cmtrip.controller;

import cn.dev33.satoken.annotation.SaCheckPermission;
import com.sc2.cmtrip.common.ApiResult;
import com.sc2.cmtrip.common.CtUtils;
import com.sc2.cmtrip.entity.CtTrip;
import com.sc2.cmtrip.service.CtTripService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

@RestController
@RequestMapping("/trip")
public class CtTripController {

    @Autowired
    private CtTripService ctTripService;

    /**
     * Retrieve the trips of the current user
     * 获取当前用户的旅行记录
     * @return
     */
    @SaCheckPermission("list-my-trips")
    @GetMapping("/list")
    public ApiResult getMyTrips() {
        List<CtTrip> trips = ctTripService.getMyTrips();
        return ApiResult.success(trips);
    }

    /**
     * Add a trip
     * 新增旅行
     * @param ctTrip
     */
    @SaCheckPermission("add-trip")
    @PostMapping("/add")
    public void addTrip(@RequestBody CtTrip ctTrip) {
        ctTripService.addTrip(ctTrip);
    }

    /**
     * Add a trip with an image
     * 新增带图片的旅行
     * @param ctTrip
     * @param image
     */
    @SaCheckPermission("add-trip")
    @PostMapping("/add-image")
    public void addTripWithImage(@RequestPart("tripForm") CtTrip ctTrip,
                                 @RequestPart("image") MultipartFile image) {
        ctTripService.addTripWithImage(ctTrip, image);
    }

    /**
     * Edit a trip
     * 编辑旅行
     * @param ctTrip
     */
    @SaCheckPermission("edit-trip")
    @PostMapping("/edit")
    public void editTrip(@RequestBody CtTrip ctTrip) {
        ctTripService.editTrip(ctTrip);
    }

    /**
     * Edit a trip with an image
     * 编辑带图片的旅行
     * @param ctTrip
     * @param image
     */
    @SaCheckPermission("edit-trip")
    @PostMapping("/edit-image")
    public void editTripWithImage(@RequestPart("tripForm") CtTrip ctTrip,
                                 @RequestPart("image") MultipartFile image) {
        ctTripService.editTripWithImage(ctTrip, image);
    }

    /**
     * Delete a trip
     * 删除旅行
     * @param id
     */
    @SaCheckPermission("delete-trip")
    @DeleteMapping("/delete")
    public void deleteTrip(@RequestParam Long id) {
        ctTripService.deleteTrip(id);
    }

}
