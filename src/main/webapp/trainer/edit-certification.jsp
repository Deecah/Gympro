<%@ page contentType="text/html" pageEncoding="UTF-8"%>
<%@ page import="model.Certification" %>
<%
    Certification cert = (Certification) request.getAttribute("certification");
    if (cert == null) {
        response.sendRedirect("trainer.jsp"); // fallback
        return;
    }
%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Edit Certification</title>
    <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/css/bootstrap.min.css" />
    <link rel="stylesheet" href="<%= request.getContextPath() %>/stylecss/profile-trainer.css" />
    
    <script>
        // Set current user ID for notification.js
        <% if (session.getAttribute("user") != null) { %>
        var currentUserId = <%= ((model.User)session.getAttribute("user")).getUserId() %>;
        <% } else { %>
        var currentUserId = null;
        <% } %>
    </script>
    <script src="../js/notification.js"></script>
</head>
<body>
<div class="profile-card mt-5">
    <div class="card shadow-sm profile-wrapper">
        <div class="row no-gutters w-100">
            <div class="col-md-12 right-section">
                <h3 class="role-header">Edit Certification</h3>
                <form action="../UpdateCertificationServlet" method="post">
                    <input type="hidden" name="id" value="<%= cert.getId() %>"/>
                    <div class="form-group">
                        <label>Name:</label>
                        <input type="text" name="name" value="<%= cert.getName() %>" class="form-control" required />
                    </div>
                    <div class="form-group">
                        <label>Description:</label>
                        <textarea name="description" class="form-control" required><%= cert.getDescription() %></textarea>
                    </div>
                    <div class="form-group">
                        <label>Expire Date:</label>
                        <input type="date" name="expireDate" value="<%= new java.text.SimpleDateFormat("yyyy-MM-dd").format(cert.getExpireDate()) %>" class="form-control" required />
                    </div>
                    <button type="submit" class="btn btn-brown">Save Certification</button>
                    <a href="profile-trainer.jsp" class="btn btn-secondary ml-2">Cancel</a>
                </form>
            </div>
        </div>
    </div>
</div>
</body>
</html>
