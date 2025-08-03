<%-- 
    Document   : searchTrainer
    Created on : Jun 15, 2025, 5:48:02 PM
    Author     : ACER
--%>

<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.List" %>
<%@ page import="model.Trainer" %>
<%@ page import="dao.TrainerDAO" %>
<%
    String keyword = request.getParameter("keyword");
    List<Trainer> list = null;
    if (keyword != null && !keyword.trim().isEmpty()) {
        TrainerDAO dao = new TrainerDAO();
        list = dao.searchByKeyword(keyword);
        request.setAttribute("trainerList", list);
    }
%>
<html>
    <head><title>Search Trainer</title>
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
        </style></head>
    <body>
        <h2>Search Trainer</h2>
        <form action="searchTrainer" method="get">
            <input type="text" name="keyword" placeholder="Enter name or specialization">
            <input type="submit" value="Search">
        </form>

        <h3>Results:</h3>
        <ul>
            <%
                if (list != null) {
                    for (Trainer t : list) {
            %>
            <li>
                <strong><%= t.getUserName() %></strong> - <%= t.getSpecialization() %> - <%= t.getExperienceYears() %> years
                <br><%= t.getDescription() %>
            </li>
            <%
                    }
                }
            %>
        </ul>

    </body>
</html>


