<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="model.Certification" %>
<%@ page import="java.time.format.DateTimeFormatter" %>
<%
    Certification cert = (Certification) request.getAttribute("cert");
    if (cert == null) {
%>
        <p style="color: red;">Error: Certification data not available.</p>
        <a href="viewCertification.jsp">Back to Certifications</a>
<%
        return;
    }
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");
    String expireDateFormatted = cert.getExpireDate().format(formatter);
%>

<!DOCTYPE html>
<html>
<head>
    <title>Edit Certification</title>
</head>
<body>
    <h2>Edit Certification</h2>
    <form action="editCertification" method="post">
        <input type="hidden" name="certificationID" value="<%= cert.getCertificationID() %>">

        <label>Name:</label><br>
        <input type="text" name="name" value="<%= cert.getName() %>" required><br><br>

        <label>Description:</label><br>
        <textarea name="description" rows="4" cols="50"><%= cert.getDescription() %></textarea><br><br>

        <label>Expire Date:</label><br>
        <input type="datetime-local" name="expireDate"
               value="<%= expireDateFormatted %>" required><br><br>

        <input type="submit" value="Update Certification">
    </form>
</body>
</html>
