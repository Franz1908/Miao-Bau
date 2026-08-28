<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.example.miaobau.model.CustomerBean" %>
<%@ page import="com.example.miaobau.model.AddressBean" %>
<%@ page import="java.util.List" %>
<%
    CustomerBean customerBean = (CustomerBean) session.getAttribute("customer");
    List<AddressBean> addresses = (List<AddressBean>) request.getAttribute("addresses");
%>
<!DOCTYPE html>
<html lang="it">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>Il mio account &mdash; Miao &amp; Bau</title>

    <!-- Bootstrap 5 (locale) + foglio di stile del brand -->
    <link rel="stylesheet" href="${pageContext.request.contextPath}/bootstrap-5.3.8-dist/css/bootstrap.min.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>

<!-- ================= NAVBAR (frammento) ================= -->
<%@ include file="Navbar.jsp" %>

<!-- ================= CONTENUTO ================= -->
<main class="container my-4">
<% if (customerBean != null) { %>

    <!-- Intestazione: saluto + logout -->
    <div class="mb-account-hero">
        <div>
            <h1>Ciao <%= customerBean.getFirstName() %>!</h1>
            <p>Gestisci i tuoi dati, i tuoi indirizzi e i tuoi ordini.</p>
        </div>
        <form method="post" action="${pageContext.request.contextPath}/logout" class="m-0">
            <button type="submit" class="btn btn-outline-secondary">Esci</button>
        </form>
    </div>

    <div class="row g-4">

        <!-- ---- I miei dati ---- -->
        <div class="col-12">
            <div class="mb-panel">
                <div class="mb-panel-head">
                    <h2 class="mb-panel-title">I miei dati</h2>
                    <a href="${pageContext.request.contextPath}/secure/account/edit"
                       class="btn btn-mb-primario btn-sm">Modifica dati</a>
                </div>
                <div class="mb-data-grid">
                    <div class="mb-data-item">
                        <span class="mb-data-label">Nome</span>
                        <span class="mb-data-value"><%= customerBean.getFirstName() %></span>
                    </div>
                    <div class="mb-data-item">
                        <span class="mb-data-label">Cognome</span>
                        <span class="mb-data-value"><%= customerBean.getLastName() %></span>
                    </div>
                    <div class="mb-data-item">
                        <span class="mb-data-label">E-mail</span>
                        <span class="mb-data-value"><%= customerBean.getEmail() %></span>
                    </div>
                    <div class="mb-data-item">
                        <span class="mb-data-label">Data di nascita</span>
                        <% if (customerBean.getBirthDate() != null) { %>
                            <span class="mb-data-value"><%= customerBean.getBirthDate() %></span>
                        <% } else { %>
                            <span class="mb-data-value mb-data-empty">Non impostata</span>
                        <% } %>
                    </div>
                    <div class="mb-data-item">
                        <span class="mb-data-label">Telefono</span>
                        <% if (customerBean.getPhone() != null) { %>
                            <span class="mb-data-value"><%= customerBean.getPhone() %></span>
                        <% } else { %>
                            <span class="mb-data-value mb-data-empty">Non impostato</span>
                        <% } %>
                    </div>
                </div>
            </div>
        </div>

        <!-- ---- I miei indirizzi ---- -->
        <div class="col-12">
            <div class="mb-panel">
                <div class="mb-panel-head">
                    <h2 class="mb-panel-title">I miei indirizzi</h2>
                </div>
                <div class="row row-cols-1 row-cols-md-2 g-3">
                    <%
                        if (addresses != null && !addresses.isEmpty()) {
                            for (AddressBean address : addresses) {
                    %>
                    <div class="col">
                        <div class="mb-address">
                            <div>
                                <p class="mb-address-street"><%= address.getStreet() %>, <%= address.getCivicNumber() %></p>
                                <p class="mb-address-city">
                                    <%= address.getPostalCode() %> <%= address.getCity() %> (<%= address.getCountry() %>)
                                </p>
                            </div>
                            <form method="post" action="${pageContext.request.contextPath}/secure/address/delete" class="m-0">
                                <input type="hidden" name="addressId" value="<%= address.getAddressID() %>">
                                <button type="submit" class="mb-icon-btn" title="Elimina indirizzo" aria-label="Elimina indirizzo">&times;</button>
                            </form>
                        </div>
                    </div>
                    <%
                            }
                        }
                    %>
                    <!-- Riquadro "aggiungi indirizzo" -->
                    <div class="col">
                        <a href="${pageContext.request.contextPath}/secure/address/new" class="mb-address-add">
                            <span>&#43;</span> Aggiungi indirizzo
                        </a>
                    </div>
                </div>
                <% if (addresses == null || addresses.isEmpty()) { %>
                    <p class="text-muted small mb-0 mt-3">Non hai ancora indirizzi salvati.</p>
                <% } %>
            </div>
        </div>

        <!-- ---- Sezioni / link rapidi ---- -->
        <div class="col-12">
            <div class="mb-panel">
                <div class="mb-panel-head">
                    <h2 class="mb-panel-title">Sezioni</h2>
                </div>
                <div class="row row-cols-1 row-cols-md-2 g-3">
                    <div class="col">
                        <a href="${pageContext.request.contextPath}/cart" class="mb-quicklink">
                            <span class="mb-quicklink-ico">&#128722;</span>
                            <span>Il mio carrello</span>
                            <span class="mb-quicklink-arrow">&rarr;</span>
                        </a>
                    </div>
                    <div class="col">
                        <a href="${pageContext.request.contextPath}/secure/orders" class="mb-quicklink">
                            <span class="mb-quicklink-ico">&#128230;</span>
                            <span>I miei ordini</span>
                            <span class="mb-quicklink-arrow">&rarr;</span>
                        </a>
                    </div>
                </div>
            </div>
        </div>

    </div>

<% } else { %>

    <!-- Utente non autenticato -->
    <section class="mb-auth">
        <span class="mb-paw mb-paw-1">&#128062;</span>
        <span class="mb-paw mb-paw-4">&#128062;</span>
        <div class="mb-auth-card">
            <div class="mb-auth-head">
                <span class="mb-auth-emoji">&#128062;</span>
                <h1 class="mb-auth-title h3">Area riservata</h1>
                <p class="mb-auth-sub">Accedi o registrati per gestire i tuoi dati, indirizzi e ordini.</p>
            </div>
            <div class="d-grid gap-2">
                <a class="btn btn-mb-cta py-2" href="${pageContext.request.contextPath}/login">Accedi</a>
                <a class="btn btn-mb-primario py-2" href="${pageContext.request.contextPath}/register">Registrati</a>
            </div>
        </div>
    </section>

<% } %>
</main>

<!-- ================= FOOTER (frammento) ================= -->
<%@ include file="Footer.jsp" %>

<!-- Bootstrap JS (locale) per navbar responsive e menu a tendina -->
<script src="${pageContext.request.contextPath}/bootstrap-5.3.8-dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>
