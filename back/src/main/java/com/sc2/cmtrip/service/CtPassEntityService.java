package com.sc2.cmtrip.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.sc2.cmtrip.entity.CtPass;
import com.sc2.cmtrip.entity.CtPassEntity;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface CtPassEntityService extends IService<CtPassEntity> {

    List<CtPassEntity> getAllPassEntities();

    List<CtPassEntity> getPassEntitiesByPassId(Long passId);

    List<Long> getPassEntityIdsByPassId(Long passId);

    List<CtPassEntity> getMyPassEntities();

    List<CtPassEntity> getMyPassEntitiesByPassId(Long passId);

    @Transactional
    void addPassEntity(CtPassEntity ctPassEntity);

    void editPassEntity(CtPassEntity ctPassEntity);

    @Transactional
    void deletePassEntity(Long id);

    List<Long> getActionIdsByPassEntityId(Long entityId);
}
