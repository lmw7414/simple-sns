package com.fastcampus.simplesns.controller;

import com.fastcampus.simplesns.controller.request.UserJoinRequest;
import com.fastcampus.simplesns.controller.request.UserLoginRequest;
import com.fastcampus.simplesns.controller.response.Response;
import com.fastcampus.simplesns.controller.response.UserJoinResponse;
import com.fastcampus.simplesns.controller.response.UserLoginResponse;
import com.fastcampus.simplesns.model.User;
import com.fastcampus.simplesns.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    // TODO: implement
    @PostMapping("/join")
    public Response<UserJoinResponse> join(@RequestBody UserJoinRequest request) {
        User user = userService.join(request.getName(), request.getPassword());
        return Response.success(UserJoinResponse.fromUser(user));
    }

    @PostMapping("/login")
    public Response<UserLoginResponse> login(@RequestBody UserLoginRequest request) {
        String token = userService.login(request.getName(), request.getPassword());
        return Response.success(new UserLoginResponse(token));
    }
}
