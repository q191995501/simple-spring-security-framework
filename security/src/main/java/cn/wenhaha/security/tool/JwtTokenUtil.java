package cn.wenhaha.security.tool;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Clock;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.KeyPair;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;

/**
 * @Author: Wyndem
 * @Description:
 * @Date: Created in  2018-11-01 11:11
 * @Modified By: JWT的工具包
 */

@Component
public class JwtTokenUtil {

    private static SignatureAlgorithm ENCRYPTION = SignatureAlgorithm.RS512;

    private static KeyPair keyPair = Keys.keyPairFor(ENCRYPTION);


    @Value("${cn.wenhaha.jwt.expiration}")
    private  Integer expiration;



    public  String doGenerateToken(String userName) {
        Calendar calendar = Calendar.getInstance ();
        calendar.add(Calendar.SECOND,expiration);

        String token = Jwts.builder()
                .setExpiration(calendar.getTime())
                .setSubject(userName)
                .signWith(keyPair.getPrivate(),ENCRYPTION).compact();

        return token;
    }



    public  String  getSubject(String token){
        return getAllClaimsFromToken(token).getSubject();
    }



    private Claims getAllClaimsFromToken(String token) {
        return Jwts.parser()
                .setSigningKey(keyPair.getPublic())
                .parseClaimsJws(token)
                .getBody();
    }





}
