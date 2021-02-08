package select.system.dto;

import org.springframework.security.core.GrantedAuthority;

import java.io.Serializable;
import java.util.List;

/**
 * @author yeyuting
 * @create 2021/1/25
 */

public class User implements Serializable {

    private static final long serialVersionUID = 3529219554011221820L;

    int id ;
    String userName ;
    String password ;

    String token ;

    String grantedAuthority ;

    //拿到对应的角色，并放到redis中
    List<GrantedAuthority> grantedAuthorities  ;


    //用户登陆的时候就获取到此用户对应的所有权限
    List<Privilege> privilegeList ;

    public User(){

    }

   /* public User(int id, String name) {
        this.id = id;
        this.userName = name;
    }

*/
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public List<GrantedAuthority> getGrantedAuthorities() {
        return grantedAuthorities;
    }

    public void setGrantedAuthorities(List<GrantedAuthority> grantedAuthorities) {
        this.grantedAuthorities = grantedAuthorities;
    }

    public String getGrantedAuthority() {
        return grantedAuthority;
    }

    public void setGrantedAuthority(String grantedAuthority) {
        this.grantedAuthority = grantedAuthority;
    }


    public List<Privilege> getPrivilegeList() {
        return privilegeList;
    }

    public void setPrivilegeList(List<Privilege> privilegeList) {
        this.privilegeList = privilegeList;
    }
}
