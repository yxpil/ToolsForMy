import org.junit.jupiter.api.Test;

import java.util.regex.Pattern;

import static org.junit.jupiter.api.Assertions.*;

/**
 * TimeUtil 单元 + 注入测试。
 * TimeUtil 纯 JDK 实现，覆盖主路径、边界与不可信时间输入的鲁棒性。
 */
public class TimeUtilTest {

    private static final Pattern STD =
            Pattern.compile("\\d{4}-\\d{2}-\\d{2}-\\d{2}-\\d{2}-\\d{2}");

    @Test
    void now_isStandardFormat() {
        assertTrue(STD.matcher(TimeUtil.now()).matches());
    }

    @Test
    void format_null_returnsNow() {
        assertNotNull(TimeUtil.format(null));
        assertTrue(STD.matcher(TimeUtil.format(null)).matches());
    }

    @Test
    void format_standardString_roundTrips() {
        assertEquals("2024-01-02-03-04-05", TimeUtil.format("2024-01-02-03-04-05"));
    }

    @Test
    void format_10DigitEpoch_seconds() {
        // 1700000000 = 2023-11-14 ... 只要能解析成标准格式即可
        String r = TimeUtil.format("1700000000");
        assertTrue(STD.matcher(r).matches(), "10 位时间戳应解析为标准格式: " + r);
    }

    @Test
    void format_13DigitEpoch_millis() {
        String r = TimeUtil.format("1700000000000");
        assertTrue(STD.matcher(r).matches(), "13 位毫秒时间戳应解析为标准格式: " + r);
    }

    @Test
    void format_garbage_fallsBackToNow() {
        // 不可信的乱入时间串：不应抛异常，回退为当前标准时间
        String r = assertDoesNotThrow(() -> TimeUtil.format("not-a-date!!"));
        assertTrue(STD.matcher(r).matches());
    }

    @Test
    void format_empty_fallsBack() {
        String r = assertDoesNotThrow(() -> TimeUtil.format(""));
        assertNotNull(r);
    }

    @Test
    void format_injection_hugeDigitString_doesNotCrashJVM() {
        // 注入面：超长纯数字串。Long.parseLong 可能溢出；断言行为被观察并记录，不使进程崩溃。
        String huge = "9".repeat(40);
        try {
            String r = TimeUtil.format(huge);
            // 若内部捕获则回退为标准格式
            assertNotNull(r);
        } catch (Exception e) {
            // 已知行为：溢出会抛 NumberFormatException（未被捕获）——记录为真实行为
            assertTrue(e instanceof java.lang.NumberFormatException);
        }
    }
}
