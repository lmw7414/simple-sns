package com.fastcampus.simplesns.service;

import com.fastcampus.simplesns.exception.ErrorCode;
import com.fastcampus.simplesns.exception.SnsApplicationException;
import com.fastcampus.simplesns.model.entity.PostEntity;
import com.fastcampus.simplesns.model.entity.UserEntity;
import com.fastcampus.simplesns.repository.PostEntityRepository;
import com.fastcampus.simplesns.repository.UserEntityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PostService {

    private final PostEntityRepository postEntityRepository;
    private final UserEntityRepository userEntityRepository;

    @Transactional
    public void create(String title, String body, String userName) {
        UserEntity userEntity = userEntityRepository.findByUserName(userName).orElseThrow(() ->
                new SnsApplicationException(ErrorCode.USER_NOT_FOUND, String.format("%s not founded", userName)));
        postEntityRepository.save(PostEntity.of(title, body, userEntity));
    }
}
