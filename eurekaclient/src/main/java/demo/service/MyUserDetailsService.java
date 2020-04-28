package demo.service;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import demo.mapper.RoleMapper;
import demo.mapper.UserMapper;

@Service 
public class MyUserDetailsService  {
    @Resource
    private UserMapper userMapper;
    @Resource
    private RoleMapper roleMapper;
    

}
