package select.config;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import java.util.Collection;

/**
 * @author yeyuting
 * @create 2021/1/28
 */
@Component
//获取当前登录用户的角色信息和FilterInvocationSecurityMetadataSource类返回的资源访问的相关信息进行比对，判断该用户是否具有访问当前URL的角色
public class AccessDecisionManager implements org.springframework.security.access.AccessDecisionManager {

    /**
    *2021/1/28 10:51
    *该方法用于判断当前登录的用户是否具备当前请求的URL所需要的角色
    * * @param auth 包含用户登陆的信息
     * @param o FilterInvocation 对象
     * @param ca CustomFilterInvocationSecurityMetadataSource 中getAttributes（）方法的返回值
    * * @return : void
    */
    @Override
    public void decide(Authentication auth, Object o, Collection<ConfigAttribute> ca) throws AccessDeniedException, InsufficientAuthenticationException {
        Collection<? extends GrantedAuthority> auths = auth.getAuthorities() ;
        for(ConfigAttribute configAttribute : ca) {
            if("ROLE_SUPERADMIN".equals(configAttribute.getAttribute()) && auth instanceof UsernamePasswordAuthenticationToken){
                return;
            }
            for(GrantedAuthority authority : auths){
                if(configAttribute.getAttribute().equals(authority.getAuthority())){
                    return;
                }
            }
            throw new AccessDeniedException("权限不足") ;
        }
    }

    @Override
    public boolean supports(ConfigAttribute configAttribute) {
        return true;
    }

    @Override
    public boolean supports(Class<?> aClass) {
        return true;
    }
}
