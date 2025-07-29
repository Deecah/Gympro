<%@page import="java.util.ArrayList"%>
<%@page import="model.User"%>
<%@page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="en">
    <head>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <title>Admin Dashboard</title>
        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-QWTKZyjpPEjISv5WaRU9OFeRpok6YctnYmDr5pNlyT2bRjXh0JMhjY6hW+ALEwIH" crossorigin="anonymous">
        <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0-beta3/css/all.min.css">
        <style>
            :root {
                --primary-gradient-start: #ff5e62; /* Brighter red-orange from image */
                --primary-gradient-end: #ff9966;   /* Orange-yellow from image */
                /* Sử dụng lại gradient chính cho sidebar và navbar, nhưng với tông màu đậm hơn */
                --sidebar-navbar-gradient-start: #E63946; /* Đỏ đậm hơn */
                --sidebar-navbar-gradient-end: #F4A261;   /* Cam cháy đậm hơn */

                --dark-bg-primary: #343a40; /* Dark background for overall wrapper */
                --light-content-bg: #ffffff; /* White background for content area */
                --dark-text-primary: #dee2e6; /* Light text for elements on dark/gradient */
                --dark-text-secondary: #adb5bd; /* Muted text for elements on dark/gradient */
                --content-text-primary: #343a40; /* Dark text for content area */
                --content-text-secondary: #6c757d; /* Muted text in content area */
                --dark-border-color: rgba(255,255,255,0.1);
                --light-border-color: rgba(0,0,0,0.1);
            }

            body {
                background-color: var(--dark-bg-primary);
                color: var(--content-text-primary);
                overflow-x: hidden;
            }
            #wrapper {
                background-color: var(--dark-bg-primary);
            }

            /* Sidebar background with gradient */
            #sidebar-wrapper {
                min-height: 100vh;
                margin-left: -17rem;
                transition: margin .25s ease-out;
                background-image: linear-gradient(to bottom, var(--sidebar-navbar-gradient-start), var(--sidebar-navbar-gradient-end)); /* Gradient background */
                color: #fff;
                position: fixed;
                top: 0;
                left: 0;
                z-index: 1000;
                width: 17rem;
                box-shadow: 2px 0 5px rgba(0,0,0,0.5);
            }

            .sidebar-heading {
                background-color: rgba(0,0,0,0.2) !important; /* Slightly darker overlay for heading */
                border-bottom: 1px solid rgba(255,255,255,0.3) !important;
            }

            /* Active state for sidebar sub-items */
            .list-group-item.active {
                background-color: rgba(0,0,0,0.4) !important; /* Darker overlay for active item */
                border-color: rgba(255,255,255,0.5) !important; /* Slightly more visible border */
                color: #fff !important;
                font-weight: bold;
            }
            .list-group-item {
                color: rgba(255,255,255,0.95); /* Slightly muted white for normal items */
                background-color: transparent; /* Transparent to show gradient background */
                border: none;
                padding: 1rem 1.5rem;
                font-size: 1.05rem;
                transition: background-color 0.2s ease-in-out, color 0.2s ease-in-out;
                border-radius: 0;
            }
            /* Hover state for sidebar items - darker overlay */
            .list-group-item:hover {
                background-color: rgba(0,0,0,0.3) !important; /* Đậm hơn 1 chút cho hover */
                color: #fff;
            }

            /* Styles for Accordion Headers (the main group titles) */
            .accordion-header .accordion-button {
                background-color: transparent !important; /* Transparent to show gradient */
                color: rgba(255,255,255,0.95) !important;
                padding: 1rem 1.5rem;
                font-weight: bold;
                border-bottom: 1px solid rgba(255,255,255,0.2);
                border-radius: 0;
                display: flex;
                align-items: center;
                justify-content: flex-start;
            }
            /* Hover state for accordion buttons - darker overlay */
            .accordion-header .accordion-button:hover {
                background-color: rgba(0,0,0,0.3) !important;
                color: #fff !important;
            }
            /* Expanded state for accordion buttons - slightly darker */
            .accordion-header .accordion-button:not(.collapsed) {
                background-color: rgba(0,0,0,0.35) !important;
                color: #fff !important;
                box-shadow: none;
            }
            .accordion-header .accordion-button:focus {
                box-shadow: none;
                outline: none;
            }

            /* Styles for Accordion Body (the sub-items) */
            .accordion-body {
                padding: 0;
                background-color: transparent; /* Transparent to show gradient */
            }
            .accordion-body .list-group-item {
                padding-left: 2.5rem;
                font-size: 0.95rem;
                background-color: transparent; /* Transparent to show gradient */
            }
            /* Hover for sub-items in accordion body */
            .accordion-body .list-group-item:hover {
                background-color: rgba(0,0,0,0.35); /* Slightly darker hover */
            }
            .accordion-item {
                background-color: transparent;
                border: none;
            }
            .accordion-collapse {
                border: none;
            }

            .accordion-button::after {
                font-family: "Font Awesome 6 Free";
                content: "\f0d7";
                font-weight: 900;
                display: inline-block;
                margin-left: auto;
                transition: transform 0.2s ease-in-out;
                background-image: none;
                color: rgba(255,255,255,0.9);
            }
            .accordion-button:not(.collapsed)::after {
                transform: rotate(180deg);
            }

            /* --- Right Content Area Styles --- */
            #page-content-wrapper {
                width: 100%;
                padding-left: 0;
                transition: margin-left .25s ease-out;
                background-color: var(--light-content-bg); /* White background */
                min-height: 100vh;
                color: var(--content-text-primary); /* Dark text for content area */
            }

            /* Navbar background with gradient */
            .navbar {
                background-image: linear-gradient(to right, var(--sidebar-navbar-gradient-start), var(--sidebar-navbar-gradient-end)) !important;
                border-bottom: 1px solid rgba(255,255,255,0.2);
            }
            .navbar-light .navbar-nav .nav-link {
                color: #fff; /* White text on gradient navbar */
            }
            .navbar-light .navbar-nav .nav-link:hover {
                color: rgba(255,255,255,0.85); /* Slightly less white on hover */
            }
            .navbar-light .navbar-nav .nav-item.active .nav-link {
                color: #fff; /* Active nav link text remains white */
                font-weight: bold;
            }

            .content-section {
                display: none;
                background-color: var(--light-content-bg); /* White background for content cards */
                border: 1px solid var(--light-border-color); /* Light border */
                color: var(--content-text-primary);
            }
            .content-section.active {
                display: block;
            }
            .content-section h1 { /* Welcome Admin title */
                color: var(--content-text-primary); /* Darker color for main titles */
            }
            .content-section h2 {
                color: var(--primary-gradient-end); /* Use gradient end color for section headings */
                margin-bottom: 1.5rem;
            }
            .content-section p.lead {
                color: var(--content-text-secondary);
            }

            .table {
                color: var(--content-text-primary);
                background-color: var(--light-content-bg);
            }
            .table th, .table td {
                border-color: rgba(0,0,0,0.15); /* Darker borders for tables on white background */
            }
            .table-striped tbody tr:nth-of-type(odd) {
                background-color: rgba(0,0,0,0.05); /* Light stripe for tables on white background */
            }
            .table-hover tbody tr:hover {
                background-color: rgba(0,0,0,0.1); /* Light hover for tables on white background */
            }

            .form-label {
                color: var(--content-text-primary);
            }
            .form-control {
                background-color: #f8f9fa; /* Light grey for input fields */
                color: var(--content-text-primary);
                border-color: rgba(0,0,0,0.2);
            }
            .form-control:focus {
                background-color: #e9ecef; /* Slightly darker grey on focus */
                color: var(--content-text-primary);
                border-color: var(--primary-gradient-end);
                box-shadow: 0 0 0 0.25rem rgba(255, 153, 102, 0.25);
            }
            .form-control::placeholder {
                color: var(--content-text-secondary);
                opacity: 1;
            }

            .btn-primary, .btn-success, .btn-danger, .btn-info {
                font-weight: 500;
                border-width: 1px;
            }
            /* Custom gradient for primary buttons */
            .btn-primary {
                background-image: linear-gradient(to right, var(--primary-gradient-start), var(--primary-gradient-end));
                border: none;
                color: #fff;
            }
            .btn-primary:hover {
                background-image: linear-gradient(to right, #e04a4e, #e08357);
                border: none;
                color: #fff;
            }

            .btn-success {
                background-color: #28a745;
                border-color: #28a745;
            }
            .btn-success:hover {
                background-color: #218838;
                border-color: #1e7e34;
            }
            .btn-danger {
                background-color: #dc3545;
                border-color: #dc3545;
            }
            .btn-danger:hover {
                background-color: #c82333;
                border-color: #bd212f;
            }
            .btn-info {
                background-color: #17a2b8;
                border-color: #17a2b8;
            }
            .btn-info:hover {
                background-color: #138496;
                border-color: #117a8b;
            }

            .btn-sm {
                padding: .25rem .5rem;
                font-size: .875rem;
                line-height: 1.5;
                border-radius: .2rem;
            }

            .badge {
                padding: .5em .75em;
                font-size: .8em;
                vertical-align: middle;
                border-radius: .25rem;
            }
            .badge.bg-success {
                background-color: #28a745 !important;
            }
            .badge.bg-danger {
                background-color: #dc3545 !important;
            }
            .badge.bg-warning {
                background-color: #ffc107 !important;
                color: #343a40 !important;
            }
            .badge.bg-primary {
                background-color: var(--primary-gradient-end) !important;
            }

            .message-area {
                min-height: 25px;
                font-size: 0.85rem;
                color: var(--content-text-secondary);
            }
            .alert {
                background-color: #e2e3e5; /* Light alert background */
                color: #383d41; /* Dark text for alerts */
                border-color: #d3d6da;
            }
            .alert-info {
                background-color: #cfe2ff !important;
                color: #055160 !important;
                border-color: #b6effb !important;
            }
            .alert-success {
                background-color: #d1e7dd !important;
                color: #0f5132 !important;
                border-color: #badbcc !important;
            }
            .alert-warning {
                background-color: #fff3cd !important;
                color: #664d03 !important;
                border-color: #ffecb5 !important;
            }
            .alert-danger {
                background-color: #f8d7da !important;
                color: #842029 !important;
                border-color: #f5c2c7 !important;
            }
            .alert .btn-close {
                filter: none; /* Remove filter for close button on light background */
            }

            @media (min-width: 768px) {
                #sidebar-wrapper {
                    margin-left: 0;
                }
                #page-content-wrapper {
                    margin-left: 17rem;
                }
                #wrapper.toggled #sidebar-wrapper {
                    margin-left: 0;
                }
                #wrapper.toggled #page-content-wrapper {
                    margin-left: 17rem;
                }
            }
            .dropdown-arrow {
                transition: transform 0.3s ease; /* Hiệu ứng chuyển động mượt mà */
            }

            .dropdown-toggle[aria-expanded="true"] .dropdown-arrow {
                transform: rotate(180deg); /* Xoay mũi tên 180 độ khi dropdown mở */
            }

            /* Quan trọng: Ẩn mũi tên mặc định của Bootstrap nếu bạn chỉ muốn dùng Font Awesome */
            .dropdown-toggle::after {
                display: none;
            }
            .actions-cell {
                display: flex; /* Biến cell thành flex container */
                justify-content: center; /* Căn giữa nội dung theo chiều ngang */
                align-items: center; /* Căn giữa nội dung theo chiều dọc (nếu có nhiều hàng) */
                padding: 0; /* Đảm bảo không có padding thừa từ cell */
            }
        </style>
    </head>
    <body>

        <div class="d-flex" id="wrapper">
            <div class="bg-dark border-right" id="sidebar-wrapper">
                <div class="sidebar-heading text-white border-bottom bg-dark p-3">
                    <h4>Admin Dashboard</h4>
                </div>

                <div class="accordion accordion-flush" id="sidebarAccordion">

                    <div class="accordion-item">
                        <h2 class="accordion-header" id="headingUser">
                            <button class="accordion-button collapsed" type="button" data-bs-toggle="collapse" data-bs-target="#collapseUser" aria-expanded="false" aria-controls="collapseUser">
                                <i class="fas fa-users-cog me-2"></i> Manage User
                            </button>
                        </h2>
                        <div id="collapseUser" class="accordion-collapse collapse" aria-labelledby="headingUser" data-bs-parent="#sidebarAccordion">
                            <div class="accordion-body p-0">
                                <div class="list-group list-group-flush">
                                    <%-- Thêm data-load-action để JS biết gọi action nào trên servlet --%>
                                    <a href="#" class="list-group-item list-group-item-action sidebar-item" data-target="view-user-list" data-load-action="viewuser">
                                        <i class="fas fa-list-alt me-2"></i> View User List
                                    </a>
                                    <a href="#" class="list-group-item list-group-item-action sidebar-item" data-target="view-trainer-list" data-load-action="viewtrainer">
                                        <i class="fas fa-list-alt me-2"></i> View Trainer List
                                    </a>
                                    <a href="#" class="list-group-item list-group-item-action sidebar-item" data-target="view-violation-report" data-load-action="viewreport">
                                        <i class="fas fa-exclamation-triangle me-2"></i> View Violation Report
                                    </a>
                                </div>
                            </div>
                        </div>
                    </div>
                    <div class="accordion-item">
                        <h2 class="accordion-header" id="headingContent">
                            <button class="accordion-button collapsed" type="button" data-bs-toggle="collapse" data-bs-target="#collapseContent" aria-expanded="false" aria-controls="collapseContent">
                                <i class="fas fa-file-alt me-2"></i> Manage Content
                            </button>
                        </h2>
                        <div id="collapseContent" class="accordion-collapse collapse" aria-labelledby="headingContent" data-bs-parent="#sidebarAccordion">
                            <div class="accordion-body p-0">
                                <div class="list-group list-group-flush">
                                    <a href="#" class="list-group-item list-group-item-action sidebar-item" data-target="edit-content">
                                        <i class="fas fa-edit me-2"></i> Edit Content
                                    </a>
                                    <a href="#" class="list-group-item list-group-item-action sidebar-item" data-target="add-content">
                                        <i class="fas fa-plus-circle me-2"></i> Add Content
                                    </a>
                                    <a href="#" class="list-group-item list-group-item-action sidebar-item" data-target="delete-content">
                                        <i class="fas fa-trash-alt me-2"></i> Delete Content
                                    </a>
                                </div>
                            </div>
                        </div>
                    </div>

                    <div class="accordion-item">
                        <h2 class="accordion-header" id="headingReports">
                            <button class="accordion-button collapsed" type="button" data-bs-toggle="collapse" data-bs-target="#collapseReports" aria-expanded="false" aria-controls="collapseReports">
                                <i class="fas fa-chart-bar me-2"></i> Reports
                            </button>
                        </h2>
                        <div id="collapseReports" class="accordion-collapse collapse" aria-labelledby="headingReports" data-bs-parent="#sidebarAccordion">
                            <div class="accordion-body p-0">
                                <div class="list-group list-group-flush">
                                    <a href="#" class="list-group-item list-group-item-action sidebar-item" data-target="view-revenue-stats">
                                        <i class="fas fa-dollar-sign me-2"></i> Monthly Revenue
                                    </a>
                                </div>
                            </div>
                        </div>
                    </div>

                </div>
            </div>

            <div id="page-content-wrapper">
                <nav class="navbar navbar-expand-lg navbar-light bg-light border-bottom">
                    <div class="container-fluid">
                        <div class="collapse navbar-collapse" id="navbarSupportedContent">
                            <ul class="navbar-nav ms-auto mt-2 mt-lg-0">
                                <li class="nav-item active">
                                    <a class="nav-link" href="http://localhost:8080/SWP391/UserServlet"><i class="fas fa-home me-1"></i> Home</a>
                                </li>
                            </ul>
                        </div>
                    </div>
                </nav>

                <div class="container-fluid py-4">
                    <%-- Các phần nội dung sẽ được tải bằng AJAX hoặc hiển thị/ẩn --%>

                    <div id="view-user-list" class="content-section card card-body shadow-sm mb-4">
                        <h2><i class="fas fa-users me-2"></i> View User List</h2>
                        <p>Displays a list of all users in the system.</p>
                        <div id="userListTableContainer" class="table-responsive">
                            <p class="text-muted text-center">Nhấn "Load User List" để xem danh sách người dùng.</p>
                        </div>
                        <button class="btn btn-success mt-3" id="loadUserListButton">Load User List</button>
                    </div>

                    <div id="view-user-profile" class="content-section card card-body shadow-sm mb-4">
                        <h2><i class="fas fa-user me-2"></i> View User Profile</h2>
                        <p>Enter user ID or Email to view profile details.</p>
                        <form class="row g-3">
                            <div class="col-md-6">
                                <label for="userIdentifierProfile" class="form-label">User ID / Email:</label>
                                <input type="text" class="form-control" id="userIdentifierProfile" name="userIdentifier" required>
                            </div>
                            <div class="col-12">
                                <button type="button" class="btn btn-success">View Profile</button>
                            </div>
                        </form>
                        <div class="mt-3 p-3 border rounded bg-light">
                            <p class="text-muted">User profile details will display here.</p>
                        </div>
                    </div>

                    <div id="view-trainer-list" class="content-section card card-body shadow-sm mb-4">
                        <h2><i class="fas fa-dumbbell me-2"></i> View Trainer List</h2>
                        <p>Displays a list of all trainers.</p>
                        <div id="trainerListTableContainer" class="table-responsive">
                            <p class="text-muted text-center">Nhấn "Load Trainer List" để xem danh sách huấn luyện viên.</p>
                        </div>
                        <button class="btn btn-success mt-3" id="loadTrainerListButton">Load Trainer List</button>
                    </div>

                    <div id="view-violation-report" class="content-section card card-body shadow-sm mb-4">
                        <h2><i class="fas fa-exclamation-triangle me-2"></i> View Violation Report</h2>
                        <p>View violation reports from users.</p>
                        <div id="violationReportTableContainer" class="table-responsive">
                            <p class="text-muted text-center">Report data will be displayed here.</p>
                        </div>
                        <button type="button" class="btn btn-success mt-3" id="loadViolationReportButton">Load Violation Reports</button>
                    </div>

                    <div id="edit-content" class="content-section card card-body shadow-sm mb-4">
                        <h2><i class="fas fa-edit me-2"></i> Edit Content</h2>
                        <p>Edit static content on the website (e.g., blog posts, class information).</p>
                        <form class="row g-3" method="post" action="EditContentServlet">
                            <div class="col-md-6">
                                <label for="contentIdEdit" class="form-label">Content ID:</label>
                                <input type="text" class="form-control" id="contentIdEdit" name="contentId" required>
                            </div>
                            <div class="col-md-6">
                                <label for="contentTitleEdit" class="form-label">Title:</label>
                                <input type="text" class="form-control" id="contentTitleEdit" name="contentTitle" required>
                            </div>
                            <div class="col-12">
                                <label for="contentBodyEdit" class="form-label">Content:</label>
                                <textarea class="form-control" id="contentBodyEdit" name="contentBody" rows="10" required></textarea>
                            </div>
                            <div class="col-12">
                                <button type="submit" class="btn btn-success">Save Changes</button>
                            </div>
                        </form>
                        <div class="mt-3 p-2 message-area">Status message will display here.</div>
                    </div>

                    <div id="add-content" class="content-section card card-body shadow-sm mb-4">
                        <h2><i class="fas fa-plus-circle me-2"></i> Add Content</h2>
                        <p>Add new content to the website.</p>
                        <form class="row g-3" action="AddContentServlet" method="post">
                            <div class="col-md-12">
                                <label for="contentTitleAdd" class="form-label">Title:</label>
                                <input type="text" class="form-control" id="contentTitleAdd" name="contentTitle" required>
                            </div>
                            <div class="col-12">
                                <label for="contentBodyAdd" class="form-label">Content:</label>
                                <textarea class="form-control" id="contentBodyAdd" name="contentBody" rows="10" required></textarea>
                            </div>
                            <div class="col-12">
                                <button type="submit" class="btn btn-success">Add New Content</button>
                            </div>
                        </form>
                        <div class="mt-3 p-2 message-area">Status message will display here.</div>
                    </div>

                    <div id="delete-content" class="content-section card card-body shadow-sm mb-4">
                        <h2><i class="fas fa-trash-alt me-2"></i> Delete Content</h2>
                        <p>Delete content from the website.</p>
                        <form class="row g-3" method="post" action="DeleteContentServlet">
                            <div class="col-md-6">
                                <label for="contentIdDelete" class="form-label">Content ID to Delete:</label>
                                <input type="text" class="form-control" id="contentIdDelete" name="contentId" required>
                            </div>
                            <div class="col-12">
                                <button type="submit" class="btn btn-danger">Delete Content</button>
                            </div>  
                        </form>
                        <div class="mt-3 p-2 message-area">Status message will display here.</div>
                    </div>

                    <div id="view-revenue-stats" class="content-section card card-body shadow-sm mb-4">
                        <h2><i class="fas fa-chart-line me-2"></i> View Monthly Revenue Statistics</h2>
                        <p>Display charts or tables for monthly revenue statistics.</p>

                        <div id="revenueStatsContainer" style="width: 100%; height: 400px; border: 1px solid rgba(0,0,0,0.1);" class="d-flex align-items-center justify-content-center bg-light rounded position-relative">
                            <p id="placeholderText" class="text-muted position-absolute">Revenue chart will display here (requires backend integration and charting library).</p>
                            <canvas id="revenueChart" width="400" height="200" style="display: none;"></canvas>
                        </div>


                        <button type="button" class="btn btn-success mt-3" id="loadRevenueStatsButton">Load Revenue Statistics</button>
                    </div>


                </div>
            </div>
        </div>

        <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js" integrity="sha384-YvpcrYf0tY3lHB60NNkmXc5s9fDVZLESaAA55NDzOxhy9GkcIdslK1eN7N6jIeHz" crossorigin="anonymous"></script>
        <script src="https://cdn.jsdelivr.net/npm/chart.js"></script>
        <script>
            document.addEventListener('DOMContentLoaded', function () {
                const sidebarItems = document.querySelectorAll('.sidebar-item');
                const contentSections = document.querySelectorAll('.content-section');
                const loadUserListButton = document.getElementById('loadUserListButton');
                const loadTrainerListButton = document.getElementById('loadTrainerListButton');
                const userListTableContainer = document.getElementById('userListTableContainer');
                const trainerListTableContainer = document.getElementById('trainerListTableContainer');

                // Hàm để ẩn tất cả các section nội dung
                function hideAllContentSections() {
                    contentSections.forEach(section => {
                        section.classList.remove('active');
                    });
                }

                // Hàm để tải dữ liệu bằng AJAX
                function loadContent(actionType, targetContainerId) {
                    const container = document.getElementById(targetContainerId);
                    if (!container) {
                        console.error("Container not found:", targetContainerId);
                        return;
                    }
                    container.innerHTML = '<p class="text-center text-info">Đang tải dữ liệu...</p>'; // Hiển thị trạng thái tải

                    fetch('AdminManagementServlet?action=' + actionType) // Gửi yêu cầu AJAX đến Servlet
                            .then(response => {
                                if (!response.ok) {
                                    throw new Error('Network response was not ok');
                                }
                                return response.text(); // Đọc phản hồi dưới dạng văn bản (HTML fragment)
                            })
                            .then(html => {
                                container.innerHTML = html; // Chèn HTML nhận được vào container
                            })
                            .catch(error => {
                                console.error('Lỗi khi tải dữ liệu:', error);
                                container.innerHTML = '<p class="text-center text-danger">Lỗi khi tải dữ liệu. Vui lòng thử lại.</p>';
                            });
                }

                // Xử lý click vào các item sidebar
                sidebarItems.forEach(item => {
                    item.addEventListener('click', function (e) {
                        e.preventDefault();

                        // Xóa active khỏi tất cả sidebar items
                        sidebarItems.forEach(i => i.classList.remove('active'));
                        // Thêm active vào item được click
                        this.classList.add('active');

                        const targetId = this.dataset.target;
                        const loadAction = this.dataset.loadAction; // Lấy action để tải dữ liệu

                        hideAllContentSections(); // Ẩn tất cả các phần nội dung

                        const activeSection = document.getElementById(targetId);
                        if (activeSection) {
                            activeSection.classList.add('active'); // Hiện phần nội dung tương ứng

                            // Nếu có data-load-action, tự động tải dữ liệu
                            if (loadAction) {
                                if (targetId === 'view-user-list') {
                                    loadContent(loadAction, 'userListTableContainer');
                                } else if (targetId === 'view-trainer-list') {
                                    loadContent(loadAction, 'trainerListTableContainer');
                                }
                                // Thêm các điều kiện khác nếu có thêm các danh sách cần tải
                            }
                        }
                    });
                });

                // Xử lý nút "Load User List"
                loadUserListButton.addEventListener('click', function () {
                    loadContent('viewuser', 'userListTableContainer');
                });

                // Xử lý nút "Load Trainer List"
                loadTrainerListButton.addEventListener('click', function () {
                    loadContent('viewtrainer', 'trainerListTableContainer');
                });

                // Xử lý nút "Load Violation Reports"
                document.getElementById('loadViolationReportButton').addEventListener('click', function () {
                    loadContent('viewreport', 'violationReportTableContainer');
                });

                // Khởi tạo trạng thái ban đầu: hiển thị View User List và tải dữ liệu nếu cần
                const initialActiveSidebarItem = document.querySelector('.sidebar-item[data-target="view-user-list"]');
                if (initialActiveSidebarItem) {
                    initialActiveSidebarItem.click(); // Giả lập click để kích hoạt logic hiển thị và tải dữ liệu ban đầu
                }
            });

            // Hàm hỗ trợ cho các hành động Ban/Unban/View Profile
            function viewUserProfile(userId) {
                alert('View profile for user ID: ' + userId);
                // Thực hiện AJAX call để tải dữ liệu profile vào section 'view-user-profile'
                // Hoặc redirect đến trang profile chi tiết nếu bạn muốn
            }

            function banUser(userId) {
                if (confirm('Bạn có chắc chắn muốn cấm người dùng ID: ' + userId + ' không?')) {
                    // Thực hiện AJAX call đến Servlet để cấm người dùng
                    fetch('AdminManagementServlet?action=banuser&userId=' + userId)
                            .then(response => response.text())
                            .then(message => {
                                alert(message); // Hiển thị thông báo từ server
                                // Tải lại danh sách người dùng sau khi ban/unban thành công
                                loadContent('viewuser', 'userListTableContainer');
                            })
                            .catch(error => {
                                console.error('Lỗi khi cấm người dùng:', error);
                                alert('Không thể cấm người dùng.');
                            });
                }
            }

            function unbanUser(userId) {
                if (confirm('Bạn có chắc chắn muốn bỏ cấm người dùng ID: ' + userId + ' không?')) {
                    // Thực hiện AJAX call đến Servlet để bỏ cấm người dùng
                    fetch('AdminManagementServlet?action=unbanuser&userId=' + userId)
                            .then(response => response.text())
                            .then(message => {
                                alert(message); // Hiển thị thông báo từ server
                                // Tải lại danh sách người dùng sau khi ban/unban thành công
                                loadContent('viewuser', 'userListTableContainer');
                            })
                            .catch(error => {
                                console.error('Lỗi khi bỏ cấm người dùng:', error);
                                alert('Không thể bỏ cấm người dùng.');
                            });
                }
            }
            function loadRevenueStats() {
                fetch("loadRevenueStats")
                        .then(response => response.json())
                        .then(data => {
                            const months = Object.keys(data);
                            const revenue = Object.values(data);

                            document.getElementById("placeholderText").style.display = "none";
                            const canvas = document.getElementById("revenueChart");
                            canvas.style.display = "block";
                            const ctx = canvas.getContext("2d");

                            // Hủy biểu đồ cũ nếu có
                            if (window.revenueChartInstance) {
                                window.revenueChartInstance.destroy();
                            }

                            // Tạo biểu đồ mới
                            window.revenueChartInstance = new Chart(ctx, {
                                type: 'bar',
                                data: {
                                    labels: months,
                                    datasets: [{
                                            label: 'Revenue (VND)',
                                            data: revenue,
                                            backgroundColor: '#28a745'
                                        }]
                                },
                                options: {
                                    responsive: true,
                                    plugins: {
                                        legend: {display: false}
                                    },
                                    scales: {
                                        y: {
                                            beginAtZero: true,
                                            ticks: {
                                                callback: function (value) {
                                                    return value.toLocaleString('vi-VN');
                                                }
                                            }
                                        }
                                    }
                                }
                            });
                        });
            }

// Gán sự kiện khi nút được bấm
            document.getElementById("loadRevenueStatsButton").addEventListener("click", loadRevenueStats);
        </script>

    </body>
</html>