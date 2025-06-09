package com.sc2.cmtrip.service.impl;

import cn.dev33.satoken.stp.StpUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sc2.cmtrip.common.CtUtils;
import com.sc2.cmtrip.entity.CtPassEntity;
import com.sc2.cmtrip.entity.CtUserPassEntity;
import com.sc2.cmtrip.mapper.CtTripMapper;
import com.sc2.cmtrip.mapper.CtUserPassEntityMapper;
import com.sc2.cmtrip.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CtUserPassEntityImpl extends ServiceImpl<CtUserPassEntityMapper, CtUserPassEntity> implements CtUserPassEntityService {

}
