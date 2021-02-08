package select.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.ScanParams;
import redis.clients.jedis.ScanResult;
import select.base.Constants;
import select.constants.JwtUtils;
import select.system.dto.Privilege;
import select.system.dto.User;

import java.util.List;

/**
 * @author yeyuting
 * @create 2021/1/26
 */
@Component
public class JedisUtil {
    @Autowired
    RedisTemplate redisTemplate ;

    public void tokenToJedis(User user){
        Jedis jedis = getSource() ;
        jedis.set(user.getUserName() , user.getToken()) ;
        jedis.expire(user.getUserName() , Constants.TOKEN_EXPIRED_TIME) ;
        jedis.set(user.getToken() , user.getUserName()) ;
        jedis.expire(user.getToken() , Constants.TOKEN_EXPIRED_TIME) ;
        Long currentTime = System.currentTimeMillis() ;
        jedis.set(user.getUserName()+user.getToken() ,currentTime.toString()) ;
        jedis.close();

    }

    public void tokenToJedis1(User user){

        redisTemplate.opsForValue().set(user.getUserName() , user);
        User newUser = (User) redisTemplate.opsForValue().get(user.getUserName());
        /*jedis.set(user.getUserName() , user.getToken()) ;
        jedis.expire(user.getUserName() , Constants.TOKEN_EXPIRED_TIME) ;

        jedis.set(user.getToken() , user.getUserName()) ;
        jedis.expire(user.getToken() , Constants.TOKEN_EXPIRED_TIME) ;
*/
        // jedis.set(user.getToken() , user) ;
        // jedis.expire(user.getToken() , Constants.TOKEN_EXPIRED_TIME) ;

        //存储对象
        /*jedis.set(user.getGrantedAuthority(), user.getGrantedAuthorities().toString()) ;
        // jedis.set(user.getGrantedAuthorities().toString() , user.getGrantedAuthority()) ;

        for(GrantedAuthority grantedAuthority : user.getGrantedAuthorities()){
            jedis.set(user.getGrantedAuthority() , grantedAuthority.toString()) ;
            System.out.println("redis中getGrantedAuthorities值为：" + jedis.get(user.getGrantedAuthority()));

        }
        for(Privilege privilege : user.getPrivilegeList()){
            jedis.set(String.valueOf(privilege.getId()) , privilege.getPrivilegeUrl()) ;
            jedis.expire(String.valueOf(privilege.getId()) , 3) ;
            System.out.println("redis中privilege值为："+ jedis.get(String.valueOf(privilege.getId())));
        }
        for(Privilege privilege : user.getPrivilegeList()){
            jedis.set(String.valueOf(privilege.getId()), privilege.getPrivilegeUrl()) ;
            jedis.expire(String.valueOf(privilege.getId()) ,Constants.TOKEN_EXPIRED_TIME) ;
            jedis.set(privilege.getPrivilegeUrl() , String.valueOf(privilege.getId())) ;
            jedis.expire(privilege.getPrivilegeUrl() , Constants.TOKEN_EXPIRED_TIME) ;

        }*/

        /*jedis.set(user.getToken() , user.getUserName()) ;
        jedis.expire(user.getToken() , Constants.TOKEN_EXPIRED_TIME) ;
        Long currentTime = System.currentTimeMillis() ;
        jedis.set(user.getUserName()+user.getToken() ,currentTime.toString()) ;*/


    }

    public void delString(String key){
        try {
            Jedis jedis = getSource() ;
            ScanParams scanParams = new ScanParams() ;
            StringBuilder paramKey = new StringBuilder("*").append(key).append("*") ;
            scanParams.match(paramKey.toString()) ;
            scanParams.count(1000) ;
            ScanResult<String> sr = jedis.scan("0" , scanParams) ;
            List<String> list = sr.getResult() ;
            for(String delKey : list){
                jedis.del(delKey) ;
            }
        }catch (Exception e){
            e.printStackTrace();
        }


    }

    public Jedis getSource(){
        return new Jedis("localhost" , 6379) ;
    }

}
