package com.sc2.cmtrip.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.sc2.cmtrip.entity.CtTripAction;

import java.util.List;

public interface CtTripActionService extends IService<CtTripAction> {
    List<Long> getActionIdsByTripId(Long tripId);

    List<Long> getActionIdsByTripIds(List<Long> tripIds);
}
