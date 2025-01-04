package edu.qingchenjia.heimacomments.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import edu.qingchenjia.heimacomments.entity.UserInfo;
import edu.qingchenjia.heimacomments.mapper.UserInfoMapper;
import edu.qingchenjia.heimacomments.service.UserInfoService;
import org.springframework.stereotype.Service;

@Service
public class UserInfoServiceImpl extends ServiceImpl<UserInfoMapper, UserInfo> implements UserInfoService {
}
