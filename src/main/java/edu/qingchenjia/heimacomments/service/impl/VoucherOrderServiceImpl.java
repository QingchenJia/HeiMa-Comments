package edu.qingchenjia.heimacomments.service.impl;

import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import edu.qingchenjia.heimacomments.common.BaseContext;
import edu.qingchenjia.heimacomments.common.R;
import edu.qingchenjia.heimacomments.dto.UserDto;
import edu.qingchenjia.heimacomments.entity.SeckillVoucher;
import edu.qingchenjia.heimacomments.entity.VoucherOrder;
import edu.qingchenjia.heimacomments.mapper.VoucherOrderMapper;
import edu.qingchenjia.heimacomments.service.SeckillVoucherService;
import edu.qingchenjia.heimacomments.service.VoucherOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
public class VoucherOrderServiceImpl extends ServiceImpl<VoucherOrderMapper, VoucherOrder> implements VoucherOrderService {
    @Autowired
    private SeckillVoucherService seckillVoucherService;

    /**
     * 执行秒杀操作
     *
     * @param voucherId 优惠券ID
     * @return 返回一个封装了长整型数据的响应对象，表示秒杀结果
     */
    @Override
    @Transactional
    public R<Long> seckill(Long voucherId) {
        // 获取秒杀优惠券的详细信息
        SeckillVoucher dbSeckillVoucher = seckillVoucherService.getById(voucherId);
        // 检查当前时间是否在秒杀时间范围内
        if (LocalDateTime.now().isBefore(dbSeckillVoucher.getBeginTime()) || LocalDateTime.now().isAfter(dbSeckillVoucher.getEndTime())) {
            return R.fail("当前时间不在秒杀时间内");
        }

        // 获取秒杀优惠券的库存数量
        Integer stock = dbSeckillVoucher.getStock();
        // 检查秒杀优惠券是否有库存
        if (stock <= 0) {
            return R.fail("当前优惠券已被抢光");
        }

        // 准备更新秒杀优惠券的库存信息
        LambdaUpdateWrapper<SeckillVoucher> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.set(SeckillVoucher::getStock, stock - 1)
                .eq(SeckillVoucher::getVoucherId, voucherId)
                .eq(SeckillVoucher::getStock, stock);

        // 获取当前登录用户信息
        UserDto userDto = BaseContext.getCurrentUser();

        // 创建一个新的优惠券订单实例
        VoucherOrder voucherOrder = new VoucherOrder();
        // 设置订单的用户ID
        voucherOrder.setUserId(userDto.getId());
        // 设置订单的优惠券ID
        voucherOrder.setVoucherId(voucherId);
        // 保存订单信息到数据库
        save(voucherOrder);

        // 返回封装了订单ID的响应对象
        return R.ok(voucherOrder.getId());
    }
}
