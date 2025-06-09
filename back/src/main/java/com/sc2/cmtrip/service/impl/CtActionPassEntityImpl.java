package com.sc2.cmtrip.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sc2.cmtrip.common.CtUtils;
import com.sc2.cmtrip.entity.CtActionPassEntity;
import com.sc2.cmtrip.entity.CtPass;
import com.sc2.cmtrip.mapper.CtActionPassEntityMapper;
import com.sc2.cmtrip.mapper.CtPassMapper;
import com.sc2.cmtrip.mapper.CtTripMapper;
import com.sc2.cmtrip.service.CtActionPassEntityService;
import com.sc2.cmtrip.service.CtPassService;
import com.sc2.cmtrip.service.CtTripActionService;
import com.sc2.cmtrip.service.CtUserTripService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CtActionPassEntityImpl extends ServiceImpl<CtActionPassEntityMapper, CtActionPassEntity> implements CtActionPassEntityService {

}
