package com.sc2.cmtrip.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sc2.cmtrip.entity.CtAction;
import com.sc2.cmtrip.entity.CtTrip;
import com.sc2.cmtrip.entity.CtTripAction;
import com.sc2.cmtrip.mapper.CtTripActionMapper;
import com.sc2.cmtrip.service.CtTripActionService;
import com.sc2.cmtrip.service.CtTripService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CtTripActionServiceImpl extends ServiceImpl<CtTripActionMapper, CtTripAction> implements CtTripActionService {

    @Autowired
    private CtTripActionMapper ctTripActionMapper;

    /**
     * Find action IDs based on a single trip ID and return the list
     * 根据单个旅行id查找行动id并返回列表
     * @param tripId
     * @return
     */
    @Override
    public List<Long> getActionIdsByTripId(Long tripId) {
        LambdaQueryWrapper<CtTripAction> lcta = new LambdaQueryWrapper<>();
        lcta.eq(CtTripAction::getTripId, tripId).select(CtTripAction::getActionId);
        if (this.count(lcta) > 0) {
            return ctTripActionMapper.selectList(lcta).stream().map(CtTripAction::getActionId).collect(Collectors.toList());
        } else {
            return new ArrayList<>();
        }
    }

    /**
     * Find action IDs based on a list of trip IDs and return the list
     * 根据旅行id列表查找行动id并返回列表
     * @param tripIds
     * @return
     */
    @Override
    public List<Long> getActionIdsByTripIds(List<Long> tripIds) {
        LambdaQueryWrapper<CtTripAction> lcta = new LambdaQueryWrapper<>();
        lcta.in(CtTripAction::getTripId, tripIds).select(CtTripAction::getActionId);
        if (this.count(lcta) > 0) {
            return ctTripActionMapper.selectList(lcta).stream().map(CtTripAction::getActionId).collect(Collectors.toList());
        } else {
            return new ArrayList<>();
        }
    }



}
