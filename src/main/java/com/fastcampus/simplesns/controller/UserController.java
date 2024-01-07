package com.fastcampus.simplesns.controller;

import com.fastcampus.simplesns.controller.request.UserJoinRequest;
import com.fastcampus.simplesns.controller.request.UserLoginRequest;
import com.fastcampus.simplesns.controller.response.AlarmResponse;
import com.fastcampus.simplesns.controller.response.Response;
import com.fastcampus.simplesns.controller.response.UserJoinResponse;
import com.fastcampus.simplesns.controller.response.UserLoginResponse;
import com.fastcampus.simplesns.exception.ErrorCode;
import com.fastcampus.simplesns.exception.SnsApplicationException;
import com.fastcampus.simplesns.model.User;
import com.fastcampus.simplesns.service.UserService;
import com.fastcampus.simplesns.util.ClassUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

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

    @GetMapping("/alarm")
    public Response<Page<AlarmResponse>> alarm(Authentication authentication, Pageable pageable) {
        User user = ClassUtils.getSafeCastInstance(authentication.getPrincipal(), User.class).orElseThrow(
                () -> new SnsApplicationException(ErrorCode.INTERNAL_SERVER_ERROR,
                        "Casing to User class failed"));
        return Response.success(userService.alarmList(user.getId(), pageable).map(AlarmResponse::fromAlarm));
    }
}
