<%@ page import="java.util.List" %>
<%@ page import="model.Package" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    List<Package> packages = (List<Package>) request.getAttribute("packages");
%>
<!DOCTYPE html>
<html>
    <head>
        <title>Packages</title>
        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
        <style>
            body { margin: 0; }
            .sidebar { width: 240px; min-height: 100vh; }
            .package-img { width: 80px; height: 80px; object-fit: cover; border-radius: 8px; }
        </style>
    </head>
    <body>
        <div class="d-flex">
            <div class="sidebar bg-dark text-white">
                <jsp:include page="sidebar.jsp" />
            </div>

            <div class="flex-grow-1 p-4 bg-light">
                <h2>Packages</h2>

                <div class="d-flex justify-content-between align-items-center mb-3">
                    <a href="<%= request.getContextPath() %>/TrainerPackageServlet?action=create" class="btn btn-primary">+ Create package</a>
                </div>

                <input class="form-control mb-3" placeholder="Search"/>

                <table class="table table-hover align-middle bg-white">
                    <thead class="table-light">
                        <tr>
                            <th>Image</th>
                            <th>Name</th>
                            <th>Description</th>
                            <th>Price</th>
                            <th>Duration (days)</th>
                            <th>Action</th>
                        </tr>
                    </thead>
                    <tbody>
                        <% for (Package p : packages) { %>
                        <tr>
                            <td>
                                <img src="<%= p.getImageUrl() != null ? p.getImageUrl() : "https://via.placeholder.com/80" %>" class="package-img" alt="Package Image"/>
                            </td>
                            <td><%= p.getName() %></td>
                            <td><%= p.getDescription() %></td>
                            <td><%= p.getPrice() %> đ</td>
                            <td><%= p.getDuration() %></td>
                            <td class="text-end">
                                <div class="btn-group">
                                    <a class="btn btn-sm btn-outline-primary" 
                                       href="<%= request.getContextPath() %>/PackageDetailServlet?packageId=<%= p.getPackageID() %>">
                                        View Programs
                                    </a>
                                    <button type="button" class="btn btn-sm btn-outline-secondary dropdown-toggle dropdown-toggle-split"
                                            data-bs-toggle="dropdown" aria-expanded="false">
                                        <span style="font-size: 18px;">&#8942;</span>
                                    </button>
                                    <ul class="dropdown-menu">
                                        <li>
                                            <a class="dropdown-item" href="<%= request.getContextPath() %>/TrainerPackageServlet?action=edit&id=<%= p.getPackageID() %>">
                                                Edit package
                                            </a>
                                        </li>
                                        <li>
                                            <a class="dropdown-item text-danger" href="<%= request.getContextPath() %>/TrainerPackageServlet?action=delete&id=<%= p.getPackageID() %>"
                                               onclick="return confirm('Are you sure you want to delete this package?')">
                                                Delete package
                                            </a>
                                        </li>
                                    </ul>
                                </div>
                            </td>
                        </tr>
                        <% } %>
                    </tbody>
                </table>
            </div>
        </div>
        <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
    </body>
</html>
