<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.List" %>
<%@ page import="com.example.miaobau.model.CategoryBean" %>
<%@ page import="com.example.miaobau.model.SpeciesBean" %>
<%@ page import="com.example.miaobau.model.CustomerBean" %>
<%@ page import="com.example.miaobau.model.CartBean" %>
<%
    // Dati caricati dalla Servlet in scope "application" / "session"
    List<CategoryBean> categories = (List<CategoryBean>) application.getAttribute("categories");
    List<SpeciesBean>  species    = (List<SpeciesBean>)  application.getAttribute("species");
    CustomerBean navCustomerBean = (CustomerBean) session.getAttribute("customer");
    // Numero di articoli nel carrello per il badge (0 se non impostato)
    CartBean navCart = (CartBean) session.getAttribute("cart");
    int cartCount = (navCart != null) ? navCart.getTotalQuantity() : 0;
%>
<nav class="navbar navbar-expand-lg mb-navbar shadow-sm">
    <div class="container">
        <!-- Logo / nome del brand -->
        <a class="navbar-brand mb-brand" href="${pageContext.request.contextPath}/home">🐾 Miao &amp; Bau</a>
        <!-- Bottone hamburger per il menu su mobile -->
        <button class="navbar-toggler" type="button" data-bs-toggle="collapse"
                data-bs-target="#menuPrincipale" aria-controls="menuPrincipale"
                aria-expanded="false" aria-label="Apri menu">
            <span class="navbar-toggler-icon"></span>
        </button>
        <div class="collapse navbar-collapse" id="menuPrincipale">
            <!-- Link categorie: una voce per specie con menu a tendina delle categorie -->
            <ul class="navbar-nav me-auto mb-2 mb-lg-0">
                <% for (SpeciesBean specie : species) { %>
                    <li class="nav-item dropdown">
                        <a class="nav-link dropdown-toggle" href="#" role="button"
                           data-bs-toggle="dropdown" aria-expanded="false">
                            <%= specie.getSpeciesName() %>
                        </a>
                        <ul class="dropdown-menu">
                            <% for (CategoryBean category : categories) { %>
                                <li>
                                    <a class="dropdown-item"
                                       href="${pageContext.request.contextPath}/catalog?speciesId=<%= specie.getSpeciesID() %>&amp;categoryId=<%= category.getCategoryID() %>">
                                        <%= category.getCategoryName() %>
                                    </a>
                                </li>
                            <% } %>
                        </ul>
                    </li>
                <% } %>
                <li class="nav-item">
                    <a class="nav-link fw-bold" style="color: var(--mb-cta);"
                       href="${pageContext.request.contextPath}/catalog?filter=sale">
                        &#127991; Offerte
                    </a>
                </li>
            </ul>
            <!-- Barra di ricerca semplice -->
            <form class="d-flex me-lg-3 my-2 my-lg-0" role="search"
                  method="get" action="${pageContext.request.contextPath}/catalog">
                <input class="form-control me-2" type="search" name="q"
                       placeholder="Cerca un prodotto..." aria-label="Cerca">
                <button class="btn btn-mb-primario" type="submit">Cerca</button>
            </form>
            <!-- Area account + carrello -->
            <ul class="navbar-nav align-items-lg-center">
                <li class="nav-item">
                    <% if (navCustomerBean != null) { %>
                        <a class="nav-link" href="${pageContext.request.contextPath}/account">Account</a>
                    <% } else { %>
                        <a class="nav-link" href="${pageContext.request.contextPath}/account">Accedi</a>
                    <% } %>
                </li>
                <li class="nav-item">
                    <!-- Icona/bottone carrello con badge numerico -->
                    <a class="nav-link position-relative" href="${pageContext.request.contextPath}/cart">
                        🛒 Carrello
                        <span class="position-absolute top-0 start-100 translate-middle badge rounded-pill mb-cart-badge">
                            <%= cartCount %>
                        </span>
                    </a>
                </li>
            </ul>
        </div>
    </div>
</nav>