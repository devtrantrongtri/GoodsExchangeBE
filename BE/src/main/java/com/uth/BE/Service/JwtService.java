package com.uth.BE.Service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.xml.bind.DatatypeConverter;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.time.Instant;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;
@Service
public class JwtService {
    private  static final String SECRET = "A59EC87C99A9964DE28A7F3317D790F12B11D72A64C170E95DD0FECE41240B5082EFA2B146E15E0B90598DD23DA0E6A295B240EDAB6B03B3454C59D8460975B3";
//    private static final Long VALIDITY  = TimeUnit.HOURS.toMillis(24);
private static final Long VALIDITY  = TimeUnit.MINUTES.toMillis(30); // 30 phuts
    // modified func to parseJwt to claims format.
    private Claims getClaims(String jwt) {
        return Jwts.parser()
                .verifyWith(generateSecretKey())
                .build()
                .parseSignedClaims(jwt)
                .getPayload();
    }
    // generate Json web token
    public String generateToken(UserDetails userDetails) {
        Map<String, String> claims = new HashMap<>();
        claims.put("username", userDetails.getUsername());
        // set a new attribute instead of set... like claims and setClaims.
        return Jwts.builder()
                .claims(claims)
                .subject(userDetails.getUsername())
                .issuedAt(Date.from(Instant.now()))
                .expiration(Date.from(Instant.now().plusMillis(VALIDITY)))
                .signWith(generateSecretKey())
                .compact(); // to Json format


    }

// generate signed key, give the right form for key
    public SecretKey generateSecretKey() {
        byte[] keyByte =  DatatypeConverter.parseHexBinary(SECRET);
        return Keys.hmacShaKeyFor(keyByte);

    }


    public String extractUsername(String jwt) {
/*  Claims :
*       String ISSUER = "iss";
*       String SUBJECT = "sub";
*       String AUDIENCE = "aud";
*       String EXPIRATION = "exp";
*       String NOT_BEFORE = "nbf";
*       String ISSUED_AT = "iat";
*       String ID = "jti";
*
*       to create a parser instant,verify jwt,and ẽtract payload
* */
        Claims claims = getClaims(jwt);
        return claims.getSubject();

    }


    public boolean isTokenValid(String jwt) {
        Claims claims = getClaims(jwt);
        return claims.getExpiration().after(Date.from(Instant.now())); // return true if valid.
    }

}
