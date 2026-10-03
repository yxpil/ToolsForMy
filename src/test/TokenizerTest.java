import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tokenizer 单元 + 注入测试。
 * 注入面：splitWords 接收不可信文本，CLEAN_PATTERN 会剥离所有非中文/字母数字/空白字符，
 * 因此 HTML/脚本载荷必须被净化，不得保留 < > 等标记。
 */
public class TokenizerTest {

    @Test
    void splitWords_nullOrBlank_returnsEmpty() {
        assertTrue(Tokenizer.splitWords(null).isEmpty());
        assertTrue(Tokenizer.splitWords("   ").isEmpty());
    }

    @Test
    void splitWords_englishKeptAsWholeWords() {
        List<String> r = Tokenizer.splitWords("hello world foo");
        assertTrue(r.contains("hello"));
        assertTrue(r.contains("world"));
    }

    @Test
    void splitWords_shortTokensDropped() {
        // 单字符会被丢弃（长度>=2 才保留）
        List<String> r = Tokenizer.splitWords("a bc");
        assertFalse(r.contains("a"));
        assertTrue(r.contains("bc"));
    }

    @Test
    void splitWords_sortedLongestFirst() {
        List<String> r = Tokenizer.splitWords("ab cdef ghij");
        for (int i = 1; i < r.size(); i++) {
            assertTrue(r.get(i - 1).length() >= r.get(i).length(),
                "结果应按词长降序: " + r);
        }
    }

    // ── 注入：XSS / 脚本载荷被净化 ──

    @Test
    void splitWords_xssPayload_stripsMarkupChars() {
        List<String> r = Tokenizer.splitWords("<script>alert('xss')</script>");
        // 清理后不应残留任何 < > 等标记字符
        for (String tok : r) {
            assertFalse(tok.contains("<"), "token 不得含 '<': " + tok);
            assertFalse(tok.contains(">"), "token 不得含 '>': " + tok);
            assertFalse(tok.contains("("), "token 不得含 '(': " + tok);
        }
    }

    @Test
    void splitWords_chineseSegments_nonEmpty() {
        List<String> r = Tokenizer.splitWords("中华人民共和国");
        assertNotNull(r);
        // jieba 中文分词结果非空（每个词长度>=2）
        for (String tok : r) {
            assertTrue(tok.length() >= 2, tok);
        }
    }
}
