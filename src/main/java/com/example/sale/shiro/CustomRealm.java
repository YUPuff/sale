package com.example.sale.shiro;

import com.example.sale.common.BusinessException;
import com.example.sale.entity.UserEntity;
import com.example.sale.enums.Role;
import com.example.sale.service.UserService;
import com.example.sale.utils.JWTUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;

/**
 * 从数据库或其他数据源中获取用户信息
 */
@Component
public class CustomRealm extends AuthorizingRealm {

    @Autowired
    private UserService userService;

    /**
     * 必须重写此方法，不然会报错
     */
    @Override
    public boolean supports(AuthenticationToken token) {
//        System.out.println("进入了CustomRealm 中的supports，判断当前拿到的token是不是自定义的token类型");
        return token instanceof JWTToken;
    }

    /**
     * 用于进行身份认证，即验证用户名和密码是否正确。
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
//        System.out.println("CustomRealm中的————身份认证方法————");
//        System.out.println("从authenticationToken中拿到Token并解密");
        String token = (String) authenticationToken.getCredentials();
        if (!JWTUtils.verify(token)) {
            throw new AuthenticationException("token认证失败！");
        }
        Long id = JWTUtils.getId(token);
        UserEntity user = userService.getById(id);
        if (user == null)
            throw new BusinessException("用户不存在");
        return new SimpleAuthenticationInfo(token, token, "MyRealm");
    }

    /**
     * 获取用户的角色和权限信息
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
//        System.out.println("CustomRealm中的————权限认证————");
        Long id = JWTUtils.getId(principals.toString());
        SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
        //获得该用户角色
        UserEntity user = userService.getById(id);
        String role = Role.format(user.getRole());
//        System.out.println("角色："+role);
        Set<String> roleSet = new HashSet<>();
        //需要将 role, permission 封装到 Set 作为 info.setRoles()的参数
        roleSet.add(role);
        //设置该用户拥有的角色和权限
        info.setRoles(roleSet);
        return info;
    }
}
