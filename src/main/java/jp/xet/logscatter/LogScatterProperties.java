package jp.xet.logscatter;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.Assert;

@Slf4j
@Data
@Configuration
@ConfigurationProperties(prefix = "logscatter")
public class LogScatterProperties implements InitializingBean {

    private Long waitSleepMilliseconds;

    @Override
    public void afterPropertiesSet() throws Exception {
        log.info("{}", waitSleepMilliseconds);
        Assert.state(waitSleepMilliseconds != null, "must set waitSleepMilliseconds property");
    }
}
