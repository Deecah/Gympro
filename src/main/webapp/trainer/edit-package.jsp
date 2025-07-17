<%@ page import="model.Package" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    Package p = (Package) request.getAttribute("package");
%>
<!DOCTYPE html>
<html>
<head>
    <title>Edit Package</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <style>
        body { margin: 0; }
        .sidebar { width: 240px; min-height: 100vh; }
    </style>
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
            <a href="<%= request.getContextPath() %>/TrainerPackageServlet" class="text-decoration-none">&larr; Back to Packages</a>
        </div>

        <h3>Edit Package</h3>

        <form action="<%= request.getContextPath() %>/TrainerPackageServlet?action=update" method="post" enctype="multipart/form-data">
            <input type="hidden" name="packageId" value="<%= p.getPackageID() %>"/>
            <input type="hidden" name="currentImageUrl" value="<%= p.getImageUrl() != null ? p.getImageUrl() : "" %>" />

            <div class="mb-3">
                <label>Package Name</label>
                <input type="text" name="name" value="<%= p.getName() %>" class="form-control" required/>
            </div>

            <div class="mb-3">
                <label>Description</label>
                <textarea name="description" class="form-control" rows="3"><%= p.getDescription() %></textarea>
            </div>

            <div class="mb-3">
                <label>Current Image</label><br>
                <img src="<%= (p.getImageUrl() != null && !p.getImageUrl().isEmpty()) ? p.getImageUrl() : "https://via.placeholder.com/150x100?text=No+Image" %>" 
                     alt="Current Image"
                     style="width: 150px; height: auto; margin-bottom: 10px;">
            </div>

            <div class="mb-3">
                <label>Upload New Image</label>
                <input type="file" name="imageFile" class="form-control" accept="image/*" />
            </div>

            <div class="mb-3">
                <label>Price (VNĐ)</label>
                <input type="number" name="price" value="<%= p.getPrice() %>" class="form-control" step="0.01" required/>
            </div>

            <div class="mb-3">
                <label>Duration (days)</label>
                <input type="number" name="duration" value="<%= p.getDuration() %>" class="form-control" required/>
            </div>

            <button type="submit" class="btn btn-success">Update</button>
            <a href="<%= request.getContextPath() %>/TrainerPackageServlet" class="btn btn-secondary">Cancel</a>
        </form>
    </div>
</div>
</body>
</html>
