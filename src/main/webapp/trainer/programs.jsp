<%@ page import="java.util.List" %>
<%@ page import="model.Program" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    List<Program> programs = (List<Program>) request.getAttribute("programs");
%>
<!DOCTYPE html>
<html>
    <head>
        <title>Programs</title>
        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
        <style>
            body {
                margin: 0;
            }
            .sidebar {
                width: 240px;
                min-height: 100vh;
            }
            .dropdown-toggle::after {
                display: none;
            }
            .dropdown-menu {
                min-width: 180px;
            }
        </style>
    </head>
    <body>
        <div class="d-flex">
            <!-- Sidebar -->
            <div class="sidebar bg-dark text-white">
                <jsp:include page="sidebar.jsp" />
            </div>

            <!-- Main Content -->
            <div class="flex-grow-1 p-4 bg-light">
                <h2>Programs</h2>

                <div class="d-flex justify-content-between align-items-center mb-3">
                    <div>
                        <button class="btn btn-primary">+ Create program</button>
                        <button class="btn btn-outline-secondary">Manage tags</button>
                    </div>
                </div>

                <input class="form-control mb-3" placeholder="Search"/>

                <table class="table table-hover align-middle bg-white">
                    <thead class="table-light">
                        <tr>
                            <th>Name</th>
                            <th>Description</th>
                            <th>Action</th>
                        </tr>
                    </thead>
                    <tbody>
                        <% for (Program p : programs) { %>
                        <tr>
                            <td><%= p.getName() %></td>
                            <td><%= p.getDescription() %></td>
                            <td class="text-end">
                                <div class="d-flex align-items-center justify-content-end">
                                    <div class="btn-group">
                                        <button class="btn btn-sm btn-outline-primary d-flex align-items-center">
                                            <svg xmlns="http://www.w3.org/2000/svg" width="16" height="16" fill="currentColor"
                                                 class="bi bi-send me-1" viewBox="0 0 16 16">
                                            <path d="M15.854.146a.5.5 0 0 0-.683-.183l-15 8a.5.5 0 0 0 .057.91l5.578 1.97 
                                                  1.97 5.578a.5.5 0 0 0 .91.057l8-15a.5.5 0 0 0-.183-.683Z"/>
                                            </svg>
                                            Assign to client
                                        </button>
                                        <button type="button" class="btn btn-sm btn-outline-secondary dropdown-toggle dropdown-toggle-split"
                                                data-bs-toggle="dropdown" aria-expanded="false">
                                            <span style="font-size: 18px;">&#8942;</span> <!-- three vertical dots -->
                                        </button>
                                        <ul class="dropdown-menu">
                                            <li>
                                                <a class="dropdown-item" href="<%= request.getContextPath() %>/ProgramDetailServlet?programId=<%= p.getProgramId() %>">
                                                    Edit program
                                                </a>
                                            </li>
                                            <li>
                                                <form action="<%= request.getContextPath() %>/DeleteProgramServlet" method="post"
                                                      onsubmit="return confirm('Are you sure you want to delete this program?')">
                                                    <input type="hidden" name="programId" value="<%= p.getProgramId() %>">
                                                    <button type="submit" class="dropdown-item text-danger">Delete program</button>
                                                </form>
                                            </li>
                                        </ul>
                                    </div>
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
