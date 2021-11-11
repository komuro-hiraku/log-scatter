package jp.xet.logscatter;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.stream.IntStream;

public class GarbageLogUtil {

    private static Map<String, String> body;

    public static final String ABYSS_SENTENSE = "Beware that, when fighting monsters, you yourself do not become a monster… for when you gaze long into the abyss. The abyss gazes also into you.";

    public static Map<String, String> getDummyBody(int itemCount) {
        if (body != null && body.size() == itemCount) {
            return body;
        } else {
            body = new HashMap<>();
            IntStream.rangeClosed(0, itemCount).forEach(v -> body.put(UUID.randomUUID().toString(), String.format("%s", ABYSS_SENTENSE)));
            return body;
        }
    }
}
