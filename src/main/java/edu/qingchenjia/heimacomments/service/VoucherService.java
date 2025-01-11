package edu.qingchenjia.heimacomments.service;

import com.baomidou.mybatisplus.extension.service.IService;
import edu.qingchenjia.heimacomments.common.R;
import edu.qingchenjia.heimacomments.entity.Voucher;

import java.util.List;

public interface VoucherService extends IService<Voucher> {
    R<Long> insertVoucher(Voucher voucher);

    R<List<Voucher>> listByShopId(Long shopId);
}
