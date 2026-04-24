class JsonUtil {
    /**
     * 对象转美化 JSON 字符串
     */
    static stringify(obj) {
        try {
            return JSON.stringify(obj, null, 2);
        } catch (e) {
            return "{}";
        }
    }

    /**
     * 解析 JSON 字符串 → 对象
     */
    static parse(jsonStr) {
        try {
            return JSON.parse(jsonStr);
        } catch (e) {
            return {};
        }
    }

    /**
     * 快速构建 JSON（直接传键值对）
     */
    static build(...args) {
        const obj = {};
        for (let i = 0; i < args.length; i += 2) {
            const key = args[i];
            const value = args[i + 1];
            if (key) obj[key] = value;
        }
        return obj;
    }
}

module.exports = JsonUtil;
