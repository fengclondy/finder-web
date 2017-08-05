<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" session="false" isErrorPage="true"%>
<%!
    private static org.slf4j.Logger logger = null;

    public void jspInit(){
        logger = org.slf4j.LoggerFactory.getLogger(this.getClass());
    }
%>
<%
    int status = response.getStatus();
    String requestURI = request.getRequestURI();
    request.setAttribute("exception", exception);
    logger.error("{} - {}", status, requestURI);

    if(exception != null) {
        logger.error("requestURI: {}", requestURI);
        logger.error(exception.getMessage(), exception);
    }
    else {
        logger.error("{} - {} exception: null", status, requestURI);
    }
%>
