package com.fastcampus.simplesns.controller;

import com.fastcampus.simplesns.controller.request.PostCreateRequest;
import com.fastcampus.simplesns.controller.request.PostModifiyRequest;
import com.fastcampus.simplesns.controller.response.PostResponse;
import com.fastcampus.simplesns.controller.response.Response;
import com.fastcampus.simplesns.model.Post;
import com.fastcampus.simplesns.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/posts")
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;

    @PostMapping
    public Response<Void> create(@RequestBody PostCreateRequest request, Authentication authentication) {
        postService.create(request.getTitle(), request.getBody(), authentication.getName());
        return Response.success();
    }

    @PutMapping("/{postId}")
    public Response<PostResponse> modify(@PathVariable Integer postId, @RequestBody PostModifiyRequest request, Authentication authentication) {
        Post post = postService.modify(postId, request.getTitle(), request.getBody(), authentication.getName());
        return Response.success(PostResponse.fromPost(post));
    }

    @DeleteMapping("/{postId}")
    public Response<Void> delete(@PathVariable Integer postId, Authentication authentication) {
        postService.delete(postId, authentication.getName());
        return Response.success();
    }
}
