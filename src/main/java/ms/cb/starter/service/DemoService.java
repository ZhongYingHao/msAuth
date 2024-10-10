package ms.cb.starter.service;

/**
 * 描述：
 *
 * @Author Declan
 * @Date 2024/10/8 11:19
 * @Version V1.0
 **/
public class DemoService {
    public String sayWhat;
    public String toWho;
    public DemoService(String sayWhat, String toWho){
        this.sayWhat = sayWhat;
        this.toWho = toWho;
    }
    public String say(){
        return this.sayWhat + "!!!  " + toWho;
    }
}
