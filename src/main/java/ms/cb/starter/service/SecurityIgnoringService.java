package ms.cb.starter.service;

import com.alibaba.fastjson.JSONObject;

import java.util.List;

public class SecurityIgnoringService {

    private List<String> ignoring;

    public String callIgnoring() {
        return JSONObject.toJSONString(ignoring);
    }

    public SecurityIgnoringService(List<String> ignoring) {
        this.ignoring = ignoring;
    }

    public List<String> getIgnoring() {
        return ignoring;
    }

    public void setIgnoring(List<String> ignoring) {
        this.ignoring = ignoring;
    }

}
