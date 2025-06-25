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
            <tr>
                <td><input type="checkbox"/></td>
                <td><img src="https://via.placeholder.com/30" class="rounded-circle me-2"/> <strong>[Demo] Jordan Cruz</strong></td>
                <td><span class="badge bg-warning text-dark">Tomorrow</span></td>
                <td>
                    <span class="text-success me-2">89%</span>
                    <span class="text-success me-2">93%</span>
                    <span class="text-success">93%</span>
                </td>
                <td><button class="btn btn-outline-secondary btn-sm">+ Create group</button></td>
                <td><button class="btn btn-sm btn-light">⋮</button></td>
            </tr>
            </tbody>
        </table>

        <p><strong>Pro tip!</strong> Tap <kbd>?</kbd> to view keyboard shortcuts for any page.</p>
    </div>
</div>

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>
