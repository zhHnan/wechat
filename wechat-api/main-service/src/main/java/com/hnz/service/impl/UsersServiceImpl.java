package com.hnz.service.impl;

import com.fasterxml.jackson.databind.util.BeanUtil;
import com.hnz.base.BaseInfoProperties;
import com.hnz.bo.ModifyUserBO;
import com.hnz.entity.Users;
import com.hnz.exceptions.GraceException;
import com.hnz.mapper.UsersMapper;
import com.hnz.result.ResponseStatusEnum;
import com.hnz.service.UsersService;
import jakarta.annotation.Resource;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

import static com.hnz.result.ResponseStatusEnum.USER_FROZEN;
import static com.hnz.result.ResponseStatusEnum.WECHAT_NUM_ALREADY_MODIFIED_ERROR;

/**
 * <p>
 * 用户表 服务实现类
 * </p>
 *
 * @author hnz
 * @since 2025-08-05
 */
@Service
public class UsersServiceImpl extends BaseInfoProperties implements UsersService {
    @Resource
    private UsersMapper usersMapper;
    @Override
    public void modifyUserInfo(ModifyUserBO modifyUserBO) {

        Users users = new Users();
        String userId = modifyUserBO.getUserId();
        String wechatNum = modifyUserBO.getWechatNum();
//        若微信号不为空，则判断是否已经修改过微信号
        if (StringUtils.isNotEmpty(wechatNum)){
            if (redis.keyIsExist(WECHAT_NUM_ALREADY_MODIFIED_ERROR + ":" + userId)){
                GraceException.display(WECHAT_NUM_ALREADY_MODIFIED_ERROR);
            }
        }
        if (StringUtils.isBlank(userId)){
            GraceException.display(ResponseStatusEnum.USER_NOT_EXIST_ERROR);
        }
        users.setId(userId);
        users.setUpdatedTime(LocalDateTime.now());
        BeanUtils.copyProperties(modifyUserBO,users);
        usersMapper.updateById(users);
        if (StringUtils.isNotEmpty(wechatNum)){
            redis.setByDays(WECHAT_NUM_ALREADY_MODIFIED_ERROR + ":" + userId, userId, 365);
        }
    }

    @Override
    public Users getUserById(String userId) {
        return usersMapper.selectById(userId);
    }
}
