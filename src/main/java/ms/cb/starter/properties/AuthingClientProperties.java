package ms.cb.starter.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * 描述：authing客户端连接配置
 *
 * @Author Declan
 * @Date 2024/10/8 13:41
 * @Version V1.0
 **/
@Component
@ConfigurationProperties(prefix = "spring.security.oauth2.client.registration.authing")
public class AuthingClientProperties {

    private String clientId;
    private String clientSecret;
    private String appHost;
    private String userPoolId;
    private String userPoolSecret;

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public String getClientSecret() {
        return clientSecret;
    }

    public void setClientSecret(String clientSecret) {
        this.clientSecret = clientSecret;
    }

    public String getAppHost() {
        return appHost;
    }

    public void setAppHost(String appHost) {
        this.appHost = appHost;
    }

    public String getUserPoolId() {
        return userPoolId;
    }

    public void setUserPoolId(String userPoolId) {
        this.userPoolId = userPoolId;
    }

    public String getUserPoolSecret() {
        return userPoolSecret;
    }

    public void setUserPoolSecret(String userPoolSecret) {
        this.userPoolSecret = userPoolSecret;
    }
}
