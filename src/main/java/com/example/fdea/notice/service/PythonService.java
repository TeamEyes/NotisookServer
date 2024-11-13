package com.example.fdea.notice.service;

//import com.google.cloud.Timestamp;
//import org.springframework.stereotype.Service;
//
//import java.io.BufferedReader;
//import java.io.IOException;
//import java.io.InputStreamReader;
//import java.time.LocalDateTime;
//import java.time.ZoneId;
//import java.time.format.DateTimeFormatter;
//import java.util.Arrays;
//import java.util.Date;
//import java.util.List;
//
////파이썬 스크립트를 실행하는 서비스
//@Service
//public class PythonService {
//    //모델을 빌드하는 파이썬 스크립트 실행
//    public void buildCFModel(){
//        try{
//            //실행할 파이썬 스크립트 경로 설정
//            String pythonScriptPath = "G:\\SchoolService\\api\\bpr_train.py";
//
//            //파이썬 스크립트 실행
//            ProcessBuilder processBuilder = new ProcessBuilder("python", pythonScriptPath);
//            processBuilder.redirectErrorStream(true);
//            processBuilder.environment().put("PYTHONIOENCODING", "utf-8");
//
//            //프로세스 시작
//            Process process = processBuilder.start();
//
//            // 에러 스트림 확인
//            BufferedReader err = new BufferedReader(new InputStreamReader(process.getErrorStream()));
//            String errorOutput = err.readLine();
//
//            //프로세스가 종료될 때까지 대기
//            int exitCode = process.waitFor();
//
//            if (exitCode == 0){
//                System.out.println("Python script executed successfully.");
//            }else {
//                throw new RuntimeException("Python 스크립트 실행 중 오류 발생: " + errorOutput);
//            }
//        }catch (IOException | InterruptedException e){
//            e.printStackTrace();
//        }
//    }
//
//    public List<String> getCFList(String userId) throws Exception {
//        // Python 스크립트 경로 설정
//        String pythonScriptPath = "G:\\SchoolService\\api\\recommend.py";
//        ProcessBuilder processBuilder = new ProcessBuilder("python", pythonScriptPath, userId);
//        processBuilder.redirectErrorStream(true);
//        processBuilder.environment().put("PYTHONIOENCODING", "utf-8");
//
//        Process process = processBuilder.start();
//
//        // 출력 및 에러 스트림을 모두 확인
//        BufferedReader in = new BufferedReader(new InputStreamReader(process.getInputStream()));
//        BufferedReader err = new BufferedReader(new InputStreamReader(process.getErrorStream()));
//
//        String output = in.readLine();
//        String errorOutput = err.readLine();
//
//        System.out.println("Python Output: " + output);
//        System.out.println("Python Error: " + errorOutput);
//
//        int exitCode = process.waitFor();
//        if (exitCode != 0) {
//            throw new RuntimeException("Python 스크립트 실행 중 오류 발생: " + errorOutput);
//        }
//
//        // Python 출력 결과를 List<String>으로 변환
//        String result = output.trim();
//        result = result.substring(1, result.length() - 1);  // 대괄호 제거
//        String[] items = result.split(",");
//
//        return Arrays.stream(items)
//                .map(item -> item.trim().replaceAll("^'|'$", ""))  // 앞뒤의 작은따옴표 제거
//                .toList();
//    }
//
////    //크롤링 스크립트 실행
////    public void startCrawling() throws Exception{
////        // Python 스크립트 경로 설정
////        String pythonScriptPath = "G:\\SchoolService\\api\\crawling_all.py";
////        ProcessBuilder processBuilder = new ProcessBuilder("python", pythonScriptPath);
////        processBuilder.redirectErrorStream(true);
////        processBuilder.environment().put("PYTHONIOENCODING", "utf-8");
////
////        Process process = processBuilder.start();
////
////        // 파이썬 출력 읽기
////        BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream(), "utf-8"));
////        String line;
////        String lastOutput = null;
////
////        while ((line = reader.readLine()) != null) {
////            lastOutput = line; // 마지막 출력값 저장
////        }
////
////        if (lastOutput != null) {
////            // 출력된 문자열을 LocalDateTime으로 변환
////            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
////            LocalDateTime dateTime = LocalDateTime.parse(lastOutput, formatter);
////
////            // LocalDateTime을 Date로 변환
////            Date date = Date.from(dateTime.atZone(ZoneId.systemDefault()).toInstant());
////
////            // Date를 Firebase Timestamp로 변환
////            Timestamp firebaseTimestamp = Timestamp.of(date);
////
////            // Firebase에 해당 Timestamp 저장 (Firebase SDK 필요)
////            //saveToFirebase(firebaseTimestamp);
////        }
////
////    }
//}
