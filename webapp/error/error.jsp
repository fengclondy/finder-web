<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" session="false" isErrorPage="true"%>
<%@ page import="com.skin.ayada.template.TemplateManager"%>
<%@ page import="com.skin.ayada.template.TemplateContext"%>
<%@ page import="com.skin.ayada.web.TemplateDispatcher"%>
<%!
    private static org.slf4j.Logger logger = null;

    public void jspInit(){
        logger = org.slf4j.LoggerFactory.getLogger(this.getClass());
    }
%>
<%
    int status = response.getStatus();
    request.setAttribute("exception", exception);
    request.setAttribute("template_writer", out);
    request.setAttribute("TemplateFilter$servletContext", application);

    if(logger.isDebugEnabled()) {
        if(exception != null) {
            logger.debug(exception.getMessage(), exception);
        }
        else {
            logger.debug(status + " - exception: null");
        }
    }
    // String home = application.getRealPath("/template");
    // TemplateContext templateContext = TemplateManager.getTemplateContext(home, false);
    // TemplateDispatcher.dispatch(templateContext, request, response, "/error/" + status + ".jsp");
%>
