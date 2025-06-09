package com.sc2.cmtrip.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.sc2.cmtrip.entity.CtPass;
import com.sc2.cmtrip.entity.CtTrip;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface CtPassService extends IService<CtPass> {

    List<CtPass> getAllPasses();

    CtPass getPassById(Long passId);

    void addPass(CtPass ctPass);

    void addPassWithImage(CtPass ctPass, MultipartFile image);

    void editPass(CtPass ctPass);

    void editPassWithImage(CtPass ctPass, MultipartFile image);

    @Transactional
    void deletePass(Long id);

    @Transactional
    void deletePasses(List<Long> ids);

    CtPass getPassOfTheEntityId(Long entityId);
}
