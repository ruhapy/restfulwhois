# -*- coding: utf-8 -*-
from urllib import quote

class UrlEncode:
    def encode_url(self, str):
        return quote(str.encode('utf-8'))