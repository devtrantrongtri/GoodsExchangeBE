package com.uth.BE;

import io.jsonwebtoken.Jwts;
import jakarta.xml.bind.DatatypeConverter;
import org.junit.jupiter.api.Test;

import javax.crypto.SecretKey;

public class JwtKeyMaker {
    @Test
    public void  generateKey() {
        SecretKey keyPure = Jwts.SIG.HS512.key().build();
        String keyEncode = DatatypeConverter.printHexBinary(keyPure.getEncoded());
        System.out.printf("\n SecretKey [%s] \n",keyEncode);
    }
}
