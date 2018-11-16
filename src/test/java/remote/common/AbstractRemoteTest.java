package remote.common;

import com.zuche.framework.common.GlobalMessage;
import com.zuche.framework.common.SpringApplicationContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.annotations.BeforeClass;

/**
 * Created with IntelliJ IDEA.
 * <p>
 * 远程服务测试工具抽象类，需要启动服务端项目（一般为当前项目），通过远程调用的方式，测试客户端的可用性。
 * <p>
 *
 * @author zhangjun (zhangjun01@zuche.com)
 * @since 2017/12/24
 */
@ActiveProfiles("test")
@ContextConfiguration(locations={"classpath:frameworkContext.xml"})
public class AbstractRemoteTest extends AbstractTestNGSpringContextTests {

    @BeforeClass
    public final void beforeClass(){
        SpringApplicationContext.initApplicationContext(applicationContext);
        GlobalMessage.setProjectPath("/ycctemplate/");
    }
}