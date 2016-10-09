var ScrollPage = {};

ScrollPage.allocate = function(length, value){
    var a = [];

    for(var i = 0; i < length; i++)
    {
        a[i] = value;
    };

    return a;
};

ScrollPage.getPages = function(num, pages, size){
    var result = null;

    if(pages <= (size + 4))
    {
        var length = (pages > 0 ? pages : 1);
        result = this.allocate(length, 0);

        for(var i = 0; i < length; i++)
        {
            result[i] = i + 1;
        }

        return result;
    }

    var start = Math.floor(num - size / 2);
    var end = start + size;

    if(start < 3)
    {
        start = 1;
        end = start + size + 1;
        result = this.allocate(size + 3, 0);
    }
    else if((start + size) >= pages)
    {
        start = pages - size;
        end = start + size;
        result = this.allocate(size + 3, 0);
    }
    else
    {
        end = start + size;
        result = this.allocate(size + 4, 0);
    }

    result[0] = 1;
    result[result.length - 1] = pages;

    if(start >= 3)
    {
        if(start == 3)
        {
            result[1] = 2;
        }

        for(var i = start; i < end; i++)
        {
            result[i - start + 2] = i;
        }
    }
    else
    {
        start = 1;
        end = size + 2;

        for(var i = start; i < end; i++)
        {
            result[i - start] = i;
        }
    }

    return result;
};

ScrollPage.render = function(page){
    if(page.pageNum < 1)
    {
        page.pageNum = 1;
    }

    if(page.pageSize < 1)
    {
        page.pageSize = 1;
    }

    if(page.className == null)
    {
        page.className = "";
    }

    if(page.href == null || page.href.trim().length() < 1)
    {
        page.href = "javascript:void(0)";
    }

    if(page.pattern == null)
    {
        page.pattern = "";
    }

    var count = this.getCount(page);
    var pages = this.getPages(page.pageNum, count, 7);

    var buffer = [];
    buffer[buffer.length] = "<div";

    if(page.className != null)
    {
        buffer[buffer.length] = " class=\"" + page.className + "\"";
    }

    buffer[buffer.length] = " page=\"" + page.pageNum + "\" count=\"" + count + "\" total=\"" + page.total + "\">";

    if(page.pageNum > 1)
    {
        var prev = this.replace(page.href, "%s", page.pageNum - 1);
        buffer[buffer.length] = "<a class=\"prev scrollpage\" href=\"" + prev + "\" page=\"" + (page.pageNum - 1) + "\" title=\"上一页\">上一页</a>";
    }
    else
    {
        var prev = "javascript:void(0)";
        buffer[buffer.length] = "<a class=\"prev\" href=\"" + prev + "\" page=\"" + (page.pageNum - 1) + "\" title=\"上一页\">上一页</a>";
    }

    for(var i = 0; i < pages.length; i++)
    {
        var n = pages[i];

        if(n != 0)
        {
            if(n == page.pageNum)
            {
                buffer[buffer.length] = "<a class=\"current scrollpage\" href=\"" + this.replace(page.href, "%s", n) + "\" page=\"" + n + "\">" + n + "</a>";
            }
            else
            {
                buffer[buffer.length] = "<a class=\"scrollpage\" href=\"" + this.replace(page.href, "%s", n) + "\" page=\"" + n + "\">" + n + "</a>";
            }
        }
        else
        {
            buffer[buffer.length] = "...";
        }
    }

    if(page.pageNum < count)
    {
        var next = this.replace(page.href, "%s", page.pageNum + 1);
        buffer[buffer.length] = "<a href=\"" + next + "\" class=\"next scrollpage\" page=\"" + (page.pageNum + 1) + "\" title=\"下一页\">下一页</a>";
    }
    else
    {
        var next = "javascript:void(0)";
        buffer[buffer.length] = "<a href=\"" + next + "\" class=\"next\" page=\"" + (page.pageNum + 1) + "\" title=\"下一页\">下一页</a>";
    }

    var info = page.pattern;
    info = this.replace(info, "!{pageSize}", page.pageSize);
    info = this.replace(info, "!{pageNum}", page.pageNum);
    info = this.replace(info, "!{total}", page.total);
    info = this.replace(info, "!{count}", count);

    buffer[buffer.length] = info;
    buffer[buffer.length] = "</div>";
    return buffer.join("\r\n");
};

ScrollPage.replace = function(source, search, page){
    if(source == null)
    {
        return "";
    }

    if(search == null)
    {
        return source;
    }

    var s = 0;
    var e = 0;
    var buffer = [];

    do
    {
        e = source.indexOf(search, s);

        if(e == -1)
        {
            buffer[buffer.length] = source.substring(s);
            break;
        }
        else
        {
            buffer[buffer.length] = source.substring(s, e) + page;
            s = e + search.length;
        }
    }
    while(true);

    return buffer.join("");
};

ScrollPage.getCount = function(page){
    return Math.floor((page.total + (page.pageSize - 1)) / page.pageSize);
};

ScrollPage.getScrollPage = function(src){
    var page = null;
    var nodeName = src.nodeName.toLowerCase();

    if(nodeName == "input")
    {
        page = parseInt(jQuery(src).siblings("input[type=text]").val());
    }
    else
    {
        page = parseInt(jQuery(src).attr("page"));
    }

    var count = parseInt(jQuery(src).parent("div.pagebar").attr("count"));
    var total = parseInt(jQuery(src).parent("div.pagebar").attr("total"));

    if(isNaN(page))
    {
        return "请输入数字 !";
    }

    if(isNaN(count))
    {
        return "系统错误, 请稍候重试 !";
    }

    if(count < 1)
    {
        count = 1;
    }

    if(page < 1)
    {
        return "请输入正确的页码 !";
    }

    if(count <= 1)
    {
        return "当前已经是最后一页 !";
    }

    if(page > count)
    {
        return "错误的页码, 请输入1 至 " + count + "之间的数字!";
    }

    if(page.toString() == jQuery(src).parent("div.pagebar").attr("page"))
    {
        return "当前已经是第" + page + "页 !";
    }

    return {"pageNum": page, "count": count, "total": total};
};

ScrollPage.scroll = function(src, callback)
{
    var scrollPage = this.getScrollPage(src);

    if(typeof(scrollPage) == "string")
    {
        MessageDialog.open(scrollPage);
    }
    else
    {
        if(callback != null)
        {
            callback(scrollPage.pageNum, scrollPage.count);
        }
    }

    return false;
};