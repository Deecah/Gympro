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
<head><title>Search Trainer</title></head>
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


