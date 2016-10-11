var Charset = {};
Charset.charsets = [
    {"name": "us-ascii",                 "text": "US-ASCII", "alias": "ANSI_X3.4-1968, ANSI_X3.4-1986, ascii, cp367, csASCII, IBM367, ISO_646.irv:1991, ISO646-US, iso-ir-6us"},
    {"name": "iso-8859-1",               "text": "ISO-8859-1", "alias": "cp819, csISO, Latin1, ibm819, iso-8859-1, iso-8859-1:1987, iso8859-1, iso-ir-100, l1, latin1"},
    {"name": "utf-8",                    "text": "UTF-8", "alias": "unicode-1-1-utf-8, unicode-2-0-utf-8, x-unicode-2-0-utf-8, utf8"},
    {"name": "utf-7",                    "text": "UTF-7", "alias": "csUnicode11UTF7, unicode-1-1-utf-7, x-unicode-2-0-utf-7"},
    {"name": "unicode",                  "text": "Unicode", "alias": "utf-16"},
    {"name": "unicodeFFFE",              "text": "Unicode (Big-Endian)", "alias": ""},
    {"name": "gbk",                      "text": "GBK     简体中文", "alias": "windows-936, CP936"},
    {"name": "gb2312",                   "text": "GB2312  简体中文", "alias": "chinese, CN-GB, csGB2312, csGB231280, csISO58GB231280, GB_2312-80, GB231280, GB2312-80, GBK, iso-ir-58"},
    {"name": "gb18030",                  "text": "GB18030 简体中文", "alias": ""},
    {"name": "hz-gb-2312",               "text": "HZ  简体中文", "alias": ""},
    {"name": "x-mac-chinesesimp",        "text": "Mac 简体中文", "alias": ""},
    {"name": "EUC-CN",                   "text": "EUC-CN 简体中文", "alias": "x-euc-cn"},
    {"name": "big5",                     "text": "Big5 繁体中文", "alias": "cn-big5, csbig5, x-x-big5"},
    {"name": "x-Chinese-CNS",            "text": "CNS  繁体中文", "alias": ""},
    {"name": "x-Chinese-Eten",           "text": "Eten 繁体中文", "alias": ""},
    {"name": "x-mac-chinesetrad",        "text": "Mac  繁体中文", "alias": ""},
    {"name": "ASMO-708",                 "text": "ASMO-708 阿拉伯语", "alias": ""},
    {"name": "DOS-720",                  "text": "DOS 阿拉伯语", "alias": ""},
    {"name": "iso-8859-6",               "text": "ISO 阿拉伯语", "alias": "arabic, csISOLatinArabic, ECMA-114, ISO_8859-6, ISO_8859-6:1987, iso-ir-127"},
    {"name": "x-mac-arabic",             "text": "Mac 阿拉伯语", "alias": ""},
    {"name": "windows-1256",             "text": "Windows 阿拉伯语", "alias": "cp1256"},
    {"name": "ibm775",                   "text": "DOS 波罗的语", "alias": "CP500"},
    {"name": "iso-8859-4",               "text": "ISO 波罗的语", "alias": "csISOLatin4, ISO_8859-4, ISO_8859-4:1988, iso-ir-110, l4, latin4"},
    {"name": "windows-1257",             "text": "Windows 波罗的语", "alias": ""},
    {"name": "ibm852",                   "text": "DOS 中欧字符", "alias": "cp852"},
    {"name": "iso-8859-2",               "text": "ISO 中欧字符", "alias": "csISOLatin2, iso_8859-2, iso_8859-2:1987, iso8859-2, iso-ir-101, l2, latin2"},
    {"name": "x-mac-ce",                 "text": "Mac 中欧字符", "alias": ""},
    {"name": "windows-1250",             "text": "Windows 中欧字符", "alias": "x-cp1250"},
    {"name": "cp866",                    "text": "DOS 西里尔语", "alias": "ibm866"},
    {"name": "iso-8859-5",               "text": "ISO 西里尔语", "alias": "csISOLatin5, csISOLatinCyrillic, cyrillic, ISO_8859-5, ISO_8859-5:1988, iso-ir-144, l5"},
    {"name": "koi8-r",                   "text": "KOI8-R 西里尔语", "alias": "csKOI8R, koi, koi8, koi8r"},
    {"name": "koi8-u",                   "text": "KOI8-U 西里尔语", "alias": "koi8-ru"},
    {"name": "x-mac-cyrillic",           "text": "Mac 西里尔语", "alias": ""},
    {"name": "windows-1251",             "text": "Windows 西里尔语", "alias": "x-cp1251"},
    {"name": "x-Europa",                 "text": "x-Europa 欧罗巴语", "alias": ""},
    {"name": "x-IA5-German",             "text": "IA5 德语", "alias": ""},
    {"name": "ibm737",                   "text": "DOS 希腊语", "alias": ""},
    {"name": "iso-8859-7",               "text": "ISO 希腊语", "alias": "csISOLatinGreek, ECMA-118, ELOT_928, greek, greek8, ISO_8859-7, ISO_8859-7:1987, iso-ir-126"},
    {"name": "x-mac-greek",              "text": "Mac 希腊语", "alias": ""},
    {"name": "windows-1253",             "text": "Windows 希腊语", "alias": ""},
    {"name": "ibm869",                   "text": "DOS 现代希腊语", "alias": ""},
    {"name": "DOS-862",                  "text": "DOS 希伯来语", "alias": ""},
    {"name": "iso-8859-8-i",             "text": "ISO-Logical 希伯来语", "alias": "logical"},
    {"name": "iso-8859-8",               "text": "ISO-Visual 希伯来语", "alias": "csISOLatinHebrew, hebrew, ISO_8859-8, ISO_8859-8:1988, ISO-8859-8, iso-ir-138, visual"},
    {"name": "x-mac-hebrew",             "text": "Mac 希伯来语", "alias": ""},
    {"name": "windows-1255",             "text": "Windows 希伯来语", "alias": "ISO_8859-8-I, ISO-8859-8, visual"},
    {"name": "x-EBCDIC-Arabic",                   "text": "IBM EBCDIC 阿拉伯语", "alias": ""},
    {"name": "x-EBCDIC-CyrillicRussian",          "text": "IBM EBCDIC 西里尔文俄语", "alias": ""},
    {"name": "x-EBCDIC-CyrillicSerbianBulgarian", "text": "IBM EBCDIC 西里尔文塞尔维亚语-保加利亚语", "alias": ""},
    {"name": "x-EBCDIC-DenmarkNorway",            "text": "IBM EBCDIC 丹麦-挪威", "alias": ""},
    {"name": "x-ebcdic-denmarknorway-euro",       "text": "IBM EBCDIC 丹麦-挪威-欧洲", "alias": ""},
    {"name": "x-EBCDIC-FinlandSweden",            "text": "IBM EBCDIC 芬兰-瑞典", "alias": ""},
    {"name": "x-ebcdic-finlandsweden-euro",       "text": "IBM EBCDIC 芬兰-瑞士-欧洲", "alias": ""},
    {"name": "x-ebcdic-finlandsweden-euro",       "text": "IBM EBCDIC 芬兰-瑞士-欧洲", "alias": "X-EBCDIC-France"},
    {"name": "x-ebcdic-france-euro",              "text": "IBM EBCDIC 法国-欧洲", "alias": ""},
    {"name": "x-EBCDIC-Germany",                  "text": "IBM EBCDIC 德国 ", "alias": ""},
    {"name": "x-ebcdic-germany-euro",             "text": "IBM EBCDIC 德国-欧洲", "alias": ""},
    {"name": "x-EBCDIC-GreekModern",              "text": "IBM EBCDIC 现代希腊语", "alias": ""},
    {"name": "x-EBCDIC-Greek",                    "text": "IBM EBCDIC 希腊语", "alias": ""},
    {"name": "x-EBCDIC-Hebrew",                   "text": "IBM EBCDIC 希伯来语", "alias": ""},
    {"name": "x-EBCDIC-Icelandic",                "text": "IBM EBCDIC 冰岛语", "alias": ""},
    {"name": "x-ebcdic-icelandic-euro",           "text": "IBM EBCDIC 冰岛语-欧洲", "alias": ""},
    {"name": "x-ebcdic-international-euro",       "text": "IBM EBCDIC 国际-欧洲", "alias": ""},
    {"name": "x-EBCDIC-Italy",                    "text": "IBM EBCDIC 意大利语", "alias": ""},
    {"name": "x-ebcdic-italy-euro",               "text": "IBM EBCDIC 意大利-欧洲", "alias": ""},
    {"name": "x-EBCDIC-JapaneseAndKana",          "text": "IBM EBCDIC 日语和日语片假名", "alias": ""},
    {"name": "x-EBCDIC-JapaneseAndJapaneseLatin", "text": "IBM EBCDIC 日语和日语-拉丁语", "alias": ""},
    {"name": "x-EBCDIC-JapaneseAndUSCanada",      "text": "IBM EBCDIC 日语和美国-加拿大", "alias": ""},
    {"name": "x-EBCDIC-JapaneseKatakana",         "text": "IBM EBCDIC 日语片假名", "alias": ""},
    {"name": "x-EBCDIC-KoreanAndKoreanExtended",  "text": "IBM EBCDIC 朝鲜语和朝鲜语扩展", "alias": ""},
    {"name": "x-EBCDIC-KoreanExtended",           "text": "IBM EBCDIC 朝鲜语扩展", "alias": ""},
    {"name": "CP870",                             "text": "IBM EBCDIC 多语言拉丁语-2", "alias": ""},
    {"name": "x-EBCDIC-SimplifiedChinese",        "text": "IBM EBCDIC 简体中文", "alias": ""},
    {"name": "X-EBCDIC-Spain",                    "text": "IBM EBCDIC 西班牙", "alias": ""},
    {"name": "x-ebcdic-spain-euro",               "text": "IBM EBCDIC 西班牙-欧洲", "alias": ""},
    {"name": "x-EBCDIC-Thai",                     "text": "IBM EBCDIC 泰语", "alias": ""},
    {"name": "x-EBCDIC-TraditionalChinese",       "text": "IBM EBCDIC 繁体中文", "alias": ""},
    {"name": "CP1026",                            "text": "IBM EBCDIC 土耳其拉丁语-5", "alias": ""},
    {"name": "x-EBCDIC-Turkish",                  "text": "IBM EBCDIC 土耳其语", "alias": ""},
    {"name": "x-EBCDIC-UK",                       "text": "IBM EBCDIC 英国 IBM", "alias": ""},
    {"name": "x-ebcdic-uk-euro",                  "text": "IBM EBCDIC 英国-欧洲", "alias": ""},
    {"name": "ebcdic-cp-us",                      "text": "IBM EBCDIC 美国-加拿大", "alias": ""},
    {"name": "x-ebcdic-cp-us-euro",               "text": "IBM EBCDIC 美国-加拿大-欧洲", "alias": ""},
    {"name": "x-iscii-as",                        "text": "ISCII-AS 阿萨姆语", "alias": ""},
    {"name": "x-iscii-be",                        "text": "ISCII-BE 孟加拉语", "alias": ""},
    {"name": "x-iscii-de",                        "text": "ISCII-DE 梵文", "alias": ""},
    {"name": "x-iscii-gu",                        "text": "ISCII-GU 古吉拉特语", "alias": ""},
    {"name": "x-iscii-ka",                        "text": "ISCII-KA 埃纳德语", "alias": ""},
    {"name": "x-iscii-ma",                        "text": "ISCII-MA 马拉雅拉姆语", "alias": ""},
    {"name": "x-iscii-or",                        "text": "ISCII-OR 奥里亚语", "alias": ""},
    {"name": "x-iscii-pa",                        "text": "ISCII-PA 旁遮普文", "alias": ""},
    {"name": "x-iscii-ta",                        "text": "ISCII-TA 泰米尔语", "alias": ""},
    {"name": "x-iscii-te",                        "text": "ISCII-TE 泰卢固语", "alias": ""},
    {"name": "ibm861",                            "text": "DOS 冰岛语", "alias": ""},
    {"name": "x-mac-icelandic",                   "text": "Mac 冰岛语", "alias": ""},
    {"name": "euc-jp",                            "text": "EUC 日语", "alias": "csEUCPkdFmtJapanese, Extended_UNIX_Code_Packed_Format_for_Japanese, x-euc, x-euc-jp"},
    {"name": "iso-2022-jp",                       "text": "JIS 日语", "alias": ""},
    {"name": "csISO2022JP",                       "text": "JIS 日语", "alias": "_iso-2022-jp"},
    {"name": "x-mac-japanese",                    "text": "Mac 日语", "alias": ""},
    {"name": "shift_jis",                         "text": "Shift-JIS 日语", "alias": "csShiftJIS, csWindows31J, ms_Kanji, shift-jis, x-ms-cp932, x-sjis"},
    {"name": "ks_c_5601-1987",                    "text": "KS_C_5601 韩语", "alias": "csKSC56011987, euc-kr, iso-ir-149, korean, ks_c_5601, ks_c_5601_1987, ks_c_5601-1989, KSC_5601, KSC5601"},
    {"name": "euc-kr",                            "text": "EUC 朝鲜语", "alias": "csEUCKR"},
    {"name": "iso-2022-kr",                       "text": "ISO 朝鲜语", "alias": "csISO2022KR"},
    {"name": "Johab",                             "text": "Johab 朝鲜语", "alias": ""},
    {"name": "x-mac-korean",                      "text": "Mac 朝鲜语", "alias": ""},
    {"name": "iso-8859-3",                        "text": "ISO Latin 3", "alias": "csISO, Latin3, ISO_8859-3, ISO_8859-3:1988, iso-ir-109, l3, latin3"},
    {"name": "iso-8859-15",                       "text": "ISO Latin 9", "alias": "csISO, Latin9, ISO_8859-15, l9, latin9"},
    {"name": "x-IA5-Norwegian",                   "text": "IA5 挪威语", "alias": ""},
    {"name": "IBM437",                            "text": "OEM 美国", "alias": "437, cp437, csPC8, CodePage437"},
    {"name": "x-IA5-Swedish",                     "text": "IA5 瑞典语", "alias": ""},
    {"name": "windows-874",                       "text": "Windows 泰语", "alias": "DOS-874, iso-8859-11, TIS-620"},
    {"name": "ibm857",                            "text": "DOS 土耳其语", "alias": ""},
    {"name": "iso-8859-9",                        "text": "ISO 土耳其语", "alias": "csISO, Latin5, ISO_8859-9, ISO_8859-9:1989, iso-ir-148, l5, latin5"},
    {"name": "x-mac-turkish",                     "text": "Mac 土耳其语", "alias": ""},
    {"name": "windows-1254",                      "text": "Windows 土耳其语", "alias": "ISO_8859-9, ISO_8859-9:1989, iso-8859-9, iso-ir-148, latin5"},
    {"name": "windows-1258",                      "text": "Windows 越南语", "alias": ""},
    {"name": "ibm850",                            "text": "DOS 西欧语", "alias": ""},
    {"name": "x-IA5",                             "text": "IA5 西欧语", "alias": ""},
    {"name": "macintosh",                         "text": "Mac 西欧语", "alias": ""},
    {"name": "Windows-1252",                      "text": "Windows 西欧语", "alias": "ANSI_X3.4-1968, ANSI_X3.4-1986, ascii, cp367, cp819, csASCII, IBM367, ibm819, ISO_646.irv:1991, iso_8859-1, iso_8859-1:1987, ISO646-US, iso8859-1, iso-8859-1, iso-ir-100, iso-ir-6, latin1, us, us-ascii, x-ansi"}
];

Charset.trim = function(source){return (source != null ? source.toString().replace(/(^\s*)|(\s*$)/g, "") : "");};
Charset.contains = function(content, value, ingoreCase) {
    if(content != null && value != null) {
        var array = content.split(",");

        if(ingoreCase == true) {
            value = value.toLowerCase();
        }

        for(var i = 0; i < array.length; i++) {
            var s = this.trim(array[i]);

            if(ingoreCase == true) {
                s = s.toLowerCase();
            }

            if(value == s) {
                return true;
            }
        }
    }
    return false;
};

Charset.get = function(name) {
    if(name == null) {
        return null;
    }

    var charsets = this.charsets;
    var search = name.toLowerCase();

    for(var i = 0; i < charsets.length; i++) {
        var charset = charsets[i];

        if(search == charset.name.toLowerCase() || this.contains(charset.alias, search, true)) {
            return charset;
        }
    }
    return null;
};

Charset.setup = function(e, value) {
    for(var i = e.length - 1; i > -1; i--) {
        e.options.remove(i);
    }

    var flag = false;
    var selected = null;
    var charsets = this.charsets;

    if(value == null) {
        value = e.getAttribute("selected-value");
    }

    if(value != null && value.length > 0) {
        selected = value.toLowerCase();
    }

    for(var i = 0; i < charsets.length; i++) {
        var charset = charsets[i];
        var option = new Option(charset.text, charset.name);

        if(selected != null && flag == false) {
            if(selected == charset.name.toLowerCase() || this.contains(charset.alias, selected, true)) {
                flag = true;
                option.selected = true;
            }
        }
        option.setAttribute("alias", charset.alias);
        e.options.add(option);
    }
};
