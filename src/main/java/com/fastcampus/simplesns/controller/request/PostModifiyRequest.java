package com.fastcampus.simplesns.controller.request;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class PostModifiyRequest {
    private String title;
    private String body;
}
