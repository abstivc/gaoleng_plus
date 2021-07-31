package com.adjs.gaoleng_plus.api;

import com.adjs.gaoleng_plus.entity.UserDo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface UserDao {
    UserDo queryUser(UserDo userDo);

    UserDo queryUserById(@Param("userId") String userId);
}
