package com.hnz.mapper;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hnz.vo.FriendCircleVO;
import org.apache.ibatis.annotations.Param;

import java.util.Map;

/**
 * <p>
 * 朋友圈表 Mapper 接口
 * </p>
 *
 * @author 风间影月
 * @since 2024-03-27
 */
public interface FriendCircleMapperCustom {

    public Page<FriendCircleVO> queryFriendCircleList(
            @Param("page") Page<FriendCircleVO> page,
            @Param("paramMap") Map<String, Object> map);

}
