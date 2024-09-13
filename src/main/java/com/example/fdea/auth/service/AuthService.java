package com.example.fdea.auth.service;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseToken;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class AuthService {
    public String checkAdmin(FirebaseToken decodedToken) {
        //계정의 이메일 주소 가져오기
        String email = decodedToken.getEmail();
        //이메일 유효성 검증
        boolean isEmailVerified = decodedToken.isEmailVerified();

        if(email != null && isEmailVerified){
            if(email.equals("sophia2359@sookmyung.ac.kr"))
                return "FA";
            else if (email.equals("chaeyeon20@sookmyung.ac.kr"))
                return "DA";
            else
                return "M";
        }else{
            return null;
        }
    }

    public void setRoleCustomClaim(String uid, String role) throws FirebaseAuthException {
        Map<String, Object> claims = new HashMap<>();
        claims.put("role", role);

        FirebaseAuth.getInstance().setCustomUserClaims(uid, claims);
    }
}
