package edu.qingchenjia.heimacomments.service;

import com.baomidou.mybatisplus.extension.service.IService;
import edu.qingchenjia.heimacomments.common.R;
import edu.qingchenjia.heimacomments.dto.LoginFormDto;
import edu.qingchenjia.heimacomments.entity.User;
import jakarta.servlet.http.HttpServletRequest;

public interface UserService extends IService<User> {
    R sendCode(HttpServletRequest request, String phone);

    R login(HttpServletRequest request, LoginFormDto loginFormDto);

    User insertUser(String phone);
}
