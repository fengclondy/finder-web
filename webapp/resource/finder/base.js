var StringUtil = {};

/**
 * @param source
 * @return boolean
 */
StringUtil.trim = function(source){return (source != null ? source.replace(/(^\s*)|(\s*$)/g, "") : "");};

/**
 * @param source
 * @return boolean
 */
StringUtil.startsWith = function(source, search){
    if(source.length >= search.length)
    {
        return (source.substring(0, search.length) == search);
    }

    return false;
};

/**
 * @param source
 * @return boolean
 */
StringUtil.endsWith = function(source, search){
    if(source.length >= search.length)
    {
        return (source.substring(source.length - search.length) == search);
    }

    return false;
};

/**
 * @param source
 * @param context
 * @return String
 */
StringUtil.replace = function(source, context){
    var c = null;
    var result = [];

    for(var i = 0, length = source.length; i < length; i++)
    {
        c = source.charAt(i);

        if(c == "$" && i < length - 1 && source.charAt(i + 1) == "{")
        {
            var buffer = [];

            for(var j = i + 2; j < length; j++)
            {
                i = j;
                c = source.charAt(j);

                if(c == "}")
                {
                    var value = context.getValue(buffer.join(""));

                    if(value != null)
                    {
                        result.push(value.toString());
                    }

                    break;
                }
                else
                {
                    buffer.push(c);
                }
            }
        }
        else
        {
            result.push(c);
        }
    }

    return result;
};

/**
 * @param source
 * @return String
 */
StringUtil.text = function(source){
    if(source == null)
    {
        return "";
    }

    var c = null;
    var buffer = [];

    for(var i = 0, length = source.length; i < length; i++)
    {
        c = source.charAt(i);

        switch (c)
        {
            case "\\":
            {
                buffer.push("\\\\"); break;
            }
            case "\'":
            {
                buffer.push("\\\'"); break;
            }
            case "\"":
            {
                buffer.push("\\\""); break;
            }
            case "\r":
            {
                buffer.push("\\r"); break;
            }
            case "\n":
            {
                buffer.push("\\n"); break;
            }
            case "\t":
            {
                buffer.push("\\t"); break;
            }
            case "\b":
            {
                buffer.push("\\b"); break;
            }
            case "\f":
            {
                buffer.push("\\f"); break;
            }
            default :
            {
                buffer.push(c); break;
            }
        }
    }

    return buffer.join("");
};

var HtmlUtil = {};

/**
 * @param source
 * @param crlf
 * @return String
 */
HtmlUtil.encode = function(source, crlf){
    if(source == null)
    {
        return "";
    }

    if(crlf == null || crlf == undefined)
    {
        crlf = "\n";
    }

    var c;
    var buffer = [];

    for(var i = 0, length = source.length; i < length; i++)
    {
        c = source.charAt(i);

        switch (c)
        {
            case "&":
            {
                buffer.push("&amp;");
                break;
            }
            case "\"":
            {
                buffer.push("&quot;");
                break;
            }
            case "<":
            {
                buffer.push("&lt;");
                break;
            }
            case ">":
            {
                buffer.push("&gt;");
                break;
            }
            case "\r":
            {
                if((i + 1) < size)
                {
                    if(source.charAt(i + 1) == "\n")
                    {
                        buffer.push(crlf);
                        i++;
                    }
                    else
                    {
                        buffer.push(c);
                    }
                }
                else
                {
                    buffer.push(c);
                }

                break;
            }
            case "\n":
            {
                buffer.push(crlf);
                break;
            }
            default :
            {
                buffer.push(c);
                break;
            }
        }
    }

    return buffer.join("");
};

var URL = {};

URL.serialize = function(json){
    var a = [];
    if(json != null)
    {
        if(typeof(json) == "object")
        {
            for(var name in json)
            {
                var value = json[name];
                var className = typeof(value);

                if(value != null)
                {
                    if(className == "object" && value.length != null)
                    {
                        for(var j = 0; j < value.length; j++)
                        {
                            if(value[j] != null)
                            {
                                a[a.length] = encodeURIComponent(name) + "=" + encodeURIComponent(value[j]);
                            }
                        }
                    }
                    else
                    {
                        a[a.length] = encodeURIComponent(name) + "=" + encodeURIComponent(value.toString());
                    }
                }
            }
        }
    }

    return a.join("&");
};