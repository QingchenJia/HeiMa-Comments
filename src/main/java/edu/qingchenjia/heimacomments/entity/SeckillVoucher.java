package edu.qingchenjia.heimacomments.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

@Data
public class SeckillVoucher implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    @TableId
    private Long voucherId;

    private Integer stock;

    private LocalDateTime createTime;

    private LocalDateTime beginTime;

    private LocalDateTime endTime;

    private LocalDateTime updateTime;
}
