package com.fastcampus.simplesns.service;

import com.fastcampus.simplesns.exception.ErrorCode;
import com.fastcampus.simplesns.exception.SnsApplicationException;
import com.fastcampus.simplesns.fixture.PostEntityFixture;
import com.fastcampus.simplesns.fixture.UserEntityFixture;
import com.fastcampus.simplesns.model.entity.PostEntity;
import com.fastcampus.simplesns.model.entity.UserEntity;
import com.fastcampus.simplesns.repository.PostEntityRepository;
import com.fastcampus.simplesns.repository.UserEntityRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@SpringBootTest
public class PostServiceTest {

    @Autowired
    private PostService postService;

    @MockBean
    private PostEntityRepository postEntityRepository;
    @MockBean
    private UserEntityRepository userEntityRepository;

    @Test
    void 포스트작성이_성공한경우() {
        String title = "title";
        String body = "body";
        String userName = "userName";

        // mocking
        when(userEntityRepository.findByUserName(userName)).thenReturn(Optional.of(mock(UserEntity.class)));
        when(postEntityRepository.save(any())).thenReturn(mock(PostEntity.class));
        Assertions.assertDoesNotThrow(() -> postService.create(title, body, userName));
    }

    @Test
    void 포스트작성시_요청한유저가_존재하지않는경우() {
        String title = "title";
        String body = "body";
        String userName = "userName";

        // mocking
        when(userEntityRepository.findByUserName(userName)).thenReturn(Optional.empty());
        when(postEntityRepository.save(any())).thenReturn(mock(PostEntity.class));

        SnsApplicationException e = Assertions.assertThrows(SnsApplicationException.class, () -> postService.create(title, body, userName));
        Assertions.assertEquals(ErrorCode.USER_NOT_FOUND, e.getErrorCode());
    }

    @Test
    void 포스트수정이_성공한경우() {
        Integer postId = 1;
        String title = "title";
        String body = "body";
        String userName = "userName";

        PostEntity postEntity = PostEntityFixture.get(postId, 1, userName);
        UserEntity userEntity = postEntity.getUser();

        // mocking
        when(userEntityRepository.findByUserName(userName)).thenReturn(Optional.of(userEntity));
        when(postEntityRepository.findById(postId)).thenReturn(Optional.of(postEntity));
        when(postEntityRepository.saveAndFlush(any())).thenReturn(postEntity);

        Assertions.assertDoesNotThrow(() -> postService.modify(postId, title, body, userName));
    }

    @Test
    void 포스트수정시_포스트가_존재하지_않는_경우() {
        Integer postId = 1;
        String title = "title";
        String body = "body";
        String userName = "userName";

        PostEntity postEntity = PostEntityFixture.get(postId, 1, userName);
        UserEntity userEntity = postEntity.getUser();

        // mocking
        when(userEntityRepository.findByUserName(userName)).thenReturn(Optional.of(userEntity));
        when(postEntityRepository.findById(postId)).thenReturn(Optional.empty());

        SnsApplicationException e = Assertions.assertThrows(SnsApplicationException.class, () -> postService.modify(postId, title, body, userName));
        Assertions.assertEquals(e.getErrorCode(), ErrorCode.POST_NOT_FOUND);
    }

    @Test
    void 포스트수정시_권한이_없는_경우() {
        Integer postId = 1;
        String title = "title";
        String body = "body";
        String userName = "userName";

        PostEntity postEntity = PostEntityFixture.get(postId, 1, userName);
        UserEntity writer = UserEntityFixture.get(2, "userName1", "password");
        // mocking
        when(userEntityRepository.findByUserName(userName)).thenReturn(Optional.of(writer));
        when(postEntityRepository.findById(postId)).thenReturn(Optional.of(postEntity));

        SnsApplicationException e = Assertions.assertThrows(SnsApplicationException.class, () -> postService.modify(postId, title, body, userName));
        Assertions.assertEquals(e.getErrorCode(), ErrorCode.INVALID_PERMISSION);
    }

    @Test
    void 포스트삭제가_성공한경우() {
        Integer postId = 1;
        String userName = "userName";

        PostEntity postEntity = PostEntityFixture.get(postId, 1, userName);
        UserEntity userEntity = postEntity.getUser();

        // mocking
        when(userEntityRepository.findByUserName(userName)).thenReturn(Optional.of(userEntity));
        when(postEntityRepository.findById(postId)).thenReturn(Optional.of(postEntity));

        Assertions.assertDoesNotThrow(() -> postService.delete(postId, userName));
    }

    @Test
    void 포스트삭제시_포스트가_존재하지_않는_경우() {
        Integer postId = 1;
        String userName = "userName";

        PostEntity postEntity = PostEntityFixture.get(postId, 1, userName);
        UserEntity userEntity = postEntity.getUser();

        // mocking
        when(userEntityRepository.findByUserName(userName)).thenReturn(Optional.of(userEntity));
        when(postEntityRepository.findById(postId)).thenReturn(Optional.empty());

        SnsApplicationException e = Assertions.assertThrows(SnsApplicationException.class, () -> postService.delete(postId, userName));
        Assertions.assertEquals(e.getErrorCode(), ErrorCode.POST_NOT_FOUND);
    }

    @Test
    void 포스트삭제시_권한이_없는_경우() {
        Integer postId = 1;
        String userName = "userName";

        PostEntity postEntity = PostEntityFixture.get(postId, 1, userName);
        UserEntity writer = UserEntityFixture.get(2, "userName1", "password");

        // mocking
        when(userEntityRepository.findByUserName(userName)).thenReturn(Optional.of(writer));
        when(postEntityRepository.findById(postId)).thenReturn(Optional.of(postEntity));

        SnsApplicationException e = Assertions.assertThrows(SnsApplicationException.class, () -> postService.delete(postId, userName));
        Assertions.assertEquals(e.getErrorCode(), ErrorCode.INVALID_PERMISSION);
    }

    @Test
    void 피드목록_요청이_성공한경우() {
        Pageable pageable = mock(Pageable.class);
        when(postEntityRepository.findAll(pageable)).thenReturn(Page.empty());
        Assertions.assertDoesNotThrow(() -> postService.list(pageable));
    }

    @Test
    void 내_피드목록_요청이_성공한경우() {
        Pageable pageable = mock(Pageable.class);
        UserEntity user = mock(UserEntity.class);
        when(userEntityRepository.findByUserName(any())).thenReturn(Optional.of(user));
        when(postEntityRepository.findAllByUser(user, pageable)).thenReturn(Page.empty());
        Assertions.assertDoesNotThrow(() -> postService.my("", pageable));
    }

}
