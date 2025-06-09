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
@TableName("ct_pass")
public class CtPass extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    private String passName;

    private String issueCompany;

    private Integer validDays;

    private Integer fare;

    private String fareCurrency;

    private String imgPath;

    private String description;

    private Boolean isDelete;

}
