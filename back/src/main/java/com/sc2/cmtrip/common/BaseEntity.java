package com.sc2.cmtrip.common;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;

/**
 * This indicates that this is a parent class for entities and will not be mapped to its own table.
 * 这是所有实体类的继承类
 */
@MappedSuperclass
@Getter
@Setter
@ToString
public class BaseEntity {

    // Annotate here to automatically insert by the framework 此处注解以在save操作时由框架自动插入创建时间
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    private String createBy;

    // Annotate here to automatically edit by the framework 此处注解以在update操作时由框架自动编辑修改时间
    @TableField(fill = FieldFill.UPDATE)
    private LocalDateTime updateTime;

    private String updateBy;

    private String remark;

}
