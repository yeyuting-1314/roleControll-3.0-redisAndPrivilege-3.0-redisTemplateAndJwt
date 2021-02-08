package select.system.dao;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;
import select.system.dto.Role;

import java.util.List;

/**
 * @author yeyuting
 * @create 2021/1/21
 */
@Repository
@Mapper
public interface RoleMapper{

    public Role selectOne(int id);

    public Role selectByPrivilegeUrl(String privilegeUrl) ;

}

