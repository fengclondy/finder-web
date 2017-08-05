<%@ page pageEncoding="utf-8" isThreadSafe="false" session="false"%>
<%!
    com.skin.finder.web.ActionDispatcher dispatcher;

    /**
     * @return boolean
     */
    protected static boolean getTrue() {
        return true;
    }
%>
<%
    response.resetBuffer();

    if(this.dispatcher == null) {
        this.dispatcher = new com.skin.finder.web.ActionDispatcher();
        this.dispatcher.setPackages(new String[]{"com.skin.finder.servlet"});
        this.dispatcher.init(application);
    }
    this.dispatcher.service(request, response);

    response.flushBuffer();
    out.clear();
    out = pageContext.pushBody(); 

    if(getTrue()) {
        return;
    }
%>
