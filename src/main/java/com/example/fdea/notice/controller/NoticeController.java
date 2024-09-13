package com.example.fdea.notice.controller;

import com.example.fdea.notice.model.NoticeBaseResponse;
import com.example.fdea.notice.service.NoticeFirestoreService;
import com.example.fdea.notice.service.PythonService;
import com.google.api.Http;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.List;
import java.util.Map;


@RestController
@RequestMapping("/notice")
public class NoticeController {
    private final NoticeFirestoreService noticeFirestoreService;
    private final PythonService pythonService;

    public NoticeController(NoticeFirestoreService noticeFirestoreService, PythonService pythonService) {
        this.noticeFirestoreService = noticeFirestoreService;
        this.pythonService = pythonService;
    }

//    @GetMapping("/crawling")
//    public ResponseEntity<?> crawling(){
//
//    }

    @GetMapping("/build-model")
    public ResponseEntity<?> getModel(){
        try{
            noticeFirestoreService.saveScrapsToCSV();
            return ResponseEntity.status(HttpStatus.OK)
                    .body(new NoticeBaseResponse(HttpStatus.OK.value(), "build model success"));
        }catch (Exception e){
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new NoticeBaseResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(), e.getMessage()));
        }
    }
    @GetMapping("/recommend/user-similar")
    public ResponseEntity<?> getCFRecommendations(
            @RequestParam("userId") String userId,
            @RequestParam(value = "nRecommendations", defaultValue = "5") String nRecommendations) {
        try {
            // 서비스 호출
            List<String> recommendations = noticeFirestoreService.getCFPosts(userId);
            return ResponseEntity.ok(recommendations);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Internal Server Error.");
        }
    }

    //@GetMapping("/recommend/post-similar")

}
