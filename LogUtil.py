import json
import traceback
from datetime import datetime

class LogUtil:

    @staticmethod
    def _now():
        return datetime.now().strftime("%Y-%m-%d-%H-%M-%S")

    @staticmethod
    def _format(msg):
        if isinstance(msg, (dict, list)):
            return "\n" + json.dumps(msg, ensure_ascii=False, indent=2)
        return str(msg)

    @staticmethod
    def info(msg):
        print(f"[{LogUtil._now()}] [INFO] {LogUtil._format(msg)}")

    @staticmethod
    def warn(msg):
        print(f"[{LogUtil._now()}] [WARN] {LogUtil._format(msg)}")

    @staticmethod
    def error(msg, e=None):
        err_str = ""
        if e:
            err_str = "\n" + traceback.format_exc()
        print(f"[{LogUtil._now()}] [ERROR] {LogUtil._format(msg)}{err_str}")

    @staticmethod
    def debug(msg):
        print(f"[{LogUtil._now()}] [DEBUG] {LogUtil._format(msg)}")
