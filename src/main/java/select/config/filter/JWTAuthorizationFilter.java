package select.config.filter;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.stereotype.Component;
import redis.clients.jedis.Jedis;
import select.constants.JwtUtils;
import select.system.dto.User;
import select.util.JedisUtil;
import select.util.TokenUtil;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author yeyuting
 * @create 2021/1/29
 */
//验证成功后开始鉴权
/*@Component("jwtAuthorizationFilter")*/
public class JWTAuthorizationFilter extends BasicAuthenticationFilter {

    @Autowired
    JedisUtil jedisUtil ;

    @Autowired
    RedisTemplate redisTemplate ;

    @Autowired
    TokenUtil tokenUtil ;


    //redisTemplate注入
    public JWTAuthorizationFilter(AuthenticationManager authenticationManager , RedisTemplate redisTemplate , TokenUtil tokenUtil) {
        super(authenticationManager);
        this.redisTemplate = redisTemplate ;
        this.tokenUtil = tokenUtil ;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain chain) throws IOException, ServletException {

        String tokenHeader = request.getHeader(JwtUtils.TOKEN_HEADER);
        // 如果请求头中没有Authorization信息则直接放行了
        if (tokenHeader == null) {
            super.doFilterInternal(request, response, chain);
            return;
        }
        // 如果请求头中有token，则进行解析
        SecurityContextHolder.getContext().setAuthentication(getAuthentication(tokenHeader));
        super.doFilterInternal(request, response, chain);
    }

    // 从token中获取用户信息
    private UsernamePasswordAuthenticationToken getAuthentication(String tokenHeader) {
        //解析token，拿到username
        DecodedJWT jwt = tokenUtil.deToken(tokenHeader) ;
        String userName = jwt.getClaim("username").asString() ;
        if(userName.equals(null)){
            return null ;
        }
        //去redis里面拿token 确认redis中存在和token对应的值
        ValueOperations UserVO = redisTemplate.opsForValue();
        User newUser = (User)  UserVO.get(userName) ;
        String redisToken = newUser.getToken() ;
        List<GrantedAuthority> grantedAuthorities = newUser.getGrantedAuthorities() ;
        if (redisToken.equals(tokenHeader)){
            return new UsernamePasswordAuthenticationToken(userName, null, grantedAuthorities);
        }
        return null;
    }

    /*@Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain chain) throws IOException, ServletException {

        String tokenHeader = request.getHeader(JwtUtils.TOKEN_HEADER);
        //String roleHeader = request.getHeader(JwtTokenUtils.ROLE_HEADER) ;
        // 如果请求头中没有Authorization信息则直接放行了
        if (tokenHeader == null) {
            super.doFilterInternal(request, response, chain);
            return;
        }
        // 如果请求头中有token，则进行解析，并且设置认证信息  ???
        SecurityContextHolder.getContext().setAuthentication(getAuthentication(tokenHeader));
        super.doFilterInternal(request, response, chain);
    }

    // 从token中获取用户信息
    private UsernamePasswordAuthenticationToken getAuthentication(String tokenHeader) {
        String token = tokenHeader.replace(JwtUtils.TOKEN_PREFIX, "");

        //去redis里面拿token 确认redis中存在和token对应的值
        Jedis jedis = new Jedis("localhost" , 6379) ;
        String redisToken = jedis.get(JwtUtils.TOKEN_HEADER) ;

        List<GrantedAuthority> grantedAuthorities = new ArrayList<>();
        GrantedAuthority grantedAuthority = new SimpleGrantedAuthority(jedis.get(JwtUtils.ROLE_HEADER)) ;
        grantedAuthorities.add(grantedAuthority) ;
        if (redisToken.equals(token)){
            return new UsernamePasswordAuthenticationToken("user", null, grantedAuthorities);
        }
        return null;
    }*/

}