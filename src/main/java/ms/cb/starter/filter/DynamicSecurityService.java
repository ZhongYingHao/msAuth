package ms.cb.starter.filter;

import org.springframework.security.access.ConfigAttribute;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * 动态定义权限数据源接口
 * Created by Declan.
 */
@Component
public interface DynamicSecurityService {
    /**
     * 加载资源ANT通配符和资源对应MAP
     */
    Map<String, ConfigAttribute> loadDataSource();
}
