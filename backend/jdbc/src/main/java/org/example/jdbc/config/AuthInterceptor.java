package org.example.jdbc.config;

import org.example.jdbc.exception.BusinessException;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;

@Component
public class AuthInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // 检查是否是HandlerMethod（即Controller中的方法）
        if (handler instanceof HandlerMethod) {
            HandlerMethod handlerMethod = (HandlerMethod) handler;
            Method method = handlerMethod.getMethod();
            
            // 检查方法是否带有@AdminRequired注解
            if (method.isAnnotationPresent(AdminRequired.class)) {
                // 获取请求头中的用户角色
                String userRole = request.getHeader("X-User-Role");
                
                // 如果角色不是admin，返回403错误
                if (userRole == null || !userRole.equals("admin")) {
                    throw new BusinessException(403, "没有权限访问此资源");
                }
            }
        }
        
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        // 不需要实现
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        // 不需要实现
    }
}