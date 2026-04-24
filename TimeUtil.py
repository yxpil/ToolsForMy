import re
from datetime import datetime

class TimeUtil:
    # 全局统一格式
    FORMAT = "%Y-%m-%d-%H-%M-%S"

    @staticmethod
    def now() -> str:
        """获取当前标准时间"""
        return datetime.now().strftime(TimeUtil.FORMAT)

    @staticmethod
    def format(time_input=None) -> str:
        """
        万能时间格式化
        支持：时间戳(秒/毫秒)、2025/xx/xx、2025.xx.xx、带T、中文日期
        """
        if time_input is None:
            return TimeUtil.now()

        try:
            # 处理数字时间戳
            if isinstance(time_input, (int, float)):
                ts = int(time_input)
                # 10位秒 / 13位毫秒
                if len(str(ts)) == 10:
                    dt = datetime.fromtimestamp(ts)
                else:
                    dt = datetime.fromtimestamp(ts / 1000)
                return dt.strftime(TimeUtil.FORMAT)

            # 统一替换分隔符 兼容 / . 、
            text = str(time_input)
            text = re.sub(r"[/\.年月]", "-", text)
            text = re.sub(r"[T时:分]", "-", text)

            dt = datetime.fromisoformat(text)
            return dt.strftime(TimeUtil.FORMAT)
        except Exception:
            return TimeUtil.now()
