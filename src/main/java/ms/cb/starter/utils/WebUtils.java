package ms.cb.starter.utils;

import org.springframework.http.ResponseEntity;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @desc: 它将给定的字符串写入 HttpServletResponse 的输出流
 * @author: Declan
 **/
public class WebUtils {

    public static String renderString(HttpServletResponse response, ResponseEntity string) {
        try {
            response.setStatus(200);
            response.setContentType("application/json");
            response.setCharacterEncoding("utf-8");
            response.getWriter().print(string);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;

    }
}
