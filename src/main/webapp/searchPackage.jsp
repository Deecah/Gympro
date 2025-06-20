<%-- 
    Document   : searchPackage
    Created on : Jun 15, 2025, 5:49:52 PM
    Author     : ACER
--%>

<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.List" %>
<%@ page import="model.Package" %>
<%@ page import="dao.PackageDAO" %>
<%
    String keyword = request.getParameter("keyword");
    List<Package> list = null;
    if (keyword != null && !keyword.trim().isEmpty()) {
        PackageDAO dao = new PackageDAO();
        list = dao.searchByKeyword(keyword);
        request.setAttribute("packageList", list); // nếu bạn dùng nó trong trang khác
    }
%>
<html>
<head><title>Search Package</title></head>
<body>
    <h2>Search Trainer's Package</h2>
    <form action="searchPackage" method="get">
        <input type="text" name="keyword" placeholder="Enter name or description">
        <input type="submit" value="Search">
    </form>

    <h3>Results:</h3>
    <ul>
        <%
            if (list != null) {
                for (Package p : list) {
        %>
        <li>
            <strong><%= p.getName() %></strong> - $<%= p.getPrice() %> - <%= p.getDuration() %> months
            <br><%= p.getDescription() %>
        </li>
        <%
                }
            }
        %>
    </ul>
</body>
</html>


