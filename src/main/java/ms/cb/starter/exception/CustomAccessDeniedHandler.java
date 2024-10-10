package ms.cb.starter.exception;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Declan 权限访问受限Handler
 */
public class CustomAccessDeniedHandler implements AccessDeniedHandler {

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException, ServletException {
        // 自定义输出的信息
        String customErrorMessage = "您无权限访问该系统!";
        response.setStatus(HttpServletResponse.SC_FORBIDDEN);
        response.getWriter().write(customErrorMessage);
    }
}
