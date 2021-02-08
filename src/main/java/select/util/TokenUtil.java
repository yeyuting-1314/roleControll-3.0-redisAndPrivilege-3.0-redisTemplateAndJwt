package select.util;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.ServletRequestAttributes;
import select.system.dto.User;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.List;

/**
 * @author yeyuting
 * @create 2021/1/26
 */
@Component
public class TokenUtil {
    /***
    *2021/1/26 11:39 
    *
    * * @param user
    * * @return : java.lang.String
    */
    public String generateToken(User user){
        Date start = new Date() ;
        long currentTime = System.currentTimeMillis() + 60*60*1000 ;
        Date end = new Date(currentTime);
        String token = "" ;
        token = JWT.create()
                .withAudience(Integer.toString(user.getId()))
                .withAudience(user.getUserName())
                .withIssuedAt(start)
                .withIssuedAt(end)
                .sign(Algorithm.HMAC256(user.getPassword())) ;
        return token ;
    }

    /***
    *2021/1/26 11:42
    *
    * * @param token
     * @param key
    * * @return : java.lang.String
    */
    public static String get(String token , String key){
        List<String> list = JWT.decode(token).getAudience() ;
        String userId = list.get(0) ;
        return userId ;
    }

    /*
     * 获取token
     * */
    /*public String getToken(HttpServletRequest request)  {
        Cookie[] cookies = request.getCookies() ;
        for(Cookie c : cookies){
            if(c.getName() .equals( "token")){
                return c.getValue() ;
            }
        }
        return null ;
    }*/

    public String getToken(User user) {
        String token = null;
        try {
            Date expiresAt = new Date(System.currentTimeMillis() + 24L * 60L * 3600L * 1000L);
            token = JWT.create()
                    .withIssuer("auth0")
                    .withClaim("username", user.getUserName())
                    .withClaim("password", user.getPassword())
                    .withExpiresAt(expiresAt)
                    // 使用了HMAC256加密算法。
                    // mysecret是用来加密数字签名的密钥。
                    .sign(Algorithm.HMAC256("mysecret"));
        } catch (JWTCreationException exception){
            //Invalid Signing configuration / Couldn't convert Claims.
        } catch (IllegalArgumentException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return token;
    }

    public DecodedJWT deToken(final String token) {
        DecodedJWT jwt = null;
        try {
            // 使用了HMAC256加密算法。
            // mysecret是用来加密数字签名的密钥。
            JWTVerifier verifier = JWT.require(Algorithm.HMAC256("mysecret"))
                    .withIssuer("auth0")
                    .build(); //Reusable verifier instance
            jwt = verifier.verify(token);
        } catch (JWTVerificationException exception) {
            //Invalid signature/claims
            exception.printStackTrace();
        } catch (IllegalArgumentException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return jwt;
    }



    }
