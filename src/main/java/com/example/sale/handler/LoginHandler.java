package com.example.sale.handler;

import com.alibaba.fastjson.JSON;
import com.example.sale.annotation.NoAuth;
import com.example.sale.common.Result;
import com.example.sale.constant.RedisConstants;
import com.example.sale.model.UserThreadLocal;
import com.example.sale.utils.JWTUtils;
import com.example.sale.vo.UserVO;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


/**
 * 实现登录拦截，注意拦截器需要放入MVC配置
 */
@Component
public class LoginHandler implements HandlerInterceptor {

    @Autowired
    private StringRedisTemplate redisTemplate;


    // 方法执行之前进行拦截
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // 不是拦截器方法直接放行
        if(!(handler instanceof HandlerMethod)) return true;
        HandlerMethod method = (HandlerMethod) handler;
        // 有无需验证注解直接放行
        if (method.hasMethodAnnotation(NoAuth.class)) return true;
        // 从前端请求头中取出token，并验证其有效性
        String token = request.getHeader("token");
        if (JWTUtils.verify(token)){
            String userJson = redisTemplate.opsForValue().get(RedisConstants.TOKEN+token);
            if (StringUtils.isNotBlank(userJson)){
                // 将用户信息放在ThreadLocal中
                UserVO userVO = JSON.parseObject(userJson, UserVO.class);
                UserThreadLocal.put(userVO);
                return true;
            }
        }
        response.setContentType("application/json;charset=utf-8");
        response.getWriter().write(JSON.toJSONString(Result.failure("请刷新")));
        return false;
    }
}
