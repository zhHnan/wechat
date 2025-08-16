package com.hnz.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.hnz.api.feign.FileServiceFeign;
import com.hnz.base.BaseInfoProperties;
import com.hnz.entity.Users;
import com.hnz.enums.Sex;
import com.hnz.mapper.UsersMapper;
import com.hnz.service.UsersService;
import com.hnz.utils.LocalDateUtils;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

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
    @Resource
    private FileServiceFeign fileServiceFeign;
    @Override
    public Users queryMobileIfExist(String mobile) {
        return usersMapper.selectOne(new QueryWrapper<Users>().eq("mobile", mobile));
    }

    @Transactional
    @Override
    public Users createUsers(String mobile, String nickname) {
        Users users = new Users();
        users.setMobile(mobile);
        String uuid = UUID.randomUUID().toString();
        String[] split = uuid.split("-");
        String wechatNum = "wx"+ split[0] + split[1];
        users.setWechatNum(wechatNum);
        String wechatNumImg = getQrCodeUrl(wechatNum, TEMP_STRING);
        users.setWechatNumImg(wechatNumImg);
        users.setSex(Sex.secret.type);
        users.setNickname(Objects.requireNonNullElseGet(nickname, () -> "用户" + wechatNum));
        users.setRealName("");
        users.setFace("https://i.loli.net/2021/08/05/XwxZwZQZYyjqKxX.png");
        users.setFriendCircleBg("https://i.loli.net/2021/08/05/XwxZwZQZYyjqKxX.png");
        users.setEmail("");
        users.setBirthday(LocalDateUtils.parseLocalDate("1980-01-01", LocalDateUtils.DATE_PATTERN));
        users.setCountry("中国");
        users.setProvince("北京");
        users.setCity("北京");
        users.setDistrict("东城区");
        users.setCreatedTime(LocalDateTime.now());
        users.setUpdatedTime(LocalDateTime.now());
        usersMapper.insert(users);
        return users;
    }

    private String getQrCodeUrl(String wechatNum, String userId){
        try {
            return fileServiceFeign.generatorQrCode(wechatNum, userId);
        } catch (Exception e) {
            return null;
        }
    }
}
