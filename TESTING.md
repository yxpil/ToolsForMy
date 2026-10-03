# ToolsForMy 测试说明 (TESTING.md)

本仓库为无构建工具的零散 Java 工具类（默认包）。测试统一放在 `src/test/`，使用 JUnit 5 (Jupiter)。

## 测了什么

| 类 | 覆盖点 |
|---|---|
| `TimeUtil` | `now()` 标准格式；`format(null)` 回退当前时间；标准串 round-trip；10 位秒级 / 13 位毫秒时间戳解析；乱入串回退不抛错；超长数字注入的行为记录 |
| `JsonUtil` | `build` 成对/奇数参数；`parse` 合法 JSON；`stringify` 美化输出；畸形/空 JSON 返回空 Map 不抛错 |
| `LogUtil` | 捕获 stdout 验证 `[INFO]` 前缀、null 消息、Map 美化输出、warn/debug 不抛错 |
| `Tokenizer` | null/空白返回空；英文整词保留；短词过滤；结果按词长降序；中文分词非空 |

## 注入测试（不可信输入，必做项）
本仓库无 HTTP 路由 / shell / SQL 拼接面，注入面集中在"解析不可信输入"：
- `JsonUtilTest.parse_malformed / parse_empty`：畸形 JSON、空串必须返回空 Map，不得抛异常。
- `JsonUtilTest.parse_sqlOrHtmlInjectionInsideString_isDataNotCode`：JSON 字符串值中的 `'; DROP TABLE users;--`、`<script>alert(1)</script>` 仅作为普通字符串数据返回，不被解释执行。
- `TokenizerTest.splitWords_xssPayload_stripsMarkupChars`：CLEAN_PATTERN 剥离 `< > ( ) ' "` 等标记字符，输出 token 不得残留 HTML/脚本标记（防 XSS 注入）。
- `TimeUtilTest.format_garbage / format_injection_hugeDigitString`：乱入时间串回退为当前时间不崩溃；超长纯数字串的溢出行为被观察记录。

## 钩子测试
本仓库为纯静态工具方法集合，**不存在钩子/插件/事件/回调机制**，故钩子测试数为 0（如实说明，不伪造）。

## 如何运行
本机无 Maven/Gradle，依赖 Jackson / hutool / jieba 与 JUnit Console Launcher，用 `javac` 手动编译：
```
# 依赖 jar（放到 lib/）：jackson-databind/core/annotations、hutool-all、jieba-analysis、
#                        junit-platform-console-standalone
javac -cp "lib/*" -d out/main *.java
javac -cp "lib/*;out/main" -d out/test src/test/*.java
java  -cp "lib/*;out/main;out/test" \
      org.junit.platform.console.ConsoleLauncher --scan-classpath=out/test
```

## 预期结果
- 新增测试 **27 个全部通过，0 失败**（单元 24 + 集成 3）。
- 注入测试 6 个；钩子测试 0 个（无该机制）。
