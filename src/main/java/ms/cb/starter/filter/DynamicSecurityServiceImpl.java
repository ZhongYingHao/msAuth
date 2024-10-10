package ms.cb.starter.filter;

import org.springframework.security.access.ConfigAttribute;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class DynamicSecurityServiceImpl implements DynamicSecurityService {

    // 实现 loadDataSource 方法，加载资源ANT通配符和资源对应MAP
    @Override
    public Map<String, ConfigAttribute> loadDataSource() {
        // 实现加载资源的逻辑，返回资源ANT通配符和资源对应MAP
        // 这里可以是从数据库或其他地方加载资源信息的逻辑
        return new HashMap<>();
    }
}