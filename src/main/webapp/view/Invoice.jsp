<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.math.BigDecimal" %>
<%@ page import="java.math.RoundingMode" %>
<%@ page import="java.time.format.DateTimeFormatter" %>
<%@ page import="com.example.miaobau.model.OrdersBean" %>
<%@ page import="com.example.miaobau.model.OrderItemBean" %>
<%@ page import="com.example.miaobau.model.AddressBean" %>
<%
    OrdersBean order = (OrdersBean) request.getAttribute("order");
    AddressBean address = (AddressBean) request.getAttribute("address");
    DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    // Totali imponibile/IVA calcolati per riga (scorporo dal prezzo ivato)
    BigDecimal totNet = BigDecimal.ZERO;
    BigDecimal totVat = BigDecimal.ZERO;
    BigDecimal totGross = BigDecimal.ZERO;
%>
<!DOCTYPE html>
<html lang="it">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>Fattura ordine n&deg; <%= order.getOrderID() %> &mdash; Miao &amp; Bau</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/bootstrap-5.3.8-dist/css/bootstrap.min.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>

<!-- Barra azioni: non viene stampata -->
<div class="container my-4 no-print">
    <div class="d-flex flex-wrap justify-content-between align-items-center gap-2">
        <a href="${pageContext.request.contextPath}/secure/order-detail?orderId=<%= order.getOrderID() %>"
           class="text-decoration-none">&larr; Torna al dettaglio ordine</a>
        <button type="button" class="btn btn-mb-cta" onclick="window.print()">&#128424; Stampa / Salva PDF</button>
    </div>
</div>

<main class="container mb-5">
    <div class="mb-invoice">

        <!-- Intestazione: venditore + dati documento -->
        <div class="mb-invoice-head">
            <div class="mb-invoice-seller">
                <strong>&#128062; Miao &amp; Bau</strong>
                <p class="mb-0 mt-1">Via degli Animali, 1 &middot; 84121 Salerno (SA)</p>
                <p class="mb-0">P. IVA 01234567890</p>
                <p class="mb-0">info@miaoebau.it</p>
            </div>
            <div class="mb-invoice-meta">
                <div class="mb-invoice-doc">FATTURA</div>
                <p class="mb-0 mt-1">N&deg; <strong><%= order.getOrderID() %></strong></p>
                <p class="mb-0">Data: <%= order.getOrderDate() != null ? order.getOrderDate().format(fmt) : "-" %></p>
            </div>
        </div>

        <!-- Intestatario + spedizione -->
        <div class="mb-invoice-parties">
            <div>
                <div class="mb-invoice-block-label">Intestata a</div>
                <p class="mb-0 fw-semibold"><%= order.getCustomerFirstName() %> <%= order.getCustomerLastName() %></p>
                <p class="mb-0"><%= order.getCustomerEmail() %></p>
                <% if (order.getCustomerPhone() != null) { %>
                <p class="mb-0">Tel. <%= order.getCustomerPhone() %></p>
                <% } %>
            </div>
            <div>
                <div class="mb-invoice-block-label">Indirizzo di spedizione</div>
                <% if (address != null) { %>
                <p class="mb-0"><%= address.getStreet() %>, <%= address.getCivicNumber() %></p>
                <p class="mb-0"><%= address.getPostalCode() %> <%= address.getCity() %> (<%= address.getCountry() %>)</p>
                <% } else { %>
                <p class="mb-0 text-muted">Non disponibile</p>
                <% } %>
            </div>
        </div>

        <!-- Righe -->
        <table class="mb-invoice-table">
            <thead>
            <tr>
                <th>Descrizione</th>
                <th class="mb-num">Q.t&agrave;</th>
                <th class="mb-num">Prezzo unit.</th>
                <th class="mb-num">IVA</th>
                <th class="mb-num">Totale</th>
            </tr>
            </thead>
            <tbody>
            <%
                for (OrderItemBean item : order.getItems()) {
                    BigDecimal unit = item.getUnitPrice() != null ? item.getUnitPrice() : BigDecimal.ZERO;
                    BigDecimal gross = unit.multiply(BigDecimal.valueOf(item.getQuantity()));
                    BigDecimal vatRate = item.getVatFrozen() != null ? item.getVatFrozen() : BigDecimal.ZERO;
                    BigDecimal divisor = BigDecimal.ONE.add(vatRate.divide(new BigDecimal("100")));
                    BigDecimal net = gross.divide(divisor, 2, RoundingMode.HALF_UP);
                    BigDecimal vatAmt = gross.subtract(net);
                    totNet = totNet.add(net);
                    totVat = totVat.add(vatAmt);
                    totGross = totGross.add(gross);
            %>
            <tr>
                <td><%= item.getProductName() %></td>
                <td class="mb-num"><%= item.getQuantity() %></td>
                <td class="mb-num">&euro; <%= unit %></td>
                <td class="mb-num"><%= vatRate %>%</td>
                <td class="mb-num">&euro; <%= gross.setScale(2, RoundingMode.HALF_UP) %></td>
            </tr>
            <%
                }
            %>
            </tbody>
        </table>

        <!-- Riepilogo importi -->
        <div class="mb-invoice-totals">
            <div class="row-line"><span>Imponibile</span><span>&euro; <%= totNet.setScale(2, RoundingMode.HALF_UP) %></span></div>
            <div class="row-line"><span>IVA</span><span>&euro; <%= totVat.setScale(2, RoundingMode.HALF_UP) %></span></div>
            <div class="row-line grand"><span>Totale</span><span>&euro; <%= totGross.setScale(2, RoundingMode.HALF_UP) %></span></div>
        </div>

        <div class="mb-invoice-foot">
            Documento generato automaticamente da Miao &amp; Bau &middot; Grazie per il tuo acquisto!
        </div>
    </div>
</main>

<script src="${pageContext.request.contextPath}/bootstrap-5.3.8-dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>

