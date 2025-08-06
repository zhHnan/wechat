package com.hnz.mapper;

import com.hnz.entity.Users;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 * 用户表 Mapper 接口
 * </p>
 *
 * @author hnz
 * @since 2025-08-05
 */

@Mapper
public interface UsersMapper extends BaseMapper<Users> {

}
