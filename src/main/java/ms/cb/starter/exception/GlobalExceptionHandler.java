package ms.cb.starter.exception;

import lombok.extern.slf4j.Slf4j;
import ms.cb.starter.entity.APIResponseCode;
import ms.cb.starter.entity.ApiResponse;
import org.springframework.core.annotation.Order;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;


/**
 * @description: wx-mini 全局异常捕获ControllerAdvice
 * @author: Declan
 * @create: 2024-07-16 12:05
 **/
@Slf4j
@Order(999)
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(value = TokenException.class)
    public ResponseEntity handler(TokenException e){
        String errorMessage = e.getMessage(); // 获取TokenException中的具体异常信息
        log.info("token被全局异常捕获: {}", errorMessage);
        return ResponseEntity.ok(ApiResponse.fail(APIResponseCode.TOKEN_ERROR,errorMessage));
    }

    @ExceptionHandler(value = org.springframework.security.access.AccessDeniedException.class)
    public ResponseEntity handlerAccess(org.springframework.security.access.AccessDeniedException e){
        String errorMessage = e.getMessage();
        log.info("权限被全局异常捕获: {}", errorMessage);
        return ResponseEntity.ok(ApiResponse.fail(APIResponseCode.ACCESS_DENIED_ERROR,errorMessage));
    }

}
