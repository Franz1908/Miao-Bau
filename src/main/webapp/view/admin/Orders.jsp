<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.example.miaobau.model.OrdersBean" %>
<%@ page import="java.util.List" %>
<%@ page import="java.time.format.DateTimeFormatter" %>
<%
    List<OrdersBean> orders = (List<OrdersBean>) request.getAttribute("orders");
    String filterError = (String) request.getAttribute("filterError");
    String fEmail = request.getParameter("email");
    String fFrom = request.getParameter("dateFrom");
    String fTo = request.getParameter("dateTo");
    if (fEmail == null) fEmail = "";
    if (fFrom == null) fFrom = "";
    if (fTo == null) fTo = "";
    DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
%>
<!DOCTYPE html>
<html lang="it">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>Ordini &mdash; Admin Miao &amp; Bau</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/bootstrap-5.3.8-dist/css/bootstrap.min.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>

<!-- Barra amministratore -->
<nav class="navbar mb-navbar shadow-sm">
    <div class="container">
        <a class="navbar-brand mb-brand mb-0" href="${pageContext.request.contextPath}/admin/home">&#128062; Miao &amp; Bau &middot; Admin</a>
        <a href="${pageContext.request.contextPath}/admin/home" class="btn btn-outline-secondary btn-sm">&larr; Pannello</a>
    </div>
</nav>

<main class="container my-4">
    <h1 class="mb-1">Ordini</h1>
    <p class="text-muted mb-4">Consulta e filtra tutti gli ordini dello store.</p>

    <!-- Filtri -->
    <div class="mb-panel mb-4">
        <form class="mb-form" method="get" action="${pageContext.request.contextPath}/admin/orders">
            <div class="row g-3 align-items-end">
                <div class="col-12 col-md-5">
                    <label for="email" class="form-label">Email cliente</label>
                    <input type="text" class="form-control" name="email" id="email" value="<%= fEmail %>" placeholder="nome@esempio.it">
                </div>
                <div class="col-6 col-md-3">
                    <label for="dateFrom" class="form-label">Dal</label>
                    <input type="date" class="form-control" name="dateFrom" id="dateFrom" value="<%= fFrom %>">
                </div>
                <div class="col-6 col-md-3">
                    <label for="dateTo" class="form-label">Al</label>
                    <input type="date" class="form-control" name="dateTo" id="dateTo" value="<%= fTo %>">
                </div>
                <div class="col-12 col-md-1 d-grid">
                    <button type="submit" class="btn btn-mb-cta">Filtra</button>
                </div>
            </div>
        </form>
    </div>

    <% if (filterError != null) { %>
        <div class="mb-alert mb-alert-error"><%= filterError %></div>
    <% } else if (orders == null || orders.isEmpty()) { %>
        <div class="mb-panel"><p class="text-muted mb-0">Nessun ordine trovato.</p></div>
    <% } else { %>
        <div class="mb-panel p-0">
            <div class="table-responsive">
                <table class="table table-hover align-middle mb-0">
                    <thead>
                        <tr>
                            <th scope="col">Ordine</th>
                            <th scope="col">Cliente</th>
                            <th scope="col">Data</th>
                            <th scope="col" class="text-end">Totale</th>
                            <th scope="col" class="text-end">Azioni</th>
                        </tr>
                    </thead>
                    <tbody>
                        <% for (OrdersBean order : orders) { %>
                        <tr>
                            <td><span class="mb-order-badge">n&deg; <%= order.getOrderID() %></span></td>
                            <td>
                                <span class="fw-semibold d-block"><%= order.getCustomerFirstName() %> <%= order.getCustomerLastName() %></span>
                                <span class="text-muted small"><%= order.getCustomerEmail() %></span>
                            </td>
                            <td><%= order.getOrderDate() != null ? order.getOrderDate().format(fmt) : "-" %></td>
                            <td class="text-end fw-semibold">&euro; <%= order.getTotalPrice() %></td>
                            <td class="text-end">
                                <a href="${pageContext.request.contextPath}/admin/order-detail?orderId=<%= order.getOrderID() %>"
                                   class="btn btn-sm btn-outline-secondary">Dettaglio</a>
                            </td>
                        </tr>
                        <% } %>
                    </tbody>
                </table>
            </div>
        </div>
    <% } %>
</main>

<script src="${pageContext.request.contextPath}/bootstrap-5.3.8-dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>
