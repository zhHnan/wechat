package com.hnz.controller;

import com.hnz.bo.ModifyUserBO;
import com.hnz.entity.Users;
import com.hnz.result.R;
import com.hnz.service.UsersService;
import com.hnz.utils.RedisOperator;
import com.hnz.vo.UserVO;
import jakarta.annotation.Resource;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

import static com.hnz.base.BaseInfoProperties.*;

/**
 * @Author：hnz
 * @Project：wechat
 * @name：HelloController
 * @Date：2025/7/2 19:29
 * @Filename：HelloController
 */

@RestController
@RequestMapping("userinfo")
public class UserController {
    @Resource
    private UsersService usersService;
    @Resource
    private RedisOperator redis;
    @PostMapping("modify")
    public R modify(@RequestBody ModifyUserBO UserBO) {
        usersService.modifyUserInfo(UserBO);
        UserVO userVO = getUserInfo(UserBO.getUserId(), true);
        return R.ok(userVO);
    }

    private UserVO getUserInfo(String userId, boolean needToken) {
        Users user = usersService.getUserById(userId);
        UserVO userVO = new UserVO();
        BeanUtils.copyProperties(user, userVO);
        if (needToken) {
            String uToken = TOKEN_USER_PREFIX + SYMBOL_DOT + UUID.randomUUID();
            redis.set(REDIS_USER_TOKEN + ":" + userId, uToken);
            userVO.setUserToken(uToken);
        }
        return userVO;
    }
    @PostMapping("get")
    public R get(@RequestParam("userId") String userId) {
        return R.ok(getUserInfo(userId, false));
    }

    @PostMapping("updateFace")
    public R updateFace(@RequestParam("userId") String userId, @RequestParam("face") String face) {
        ModifyUserBO userBO = new ModifyUserBO();
        userBO.setUserId(userId);
        userBO.setFace(face);
        usersService.modifyUserInfo(userBO);
        UserVO userVO = getUserInfo(userBO.getUserId(), true);
        return R.ok(userVO);
    }
    @PostMapping("updateFriendCircleBg")
    public R updateFriendCircleBg(@RequestParam("userId") String userId, @RequestParam("friendCircleBg") String friendCircleBg) {
        ModifyUserBO userBO = new ModifyUserBO();
        userBO.setUserId(userId);
        userBO.setFriendCircleBg(friendCircleBg);
        usersService.modifyUserInfo(userBO);
        UserVO userVO = getUserInfo(userBO.getUserId(), true);
        return R.ok(userVO);
    }
}
