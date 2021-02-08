package select.system.dao;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;
import select.system.dto.Privilege;
import select.system.dto.Role;

import java.util.List;

/**
 * @author yeyuting
 * @create 2021/1/28
 */
@Repository
@Mapper
public interface PrivilegeMapper {
    List<Privilege>  getAllPrivileges()  ;

    List<Privilege> selectPrivilegesByUrl(String privilegeUrl) ;


}
