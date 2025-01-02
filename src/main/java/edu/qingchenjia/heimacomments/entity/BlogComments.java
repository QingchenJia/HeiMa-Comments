package edu.qingchenjia.heimacomments.entity;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

@Data
public class BlogComments implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    private Long id;

    private Long userId;

    private Long blogId;

    private Long parentId;

    private Long answerId;

    private String content;

    private Integer liked;

    private Boolean status;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;
}
