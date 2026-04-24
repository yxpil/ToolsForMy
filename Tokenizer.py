import jieba

def split_words(text: str) -> list:
    if not text:
        return []

    # 1. 去掉所有符号，只保留中文、英文、数字、空格
    import re
    text = re.sub(r'[^\u4e00-\u9fa5a-zA-Z0-9\s]', '', text)

    # 2. 按空格分割
    fragments = [p.strip() for p in text.split() if p.strip()]

    result = []
    for frag in fragments:
        # 判断是否包含中文
        has_chinese = any('\u4e00' <= c <= '\u9fff' for c in frag)

        if has_chinese:
            # 中文：jieba 分词
            words = jieba.cut_for_search(frag)
            result.extend(words)
        else:
            # 英文/数字：整块保留
            result.append(frag)

    # 3. 过滤 + 去重 + 长度 >=2
    result = [w.strip() for w in result if len(w.strip()) >= 2]
    result = list(set(result))  # 去重

    # 4. 长词优先排序
    result.sort(key=lambda x: len(x), reverse=True)

    return result
