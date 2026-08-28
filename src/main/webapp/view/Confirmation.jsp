<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    String orderId = (String) request.getAttribute("orderId");
    if (orderId == null) orderId = request.getParameter("orderId");
%>
<!DOCTYPE html>
<html lang="it">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>Ordine confermato &mdash; Miao &amp; Bau</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/bootstrap-5.3.8-dist/css/bootstrap.min.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>

<%@ include file="Navbar.jsp" %>

<main class="container my-4">
    <section class="mb-empty">
        <span class="mb-paw mb-paw-1">&#128062;</span>
        <span class="mb-paw mb-paw-2">&#128062;</span>
        <span class="mb-paw mb-paw-3">&#128062;</span>
        <span class="mb-paw mb-paw-4">&#128062;</span>

        <div class="mb-empty-card">
            <span class="mb-success-badge">&#10003;</span>
            <h1 class="mb-empty-title">Ordine confermato!</h1>
            <p class="mb-empty-text">
                Grazie per aver acquistato da Miao &amp; Bau. Abbiamo ricevuto il tuo ordine
                e lo stiamo preparando con cura.
            </p>

            <p class="mb-4">
                <span class="mb-order-badge">Ordine n&deg; <%= orderId %></span>
            </p>

            <div class="d-flex flex-wrap justify-content-center gap-2">
                <a href="${pageContext.request.contextPath}/secure/orders" class="btn btn-mb-cta px-4 py-2">I miei ordini</a>
                <a href="${pageContext.request.contextPath}/catalog" class="btn btn-mb-primario px-4 py-2">Torna al catalogo</a>
            </div>
        </div>
    </section>
</main>

<%@ include file="Footer.jsp" %>

<script src="${pageContext.request.contextPath}/bootstrap-5.3.8-dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>
