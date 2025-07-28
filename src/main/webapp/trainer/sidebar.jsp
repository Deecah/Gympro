<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="model.User" %>
<%
    String context = request.getContextPath();
    User user = (User) session.getAttribute("user");
    String avatarUrl = (user != null && user.getAvatarUrl() != null && !user.getAvatarUrl().isEmpty())
        ? user.getAvatarUrl()
        : context + "/img/default-avatar.jpg";
%>
<link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css" rel="stylesheet">
<link rel="stylesheet" href="<%= context %>/stylecss/trainer-sidebar.css">
<div id="sidebar" class="bg-dark text-white p-3 d-flex flex-column">
    <a href="<%= context %>/CustomerServlet" class="d-flex align-items-center mb-3 text-white text-decoration-none">
        <span class="fs-4">🏋️ Gympro</span>
    </a>
    <hr>
    <ul class="nav nav-pills flex-column mb-auto text-start">
        <li class="nav-item mb-2">
            <a href="<%= context %>/CustomerServlet" class="nav-link text-white btn btn-dark w-100 text-start">
                <i class="fas fa-users me-2"></i>Customers
            </a>
        </li>
        <li class="mb-2">
            <a href="<%= context %>/trainer/library.jsp" class="nav-link text-white btn btn-dark w-100 text-start">
                <i class="fas fa-dumbbell me-2"></i>Library
            </a>
        </li>
        <li class="mb-2">
            <a href="<%= context %>/ProgramServlet" class="nav-link text-white btn btn-dark w-100 text-start">
                <i class="fas fa-clipboard-list me-2"></i>Programs
            </a>
        </li>
        <li class="mb-2">
            <a href="<%= context %>/TrainerPackageServlet" class="nav-link text-white btn btn-dark w-100 text-start">
                <i class="fas fa-box me-2"></i>Packages
            </a>
        </li>
        <li class="mb-2">
            <a href="<%= context %>/trainer/schedule.jsp" class="nav-link text-white btn btn-dark w-100 text-start">
                <i class="fas fa-calendar me-2"></i>Schedule
            </a>
        </li>
        <li class="mb-2">
            <a href="#" class="nav-link text-white btn btn-dark w-100 text-start d-flex justify-content-between align-items-center">
                <span><i class="fas fa-bullhorn me-2"></i>Marketing</span>
                <span class="badge bg-warning text-dark ms-2">!</span>
            </a>
        </li>
    </ul>
    <hr class="mt-auto">
    <div class="header-avatar position-relative text-center">
        <img src="<%= avatarUrl %>" class="rounded-circle" alt="Avatar" onclick="toggleMenu()" />
        <div class="mt-2"><%= user != null ? user.getUserName() : "Trainer" %></div>
        <div id="dropdownMenu">
            <a href="<%= context %>/trainer/profile-trainer.jsp">👤 Profile</a>
            <a href="<%= context %>/logout">🚪 Logout</a>
        </div>
    </div>
</div>

<script>
    function toggleMenu() {
        const menu = document.getElementById("dropdownMenu");
        menu.style.display = (menu.style.display === "block") ? "none" : "block";
    }
    document.addEventListener("click", function (event) {
        const avatar = document.querySelector(".header-avatar img");
        const dropdown = document.getElementById("dropdownMenu");
        if (!avatar.contains(event.target) && !dropdown.contains(event.target)) {
            dropdown.style.display = "none";
        }
    });
</script>
