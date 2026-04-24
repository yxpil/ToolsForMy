import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.regex.Pattern;

public class TimeUtil {
    // 固定统一格式
    private static final String PATTERN = "yyyy-MM-dd-HH-mm-ss";
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern(PATTERN);

    /**
     * 获取当前标准时间
     */
    public static String now() {
        return LocalDateTime.now(ZoneId.systemDefault()).format(FORMATTER);
    }

    /**
     * 万能格式化，兼容各种奇葩时间、时间戳
     */
    public static String format(Object timeObj) {
        if (timeObj == null) {
            return now();
        }
        String time = timeObj.toString();

        // 处理时间戳
        if (Pattern.matches("\\d+", time)) {
            long ts = Long.parseLong(time);
            LocalDateTime dt;
            if (time.length() == 10) {
                dt = LocalDateTime.ofEpochSecond(ts, 0, ZoneId.systemDefault().getRules().getOffset(LocalDateTime.now()));
            } else {
                dt = LocalDateTime.ofEpochSecond(ts / 1000, 0, ZoneId.systemDefault().getRules().getOffset(LocalDateTime.now()));
            }
            return dt.format(FORMATTER);
        }

        // 统一清洗分隔符
        time = time.replace("/", "-")
                   .replace(".", "-")
                   .replace("T", "-")
                   .replace(":", "-");

        try {
            LocalDateTime dateTime = LocalDateTime.parse(time, DateTimeFormatter.ofPattern("yyyy-MM-dd-HH-mm-ss"));
            return dateTime.format(FORMATTER);
        } catch (DateTimeParseException e) {
            return now();
        }
    }
}
