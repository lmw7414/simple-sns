package com.fastcampus.simplesns.controller.request;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class PostCreateRequest {
    private String title;
    private String body;
}
