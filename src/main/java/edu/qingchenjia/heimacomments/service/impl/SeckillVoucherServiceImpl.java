package edu.qingchenjia.heimacomments.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import edu.qingchenjia.heimacomments.common.R;
import edu.qingchenjia.heimacomments.entity.SeckillVoucher;
import edu.qingchenjia.heimacomments.entity.Voucher;
import edu.qingchenjia.heimacomments.mapper.SeckillVoucherMapper;
import edu.qingchenjia.heimacomments.service.SeckillVoucherService;
import edu.qingchenjia.heimacomments.service.VoucherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SeckillVoucherServiceImpl extends ServiceImpl<SeckillVoucherMapper, SeckillVoucher> implements SeckillVoucherService {
    @Autowired
    private VoucherService voucherService;

    /**
     * 插入秒杀凭证信息
     * <p>
     * 该方法首先保存凭证的基本信息，然后创建并保存秒杀凭证的详细信息，包括库存、开始时间和结束时间
     * 最后，返回保存的凭证ID
     *
     * @param voucher 凭证对象，包含凭证的基本信息和秒杀相关信息
     * @return 返回一个结果对象，包含插入凭证的ID
     */
    @Override
    public R<Long> insertSeckillVoucher(Voucher voucher) {
        // 保存凭证的基本信息
        voucherService.save(voucher);

        // 创建秒杀凭证对象，并设置从凭证对象中获取的相关信息
        SeckillVoucher seckillVoucher = new SeckillVoucher();
        seckillVoucher.setVoucherId(voucher.getId());
        seckillVoucher.setStock(voucher.getStock());
        seckillVoucher.setBeginTime(voucher.getBeginTime());
        seckillVoucher.setEndTime(voucher.getEndTime());

        // 保存秒杀凭证的详细信息
        save(seckillVoucher);

        // 返回保存成功的凭证ID
        return R.ok(voucher.getId());
    }
}
