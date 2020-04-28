package demo.mapper;


import demo.model.Role;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface RoleMapper {

    @Select( "SELECT role_t.id,role_t.name FROM role role_t " +
            "LEFT JOIN user_role user_role_t " +
            "ON role_t.id = user_role_t.role_id WHERE user_role_t.user_id=${userId}" )
    List<Role> getRolesByUserId(@Param("userId") Long userId);

}
