package edu.qingchenjia.heimacomments.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import edu.qingchenjia.heimacomments.entity.ShopType;
import edu.qingchenjia.heimacomments.mapper.ShopTypeMapper;
import edu.qingchenjia.heimacomments.service.ShopTypeService;
import org.springframework.stereotype.Service;

@Service
public class ShopTypeServiceImpl extends ServiceImpl<ShopTypeMapper, ShopType> implements ShopTypeService {
}
