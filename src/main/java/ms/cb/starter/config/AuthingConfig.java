package ms.cb.starter.config;

import ms.cb.starter.properties.AuthingClientProperties;
import ms.cb.starter.properties.SecurityIgnoringProperties;
import ms.cb.starter.service.AuthingService;
import ms.cb.starter.service.DemoService;
import ms.cb.starter.service.SecurityIgnoringService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 描述：
 *
 * @Author Declan
 * @Date 2024/10/8 13:47
 * @Version V1.0
 **/
@Configuration
@EnableConfigurationProperties({AuthingClientProperties.class, SecurityIgnoringProperties.class})
public class AuthingConfig {

    @Autowired
    private AuthingClientProperties authingClientProperties;

    @Autowired
    private SecurityIgnoringProperties securityIgnoringProperties;

    @Bean(name = "authingService")
    public AuthingService authingService(){
        return new AuthingService(authingClientProperties.getClientId(), authingClientProperties.getClientSecret());
    }

    @Bean(name = "securityIgnoringService")
    public SecurityIgnoringService securityIgnoringService(){
        return new SecurityIgnoringService(securityIgnoringProperties.getIgnoring());
    }
}
