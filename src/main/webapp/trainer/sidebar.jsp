<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="model.User" %>
<%
    String context = request.getContextPath();
%>
<link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css" rel="stylesheet">
<div id="sidebar" class="bg-dark text-white p-3 d-flex flex-column" style="width: 240px; height: 100vh; position: fixed; top: 0; left: 0; z-index: 1000;">
    <a href="trainer.jsp" class="d-flex align-items-center mb-3 text-white text-decoration-none">
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
    <div class="header-avatar position-relative">
        <img src="<%= (request.getAttribute("user") != null && ((User) request.getAttribute("user")).getAvatarUrl() != null && !((User) request.getAttribute("user")).getAvatarUrl().isEmpty()) 
            ? ((User) request.getAttribute("user")).getAvatarUrl() 
            : "images/default-avatar.png" %>" 
             onclick="toggleMenu()" 
             style="width: 40px; height: 40px; object-fit: cover; border-radius: 50%; border: 2px solid white; cursor: pointer;">
        <div id="dropdownMenu" 
             style="display: none; position: absolute; right: 0; bottom: 60px; background-color: white; border: 1px solid #ccc; border-radius: 5px; min-width: 160px; box-shadow: 0px 4px 8px rgba(0,0,0,0.1); font-family: sans-serif; z-index: 1000;">
            <a href="<%= context %>/trainer/profile-trainer.jsp" style="display: block; padding: 10px; text-decoration: none; color: #333;">👤 Profile</a>
            <a href="<%= context %>/logout" style="display: block; padding: 10px; text-decoration: none; color: #333;">🚪 Logout</a>
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
