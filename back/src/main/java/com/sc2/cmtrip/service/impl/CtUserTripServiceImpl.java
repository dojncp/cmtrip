package com.sc2.cmtrip.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sc2.cmtrip.entity.CtUserTrip;
import com.sc2.cmtrip.mapper.CtTripActionMapper;
import com.sc2.cmtrip.mapper.CtUserTripMapper;
import com.sc2.cmtrip.service.CtTripService;
import com.sc2.cmtrip.service.CtUserTripService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CtUserTripServiceImpl extends ServiceImpl<CtUserTripMapper, CtUserTrip> implements CtUserTripService {

    @Autowired
    private CtUserTripMapper ctUserTripMapper;

    /**
     * Find trip IDs by user ID and return the list
     * 根据用户id查找旅行id并返回列表
     * @param userId
     * @return
     */
    @Override
    public List<Long> getTripIdsByUserId(Long userId) {
        LambdaQueryWrapper<CtUserTrip> lcut = new LambdaQueryWrapper<>();
        lcut.eq(CtUserTrip::getUserId, userId).select(CtUserTrip::getTripId);
        if (this.count(lcut) > 0) {
            return ctUserTripMapper.selectList(lcut).stream().map(CtUserTrip::getTripId).collect(Collectors.toList());
        } else {
            return new ArrayList<>();
        }
    }



}
