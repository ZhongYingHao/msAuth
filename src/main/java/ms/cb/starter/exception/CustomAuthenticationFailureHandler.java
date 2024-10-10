package ms.cb.starter.exception;

import com.alibaba.fastjson.JSON;
import ms.cb.starter.utils.WebUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * @author: Declan
 **/
@Component
public class CustomAuthenticationFailureHandler implements AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        Map<String,Object> map = new HashMap<>();
        map.put("msg","用户认证失败,请重新登录!");
        map.put("code",HttpStatus.UNAUTHORIZED.value());
        String json = JSON.toJSONString(map);
        WebUtils.renderString(response,ResponseEntity.ok(json));
    }
}

//
//public class CustomAuthenticationFailureHandler implements AuthenticationFailureHandler {
//
//    @Override
//    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
//                                        AuthenticationException exception) throws IOException, ServletException {
//        // 检查异常类型
//        if (exception instanceof BadCredentialsException) {
//            // 设置自定义的错误信息
//            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "用户名或密码错误，请重新输入！");
//        } else {
//            // 处理其他类型的认证异常
//            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "认证失败！");
//        }
//    }
//}

