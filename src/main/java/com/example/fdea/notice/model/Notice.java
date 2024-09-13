package com.example.fdea.notice.model;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
public class Notice {
    String category;
    String content;
    LocalDateTime deadline;
    List<String> keywords;
    int scraps;
    String title;
    LocalDateTime uploadTime;
    String url;
    int views;
    String writer;
}
