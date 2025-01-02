package edu.qingchenjia.heimacomments.entity;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

@Data
public class User implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    private Long id;

    private String phone;

    private String password;

    private String nickName;

    private String icon;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;
}
