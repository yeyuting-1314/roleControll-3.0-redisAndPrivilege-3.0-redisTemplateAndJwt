package select.config;

import com.auth0.jwt.interfaces.DecodedJWT;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.access.SecurityConfig;
import org.springframework.security.web.FilterInvocation;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import select.constants.JwtUtils;
import select.system.dao.PrivilegeMapper;
import select.system.dao.RoleMapper;
import select.system.dto.Privilege;
import select.system.dto.Role;
import select.system.dto.User;
import select.util.JedisUtil;
import select.util.TokenUtil;

import java.util.*;

/**
 * @author yeyuting
 * @create 2021/1/28
 */
//用来匹配当前用户访问资源的URL和数据库中该资源对应的URL，
//和获取数据库中该资源对应的角色
    @Component
public class FilterInvocationSecurityMetadataSource implements org.springframework.security.web.access.intercept.FilterInvocationSecurityMetadataSource {
    /**
     * 用于实现ant风格的Url匹配
     */
    AntPathMatcher antPathMatcher = new AntPathMatcher() ;

    @Autowired
    PrivilegeMapper privilegeMapper ;

    @Autowired
    RedisTemplate redisTemplate ;

    @Autowired
    RoleMapper roleMapper ;

    @Autowired
    TokenUtil tokenUtil ;

    @Override
    public Collection<ConfigAttribute> getAttributes(Object o) throws IllegalArgumentException {
        /**
         * 从参数中获取当前请求的URL
         */
        String requestUrl = ((FilterInvocation) o).getRequestUrl() ;
        int len = requestUrl.indexOf("?");
        if(len != -1){
            requestUrl = requestUrl.substring(0,len);
        }
        String token = ((FilterInvocation) o).getRequest().getHeader("token") ;
        DecodedJWT jwt = tokenUtil.deToken(token) ;
        String userName = jwt.getClaim("username").asString() ;

        //从redis缓存中拿到当前对象拥有的资源访问权限，并和当前请求接口匹配
        User newUser = (User) redisTemplate.opsForValue().get(userName);
        List<Privilege> privilegeList = newUser.getPrivilegeList() ;

        for(Privilege privilegeUrl : privilegeList) {
            //将数据库中访问资源的URL与当前访问的URL进行ant风格匹配
            if (antPathMatcher.match(privilegeUrl.getPrivilegeUrl() , requestUrl)) {
                //获取访问此资源需要的角色
                Role role = roleMapper.selectByPrivilegeUrl(requestUrl) ;
                return SecurityConfig.createList(role.getRole()) ;
            }
        }
        throw new AccessDeniedException("权限不足");

    }

    @Override
    public Collection<ConfigAttribute> getAllConfigAttributes() {

        return null;
    }

    @Override
    public boolean supports(Class<?> clazz) {

        return FilterInvocation.class.isAssignableFrom(clazz);
    }
}
