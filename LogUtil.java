import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class LogUtil {

    private static final String FMT = "yyyy-MM-dd-HH-mm-ss";
    private static final DateTimeFormatter DT = DateTimeFormatter.ofPattern(FMT);

    private static String now() {
        return LocalDateTime.now().format(DT);
    }

    private static String format(Object msg) {
        if (msg == null) return "null";
        if (msg instanceof java.util.Map || msg instanceof java.util.List) {
            try {
                return "\n" + new com.fasterxml.jackson.databind.ObjectMapper()
                        .writerWithDefaultPrettyPrinter()
                        .writeValueAsString(msg);
            } catch (Exception e) {
                return msg.toString();
            }
        }
        return msg.toString();
    }

    public static void info(Object msg) {
        System.out.printf("[%s] [INFO] %s%n", now(), format(msg));
    }

    public static void warn(Object msg) {
        System.out.printf("[%s] [WARN] %s%n", now(), format(msg));
    }

    public static void error(Object msg, Throwable t) {
        String error = t != null ? "\n" + t.getMessage() : "";
        System.err.printf("[%s] [ERROR] %s%s%n", now(), format(msg), error);
    }

    public static void debug(Object msg) {
        System.out.printf("[%s] [DEBUG] %s%n", now(), format(msg));
    }
}
