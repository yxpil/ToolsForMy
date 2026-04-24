import cn.hutool.core.util.StrUtil;
import com.huaban.analysis.jieba.JiebaSegmenter;
import com.huaban.analysis.jieba.SegToken;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Tokenizer {

    private static final JiebaSegmenter jieba = new JiebaSegmenter();
    private static final Pattern CLEAN_PATTERN = Pattern.compile("[^\\u4e00-\\u9fa5a-zA-Z0-9\\s]");
    private static final Pattern CHINESE_PATTERN = Pattern.compile("[\\u4e00-\\u9fa5]");

    public static List<String> splitWords(String text) {
        if (StrUtil.isBlank(text)) {
            return new ArrayList<>();
        }

        // 1. 去掉符号
        text = CLEAN_PATTERN.matcher(text).replaceAll("");

        // 2. 按空格分割
        String[] fragments = text.trim().split("\\s+");

        Set<String> resultSet = new HashSet<>();

        for (String frag : fragments) {
            if (StrUtil.isBlank(frag)) continue;

            // 判断是否包含中文
            Matcher m = CHINESE_PATTERN.matcher(frag);
            if (m.find()) {
                // 中文分词
                List<SegToken> tokens = jieba.process(frag, JiebaSegmenter.SegMode.SEARCH);
                for (SegToken token : tokens) {
                    String word = token.word.trim();
                    if (word.length() >= 2) {
                        resultSet.add(word);
                    }
                }
            } else {
                // 英文数字整块保留
                if (frag.length() >= 2) {
                    resultSet.add(frag);
                }
            }
        }

        // 3. 长词优先排序
        List<String> result = new ArrayList<>(resultSet);
        result.sort((a, b) -> b.length() - a.length());

        return result;
    }
}
