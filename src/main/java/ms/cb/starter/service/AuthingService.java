package ms.cb.starter.service;

/**
 * 描述：
 *
 * @Author Declan
 * @Date 2024/10/8 13:49
 * @Version V1.0
 **/
public class AuthingService {

    public String clientId;
    public String clientSecret;

    public AuthingService(String clientId, String clientSecret) {
        this.clientId = clientId;
        this.clientSecret = clientSecret;
    }

    public String call() {
        return this.clientId + "::"+ this.clientSecret;
    }

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
}
