package jp.xet.logscatter;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import lombok.experimental.UtilityClass;

@UtilityClass
public class ObjectMapperFactory {

    /**
     * Mantaで使用するObjectMapperを生成する。<br>
     * <p>実装はBeanとして利用し、主にTest側で共通設定で利用するため</p>
     *
     * @return {@link ObjectMapper}
     */
    public static ObjectMapper createObjectMapper() {
        return new ObjectMapper()
                .setPropertyNamingStrategy(PropertyNamingStrategy.SNAKE_CASE)
                .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }
}