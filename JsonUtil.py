import json

class JsonUtil:
    @staticmethod
    def stringify(obj, indent=2):
        try:
            return json.dumps(obj, ensure_ascii=False, indent=indent)
        except:
            return "{}"

    @staticmethod
    def parse(json_str):
        try:
            return json.loads(json_str)
        except:
            return {}

    @staticmethod
    def build(**kwargs):
        return kwargs
