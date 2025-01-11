package edu.qingchenjia.heimacomments.controller;

import edu.qingchenjia.heimacomments.common.R;
import edu.qingchenjia.heimacomments.service.VoucherOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/voucher-order")
public class VoucherOrderController {
    @Autowired
    private VoucherOrderService voucherOrderService;

    /**
     * 处理秒杀请求的控制器方法
     * 该方法使用POST请求映射到/seckill/{id}路径，用于执行特定优惠券的秒杀操作
     *
     * @param voucherId 优惠券的唯一标识符，通过URL路径传递
     * @return 返回一个响应对象R，包含受影响的行数（通常是数据库操作的结果）
     */
    @PostMapping("/seckill/{id}")
    public R<Long> seckill(@PathVariable("id") Long voucherId) {
        return voucherOrderService.seckill(voucherId);
    }
}
