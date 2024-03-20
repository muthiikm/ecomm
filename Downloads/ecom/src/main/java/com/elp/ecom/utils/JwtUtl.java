package com.elp.ecom.utils;

import org.springframework.stereotype.Component;
import sun.jvm.hotspot.debugger.cdbg.Sym;

import java.security.Key;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component

public class JwtUtl {
    // This is a secret key. You should keep it secure and not expose it in your code.
    public static final  String SECRET ="2345678900000000987654567890";
    public String generateToken(String userName){

        Map<String, Object> claims = new HashMap<>();
        return createToken(claims , userName);
    }
    private String createToken(Map<String,Object> claims, String userName){
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(userName)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpirationDate(System.currentTimeMillis()+1000*60*30)
                .signWith(getSignKey().SignatureAlgorithm.HS256).compact();

    }
    private Key getSignKey(){

        byte[]keybytes = Decoders.BASE64.decode(SECRET);
        return Keys.hmacShaKeyFor(keybytes);
    }

    public String extractUserName(String token){

        return extractUserName(token, Class::getSubject);
    }
    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver){

        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

}
