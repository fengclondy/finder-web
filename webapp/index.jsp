<%
    String contextPath = request.getContextPath();

    if(contextPath == null || contextPath.equals("/")) {
        contextPath = "";
    }
    pageContext.setAttribute("contextPath", contextPath);
%>

<script type="text/javascript">
window.top.location.href = "${contextPath}/finder/index.html";
</script>
