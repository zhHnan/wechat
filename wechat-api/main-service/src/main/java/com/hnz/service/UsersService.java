package com.hnz.service;

import com.hnz.bo.ModifyUserBO;
import com.hnz.entity.Users;

/**
 * <p>
 * 用户表 服务类
 * </p>
 *
 * @author hnz
 * @since 2025-08-05
 */
public interface UsersService {
    void modifyUserInfo(ModifyUserBO modifyUserBO);
    Users getUserById(String userId);
}
