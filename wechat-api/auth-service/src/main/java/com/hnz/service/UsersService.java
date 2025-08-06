package com.hnz.service;

import com.hnz.entity.Users;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 用户表 服务类
 * </p>
 *
 * @author hnz
 * @since 2025-08-05
 */
public interface UsersService {

    Users queryMobileIfExist(String mobile);
    Users createUsers(String mobile, String nickname);
}
