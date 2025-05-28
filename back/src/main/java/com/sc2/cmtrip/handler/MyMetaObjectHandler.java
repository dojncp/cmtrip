package com.sc2.cmtrip.handler;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Date;

/**
 * MetaObjectHandler interface for setting createTime and updateTime columns automatically.
 */

@Component
public class MyMetaObjectHandler implements MetaObjectHandler {

    /**
     * When performing an add operation using MyBatis-Plus, this method is automatically executed.
     * 通过 MyBatis-Plus执行添加命令时，以下两个字段自动添加。
     * @param metaObject
     */
    @Override
    public void insertFill(MetaObject metaObject) {
        this.setFieldValByName("createTime", LocalDateTime.now(), metaObject);
        this.setFieldValByName("updateTime", LocalDateTime.now(), metaObject);
    }

    /**
     * When performing an update operation using MyBatis-Plus, this method is automatically executed.
     * 通过 MyBatis-Plus执行修改命令时，以下字段自动修改。
     * @param metaObject
     */
    @Override
    public void updateFill(MetaObject metaObject) {
        this.setFieldValByName("updateTime", LocalDateTime.now(), metaObject);
    }
}
