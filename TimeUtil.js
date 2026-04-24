class TimeUtil {
  // 统一输出格式
  static #PATTERN = "yyyy-MM-dd-HH-mm-ss";

  /**
   * 获取当前标准时间
   * @returns {string} yyyy-MM-dd-HH-mm-ss
   */
  static now() {
    const d = new Date();
    const y = d.getFullYear();
    const M = String(d.getMonth() + 1).padStart(2, "0");
    const day = String(d.getDate()).padStart(2, "0");
    const h = String(d.getHours()).padStart(2, "0");
    const m = String(d.getMinutes()).padStart(2, "0");
    const s = String(d.getSeconds()).padStart(2, "0");
    return `${y}-${M}-${day}-${h}-${m}-${s}`;
  }

  /**
   * 万能格式化：任意奇葩时间转统一格式
   * @param {string|number|Date|null} time
   * @returns {string}
   */
  static format(time = null) {
    if (!time) return this.now();
    let date;

    // 时间戳兼容 秒/毫秒
    if (typeof time === "number") {
      const ts = time.toString().length === 10 ? time * 1000 : time;
      date = new Date(ts);
    } else {
      date = new Date(time);
    }

    if (isNaN(date.getTime())) return this.now();
    return this.#build(date);
  }

  static #build(d) {
    const y = d.getFullYear();
    const M = String(d.getMonth() + 1).padStart(2, "0");
    const day = String(d.getDate()).padStart(2, "0");
    const h = String(d.getHours()).padStart(2, "0");
    const m = String(d.getMinutes()).padStart(2, "0");
    const s = String(d.getSeconds).padStart(2, "0");
    return `${y}-${M}-${day}-${h}-${m}-${s}`;
  }
}

module.exports = TimeUtil;
