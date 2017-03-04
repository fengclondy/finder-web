<%@tag description="Hello" pageEncoding="UTF-8"%>
<%@attribute name="message"%>

<%!
    public void test1() {
        this.message = "[hello]";
    }
%>

<%
    this.test1();
    this.setMessage("[hello]");
%>
<h2 style="color:red">${message}</h2>
<h2>${myvar}</h2>