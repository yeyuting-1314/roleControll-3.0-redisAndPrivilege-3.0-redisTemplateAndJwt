package select.system.dao;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;
import select.system.dto.UserRole;

import java.util.List;

/**
 * @author yeyuting
 * @create 2021/1/27
 */
@Repository
@Mapper
public interface UserRoleMapper {
    public List<UserRole> selectList(int id);

}
