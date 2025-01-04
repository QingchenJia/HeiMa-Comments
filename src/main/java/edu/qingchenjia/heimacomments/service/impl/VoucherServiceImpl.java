package edu.qingchenjia.heimacomments.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import edu.qingchenjia.heimacomments.entity.Voucher;
import edu.qingchenjia.heimacomments.mapper.VoucherMapper;
import edu.qingchenjia.heimacomments.service.VoucherService;
import org.springframework.stereotype.Service;

@Service
public class VoucherServiceImpl extends ServiceImpl<VoucherMapper, Voucher> implements VoucherService {
}
