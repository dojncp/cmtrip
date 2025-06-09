package com.sc2.cmtrip.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.sc2.cmtrip.common.BaseEntity;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Getter
@Setter
@ToString
@TableName("ct_pass_entity")
public class CtPassEntity extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    private Long passId;

    private String entityName;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate passStartDate;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate passEndDate;

    private String description;

}
