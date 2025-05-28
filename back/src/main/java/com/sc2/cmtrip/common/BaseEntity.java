package com.sc2.cmtrip.common;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;

/**
 * This indicates that this is a parent class for entities and will not be mapped to its own table.
  */
@MappedSuperclass
@Getter
@Setter
@ToString
public class BaseEntity {

    private LocalDateTime createTime;

    private String createBy;

    private LocalDateTime updateTime;

    private String updateBy;

    private String remark;

}
