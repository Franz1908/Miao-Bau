<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.List" %>
<%@ page import="com.example.miaobau.model.CategoryBean" %>
<%@ page import="com.example.miaobau.model.SpeciesBean" %>
<%
    List<CategoryBean> categories = (List<CategoryBean>) application.getAttribute("categories");
    List<SpeciesBean> species = (List<SpeciesBean>) application.getAttribute("species");
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
    <a href="${pageContext.request.contextPath}/account">Account</a>
</nav>
