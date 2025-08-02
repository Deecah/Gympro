<%@ page import="java.util.List" %>
<%@ page import="model.Program" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="model.User" %>
<%@ page import="model.Package" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>
<%@ taglib uri="jakarta.tags.functions" prefix="fn" %>
<%
    List<Program> programs = (List<Program>) request.getAttribute("programs");
    List<User> customers = (List<User>) request.getAttribute("customers");
    List<Package> packages = (List<Package>) request.getAttribute("packages");
%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Programs</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css" rel="stylesheet">
    <style>
        body {
            font-family: 'Inter', -apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, sans-serif;
            background-color: #f5f7fa;
            margin: 0;
            overflow-x: hidden;
        }
        .sidebar {
            width: 260px;
            min-height: 100vh;
            background: linear-gradient(180deg, #1a1a2e 0%, #16213e 100%);
            box-shadow: 2px 0 10px rgba(0, 0, 0, 0.1);
            position: fixed;
            z-index: 1000;
        }
        .main-content {
            flex-grow:1;
            margin-left: 260px;
            padding: 2rem;
            min-height: 100vh;
            background-color: #f5f7fa;
        }
        .programs-header {
            margin-bottom: 2rem;
            text-align: center;
        }
        .programs-title {
            font-size: 2.5rem;
            font-weight: 700;
            color: #1a1a2e;
            margin-bottom: 0.5rem;
        }
        .programs-subtitle {
            font-size: 1.1rem;
            color: #6c757d;
        }
        .action-buttons {
            display: flex;
            gap: 1rem;
            justify-content: center;
            margin-bottom: 1.5rem;
        }
        .btn-create-program, .btn-manage-tags {
            padding: 0.75rem 1.5rem;
            font-size: 1rem;
            font-weight: 500;
            border-radius: 8px;
            transition: all 0.3s ease;
        }
        .btn-create-program {
            background-color: #007bff;
            border: none;
        }
        .btn-create-program:hover {
            background-color: #0056b3;
            transform: translateY(-2px);
        }
        .btn-manage-tags {
            background-color: #17a2b8;
            border: none;
        }
        .btn-manage-tags:hover {
            background-color: #117a8b;
            transform: translateY(-2px);
        }
        .search-container {
            position: relative;
            max-width: 500px;
            margin: 0 auto 2rem;
        }
        .search-icon {
            position: absolute;
            top: 50%;
            left: 1rem;
            transform: translateY(-50%);
            color: #6c757d;
        }
        .search-input {
            padding-left: 2.5rem;
            border-radius: 8px;
            border: 1px solid #ced4da;
            box-shadow: 0 2px 5px rgba(0, 0, 0, 0.05);
            transition: all 0.3s ease;
        }
        .search-input:focus {
            border-color: #007bff;
            box-shadow: 0 0 0 0.2rem rgba(0, 123, 255, 0.25);
        }
        .program-card {
            background: #ffffff;
            border: none;
            border-radius: 12px;
            box-shadow: 0 4px 15px rgba(0, 0, 0, 0.08);
            transition: transform 0.3s ease, box-shadow 0.3s ease;
            margin-bottom: 1.5rem;
        }
        .program-card:hover {
            transform: translateY(-5px);
            box-shadow: 0 6px 20px rgba(0, 0, 0, 0.12);
        }
        .program-name {
            font-size: 1.25rem;
            font-weight: 600;
            color: #1a1a2e;
        }
        .program-description {
            font-size: 0.95rem;
            color: #6c757d;
            line-height: 1.4;
        }
        .action-buttons-cell .btn {
            padding: 0.5rem 1rem;
            font-size: 0.9rem;
            border-radius: 6px;
        }
        .btn-assign {
            background-color: #28a745;
            border: none;
        }
        .btn-assign:hover {
            background-color: #218838;
        }
        .btn-dropdown {
            background-color: #6c757d;
            border: none;
        }
        .btn-dropdown:hover {
            background-color: #5a6268;
        }
        .dropdown-menu {
            border-radius: 8px;
            box-shadow: 0 4px 15px rgba(0, 0, 0, 0.1);
            border: none;
        }
        .dropdown-item {
            padding: 0.5rem 1rem;
            font-size: 0.9rem;
            transition: background-color 0.2s ease;
        }
        .dropdown-item:hover {
            background-color: #f1f3f5;
        }
        .empty-state {
            text-align: center;
            padding: 3rem;
            background: #ffffff;
            border-radius: 12px;
            box-shadow: 0 4px 15px rgba(0, 0, 0, 0.08);
            margin: 2rem auto;
            max-width: 500px;
        }
        .empty-state-icon i {
            font-size: 3rem;
            color: #007bff;
            margin-bottom: 1rem;
        }
        .empty-state-title {
            font-size: 1.5rem;
            font-weight: 600;
            color: #1a1a2e;
        }
        .empty-state-description {
            font-size: 1rem;
            color: #6c757d;
            margin-bottom: 1.5rem;
        }
        .modal-content {
            border-radius: 12px;
            border: none;
            box-shadow: 0 4px 20px rgba(0, 0, 0, 0.15);
        }
        .modal-header {
            background: #f8f9fa;
            border-bottom: none;
            padding: 1.5rem;
        }
        .modal-title {
            font-weight: 600;
            color: #1a1a2e;
        }
        .modal-body {
            padding: 1.5rem;
        }
        .modal-footer {
            border-top: none;
            padding: 1rem 1.5rem;
        }
        .form-label {
            font-weight: 500;
            color: #1a1a2e;
        }
        .form-control, .form-select {
            border-radius: 8px;
            border: 1px solid #ced4da;
            box-shadow: 0 2px 5px rgba(0, 0, 0, 0.05);
            transition: all 0.3s ease;
        }
        .form-control:focus, .form-select:focus {
            border-color: #007bff;
            box-shadow: 0 0 0 0.2rem rgba(0, 123, 255, 0.25);
        }
        .exercise-entry {
            background: #f8f9fa;
            border-radius: 8px;
            padding: 1rem;
            margin-bottom: 1rem;
            position: relative;
            transition: all 0.3s ease;
        }
        .exercise-entry:hover {
            background: #e9ecef;
        }
        .remove-btn {
            position: absolute;
            top: 1rem;
            right: 1rem;
            background: #dc3545;
            border: none;
            padding: 0.5rem;
            border-radius: 6px;
        }
        .remove-btn:hover {
            background: #c82333;
        }
        .exercise-list-container {
            max-height: 400px;
            overflow-y: auto;
            border-radius: 8px;
            border: 1px solid #ced4da;
            padding: 1rem;
            background: #ffffff;
        }
        .exercise-preview {
            border-radius: 8px;
            box-shadow: 0 2px 10px rgba(0, 0, 0, 0.1);
        }
        .notification-toast {
            position: fixed;
            top: 20px;
            right: 20px;
            z-index: 9999;
            min-width: 300px;
            border-radius: 8px;
            box-shadow: 0 4px 15px rgba(0, 0, 0, 0.2);
            backdrop-filter: blur(10px);
            border: none;
            animation: slideInRight 0.3s ease;
        }
        @keyframes slideInRight {
            from { transform: translateX(100%); opacity: 0; }
            to { transform: translateX(0); opacity: 1; }
        }
        @keyframes slideOutRight {
            from { transform: translateX(0); opacity: 1; }
            to { transform: translateX(100%); opacity: 0; }
        }
        @keyframes fadeInUp {
            from { opacity: 0; transform: translateY(20px); }
            to { opacity: 1; transform: translateY(0); }
        }
        @media (max-width: 768px) {
            .sidebar {
                width: 100%;
                position: relative;
                min-height: auto;
            }
            .main-content {
                margin-left: 0;
                padding: 1rem;
            }
            .programs-title {
                font-size: 2rem;
            }
            .program-card {
                flex-direction: column;
                text-align: center;
            }
            .action-buttons-cell {
                margin-top: 1rem;
                justify-content: center;
            }
        }
    </style>
    <script src="../js/notification.js"></script>
</head>
<body>
    <div class="d-flex">
        <!-- Sidebar -->
        <div class="sidebar bg-dark text-white">
            <jsp:include page="sidebar.jsp" />
        </div>

        <!-- Main Content -->
        <div class="main-content">
            <!-- Header Section -->
            <div class="programs-header">
                <h1 class="programs-title">
                    <i class="fas fa-dumbbell me-3"></i>Programs
                </h1>
                <p class="programs-subtitle">Manage and organize your training programs</p>
            </div>

            <!-- Action Buttons -->
            <div class="action-buttons">
                <button class="btn btn-create-program" data-bs-toggle="modal" data-bs-target="#createProgramModal">
                    <i class="fas fa-plus me-2"></i>Create Program
                </button>
                <button class="btn btn-manage-tags">
                    <i class="fas fa-tags me-2"></i>Manage Tags
                </button>
</div>

            <!-- Search Bar -->
            <div class="search-container">
                <i class="fas fa-search search-icon"></i>
                <input class="form-control search-input" placeholder="Search programs..." id="searchInput" aria-label="Search programs"/>
            </div>

            <!-- Programs List -->
            <div class="program-list">
                <c:forEach var="p" items="${programs}">
                    <div class="program-card d-flex flex-column flex-md-row align-items-md-center p-3">
                        <div class="flex-grow-1">
                            <div class="program-name">${p.name}</div>
                            <div class="program-description">${p.description}</div>
                            <div class="programs-subtitle">
                            PACKAGE:
                             <c:forEach var="pk" items="${packages}">
                                 <c:if test="${pk.packageID == p.packageId}">
                                     ${pk.name}
                                 </c:if>
                             </c:forEach>
                             </div>
                        </div>
                        <div class="action-buttons-cell d-flex gap-2">
                            <button class="btn btn-assign" onclick="openAssignModal(${p.programId},'${p.name}','${p.description}')">
                                <i class="fas fa-paper-plane me-1"></i>Assign
                            </button>
                            <div class="btn-group">
                                <button type="button" class="btn btn-dropdown dropdown-toggle dropdown-toggle-split"
                                        data-bs-toggle="dropdown" aria-expanded="false">
                                    <i class="fas fa-ellipsis-v"></i>
                                </button>
                                <ul class="dropdown-menu">
                                    <li>
                                        <button class="dropdown-item" onclick="openAddExerciseModal(${p.programId})">
                                            <i class="fas fa-dumbbell me-2"></i>Add Exercise
                                        </button>
                                    </li>
                                    <li>
                                        <button class="dropdown-item" onclick="openViewExerciseModal(${p.programId})">
                                            <i class="fas fa-eye me-2"></i>View Exercises
                                        </button>
                                    </li>
                                    <li>
                                        <button class="dropdown-item" onclick="openEditProgramModal(${p.programId}, '${p.name.replace("'", "\\'")}', '${p.description.replace("'", "\\'")}', ${p.packageId})">
                                            <i class="fas fa-edit me-2"></i>Edit Program
                                        </button>
                                    </li>
                                    <li>
                                        <form action="${pageContext.request.contextPath}/DeleteProgramServlet" method="post"
                                              onsubmit="return confirm('Are you sure you want to delete this program?')">
                                            <input type="hidden" name="programId" value="${p.programId}">
                                            <button type="submit" class="dropdown-item text-danger">
                                                <i class="fas fa-trash me-2"></i>Delete Program
                                            </button>
                                        </form>
                                    </li>
                                </ul>
</div>
                        </div>
                    </div>
                </c:forEach>
            </div>

            <!-- Empty State -->
            <c:if test="${empty programs}">
                <div class="empty-state">
                    <div class="empty-state-icon">
                        <i class="fas fa-dumbbell"></i>
                    </div>
                    <h3 class="empty-state-title">No Programs Yet</h3>
                    <p class="empty-state-description">Create your first training program to get started</p>
                    <button class="btn btn-create-program" data-bs-toggle="modal" data-bs-target="#createProgramModal">
                        <i class="fas fa-plus me-2"></i>Create Your First Program
                    </button>
                </div>
            </c:if>
        </div>
    </div>

    <!-- Create/Edit Program Modal -->
    <div class="modal fade" id="createProgramModal" tabindex="-1" aria-labelledby="programModalTitle" aria-hidden="true">
        <div class="modal-dialog">
            <form id="programForm" action="${pageContext.request.contextPath}/ProgramServlet" method="post" class="modal-content">
                <div class="modal-header">
                    <h5 class="modal-title" id="programModalTitle">
                        <i class="fas fa-plus me-2"></i>Create New Program
                    </h5>
                    <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                </div>
                <div class="modal-body">
                    <input type="hidden" name="action" id="programAction" value="create" />
                    <input type="hidden" name="programId" id="programId" value="0" />
                    <div class="mb-3">
                        <label class="form-label" for="programName">
                            <i class="fas fa-file-alt me-2"></i>Program Name
                        </label>
                        <input type="text" name="name" id="programName" class="form-control" placeholder="Enter program name..." required />
                    </div>
                    <div class="mb-3">
                        <label class="form-label" for="programDescription">
                            <i class="fas fa-info-circle me-2"></i>Description
                        </label>
                        <textarea name="description" id="programDescription" class="form-control" placeholder="Enter program description..." rows="3"></textarea>
                    </div>
                    <div class="mb-3">
                        <label class="form-label" for="packageId">
                            <i class="fas fa-box me-2"></i>Package
                        </label>
                        <select name="packageId" id="packageId" class="form-select" required>
                            <option value="">Select a package...</option>
                            <c:forEach var="pkg" items="${packages}">
                                <option value="${pkg.packageID}">${pkg.name}</option>
                            </c:forEach>
                        </select>
                    </div>
                </div>
                <div class="modal-footer">
                    <button class="btn btn-success" type="submit">
                        <i class="fas fa-check me-2"></i>Save Program
                    </button>
                    <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">
                        <i class="fas fa-times me-2"></i>Cancel
                    </button>
                </div>
            </form>
        </div>
    </div>

    <!-- Assign Program Modal -->
    <div class="modal fade" id="assignProgramModal" tabindex="-1" aria-labelledby="assignProgramModalLabel" aria-hidden="true">
        <div class="modal-dialog">
            <form action="${pageContext.request.contextPath}/AssignProgramServlet" method="post" class="modal-content">
                <div class="modal-header">
                    <h5 class="modal-title" id="assignProgramModalLabel">
                        <i class="fas fa-paper-plane me-2"></i>Assign Program to Customer
                    </h5>
                    <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                </div>
                <div class="modal-body">
                    <input type="hidden" name="programId" id="assignProgramId" />
                    <c:if test="${not empty error}">
                        <div class="alert alert-danger">
                            <i class="fas fa-exclamation-triangle me-2"></i>${error}
                        </div>
                    </c:if>
                    <div class="mb-3">
                        <label class="form-label" for="customerId">
                            <i class="fas fa-users me-2"></i>Select Customer
                        </label>
                        <c:choose>
                            <c:when test="${not empty customers}">
                                <select name="customerId" id="customerId" class="form-select" required>
                                    <option value="">Choose a customer...</option>
                                    <c:forEach var="customer" items="${customers}">
                                        <option value="${customer.userId}">
                                            ${customer.userName} (${customer.email})
                                        </option>
                                    </c:forEach>
                                </select>
                            </c:when>
                            <c:otherwise>
                                <div class="alert customer-warning-alert alert-warning">
                                    <i class="fas fa-exclamation-triangle me-2"></i>
                                    <strong>No customers found!</strong><br>
                                    You don't have any active contracts with customers yet.
                                    Customers will appear here once they purchase your packages.
                                </div>
                                <select name="customerId" class="form-select" disabled>
                                    <option value="">No customers available</option>
                                </select>
                            </c:otherwise>
                        </c:choose>
                    </div>
                    <div class="mb-3">
                        <label class="form-label" for="startDate">
                            <i class="fas fa-calendar me-2"></i>Start Date
                        </label>
                        <input type="date" name="startDate" id="startDate" class="form-control" required
                               min="<%= java.time.LocalDate.now() %>" />
                        <div class="form-text">
                            <i class="fas fa-info-circle me-1"></i>Start date must be today or in the future.
                        </div>
                    </div>
                </div>
                <div class="modal-footer">
                    <c:choose>
                        <c:when test="${not empty customers}">
                            <button class="btn btn-success" type="submit">
                                <i class="fas fa-check me-2"></i>Assign Program
                            </button>
                        </c:when>
                        <c:otherwise>
                            <button class="btn btn-success" type="submit" disabled>
                                <i class="fas fa-check me-2"></i>Assign Program
                            </button>
                        </c:otherwise>
                    </c:choose>
                    <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">
                        <i class="fas fa-times me-2"></i>Cancel
                    </button>
                </div>
            </form>
        </div>
    </div>

    <!-- Add Exercise Modal -->
    <div class="modal fade" id="addExerciseModal" tabindex="-1" aria-labelledby="addExerciseModalLabel" aria-hidden="true">
        <div class="modal-dialog">
            <form id="addExerciseForm" action="${pageContext.request.contextPath}/AddExerciseToProgramServlet" method="post" class="modal-content">
                <div class="modal-header">
                    <h5 class="modal-title" id="addExerciseModalLabel"><i class="fas fa-dumbbell me-2"></i>Add Exercises to Program</h5>
                    <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                </div>
                <div class="modal-body">
                    <input type="hidden" name="programId" id="addExerciseProgramId" />
                    <div id="exerciseList">
                        <div class="exercise-entry">
                            <div class="mb-2">
                                <label class="form-label" for="exerciseLibraryIds">Exercise</label>
                                <select name="exerciseLibraryIds" class="form-select exercise-select" required onchange="previewExercise(this)">
                                    <option value="">Select an exercise...</option>
                                </select>
                            </div>
                            <div class="mb-2 preview">
                                <iframe class="exercise-preview" width="100%" height="200" style="display:none;" allowfullscreen></iframe>
                                <p class="exercise-description text-muted mt-1"></p>
                                <p class="exercise-details text-muted mt-1"></p>
                            </div>
                            <div class="mb-2">
                                <button type="button" class="btn btn-danger" onclick="removeExerciseEntry(this)">
                                    <i class="fas fa-trash"></i>
                                </button>
                            </div>
                        </div>
                    </div>
                    <button type="button" class="btn btn-outline-primary mt-2" onclick="addExerciseEntry()">
                        <i class="fas fa-plus me-2"></i>Add Another Exercise
                    </button>
                </div>
                <div class="modal-footer">
                    <button type="submit" class="btn btn-success">
                        <i class="fas fa-check me-2"></i>Add Exercises
                    </button>
                    <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">
                        <i class="fas fa-times me-2"></i>Cancel
                    </button>
                </div>
            </form>
        </div>
    </div>

    <!-- View Exercises Modal -->
    <div class="modal fade" id="viewExerciseModal" tabindex="-1" aria-labelledby="viewExerciseModalLabel" aria-hidden="true">
        <div class="modal-dialog modal-lg">
            <div class="modal-content">
                <div class="modal-header">
                    <h5 class="modal-title" id="viewExerciseModalLabel"><i class="fas fa-eye me-2"></i>Program Exercises</h5>
                    <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                </div>
                <div class="modal-body">
                    <div class="exercise-list-container">
                        <table class="table table-striped">
                            <thead>
                                <tr>
                                    <th>Name</th>
                                    <th>Sets</th>
                                    <th>Reps</th>
                                    <th>Rest (s)</th>
                                    <th>Description</th>
                                    <th>Video</th>
                                </tr>
                            </thead>
                            <tbody id="exerciseListTable">
                            </tbody>
                        </table>
                    </div>
                </div>
                <div class="modal-footer">
                    <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">
                        <i class="fas fa-times me-2"></i>Close
                    </button>
                </div>
            </div>
        </div>
    </div>

    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
    <script>
        // Search functionality
        document.getElementById('searchInput').addEventListener('input', function() {
            const searchTerm = this.value.toLowerCase();
            const cards = document.querySelectorAll('.program-card');
            cards.forEach(card => {
                const name = card.querySelector('.program-name').textContent.toLowerCase();
                const description = card.querySelector('.program-description').textContent.toLowerCase();
                if (name.includes(searchTerm) || description.includes(searchTerm)) {
                    card.style.display = '';
                    card.style.animation = 'fadeInUp 0.3s ease forwards';
                } else {
                    card.style.display = 'none';
                }
            });
        });

        // Open Assign Modal
        function openAssignModal(programId, name, description) {
            document.getElementById('assignProgramId').value = programId;
            document.getElementById('assignProgramModalLabel').innerHTML =
                `<i class="fas fa-paper-plane me-2"></i>Assign Program: ${name}`;
            // Optionally display program info
            const modalBody = document.querySelector('#assignProgramModal .modal-body');
            let programInfo = modalBody.querySelector('.program-info-alert');
            if (!programInfo) {
                programInfo = document.createElement('div');
                programInfo.className = 'alert program-info-alert alert-info';
                modalBody.insertBefore(programInfo, modalBody.firstChild);
            }
            programInfo.innerHTML = `<strong>${name}</strong><br><span>${description}</span>`;
            new bootstrap.Modal(document.getElementById('assignProgramModal')).show();
        }

        // Open Add Exercise Modal
       function openAddExerciseModal(programId) {
            if (!programId) {
                showNotification('Invalid program ID', 'error');
                return;
            }

            document.getElementById('addExerciseProgramId').value = programId;
            fetch(`${pageContext.request.contextPath}/GetAvailableExercisesServlet?programId=`+programId)
                .then(response => {
                console.log(response);
                    if (!response.ok) throw new Error('Server error');
                    return response.json();
                })
                .then(data => {
                console.log(data);
                    const select = document.querySelector('#addExerciseModal .exercise-select');
                    if (!select) {
                        showNotification('Exercise select element not found', 'error');
                        return;
                    }
                    select.innerHTML = '<option value="">Select an exercise...</option>';
                    data.forEach(exercise => {
                        const option = document.createElement('option');
                        option.value = exercise.exerciseID;
                        option.textContent = exercise.name;
                        option.dataset.video = exercise.videoURL || '';
                        option.dataset.description = exercise.description || 'No description.';
                        option.dataset.sets = exercise.sets;
                        option.dataset.reps = exercise.reps;
                        option.dataset.rest = exercise.restTimeSeconds;
                        select.appendChild(option);
                    });
                    document.getElementById('exerciseList').innerHTML = document.querySelector('.exercise-entry').outerHTML;
                    new bootstrap.Modal(document.getElementById('addExerciseModal')).show();
                })
                .catch(error => {
                    console.error('Error fetching exercises:', error);
                    showNotification('Error loading exercises', 'error');
                });
        }

        // Open View Exercises Modal
        function openViewExerciseModal(programId) {
            fetch(`${pageContext.request.contextPath}/ViewExercisesServlet?programId=`+programId)
                .then(response => {
                console.log(response);
                    if (!response.ok) throw new Error('Server error');
                    return response.json();
                })
                .then(data => {
                console.log(data);
                    const tbody = document.getElementById('exerciseListTable');
                    tbody.innerHTML = '';
                    if (data.length === 0) {
                        tbody.innerHTML = '<tr><td colspan="6">No exercises added to this program.</td></tr>';
                    } else {
                        data.forEach(exercise => {
                            const safeName = exercise.name.replace(/'/g, "\\'");
                            const videoCell = exercise.videoURL
                                ? '<a href="#" class="video-link" onclick=\'openVideoModal("' + exercise.videoURL.replace(/'/g, "\\'") + '", "' + safeName.replace(/'/g, "\\'") + '")\'>View Video</a>'
                                : 'No video';
                            const row = document.createElement('tr');
                            const description = "<td>"+exercise.description+"</td>"
                            const sets = "<td>"+exercise.sets+"</td>"
                            const reps = "<td>"+exercise.reps+"</td>"
                            const name = "<td>"+exercise.name+"</td>"
                            const video = "<td>"+videoCell+"</td>"
                            const restTimeSeconds = "<td>"+exercise.restTimeSeconds+"</td>"
                            row.innerHTML = name+sets+reps+restTimeSeconds+description+video;
                            tbody.appendChild(row);
                        });
                    }
                    new bootstrap.Modal(document.getElementById('viewExerciseModal')).show();
                })
                .catch(error => {
                    console.error('Error fetching exercises:', error);
                    showNotification('Error loading exercises', 'error');
                });
        }

        // Open Edit Program Modal
        function openEditProgramModal(programId, name, description, packageId) {
            document.getElementById('programModalTitle').textContent = 'Edit Program';
            document.getElementById('programAction').value = 'edit';
            document.getElementById('programId').value = programId;
            document.getElementById('programName').value = name;
            document.getElementById('programDescription').value = description;
            document.getElementById('packageId').value = packageId;
            new bootstrap.Modal(document.getElementById('createProgramModal')).show();
        }

        // Add Exercise Entry
        function addExerciseEntry() {
            const exerciseList = document.getElementById('exerciseList');
            const entry = document.createElement('div');
            entry.className = 'exercise-entry';
            entry.innerHTML = `
                <div class="mb-2">
                    <label class="form-label" for="exerciseLibraryIds">Exercise</label>
                    <select name="exerciseLibraryIds" class="form-select exercise-select" required onchange="previewExercise(this)">
                        <option value="">Select an exercise...</option>
                    </select>
                </div>
                <div class="mb-2 preview">
                    <iframe class="exercise-preview" width="100%" height="200" style="display:none;" allowfullscreen></iframe>
                    <p class="exercise-description text-muted mt-1"></p>
                    <p class="exercise-details text-muted mt-1"></p>
                </div>
                <div class="mb-2">
                    <button type="button" class="btn btn-danger" onclick="removeExerciseEntry(this)">
                        <i class="fas fa-trash"></i>
                    </button>
                </div>
            `;
            exerciseList.appendChild(entry);
            const firstSelect = document.querySelector('.exercise-select');
            const newSelect = entry.querySelector('.exercise-select');
            newSelect.innerHTML = firstSelect.innerHTML;
        }

        // Remove Exercise Entry
        function removeExerciseEntry(button) {
            if (document.querySelectorAll('.exercise-entry').length > 1) {
                button.closest('.exercise-entry').remove();
            } else {
                alert('At least one exercise is required.');
            }
        }

        // Preview Exercise
        function previewExercise(select) {
            const entry = select.closest('.exercise-entry');
            const iframe = entry.querySelector('.exercise-preview');
            const desc = entry.querySelector('.exercise-description');
            const details = entry.querySelector('.exercise-details');
            const option = select.selectedOptions[0];
            if (option.dataset.video) {
                iframe.src = option.dataset.video;
                iframe.style.display = 'block';
            } else {
                iframe.src = '';
                iframe.style.display = 'none';
            }
            desc.textContent = option.dataset.description || 'No description.';
            details.textContent = "Sets:" +option.dataset.sets +", Reps: "+option.dataset.reps +", Rest:"+ option.dataset.rest + "s";
        }

        // Open Video Modal
        function openVideoModal(videoUrl, title) {
            console.log("videoUrl"+videoUrl);
            // Create modal wrapper
            const modalDiv = document.createElement('div');
            modalDiv.className = 'modal fade';
            modalDiv.tabIndex = -1;
            modalDiv.innerHTML = `
                <div class="modal-dialog modal-lg modal-dialog-centered">
                    <div class="modal-content">
                        <div class="modal-header">
                            <h5 class="modal-title">${title}</h5>
                            <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                        </div>
                        <div class="modal-body text-center">
                            <video width="100%" height="400" controls style="border-radius:10px;">
                                <source src="${videoUrl}" type="video/mp4">
                                Your browser does not support the video tag.
                            </video>
                        </div>
                    </div>
                </div>
            `;
            document.body.appendChild(modalDiv);
            const modal = new bootstrap.Modal(modalDiv);
            modal.show();
            modalDiv.addEventListener('hidden.bs.modal', () => modalDiv.remove());
        }

        // Notifications
        <% if (request.getParameter("error") != null) { %>
            showNotification('<%= request.getParameter("error").replace("'", "\\'") %>', 'error');
        <% } %>
        <% if (request.getParameter("success") != null) { %>
            showNotification('<%= request.getParameter("success").replace("'", "\\'") %>', 'success');
        <% } %>

       function showNotification(message, type) {
            if (!message) return; // Prevent empty notifications
            const notification = document.createElement('div');
            const alertClass = type === 'error' ? 'danger' : 'success';
            const iconClass = type === 'error' ? 'exclamation-triangle' : 'check-circle';
            notification.className = `alert alert-${alertClass} notification-toast`;
            notification.innerHTML =
                `<i class="fas fa-${iconClass} me-2"></i>`+message+`<button type="button" class="btn-close ms-auto" onclick="this.parentElement.remove()"></button>`;
            document.body.appendChild(notification);
            setTimeout(() => {
                if (notification.parentElement) {
                    notification.style.animation = 'slideOutRight 0.3s ease';
                    setTimeout(() => notification.remove(), 300);
                }
            }, 5000);
        }

        // Form Submission Feedback
        document.querySelectorAll('form').forEach(form => {
            form.addEventListener('submit', function() {
                const submitBtn = this.querySelector('button[type="submit"]');
                if (submitBtn) {
                    submitBtn.innerHTML = '<i class="fas fa-spinner fa-spin me-2"></i>Processing...';
                    submitBtn.disabled = true;
                }
            });
        });
    </script>

    <!-- Modal -->
    <div class="modal fade" id="createProgramModal" tabindex="-1" aria-labelledby="createProgramModalLabel" aria-hidden="true">
        <div class="modal-dialog">
            <div class="modal-content">
                <div class="modal-header">
                    <h5 class="modal-title" id="createProgramModalLabel">Create New Program</h5>
                    <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                </div>
                <div class="modal-body">
                    <!-- Example form -->
                    <form action="addProgram" method="post">
                        <div class="mb-3">
                            <label for="programName" class="form-label">Program Name</label>
                            <input type="text" class="form-control" id="programName" name="programName" required>
                        </div>
                        <div class="mb-3">
                            <label for="programDescription" class="form-label">Description</label>
                            <textarea class="form-control" id="programDescription" name="programDescription" rows="3"></textarea>
                        </div>
                        <button type="submit" class="btn btn-primary">Save Program</button>
                    </form>
                </div>
            </div>
        </div>
    </div>

</body>
</html>