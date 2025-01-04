package edu.qingchenjia.heimacomments.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import edu.qingchenjia.heimacomments.entity.VoucherOrder;
import edu.qingchenjia.heimacomments.mapper.VoucherOrderMapper;
import edu.qingchenjia.heimacomments.service.VoucherOrderService;
import org.springframework.stereotype.Service;

@Service
public class VoucherOrderServiceImpl extends ServiceImpl<VoucherOrderMapper, VoucherOrder> implements VoucherOrderService {
}
