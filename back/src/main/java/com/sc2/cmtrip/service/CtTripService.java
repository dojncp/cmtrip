package com.sc2.cmtrip.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.sc2.cmtrip.entity.CtTrip;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface CtTripService extends IService<CtTrip> {
    List<CtTrip> getTripsByTripName(String tripName);

    List<CtTrip> getTripsByTripId(Long tripId);

    List<CtTrip> getMyTrips();

    List<CtTrip> getTripsByUserId(Long userId);

    void addTrip(CtTrip ctTrip);

    void addTripWithImage(CtTrip ctTrip, MultipartFile image);

    void editTrip(CtTrip ctTrip);

    void editTripWithImage(CtTrip ctTrip, MultipartFile image);

    void deleteTrip(Long id);

    @Transactional
    void deleteTrips(List<Long> ids);
}
