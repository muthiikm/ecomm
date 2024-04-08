package com.elp.ecom.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.security.KeyStore;
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
                .setExpiration (new Date((System.currentTimeMillis()+10000*60*30)))
                .signWith(SignatureAlgorithm.HS256, getSignKey()).compact();

    }
    private Key getSignKey
            (){

        String base64EncodedSecret = "SECRET";
        byte[] keyBytes = Base64.getDecoder().decode(base64EncodedSecret);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    public String extractUserName(String token){

        return extractUserName(token, Class::getSubject);
    }
    //implementation of claims
    public <T> T extractClaim(String token,Function<Claims, T> claimsResolver){

        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }
    //implementation of extract expiration
private Claims extractAllClaims(String token){
        return Jwts.parserBuilder().setSigningKey(getSignKey().build().parseClaimsJws(token).getBody());
}
//Extract expiration
private Boolean isTokenExpired( String token){
        return extractExpiration(token).before(new Date());
}
//implementation of extract expiration
    private Date extractExpiration(String token){
        return extractClaim(token, claims::getExpiration);
    }
    //validation
    public Boolean validateToken(String token, UserDetails userDetails){
        final String userName = extractUserName(token);
        return (username.equals(userDetails.getUsername()) && ! isTokenExpired(token));
    }

}
