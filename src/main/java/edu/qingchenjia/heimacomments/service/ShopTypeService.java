package edu.qingchenjia.heimacomments.service;

import com.baomidou.mybatisplus.extension.service.IService;
import edu.qingchenjia.heimacomments.common.R;
import edu.qingchenjia.heimacomments.entity.ShopType;

import java.util.List;

public interface ShopTypeService extends IService<ShopType> {
    R<List<ShopType>> selectList();
}
