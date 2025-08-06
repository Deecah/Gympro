<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
    <head>
        <title>Create Package</title>
        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
        <style>
            body {
                margin: 0;
            }
            .sidebar {
                width: 240px;
                min-height: 100vh;
            }
        </style>
        
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
        <div class="d-flex">
            <!-- Sidebar -->
            <div class="sidebar bg-dark text-white">
                <jsp:include page="sidebar.jsp"/>
            </div>

            <!-- Main Content -->
            <div class="flex-grow-1 p-4 bg-light">
                <div class="d-flex justify-content-between align-items-center mb-3">
                    <a href="<%= request.getContextPath() %>/TrainerPackageServlet?action=list" class="text-decoration-none">&larr; Back to Packages</a>
                </div>

                <h3>Create New Package</h3>

                <form action="<%= request.getContextPath() %>/TrainerPackageServlet?action=create" method="post" enctype="multipart/form-data">
                    <div class="mb-3">
                        <label>Package Name</label>
                        <input type="text" name="name" class="form-control" required/>
                    </div>
                    <div class="mb-3">
                        <label>Description</label>
                        <textarea name="description" class="form-control" rows="3"></textarea>
                    </div>
                    <div class="mb-3">
                        <label>Upload Image</label>
                        <input type="file" name="imageFile" class="form-control" accept="image/*" required/>
                    </div>
                    <div class="mb-3">
                        <label>Price (VNĐ)</label>
                        <input type="number" name="price" step="0.01" class="form-control" required/>
                    </div>
                    <div class="mb-3">
                        <label>Duration (days)</label>
                        <input type="number" name="duration" class="form-control" required/>
                    </div>

                    <button type="submit" class="btn btn-success">Create</button>
                    <a href="<%= request.getContextPath() %>/TrainerPackageServlet?action=list" class="btn btn-secondary">Cancel</a>
                </form>
            </div>
        </div>
    </body>
</html>
