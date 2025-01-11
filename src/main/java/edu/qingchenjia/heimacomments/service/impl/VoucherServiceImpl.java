package edu.qingchenjia.heimacomments.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import edu.qingchenjia.heimacomments.common.R;
import edu.qingchenjia.heimacomments.entity.Voucher;
import edu.qingchenjia.heimacomments.mapper.VoucherMapper;
import edu.qingchenjia.heimacomments.service.VoucherService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class VoucherServiceImpl extends ServiceImpl<VoucherMapper, Voucher> implements VoucherService {
    /**
     * 插入优惠券信息
     * <p>
     * 该方法负责将给定的优惠券信息保存到数据库中，并返回保存后的优惠券ID
     * 使用了@Override注解来表明这是在重写父类或接口中的方法
     *
     * @param voucher 优惠券对象，包含需要保存的优惠券信息
     * @return 返回一个封装了优惠券ID的响应对象，表示操作结果
     */
    @Override
    public R<Long> insertVoucher(Voucher voucher) {
        // 保存优惠券信息到数据库
        save(voucher);
        // 返回成功响应，携带保存后的优惠券ID
        return R.ok(voucher.getId());
    }

    /**
     * 根据店铺ID获取优惠券列表
     *
     * @param shopId 店铺ID，用于查询属于特定店铺的优惠券
     * @return 返回一个响应对象，包含优惠券列表和列表大小
     * <p>
     * 此方法首先创建一个Lambda查询包装器，用于构造查询条件，
     * 然后使用该条件查询数据库，获取与店铺ID匹配的优惠券列表，
     * 最后，构建并返回一个响应对象，其中包含查询到的优惠券列表和列表的大小
     */
    @Override
    public R<List<Voucher>> listByShopId(Long shopId) {
        // 创建Lambda查询包装器，并设置查询条件为店铺ID等于传入的shopId
        LambdaQueryWrapper<Voucher> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Voucher::getShopId, shopId);

        // 执行查询，获取符合条件的优惠券列表
        List<Voucher> dbVouchers = list(queryWrapper);

        // 构建并返回响应对象，包含查询到的优惠券列表和列表大小
        return R.ok(dbVouchers, (long) dbVouchers.size());
    }
}
