<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.List" %>
<%@ page import="java.time.format.DateTimeFormatter" %>
<%@ page import="com.example.miaobau.model.OrdersBean" %>
<%
    List<OrdersBean> orders = (List<OrdersBean>) request.getAttribute("orders");
    DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
%>
<!DOCTYPE html>
<html lang="it">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>I miei ordini &mdash; Miao &amp; Bau</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/bootstrap-5.3.8-dist/css/bootstrap.min.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>

<%@ include file="Navbar.jsp" %>

<main class="container my-4">
    <h1 class="mb-1">I miei ordini</h1>
    <p class="text-muted mb-4">Lo storico dei tuoi acquisti su Miao &amp; Bau.</p>

    <%
        if (orders == null || orders.isEmpty()) {
            request.setAttribute("esTitle", "Nessun ordine effettuato");
            request.setAttribute("esText", "Non hai ancora effettuato ordini. Scopri il catalogo e fai felice il tuo amico a quattro zampe!");
            request.setAttribute("esImg", request.getContextPath() + "/img/nessun-ordine.png");
            request.setAttribute("esCta", "Vai al catalogo");
            request.setAttribute("esLink", request.getContextPath() + "/catalog");
    %>
    <%@ include file="error/EmptyState.jsp" %>
    <%
    } else {
    %>
    <div class="d-flex flex-column gap-3">
        <%
            for (OrdersBean order : orders) {
        %>
        <div class="mb-order-card">
            <span class="mb-order-badge">Ordine n&deg; <%= order.getOrderID() %></span>
            <div class="mb-order-field">
                <span class="mb-order-flabel">Data</span>
                <span class="mb-order-fvalue"><%= order.getOrderDate() != null ? order.getOrderDate().format(fmt) : "-" %></span>
            </div>
            <div class="mb-order-field">
                <span class="mb-order-flabel">Totale</span>
                <span class="mb-order-total-val">&euro; <%= order.getTotalPrice() %></span>
            </div>
            <a href="${pageContext.request.contextPath}/secure/order-detail?orderId=<%= order.getOrderID() %>"
               class="btn btn-mb-primario ms-md-auto">Vedi dettaglio</a>
        </div>
        <%
            }
        %>
    </div>
    <%
        }
    %>
</main>

<%@ include file="Footer.jsp" %>

<script src="${pageContext.request.contextPath}/bootstrap-5.3.8-dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>
