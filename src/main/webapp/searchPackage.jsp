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
    }
%>

<!DOCTYPE html>
<html>
    <head>
        <meta charset="UTF-8">
        <title>Search Package</title>
        <style>
            body {
                font-family: Arial, sans-serif;
                padding: 20px;
            }
            form {
                margin-bottom: 20px;
            }
            input[type="text"] {
                padding: 5px;
                width: 300px;
            }
            input[type="submit"] {
                padding: 5px 10px;
            }
            ul {
                list-style: none;
                padding-left: 0;
            }
            li {
                margin-bottom: 15px;
                border-bottom: 1px solid #ccc;
                padding-bottom: 10px;
            }
        </style>
    </head>
    <body>

        <h2>Search Training Packages</h2>

        <form action="searchPackage" method="get">
            <input type="text" name="keyword" placeholder="Enter name or description" value="<%= keyword != null ? keyword : "" %>">
            <input type="submit" value="Search">
        </form>

        <h3>Results:</h3>
        <ul>
            <%
                if (list != null && !list.isEmpty()) {
                    for (Package p : list) {
            %>
            <li>
                <strong><%= p.getName() %></strong> - $<%= p.getPrice() %> - <%= p.getDuration() %> months<br>
                <em><%= p.getDescription() %></em>
            </li>
            <%
                    }
                } else if (keyword != null) {
            %>
            <li>No packages found matching "<%= keyword %>"</li>
                <%
                    }
                %>
        </ul>

    </body>
</html>
