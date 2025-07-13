<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="model.Certification" %>
<%@ page import="java.time.format.DateTimeFormatter" %>
<%
    Certification cert = (Certification) request.getAttribute("cert");
    // Định dạng LocalDateTime theo kiểu datetime-local
    String formattedDateTime = cert != null && cert.getExpireDate() != null
        ? cert.getExpireDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm"))
        : "";
%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Edit Certification</title>
    <style>
        body {
            font-family: Arial, sans-serif;
            max-width: 600px;
            margin: 40px auto;
            padding: 20px;
            border: 1px solid #ccc;
            border-radius: 8px;
        }
        label {
            display: block;
            margin-top: 15px;
        }
        input[type="text"], textarea, input[type="datetime-local"] {
            width: 100%;
            padding: 8px;
            margin-top: 5px;
        }
        input[type="submit"] {
            margin-top: 20px;
            padding: 10px 20px;
            cursor: pointer;
        }
        a {
            display: inline-block;
            margin-top: 20px;
            text-decoration: none;
        }
    </style>
</head>
<body>
    <h2>Edit Certification</h2>

    <form action="EditCertificationServlet" method="post">
        <input type="hidden" name="certificationID" value="<%= cert.getCertificationID() %>">

        <label for="name">Name:</label>
        <input type="text" name="name" id="name" value="<%= cert.getName() %>" required>

        <label for="description">Description:</label>
        <textarea name="description" id="description" rows="4" required><%= cert.getDescription() %></textarea>

        <label for="expireDate">Expire Date:</label>
        <input type="datetime-local" name="expireDate" id="expireDate" value="<%= formattedDateTime %>" required>

        <input type="submit" value="Update Certification">
    </form>

    <a href="ViewCertificationServlet">← Back to list</a>
</body>
</html>
