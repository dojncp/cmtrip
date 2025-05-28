package com.sc2.cmtrip.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.sc2.cmtrip.common.BaseEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Transient;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@ToString
@TableName("ct_menu")
@Entity
public class CtMenu extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @Id
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    private String menuName;

    private String path;

    private String component;

    private Long parentId;

    private String icon;

    private Integer sortOrder;

    private boolean visible;

    @Transient// This field will not be stored in the database; it is only used for returning data 这个字段不会存入数据库，只是用来返回数据
    @TableField(exist = false)  // Explicitly inform MyBatis-Plus that this is not a database field 明确告知 MyBatis-Plus 这不是数据库字段
    private List<CtMenu> children = new ArrayList<>();

}
