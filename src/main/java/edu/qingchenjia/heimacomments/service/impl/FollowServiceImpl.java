package edu.qingchenjia.heimacomments.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import edu.qingchenjia.heimacomments.common.BaseContext;
import edu.qingchenjia.heimacomments.common.R;
import edu.qingchenjia.heimacomments.dto.UserDto;
import edu.qingchenjia.heimacomments.entity.Follow;
import edu.qingchenjia.heimacomments.mapper.FollowMapper;
import edu.qingchenjia.heimacomments.service.FollowService;
import org.springframework.stereotype.Service;

@Service
public class FollowServiceImpl extends ServiceImpl<FollowMapper, Follow> implements FollowService {
    /**
     * 关注或取消关注用户
     *
     * @param followUserId 被关注用户的ID
     * @param isFollow     是否关注，true为关注，false为取消关注
     * @return 返回操作结果的响应对象
     */
    @Override
    public R<?> follow(Long followUserId, Boolean isFollow) {
        // 获取当前用户信息
        UserDto userDto = BaseContext.getCurrentUser();

        // 根据isFollow参数决定是关注还是取消关注操作
        if (isFollow) {
            // 创建关注记录并设置关注的用户ID和当前用户的ID
            Follow follow = new Follow();
            follow.setFollowUserId(followUserId);
            follow.setUserId(userDto.getId());

            // 保存关注记录
            save(follow);
        } else {
            // 创建查询条件，用于查找并删除关注记录
            LambdaQueryWrapper<Follow> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(Follow::getUserId, userDto.getId())
                    .eq(Follow::getFollowUserId, followUserId);

            // 根据查询条件删除关注记录，即取消关注
            remove(queryWrapper);
        }

        // 返回操作成功的结果
        return R.ok();
    }

    /**
     * 检查当前用户是否关注了指定用户
     *
     * @param followUserId 被检查的关注目标用户ID
     * @return 返回一个R对象，包含关注状态信息
     */
    @Override
    public R<?> isFollow(Long followUserId) {
        // 获取当前用户信息
        UserDto userDto = BaseContext.getCurrentUser();

        // 创建查询条件，检查当前用户是否关注了指定用户
        LambdaQueryWrapper<Follow> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Follow::getUserId, userDto.getId())
                .eq(Follow::getFollowUserId, followUserId);

        // 判断关注关系是否存在
        boolean isExist = exists(queryWrapper);
        // 返回关注状态结果
        return R.ok(isExist);
    }
}
