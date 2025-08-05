package com.hnz.service.impl;

import com.hnz.entity.Users;
import com.hnz.mapper.UsersMapper;
import com.hnz.service.UsersService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 用户表 服务实现类
 * </p>
 *
 * @author hnz
 * @since 2025-08-05
 */
@Service
public class UsersServiceImpl extends ServiceImpl<UsersMapper, Users> implements UsersService {

}
