package edu.qingchenjia.heimacomments.service;

import com.baomidou.mybatisplus.extension.service.IService;
import edu.qingchenjia.heimacomments.common.R;
import edu.qingchenjia.heimacomments.entity.VoucherOrder;

public interface VoucherOrderService extends IService<VoucherOrder> {
    R<Long> seckill(Long voucherId);
}
