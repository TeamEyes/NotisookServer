package com.example.fdea.auth.controller;


import com.example.fdea.auth.model.IdTokenResponse;
import com.example.fdea.auth.service.AuthService;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/auth")
public class AuthController {
    private final AuthService authService;
    private String idToken;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/verifyToken")
    public ResponseEntity<?> verifyToken(@RequestBody Map<String, String> requestBody){
        idToken = requestBody.get("idToken");

        try{
            //클라이언트로부터 받은 토큰이 유효한 토큰인지 검증
            FirebaseToken decodedToken = FirebaseAuth.getInstance().verifyIdToken(idToken);
            //해당 계정이 관리자 계정인지 검증
            String role = authService.checkAdmin(decodedToken);

            //계정에 따라 role 값 set
            if(role != null) {
                authService.setRoleCustomClaim(decodedToken.getUid(), role);
                return ResponseEntity.ok().body(new IdTokenResponse("true", "User is " + role));
            }else{
                return ResponseEntity.badRequest().body("Failed to verify ID token: your email is not available");
            }

        } catch (FirebaseAuthException e) {
            return ResponseEntity.badRequest().body("Failed to verify ID token: " + e.getMessage());
        }
    }

}
