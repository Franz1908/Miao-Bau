<%-- =======================================================================
     403.jsp  —  Pagina di errore "Accesso negato"
     Registrala in web.xml:
       <error-page>
         <error-code>403</error-code>
         <location>/view/error/403.jsp</location>
       </error-page>
     Usa il frammento riutilizzabile EmptyState.jsp.
     ======================================================================= --%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="it">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>Accesso negato (403) — Miao &amp; Bau</title>
    <link rel="stylesheet" href="<%= request.getContextPath() %>/bootstrap-5.3.8-dist/css/bootstrap.min.css">
    <link rel="stylesheet" href="<%= request.getContextPath() %>/css/style.css">
</head>
<body>

<%@ include file="../Navbar.jsp" %>

<main class="container my-4">
    <%
        // Parametri dello stato vuoto per la 403
        request.setAttribute("esTitle", "Accesso negato");
        request.setAttribute("esText", "Non hai i permessi per visualizzare questa pagina. "
                + "Se pensi sia un errore, torna al catalogo o accedi con un altro account.");
        request.setAttribute("esImg", request.getContextPath() + "/img/403.png");
        request.setAttribute("esLink", request.getContextPath() + "/catalog");
        request.setAttribute("esCta", "Torna al catalogo");
    %>
    <%@ include file="EmptyState.jsp" %>
</main>

<%@ include file="../Footer.jsp" %>

<script src="<%= request.getContextPath() %>/bootstrap-5.3.8-dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>

