package edu.qingchenjia.heimacomments.entity;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

@Data
public class VoucherOrder implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    private Long id;

    private Long userId;

    private Long voucherId;

    private Integer payType;

    private Integer status;

    private LocalDateTime createTime;

    private LocalDateTime payTime;

    private LocalDateTime useTime;

    private LocalDateTime refundTime;

    private LocalDateTime updateTime;
}
