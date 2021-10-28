package jp.xet.logscatter;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.pattern.ThrowableProxyConverter;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.classic.spi.IThrowableProxy;
import ch.qos.logback.contrib.json.JsonLayoutBase;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;
import lombok.Setter;
import org.slf4j.Marker;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.util.Collections;
import java.util.Map;

abstract public class AbstractLoggingLayout extends JsonLayoutBase<ILoggingEvent> {

    private final ThrowableProxyConverter throwableProxyConverter;

    public AbstractLoggingLayout() {
        throwableProxyConverter = new ThrowableProxyConverter();
    }


    static final String MARKER_ATTR_NAME = "marker";

    static final String TIMESTAMP_ATTR_NAME = "timestamp";

    static final String LEVEL_ATTR_NAME = "level";

    static final String THREAD_ATTR_NAME = "thread";

    static final String LOGGER_ATTR_NAME = "logger";

    static final String MDC_ATTR_NAME = "mdc";

    static final String FORMATTED_MESSAGE_ATTR_NAME = "message";

    static final String EXCEPTION_ATTR_NAME = "exception";

    @Setter
    @Getter
    private ObjectMapper mapper = ObjectMapperFactory.createObjectMapper();

    @Override
    protected Map<String, Object> toJsonMap(ILoggingEvent iLoggingEvent) {
        Map<String, Object> map = fillLogContent(iLoggingEvent);

        map.putAll(getMarkerMap(iLoggingEvent));

        return map;
    }

    abstract Map<String, Object> fillLogContent(ILoggingEvent event);

    protected Map<String, String> getMarkerMap(ILoggingEvent event) {
        Marker marker = event.getMarker();
        if (marker != null) {
            return Collections.singletonMap(MARKER_ATTR_NAME, marker.getName());
        } else {
            return Collections.singletonMap(MARKER_ATTR_NAME, null);
        }
    }

    protected Map<String, String> getTimestampMap(ILoggingEvent event) {
        if (this.includeTimestamp) {
            String formatted = formatTimestamp(event.getTimeStamp());
            if (formatted != null) {
                return Collections.singletonMap(TIMESTAMP_ATTR_NAME, formatted);
            }
        }
        return Collections.emptyMap();
    }

    protected Map<String, String> getLogLevelMap(ILoggingEvent event) {
        Level level = event.getLevel();
        if (level != null) {
            String lvlString = String.valueOf(level);
            return Collections.singletonMap(LEVEL_ATTR_NAME, lvlString);
        }
        return Collections.emptyMap();
    }

    protected Map<String, String> getThreadMap(ILoggingEvent event) {

        String threadName = event.getThreadName();
        if (threadName != null) {
            return Collections.singletonMap(THREAD_ATTR_NAME, threadName);
        }
        return Collections.emptyMap();
    }

    protected Map<String, String> getLoggerMap(ILoggingEvent event) {
        String loggerName = event.getLoggerName();
        if (loggerName != null) {
            return Collections.singletonMap(LOGGER_ATTR_NAME, loggerName);
        }
        return Collections.emptyMap();
    }

    protected Map<String, Object> getMDCMap(ILoggingEvent event) {
        Map<String, String> mdc = event.getMDCPropertyMap();
        if (mdc != null && mdc.isEmpty() == false) {
            return Collections.singletonMap(MDC_ATTR_NAME, mdc);
        }
        return Collections.emptyMap();
    }

    protected Map<String, Object> getFormattedMessageMap(ILoggingEvent event) {
        String msg = event.getFormattedMessage();
        if (msg != null) {
            if (msg.charAt(0) == '{' && msg.endsWith("}")) {
                try {
                    JsonNode m = mapper.readTree(msg);
                    return Collections.singletonMap(FORMATTED_MESSAGE_ATTR_NAME, m);
                } catch (IOException e) {
                    return Collections.singletonMap(FORMATTED_MESSAGE_ATTR_NAME, msg);
                }
            }
            return Collections.singletonMap(FORMATTED_MESSAGE_ATTR_NAME, msg);
        }
        return Collections.emptyMap();
    }

    protected Map<String, String> getExceptionMessageMap(ILoggingEvent event) {
        IThrowableProxy throwableProxy = event.getThrowableProxy();
        if (throwableProxy != null) {
            String ex = throwableProxyConverter.convert(event);
            if (StringUtils.isEmpty(ex) == false) {
                return Collections.singletonMap(EXCEPTION_ATTR_NAME, ex);
            }
        }
        return Collections.emptyMap();
    }
}
