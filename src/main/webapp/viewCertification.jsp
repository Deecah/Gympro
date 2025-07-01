<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.List" %>
<%@ page import="model.Certification" %>
<%
    List<Certification> certList = (List<Certification>) request.getAttribute("certList");
%>
<!DOCTYPE html>
<html>
<head>
    <title>View Certifications</title>
</head>
<body>
    <h2>All Certifications</h2>
    <table border="1" cellpadding="10">
        <tr>
            <th>ID</th>
            <th>Name</th>
            <th>Description</th>
            <th>Expire Date</th>
            <th>Actions</th>
        </tr>
        <%
            if (certList != null) {
                for (Certification cert : certList) {
        %>
        <tr>
            <td><%= cert.getCertificationID() %></td>
            <td><%= cert.getName() %></td>
            <td><%= cert.getDescription() %></td>
            <td><%= cert.getExpireDate() %></td>
            <td>
                <a href="editCertification?id=<%= cert.getCertificationID() %>">Edit</a>
                <!-- Có thể thêm Delete nếu cần -->
            </td>
        </tr>
        <%
                }
            }
        %>
    </table>
</body>
</html>
