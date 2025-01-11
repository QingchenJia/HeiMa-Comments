package edu.qingchenjia.heimacomments.controller;

import edu.qingchenjia.heimacomments.common.R;
import edu.qingchenjia.heimacomments.entity.Voucher;
import edu.qingchenjia.heimacomments.service.SeckillVoucherService;
import edu.qingchenjia.heimacomments.service.VoucherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/voucher")
public class VoucherController {
    @Autowired
    private VoucherService voucherService;
    @Autowired
    private SeckillVoucherService seckillVoucherService;

    /**
     * 添加优惠券接口
     * <p>
     * 该方法通过POST请求接收一个Voucher对象，将其插入到数据库中，并返回插入后的优惠券ID
     * 使用@PostMapping注解指定该方法处理POST请求，请求体中的数据被绑定到Voucher对象中
     *
     * @param voucher 优惠券对象，包含优惠券的详细信息
     * @return 返回一个R对象，泛型为Long，表示插入后的优惠券ID
     */
    @PostMapping
    public R<Long> add(@RequestBody Voucher voucher) {
        return voucherService.insertVoucher(voucher);
    }

    /**
     * 添加秒杀商品接口
     * <p>
     * 该接口用于将特定的商品添加到秒杀活动中通过HTTP POST请求接收客户端发送的秒杀商品信息
     * 并调用服务层方法进行处理，最终返回秒杀商品的ID
     *
     * @param voucher 秒杀商品对象，包含商品的详细信息
     * @return 返回一个响应对象，其中包含新插入的秒杀商品的ID
     */
    @PostMapping("/seckill")
    public R<Long> addSeckill(@RequestBody Voucher voucher) {
        return seckillVoucherService.insertSeckillVoucher(voucher);
    }

    @GetMapping("/list/{shopId}")
    public R<List<Voucher>> listByShopId(@PathVariable Long shopId) {
        return voucherService.listByShopId(shopId);
    }
}
