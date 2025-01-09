package edu.qingchenjia.heimacomments.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
public class UserInfo implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    @TableId
    private Long userId;

    private String city;

    private String introduce;

    private Integer fans;

    private Integer followee;

    private Boolean gender;

    private LocalDate birthday;

    private Integer credits;

    private Boolean level;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;
}
