package com.example.fdea.notice.service;

//import com.google.api.core.ApiFuture;
//import com.google.cloud.Timestamp;
//import com.google.cloud.firestore.*;
//import com.opencsv.CSVWriter;
//import org.springframework.stereotype.Service;
//
//import java.io.FileWriter;
//import java.io.IOException;
//import java.time.Instant;
//import java.time.LocalDate;
//import java.time.ZoneId;
//import java.time.temporal.ChronoUnit;
//import java.util.*;
//import java.util.concurrent.ExecutionException;
//
//@Service
//public class NoticeFirestoreService {
//    private final Firestore firestore;
//    private final PythonService pythonService;
//    private final String userDataFile;
//    private final String postDataFile;
//    public NoticeFirestoreService(Firestore firestore, PythonService pythonService) {
//        this.userDataFile = "G:\\SchoolService\\api\\userData.csv";
//        this.postDataFile = "G:\\SchoolService\\api\\postData.csv";
//        this.firestore = firestore;
//        this.pythonService = pythonService;
//    }
//
//    //사용자간 스크랩에 따른 유사도를 구하기 위한 데이터 준비 (csv)
//    public void saveScrapsToCSV () throws IOException, ExecutionException, InterruptedException{
//        // CSV 파일 작성 준비
//        FileWriter userFileWriter = new FileWriter(userDataFile, false);
//        CSVWriter userCsvWriter = new CSVWriter(userFileWriter);
//
//        FileWriter postFileWriter = new FileWriter(postDataFile, false);
//        CSVWriter postCsvWriter = new CSVWriter(postFileWriter);
//
//        // CSV 헤더
//        String[] userHeader = {"userID", "recommendPosts", "viewedPosts", "scrappedPosts"};
//        userCsvWriter.writeNext(userHeader);
//
//        String[] postHeader = {"postID", "uploadTime"};
//        postCsvWriter.writeNext(postHeader);
//
//
//        //모든 사용자 문서 가져오기
//        CollectionReference userCollection = firestore.collection("users");
//        ApiFuture<QuerySnapshot> future = userCollection.get(); //apiFuture을 사용해 동기화
//        List<QueryDocumentSnapshot> usersDocuments = future.get().getDocuments();
//
//        // postId의 중복을 제거하기 위해 HashSet 사용 (포스트 데이터 중복 제거용)
//        Set<String> uniquePosts = new HashSet<>();
//
//        // 각 사용자 문서에 대해 공지사항 관련 정보 가져오기
//        for (DocumentSnapshot userDoc : usersDocuments) {
//            String userId = userDoc.getId();
//
//            // 사용자 데이터 추출
//            String recommendPosts = getPostIds(userDoc.getReference(), "recommendPosts");
//            String viewedPosts = getPostIds(userDoc.getReference(), "viewed");
//            String scrappedPosts = getPostIds(userDoc.getReference(), "scrappedPosts");
//
//            // scrappedPosts 데이터가 있는 사용자만 추가
//            if (scrappedPosts != null && !scrappedPosts.isEmpty()) {
//                String[] userRow = {userId, recommendPosts, viewedPosts, scrappedPosts};
//                userCsvWriter.writeNext(userRow);
//
//                // 포스트 데이터 추출
//                extractPostInfo(userDoc.getReference(), "scrappedPosts", uniquePosts);
//            }
//
//        }
//
//        // 중복을 제거한 포스트 데이터를 CSV 파일에 쓰기
//        for (String postInfo : uniquePosts) {
//            // postId와 uploadTime을 분리하여 CSV에 저장
//            String[] postDetails = postInfo.split(", ");
//            postCsvWriter.writeNext(postDetails);
//        }
//
//        // 파일 닫기
//        userCsvWriter.close();
//        userFileWriter.close();
//
//        postCsvWriter.close();
//        postFileWriter.close();
//
//        // 모델을 빌드하는 파이썬 스크립트 실행
//        pythonService.buildCFModel();
//    }
//
//    // 사용자 서브 컬렉션에서 postId와 uploadTime을 추출하는 메소드
//    public void extractPostInfo(DocumentReference userDocRef, String collectionName, Set<String> uniquePosts) throws ExecutionException, InterruptedException {
//        CollectionReference subCollection = userDocRef.collection(collectionName);
//        ApiFuture<QuerySnapshot> future = subCollection.get();
//        List<QueryDocumentSnapshot> documents = future.get().getDocuments();
//
//        // 각 문서에서 postId와 uploadTime을 가져와서 저장
//        for (DocumentSnapshot document : documents) {
//            String postId = document.getId();
//            //posts문서에서 uploadTime 가져오기
//            DocumentReference postDocRef = firestore.collection("posts").document(postId);
//            ApiFuture<DocumentSnapshot> postFuture = postDocRef.get();
//            DocumentSnapshot postDoc = postFuture.get();
//
//            // posts 문서에서 uploadTime 가져오기
//            Date uploadTime = null;
//            if (postDoc.exists()) {
//                uploadTime = postDoc.getDate("uploadTime");
//            }
//
//            // uploadTime이 존재하는지 확인
//            if (uploadTime != null) {
//                // postId와 uploadTime을 하나의 문자열로 합쳐서 저장 (중복 제거)
//                String postInfo = postId + ", " + uploadTime.toString();
//                uniquePosts.add(postInfo);  // Set<String>에 저장
//            }
//        }
//    }
//
//    //userDoc의 서브 콜렉션(조회, 추천, 스크랩)의 문서 가져오기
//    public String getPostIds(DocumentReference userDocRef, String collectionName) throws ExecutionException, InterruptedException {
//        CollectionReference subCollection = userDocRef.collection(collectionName);
//        ApiFuture<QuerySnapshot> future = subCollection.get();
//        List<QueryDocumentSnapshot> documents = future.get().getDocuments();
//
//        //"id,id,id," 형태로 저장하기 위함 -> 나중에 파이썬에서 파싱하여 배열로 사용
//        StringBuilder postIds = new StringBuilder();
//        for (DocumentSnapshot document : documents){
//            postIds.append(document.getId()).append(",");
//        }
//
//        //마지막 콤마는 제거
//        if (!postIds.isEmpty()){
//            postIds.setLength(postIds.length() - 1);
//        }
//
//        return postIds.toString();
//    }
//
//    public List<String> getCFPosts(String userId) throws Exception {
//        // Python 스크립트 실행
//        List<String> postIds = pythonService.getCFList(userId);
//
////        // Firestore에서 문서 조회 및 업로드 날짜로 정렬
////        List<DocumentSnapshot> documents = sortPosts(postIds);
////
////        // 문서를 업로드 날짜로 정렬
////        documents.sort(Comparator.comparing(doc -> doc.getDate("uploadTime")));
//
////        // 정렬된 문서를 원하는 형태로 변환하여 반환
////        List<Map<String, Object>> response = new ArrayList<>();
////        for (DocumentSnapshot doc : documents) {
////            response.add(doc.getData());
////        }
//
//        return postIds;
//    }
//
////    private List<DocumentSnapshot> sortPosts(List<String> postIds) throws ExecutionException, InterruptedException {
////        List<DocumentSnapshot> documents = new ArrayList<>();
////
////        for (String postId : postIds) {
////            String[] parts = postId.split("_");  // 예: site1_board1_post1
////            String siteID = parts[0];
////            String boardID = parts[1];
////            String documentID = parts[2];
////
////            // Firestore에서 문서 조회
////            DocumentReference docRef = firestore.collection("sites").document(siteID)
////                    .collection(boardID).document(documentID);
////
////            ApiFuture<DocumentSnapshot> future = docRef.get();
////            DocumentSnapshot document = future.get();
////
////            if (document.exists()) {
////                documents.add(document);
////            }
////        }
////
////        return documents;
////    }
//
//
//
//    //최근 term 동안 업로드된 필드들만 가져오기 위한 limitDate 생성
//    public Timestamp getLimitDate(int term){
//        LocalDate termAgoDate  = LocalDate.now().minusDays(term); //term(일) 전 날짜 계산
//        Instant limitDate = termAgoDate.atStartOfDay(ZoneId.systemDefault()).toInstant();
//
//        return Timestamp.ofTimeSecondsAndNanos(limitDate.getEpochSecond(), 0);
//    }
//}
