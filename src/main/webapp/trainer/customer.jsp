<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
    <head>
        <title>Clients</title>
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
    </head>
    <body>

        <div class="d-flex">
            <!-- Sidebar -->
            <div class="sidebar bg-dark text-white">
                <jsp:include page="sidebar.jsp" />
            </div>

            <!-- Main Content -->
            <div class="flex-grow-1 p-4 bg-light">
                <h2>Clients</h2>

                <div class="d-flex justify-content-between align-items-center mb-3">
                    <div>
                        <button class="btn btn-primary">+ Add client</button>
                        <button class="btn btn-outline-secondary">Manage groups</button>
                    </div>
                    <button class="btn btn-outline-dark">Export</button>
                </div>

                <ul class="nav nav-tabs mb-3">
                    <li class="nav-item">
                        <a class="nav-link active">Active <span class="badge bg-secondary">1</span></a>
                    </li>
                    <li class="nav-item">
                        <a class="nav-link">Archived</a>
                    </li>
                </ul>

                <input class="form-control mb-3" placeholder="Search clients and groups"/>

                <table class="table align-middle table-hover bg-white">
                    <thead class="table-light">
                        <tr>
                            <th></th>
                            <th>Name</th>
                            <th>Due</th>
                            <th>Compliance Rate</th>
                            <th>Groups</th>
                            <th></th>
                        </tr>
                    </thead>
                    <tbody>
                    <c:forEach var="client" items="${clients}">
                        <tr>
                            <td><input type="checkbox"/></td>
                            <td>
                                <img src="${client.avatarUrl}" class="rounded-circle me-2" style="width: 30px; height: 30px; object-fit: cover;"/>
                                <strong>${client.name}</strong>
                            </td>
                            <td>
                                <span class="badge bg-warning text-dark">${client.nextDue}</span>
                            </td>
                            <td>
                                <span class="text-success me-2">${client.compliance1}%</span>
                                <span class="text-success me-2">${client.compliance2}%</span>
                                <span class="text-success">${client.compliance3}%</span>
                            </td>
                            <td>${client.groupName}</td>
                            <td>
                                <a href="${pageContext.request.contextPath}/ChatServlet?chatWith=${client.id}&userId=${sessionScope.userId}">
                                    💬 Chat
                                </a>
                            </td>
                        </tr>
                    </c:forEach>
                    </tbody>

                </table>

                <p><strong>Pro tip!</strong> Tap <kbd>?</kbd> to view keyboard shortcuts for any page.</p>
            </div>
        </div>

        <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
    </body>
</html>
