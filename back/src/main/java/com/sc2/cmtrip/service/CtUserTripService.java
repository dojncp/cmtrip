package com.sc2.cmtrip.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.sc2.cmtrip.entity.CtUserTrip;

import java.util.List;

public interface CtUserTripService extends IService<CtUserTrip> {
    List<Long> getTripIdsByUserId(Long userId);
}
