package edu.qingchenjia.heimacomments.service;

import com.baomidou.mybatisplus.extension.service.IService;
import edu.qingchenjia.heimacomments.common.R;
import edu.qingchenjia.heimacomments.dto.UserDto;
import edu.qingchenjia.heimacomments.entity.Follow;

import java.util.List;

public interface FollowService extends IService<Follow> {
    R<?> follow(Long followUserId, Boolean isFollow);

    R<?> isFollow(Long followUserId);

    R<List<UserDto>> commonFollows(Long id);
}
