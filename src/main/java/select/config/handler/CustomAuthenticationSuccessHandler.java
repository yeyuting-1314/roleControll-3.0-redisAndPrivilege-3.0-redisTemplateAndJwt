package select.config.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.aop.target.LazyInitTargetSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import redis.clients.jedis.Jedis;
import select.system.dto.Privilege;
import select.system.dto.User;
import select.util.JedisUtil;
import select.util.TokenUtil;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 登陆成功
 * @author yeyuting
 * @create 2021/1/29
 */
@Component("customAuthenticationSuccessHandler")
public class CustomAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    @Autowired
    TokenUtil tokenUtil ;

    @Autowired
    JedisUtil jedisUtil ;

    @Autowired
    RedisTemplate redisTemplate ;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication auth) throws IOException, ServletException {
        Object principal = auth.getPrincipal() ;
        String userName =  auth.getName() ;
        User newUser = (User) redisTemplate.opsForValue().get(userName);
        String token = newUser.getToken() ;

        List<Privilege> privilegeList = newUser.getPrivilegeList() ;

        response.setContentType("application/json;charset=utf-8");
        PrintWriter out = response.getWriter();
        response.setStatus(200);
        Map<String , Object > map = new HashMap<>(16) ;
        map.put("status", 200);
        map.put("msg", principal);
        map.put("token", token);
        map.put("privilege" , privilegeList) ;

        ObjectMapper om = new ObjectMapper();
        out.write(om.writeValueAsString(map));
        out.flush();
        out.close();
    }
}
