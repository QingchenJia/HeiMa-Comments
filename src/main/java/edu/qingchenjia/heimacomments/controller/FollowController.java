package edu.qingchenjia.heimacomments.controller;

import edu.qingchenjia.heimacomments.common.R;
import edu.qingchenjia.heimacomments.service.FollowService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/follow")
public class FollowController {
    @Autowired
    private FollowService followService;

    /**
     * 处理关注或未关注操作的API接口
     * 该接口用于接收PUT请求，并根据路径变量更新用户的关注状态
     *
     * @param followUserId 用户ID，表示需要进行关注或取消关注的目标用户
     * @param isFollow     关注状态，true表示关注，false表示取消关注
     * @return 返回关注操作的结果，具体结果类型依赖于followService.follow方法的实现
     */
    @PutMapping("/{id}/{isFollow}")
    public R<?> follow(@PathVariable("id") Long followUserId, @PathVariable("isFollow") Boolean isFollow) {
        return followService.follow(followUserId, isFollow);
    }

    /**
     * 检查当前用户是否关注了指定的实体
     *
     * @param followUserId 实体的唯一标识符
     * @return 返回关注状态的结果
     */
    @GetMapping("/or/not/{id}")
    public R<?> isFollow(@PathVariable("id") Long followUserId) {
        return followService.isFollow(followUserId);
    }
}
