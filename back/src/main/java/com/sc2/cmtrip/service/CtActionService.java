package com.sc2.cmtrip.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.sc2.cmtrip.entity.CtAction;
import com.sc2.cmtrip.entity.CtPassEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface CtActionService extends IService<CtAction> {

    List<CtAction> getMyActions(Long tripId);

    @Transactional
    void addAction(Long tripId, CtAction ctAction);

    void addActionWithImage(Long tripId, CtAction ctAction, MultipartFile image);

    void editAction(CtAction ctAction);

    void editActionWithImage(CtAction ctAction, MultipartFile image);

    @Transactional
    void deleteAction(Long id);

    @Transactional
    void deleteActions(List<Long> ids);

    void bindActionToPassEntity(Long actionId, Long entityId);

    void releaseActionFromPassEntity(Long actionId, Long entityId);

    CtPassEntity getBoundPassEntity(Long actionId);

    Integer getPassEntitySavedFare(Long entityId);
}
