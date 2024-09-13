package com.example.fdea.notice.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class NoticeBaseResponse {
    int status;
    String message;

    public NoticeBaseResponse(int status, String message) {
        this.status = status;
        this.message = message;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
