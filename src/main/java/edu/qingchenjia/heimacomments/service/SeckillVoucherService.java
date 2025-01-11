package edu.qingchenjia.heimacomments.service;

import com.baomidou.mybatisplus.extension.service.IService;
import edu.qingchenjia.heimacomments.common.R;
import edu.qingchenjia.heimacomments.entity.SeckillVoucher;
import edu.qingchenjia.heimacomments.entity.Voucher;

public interface SeckillVoucherService extends IService<SeckillVoucher> {
    R<Long> insertSeckillVoucher(Voucher voucher);
}
