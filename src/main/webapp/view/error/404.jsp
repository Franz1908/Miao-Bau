<%-- =======================================================================
     404.jsp  —  Pagina di errore "Errore del server"
     Usa il frammento riutilizzabile EmptyState.jsp.
     ======================================================================= --%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="it">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>Pagina non trovata (404) — Miao &amp; Bau</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/bootstrap-5.3.8-dist/css/bootstrap.min.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>

    <%@ include file="../Navbar.jsp" %>

    <main class="container my-4">
        <%
            // Parametri dello stato vuoto per la 404
            request.setAttribute("esTitle", "Oops! Pagina non trovata");
            request.setAttribute("esText", "Sembra che la pagina che cercavi sia andata dispersa. "
                    + "Non preoccuparti, i tuoi acquisti sono al sicuro.");
            request.setAttribute("esImg", request.getContextPath() + "/img/404.png");
            request.setAttribute("esLink", request.getContextPath() + "/catalog");
            request.setAttribute("esCta", "Torna al catalogo");
        %>
        <%@ include file="EmptyState.jsp" %>
    </main>

    <%@ include file="../Footer.jsp" %>

    <script src="${pageContext.request.contextPath}/src/main/webapp/bootstrap-5.3.8-dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>
