<%-- =======================================================================
     EmptyState.jsp  —  FRAGMENT riutilizzabile "stato vuoto"
     Mostra una card centrata con illustrazione/icona, titolo, testo e un
     bottone opzionale. Va incluso dentro il <main> di una pagina che ha
     gia' caricato bootstrap.min.css e style.css.

     Come si usa: PRIMA dell'include imposta gli attributi di request:
       <% request.setAttribute("esTitle","Il tuo carrello è vuoto");
          request.setAttribute("esText","Aggiungi qualche prodotto.");
          request.setAttribute("esIcon","🛒");                 // opzionale
          request.setAttribute("esImg", ctx+"/img/vuoto.png"); // opzionale (ha priorita' sull'icona)
          request.setAttribute("esLink", ctx+"/catalog");      // opzionale (url bottone)
          request.setAttribute("esCta","Vai al catalogo"); %>  // opzionale (testo bottone)
       <%@ include file="EmptyState.jsp" %>

     Tutti i parametri sono opzionali: se non impostati usa valori di default.
     ======================================================================= --%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    // Lettura parametri con valori di default
    String esTitle = (request.getAttribute("esTitle") != null) ? (String) request.getAttribute("esTitle") : "Nessun elemento";
    String esText  = (request.getAttribute("esText")  != null) ? (String) request.getAttribute("esText")  : "";
    String esIcon  = (request.getAttribute("esIcon")  != null) ? (String) request.getAttribute("esIcon")  : "🐾";
    String esImg   = (String) request.getAttribute("esImg");   // se presente, vince sull'icona
    String esLink  = (String) request.getAttribute("esLink");  // url del bottone
    String esCta   = (String) request.getAttribute("esCta");   // testo del bottone
%>
<section class="mb-empty">

    <!-- Zampette decorative di sfondo -->
    <span class="mb-paw mb-paw-1">🐾</span>
    <span class="mb-paw mb-paw-2">🐾</span>
    <span class="mb-paw mb-paw-3">🐾</span>
    <span class="mb-paw mb-paw-4">🐾</span>

    <!-- Card centrale -->
    <div class="mb-empty-card">
        <h1 class="mb-empty-title"><%= esTitle %></h1>

        <% if (esText != null && !esText.isEmpty()) { %>
        <p class="mb-empty-text"><%= esText %></p>
        <% } %>

        <!-- Illustrazione (immagine) oppure icona emoji di ripiego -->
        <% if (esImg != null && !esImg.isEmpty()) { %>
        <img src="<%= esImg %>" alt="<%= esTitle %>" class="mb-empty-img">
        <% } else { %>
        <div class="mb-empty-icon"><%= esIcon %></div>
        <% } %>

        <!-- Bottone opzionale (mostrato solo se ci sono link e testo) -->
        <% if (esLink != null && !esLink.isEmpty() && esCta != null && !esCta.isEmpty()) { %>
        <a href="<%= esLink %>" class="btn btn-mb-cta btn-lg px-4"><%= esCta %></a>
        <% } %>
    </div>
</section>

