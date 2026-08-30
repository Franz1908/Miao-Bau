<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.example.miaobau.model.ProductBean" %>
<%@ page import="java.util.List" %>
<%
    List<ProductBean> products = (List<ProductBean>) request.getAttribute("adminProducts");
%>
<!DOCTYPE html>
<html lang="it">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>Gestione prodotti &mdash; Admin Miao &amp; Bau</title>
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
    <div class="d-flex justify-content-between align-items-center flex-wrap gap-2 mb-4">
        <div>
            <h1 class="mb-0">Gestione prodotti</h1>
            <p class="text-muted mb-0">Tutti i prodotti, inclusi quelli disattivati.</p>
        </div>
        <a href="${pageContext.request.contextPath}/admin/product/insert" class="btn btn-mb-cta">&#10133; Aggiungi prodotto</a>
    </div>

    <% if (products == null || products.isEmpty()) { %>
        <div class="mb-panel"><p class="text-muted mb-0">Nessun prodotto disponibile.</p></div>
    <% } else { %>
        <div class="mb-panel p-0">
            <div class="table-responsive">
                <table class="table table-hover align-middle mb-0">
                    <thead>
                        <tr>
                            <th scope="col">Immagine</th>
                            <th scope="col">Nome</th>
                            <th scope="col">Marca</th>
                            <th scope="col" class="text-end">Prezzo</th>
                            <th scope="col">Stato</th>
                            <th scope="col" class="text-end">Azioni</th>
                        </tr>
                    </thead>
                    <tbody>
                        <% for (ProductBean product : products) {
                               String pimg = (product.getImage() == null) ? "" : product.getImage(); %>
                        <tr<%= product.isDeleted() ? " class=\"opacity-50\"" : "" %>>
                            <td>
                                <div class="mb-img-box<%= pimg.isEmpty() ? " mb-img-vuota" : "" %>"
                                     style="width:56px;height:56px;border-radius:.5rem;">
                                    <% if (!pimg.isEmpty()) { %>
                                    <img src="${pageContext.request.contextPath}/img/products/<%= pimg %>"
                                         class="mb-card-img" alt="<%= product.getName() %>"
                                         onerror="this.onerror=null; this.style.display='none'; this.parentNode.classList.add('mb-img-vuota');">
                                    <% } %>
                                </div>
                            </td>
                            <td class="fw-semibold"><%= product.getName() %></td>
                            <td class="text-muted"><%= product.getBrand() %></td>
                            <td class="text-end">&euro; <%= product.getPrice() %></td>
                            <td>
                                <% if (product.isDeleted()) { %>
                                    <span class="badge text-bg-secondary">Inattivo</span>
                                <% } else { %>
                                    <span class="badge mb-badge mb-badge-bio">Attivo</span>
                                <% } %>
                            </td>
                            <td>
                                <div class="d-flex gap-2 justify-content-end">
                                    <a href="${pageContext.request.contextPath}/admin/product/update?productId=<%= product.getProductID() %>"
                                       class="btn btn-sm btn-outline-secondary">Modifica</a>
                                    <% if (product.isDeleted()) { %>
                                        <form method="post" action="${pageContext.request.contextPath}/admin/product/restore" class="m-0">
                                            <input type="hidden" name="productId" value="<%= product.getProductID() %>">
                                            <button type="submit" class="btn btn-sm btn-mb-primario">Ripristina</button>
                                        </form>
                                    <% } else { %>
                                        <form method="post" action="${pageContext.request.contextPath}/admin/product/delete" class="m-0"
                                              onsubmit="return confirm('Disattivare questo prodotto?');">
                                            <input type="hidden" name="productId" value="<%= product.getProductID() %>">
                                            <button type="submit" class="btn btn-sm btn-outline-danger">Elimina</button>
                                        </form>
                                    <% } %>
                                </div>
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
