package ms.cb.starter.config.security;

import cn.authing.sdk.java.util.JsonUtils;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import ms.cb.starter.exception.CustomAccessDeniedHandler;
import ms.cb.starter.exception.CustomAuthenticationFailureHandler;
import ms.cb.starter.exception.GlobalExceptionHandler;
import ms.cb.starter.exception.TokenException;
import ms.cb.starter.filter.*;
import ms.cb.starter.manager.DynamicAccessDecisionManager;
import ms.cb.starter.properties.SecurityIgnoringProperties;
import ms.cb.starter.service.SecurityIgnoringService;
import ms.cb.starter.utils.AuthingUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.mail.MailProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.access.intercept.FilterSecurityInterceptor;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.util.List;

import static org.springframework.security.config.Customizer.withDefaults;

/**
 * @desc 安全配置类
 * @author: Declan
 **/
@Slf4j
@Configuration
@EnableWebSecurity(debug = true)
@EnableGlobalMethodSecurity(prePostEnabled = true)
//@ConditionalOnBean({ DynamicSecurityService.class,DynamicSecurityFilter.class })
@EnableConfigurationProperties(SecurityIgnoringProperties.class)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private SecurityIgnoringProperties securityIgnoringProperties;

    @Bean
    @ConditionalOnMissingBean
    public JwtAuthenticationTokenFilter jwtAuthenticationTokenFilter() {
        return new JwtAuthenticationTokenFilter();
    }

    @Bean
    @ConditionalOnMissingBean
    public JwtAuthenticationAuthFilter jwtAuthenticationAuthFilter() {
        return new JwtAuthenticationAuthFilter();
    }


    @Bean
    @ConditionalOnMissingBean
    public DynamicSecurityService dynamicSecurityService(){
        return new DynamicSecurityServiceImpl();
    }

    @Bean
    @ConditionalOnMissingBean
    public AuthingUtils authingUtils(){
        return new AuthingUtils();
    }

    @Bean
    @ConditionalOnMissingBean
    public DynamicSecurityMetadataSource dynamicSecurityMetadataSource() {
        return new DynamicSecurityMetadataSource();
    }

    @Bean
    @ConditionalOnMissingBean
    public DynamicAccessDecisionManager dynamicAccessDecisionManager() {
        return new DynamicAccessDecisionManager();
    }

    @Bean
    @ConditionalOnMissingBean
    public DynamicSecurityFilter dynamicSecurityFilter(){
        return new DynamicSecurityFilter();
    }

    @Bean
    @ConditionalOnMissingBean
    public CustomAccessDeniedHandler customAccessDeniedHandler(){
        return new CustomAccessDeniedHandler();
    }

    @Bean
    @ConditionalOnMissingBean
    public CustomAuthenticationFailureHandler customAuthenticationFailureHandler(){
        return new CustomAuthenticationFailureHandler();
    }

    @Bean
    @ConditionalOnMissingBean
    public TokenException tokenException(){
        return new TokenException();
    }

    @Bean
    @ConditionalOnMissingBean
    public GlobalExceptionHandler globalExceptionHandler(){
        return new GlobalExceptionHandler();
    }

    //BCryptPasswordEncoder注入
//    @Bean
//    @ConditionalOnMissingBean
//    public PasswordEncoder passwordEncoder(){
//        return new BCryptPasswordEncoder();
//    }

    //把AuthenticationManager注入
//    @Bean
//    @Override
//    public AuthenticationManager authenticationManagerBean() throws Exception {
//        return super.authenticationManagerBean();
//    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        List<String> ignoringPaths = securityIgnoringProperties.getIgnoring();
        log.info("拦截url...");
        http
            .formLogin().disable()
            .csrf().disable()
            // 不通过Session获取SecurityContext
            .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            .and()
            .authorizeRequests()
            .antMatchers(ignoringPaths.toArray(new String[0])).anonymous()
            .anyRequest().authenticated()
                .and()
                .oauth2Login(withDefaults())
//                .oauth2ResourceServer().jwt()
                // 设置自定义的访问决策管理器
                .addFilterBefore(jwtAuthenticationTokenFilter(), UsernamePasswordAuthenticationFilter.class)
                .addFilterAfter(jwtAuthenticationAuthFilter(),UsernamePasswordAuthenticationFilter.class);
        // 有动态权限配置时添加动态权限校验过滤器
        if (dynamicSecurityService() != null) {
            http.addFilterBefore(dynamicSecurityFilter(), FilterSecurityInterceptor.class);
//            http.exceptionHandling().accessDeniedHandler(new CustomAccessDeniedHandler());
        }
    }
}
