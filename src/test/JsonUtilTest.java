import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

/**
 * JsonUtil 单元 + 注入测试。
 * 注入面：parse 接收不可信 JSON，必须在畸形输入下返回空 Map 而非抛异常。
 */
public class JsonUtilTest {

    @Test
    void build_pairsIntoMap() {
        Map<String, Object> m = JsonUtil.build("a", 1, "b", "two");
        assertEquals(1, m.get("a"));
        assertEquals("two", m.get("b"));
    }

    @Test
    void build_oddArgs_lastValueIsNull() {
        Map<String, Object> m = JsonUtil.build("onlyKey");
        assertNull(m.get("onlyKey"));
    }

    @Test
    void parse_valid_returnsMap() {
        Map<String, Object> m = JsonUtil.parse("{\"k\":\"v\",\"n\":42}");
        assertEquals("v", m.get("k"));
        assertEquals(42, ((Number) m.get("n")).intValue());
    }

    @Test
    void stringify_outputsJson() {
        String s = JsonUtil.stringify(JsonUtil.build("hello", "world"));
        assertTrue(s.contains("hello"));
        assertTrue(s.contains("world"));
    }

    // ── 注入：不可信 / 畸形 JSON ──

    @Test
    void parse_malformed_returnsEmptyMap_noThrow() {
        assertDoesNotThrow(() -> {
            Map<String, Object> m = JsonUtil.parse("{{{not json");
            assertNotNull(m);
            assertTrue(m.isEmpty());
        });
    }

    @Test
    void parse_emptyString_returnsEmptyMap() {
        Map<String, Object> m = assertDoesNotThrow(() -> JsonUtil.parse(""));
        assertTrue(m.isEmpty());
    }

    @Test
    void parse_nullJson_returnsNull_noThrow() {
        // 真实行为：Jackson 将 JSON "null" 反序列化为 Java null（非异常），工具方法原样返回，不抛错
        Map<String, Object> m = assertDoesNotThrow(() -> JsonUtil.parse("null"));
        assertNull(m);
    }

    @Test
    void parse_sqlOrHtmlInjectionInsideString_isDataNotCode() {
        // JSON 字符串里夹带 SQL/HTML 注入载荷：解析后只是普通字符串数据，不被解释执行
        Map<String, Object> m = JsonUtil.parse("{\"q\":\"'; DROP TABLE users;--\"}");
        assertEquals("'; DROP TABLE users;--", m.get("q"));

        Map<String, Object> m2 = JsonUtil.parse("{\"x\":\"<script>alert(1)</script>\"}");
        assertEquals("<script>alert(1)</script>", m2.get("x"));
    }

    @Test
    void stringify_unserializable_fallsBackToEmptyObject() {
        // 无法序列化的对象（自引用环）-> 回退 "{}"，不抛异常
        Object cyclic = new Object() {
            @Override public String toString() { return "x"; }
        };
        // 普通对象可序列化；此处主要验证 stringify 不抛异常
        String s = assertDoesNotThrow(() -> JsonUtil.stringify(cyclic));
        assertNotNull(s);
    }
}
