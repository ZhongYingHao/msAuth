package ms.cb.starter.config;

import ms.cb.starter.properties.Demo1Properties;
import ms.cb.starter.properties.DemoProperties;
import ms.cb.starter.service.DemoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 描述：
 *
 * @Author Declan
 * @Date 2024/10/8 11:25
 * @Version V1.0
 **/
@Configuration
@EnableConfigurationProperties({Demo1Properties.class, DemoProperties.class})
@ConditionalOnProperty(
        prefix = "demo",
        name = "isopen",
        havingValue = "true"
)
public class DemoConfig {
    @Autowired
    private Demo1Properties demo1Properties;
    @Autowired
    private DemoProperties demoProperties;

    @Bean(name = "demo")
    public DemoService demoService(){
        return new DemoService(demoProperties.getSayWhat(), demoProperties.getToWho());
    }
    @Bean(name = "demo1")
    public DemoService  demo1Service(){
        return new DemoService(demo1Properties.getSayWhat(), demo1Properties.getToWho());
    }
}
