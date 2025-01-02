package edu.qingchenjia.heimacomments.entity;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

@Data
public class Follow implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    private Long id;

    private Long userId;

    private Long followUserId;

    private LocalDateTime createTime;
}
