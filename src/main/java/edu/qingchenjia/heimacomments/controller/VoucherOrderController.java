package edu.qingchenjia.heimacomments.controller;

import edu.qingchenjia.heimacomments.service.VoucherOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/voucher-order")
public class VoucherOrderController {
    @Autowired
    private VoucherOrderService voucherOrderService;
}
