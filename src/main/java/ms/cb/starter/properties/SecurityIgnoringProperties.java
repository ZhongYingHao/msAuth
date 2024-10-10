package ms.cb.starter.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 描述：security filter 配置
 *
 * @Author Declan
 * @Date 2024/10/8 14:10
 * @Version V1.0
 **/
@Component
@ConfigurationProperties(prefix = "security")
public class SecurityIgnoringProperties {

    private List<String> ignoring;

    public List<String> getIgnoring() {
        return ignoring;
    }

    public void setIgnoring(List<String> ignoring) {
        this.ignoring = ignoring;
    }

}
