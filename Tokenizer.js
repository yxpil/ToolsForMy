const jieba = require("nodejieba");

// 搜索专用纯净分词：自动去除符号，只保留文字/数字/英文
function splitWords(text) {
    if (!text) return [];
    //删除符号
    text = text.replace(/[^\u4e00-\u9fa5a-zA-Z0-9\s]/g, "");

    // 按空格分割
    const fragments = text.split(/\s+/).filter(i => i.trim());
    let result = [];

    for (let frag of fragments) {
        const hasChinese = /[\u4e00-\u9fa5]/.test(frag);

        if (hasChinese) {
            // 中文 → 分词
            const words = jieba.cutForSearch(frag);
            result.push(...words);
        } else {
            // 英文/数字 → 整块保留
            result.push(frag);
        }
    }

    // 过滤：长度≥2 + 去重
    result = [...new Set(result.filter(w => w.length >= 2))];

    // 长词在前
    return result.sort((a, b) => b.length - a.length);
}

module.exports = { splitWords };