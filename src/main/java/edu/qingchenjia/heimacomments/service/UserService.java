package edu.qingchenjia.heimacomments.service;

import com.baomidou.mybatisplus.extension.service.IService;
import edu.qingchenjia.heimacomments.common.R;
import edu.qingchenjia.heimacomments.dto.LoginFormDto;
import edu.qingchenjia.heimacomments.entity.User;

public interface UserService extends IService<User> {
    R sendCode(String phone);

    R login(LoginFormDto loginFormDto);

    User insertUser(String phone);
}
