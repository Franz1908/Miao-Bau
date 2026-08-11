<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.List" %>
<%@ page import="com.example.miaobau.model.CategoryBean" %>
<%@ page import="com.example.miaobau.model.SpeciesBean" %>
<%@ page import="com.example.miaobau.model.CustomerBean" %>
<%
    List<CategoryBean> categories = (List<CategoryBean>) application.getAttribute("categories");
    List<SpeciesBean> species = (List<SpeciesBean>) application.getAttribute("species");
    CustomerBean customerBean = (CustomerBean) session.getAttribute("customer");
%>
<nav>
    <ul>
        <% for (SpeciesBean specie : species) { %>
            <li>
                <a href="${pageContext.request.contextPath}/catalog?speciesId=<%= specie.getSpeciesID() %>">
                    <%= specie.getSpeciesName() %>
                </a>
                <ul>
                    <% for (CategoryBean category : categories) { %>
                        <li>
                            <a href="${pageContext.request.contextPath}/catalog?speciesId=<%= specie.getSpeciesID() %>&categoryId=<%= category.getCategoryID() %>">
                                <%= category.getCategoryName() %>
                            </a>
                        </li>
                    <% } %>
                </ul>
            </li>
        <% } %>
    </ul>
    <a href="${pageContext.request.contextPath}/catalog">Vai al catalogo</a>
    <br>
    <%
        if(customerBean != null) {
    %>
        <a href="${pageContext.request.contextPath}/account">Account</a>
    <%
        } else {
    %>
        <a href="${pageContext.request.contextPath}/account">Accedi</a>
    <%
        }
    %>

</nav>
