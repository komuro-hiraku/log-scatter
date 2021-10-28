package jp.xet.logscatter;

import ch.qos.logback.classic.spi.ILoggingEvent;

import java.util.LinkedHashMap;
import java.util.Map;

public class GeneralLoggingLayout extends AbstractLoggingLayout {

    @Override
    Map<String, Object> fillLogContent(ILoggingEvent event) {
        Map<String, Object> map = new LinkedHashMap<>();

        map.putAll(getTimestampMap(event));
        map.putAll(getLogLevelMap(event));
        map.putAll(getThreadMap(event));
        map.putAll(getLoggerMap(event));
        map.putAll(getMDCMap(event));
        map.putAll(getFormattedMessageMap(event));
        map.putAll(getExceptionMessageMap(event));

        return map;
    }
}
