package ms.cb.starter.filter;

import cn.authing.sdk.java.client.AuthenticationClient;
import cn.authing.sdk.java.dto.IntrospectTokenRespDto;
import cn.authing.sdk.java.model.AuthenticationClientOptions;
import lombok.extern.slf4j.Slf4j;
import ms.cb.starter.utils.AuthingUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.ParseException;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @author: Declan
 * 超级过滤器Authorization order:98
 **/
@Slf4j
@Component
public class JwtAuthenticationAuthFilter extends OncePerRequestFilter {

    @Autowired
    AuthingUtils authingUtils;
//    @Bean
//    @ConditionalOnMissingBean
//    public AuthingUtils authingUtils() {
//        return new AuthingUtils();
//    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        //编程账号访问
        log.info("Authorization filter...");
        String token = request.getHeader("Authorization");
        if (!StringUtils.hasText(token)) {
            filterChain.doFilter(request, response);
            return;
        }
        AuthenticationClientOptions clientOptions = authingUtils.getAuthenticationClientOptionsByAccessToken(token);
        AuthenticationClient client = null;
        try {
            client = new AuthenticationClient(clientOptions);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        //客户权限资源列表
        IntrospectTokenRespDto tokenRespDto = client.introspectToken(token);
        List<SimpleGrantedAuthority> scopeList = null;
        if (Objects.nonNull(tokenRespDto)) {
            scopeList = Arrays.stream(tokenRespDto.getScope().split(" "))
                    .map(authority -> new SimpleGrantedAuthority(authority))
                    .collect(Collectors.toList());
        }

        //封装Authentication对象存入SecurityContextHolder
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(null,token,scopeList);

        SecurityContextHolder.getContext().setAuthentication(authenticationToken);
        //放行
        filterChain.doFilter(request, response);
    }

}
