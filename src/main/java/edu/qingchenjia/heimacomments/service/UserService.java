package edu.qingchenjia.heimacomments.service;

import com.baomidou.mybatisplus.extension.service.IService;
import edu.qingchenjia.heimacomments.common.R;
import edu.qingchenjia.heimacomments.dto.LoginFormDto;
import edu.qingchenjia.heimacomments.dto.UserDto;
import edu.qingchenjia.heimacomments.entity.User;
import edu.qingchenjia.heimacomments.entity.UserInfo;
import jakarta.servlet.http.HttpServletRequest;

public interface UserService extends IService<User> {
    R<String> sendCode(String phone);

    R<String> login(LoginFormDto loginFormDto);

    User insertUser(String phone);

    R<String> logout(HttpServletRequest request);

    R<UserDto> me();

    R<UserInfo> info(Long id);

    R<UserDto> queryUserById(Long id);

    R<?> sign();
}
