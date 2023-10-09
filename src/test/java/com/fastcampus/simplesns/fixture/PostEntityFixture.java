package com.fastcampus.simplesns.fixture;

import com.fastcampus.simplesns.model.entity.PostEntity;
import com.fastcampus.simplesns.model.entity.UserEntity;

public class PostEntityFixture {
    public static PostEntity get(Integer postId, Integer userId, String userName) {
        UserEntity user = new UserEntity();
        user.setId(userId);
        user.setUserName(userName);

        PostEntity result = new PostEntity();
        result.setUser(user);
        result.setId(postId);
        return result;
    }
}
