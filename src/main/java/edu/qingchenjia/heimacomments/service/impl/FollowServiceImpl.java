package edu.qingchenjia.heimacomments.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import edu.qingchenjia.heimacomments.entity.Follow;
import edu.qingchenjia.heimacomments.mapper.FollowMapper;
import edu.qingchenjia.heimacomments.service.FollowService;
import org.springframework.stereotype.Service;

@Service
public class FollowServiceImpl extends ServiceImpl<FollowMapper, Follow> implements FollowService {
}
