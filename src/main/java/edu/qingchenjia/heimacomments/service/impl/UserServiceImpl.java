package edu.qingchenjia.heimacomments.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import edu.qingchenjia.heimacomments.entity.User;
import edu.qingchenjia.heimacomments.mapper.UserMapper;
import edu.qingchenjia.heimacomments.service.UserService;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {
}
