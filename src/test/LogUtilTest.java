import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

/**
 * LogUtil 单元测试：捕获 stdout/stderr 验证日志前缀、null 处理与 Map 美化输出。
 */
public class LogUtilTest {

    private PrintStream origOut;
    private ByteArrayOutputStream outBuf;

    @BeforeEach
    void setUp() {
        origOut = System.out;
        outBuf = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outBuf, true));
    }

    @AfterEach
    void tearDown() {
        System.setOut(origOut);
    }

    @Test
    void info_printsInfoPrefixAndMessage() {
        LogUtil.info("hello-world");
        String s = outBuf.toString();
        assertTrue(s.contains("[INFO]"), s);
        assertTrue(s.contains("hello-world"), s);
    }

    @Test
    void info_nullMessage_printsNull() {
        LogUtil.info(null);
        String s = outBuf.toString();
        assertTrue(s.contains("null"), s);
    }

    @Test
    void info_map_isPrettyPrinted() {
        LogUtil.info(Map.of("user", "yxpil"));
        String s = outBuf.toString();
        assertTrue(s.contains("[INFO]"), s);
        assertTrue(s.contains("user"), s);
    }

    @Test
    void warn_and_debug_doNotThrow() {
        assertDoesNotThrow(() -> { LogUtil.warn("w"); LogUtil.debug("d"); });
    }
}
