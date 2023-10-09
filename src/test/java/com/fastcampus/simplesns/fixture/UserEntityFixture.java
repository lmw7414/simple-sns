package com.fastcampus.simplesns.fixture;

import com.fastcampus.simplesns.model.entity.UserEntity;

public class UserEntityFixture {
    public static UserEntity get(Integer userId, String userName, String password) {
        UserEntity result = new UserEntity();
        result.setId(userId);
        result.setUserName(userName);
        result.setPassword(password);
        return result;
    }
}
