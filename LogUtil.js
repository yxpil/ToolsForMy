class LogUtil {

    static #now() {
        const d = new Date();
        const y = d.getFullYear();
        const M = String(d.getMonth() + 1).padStart(2, '0');
        const D = String(d.getDate()).padStart(2, '0');
        const H = String(d.getHours()).padStart(2, '0');
        const m = String(d.getMinutes()).padStart(2, '0');
        const s = String(d.getSeconds()).padStart(2, '0');
        return `${y}-${M}-${D}-${H}-${m}-${s}`;
    }

    static #format(msg) {
        if (typeof msg === 'object' && msg !== null) {
            return '\n' + JSON.stringify(msg, null, 2);
        }
        return msg;
    }

    static info(msg) {
        console.log(`[${this.#now()}] [INFO] ${this.#format(msg)}`);
    }

    static warn(msg) {
        console.log(`[${this.#now()}] [WARN] ${this.#format(msg)}`);
    }

    static error(msg, err) {
        let errStack = err?.stack ? '\n' + err.stack : '';
        console.error(`[${this.#now()}] [ERROR] ${this.#format(msg)}${errStack}`);
    }

    static debug(msg) {
        console.log(`[${this.#now()}] [DEBUG] ${this.#format(msg)}`);
    }
}

module.exports = LogUtil;
