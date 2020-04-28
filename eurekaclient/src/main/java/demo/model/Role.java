package demo.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.io.Serializable;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
/**
 * Role 类实现了 GrantedAuthority 接口，并重写 getAuthority() 方法。
 * 权限点可以为任何字符串，不一定非要用角色名。
 * 所有的Authentication实现类都保存了一个GrantedAuthority列表，
 * 其表示用户所具有的权限。GrantedAuthority是通过AuthenticationManager设置到Authentication对象中的，
 * 然后AccessDecisionManager将从Authentication中获取用户所具有的GrantedAuthority
 * 来鉴定用户是否具有访问对应资源的权限。
 */
public class Role implements Serializable {
    private long id;
    private String name;
}
