package demo.mapper;

import demo.model.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface UserMapper {
    @Select( "select id , username , password from user where username = #{username}" )
    User loadUserByUsername(@Param("username") String username);

    @Select( "select id , username , password from user where id = #{id}" )
    User loadUserByUserId(@Param("id") Long id);
}
