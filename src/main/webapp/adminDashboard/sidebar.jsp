<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%
    String context = request.getContextPath();
%>
<div class="bg-dark border-right" id="sidebar-wrapper">
    <div class="sidebar-heading text-white border-bottom bg-dark p-3">
        <h4>Admin Dashboard</h4>
    </div>
    
    <div class="accordion accordion-flush" id="sidebarAccordion">

        <!-- Manage User -->
        <div class="accordion-item">
            <h2 class="accordion-header" id="headingUser">
                <button class="accordion-button collapsed" type="button" data-target="#collapseUser">
                    <i class="fas fa-users-cog me-2"></i> Manage User
                </button>
            </h2>
            <div id="collapseUser" class="accordion-collapse collapse">
                <div class="accordion-body p-0">
                    <div class="list-group list-group-flush">
                        <a href="<%=context%>/UserServlet?action=view" class="list-group-item list-group-item-action sidebar-item">
                            <i class="fas fa-list-alt me-2"></i> View User List
                        </a>
                        <a href="<%=context%>/ReportServlet" class="list-group-item list-group-item-action sidebar-item">
                            <i class="fas fa-exclamation-triangle me-2"></i> Violation Reports
                        </a>
                    </div>
                </div>
            </div>
        </div>

        <!-- Manage Content -->
        <div class="accordion-item">
            <h2 class="accordion-header" id="headingContent">
                <button class="accordion-button collapsed" type="button" data-target="#collapseContent">
                    <i class="fas fa-file-alt me-2"></i> Manage Content
                </button>
            </h2>
            <div id="collapseContent" class="accordion-collapse collapse">
                <div class="accordion-body p-0">
                    <div class="list-group list-group-flush">
                        <a href="#" class="list-group-item list-group-item-action sidebar-item">
                            <i class="fas fa-edit me-2"></i> Edit Content
                        </a>
                        <a href="#" class="list-group-item list-group-item-action sidebar-item">
                            <i class="fas fa-plus-circle me-2"></i> Add Content
                        </a>
                        <a href="#" class="list-group-item list-group-item-action sidebar-item">
                            <i class="fas fa-trash-alt me-2"></i> Delete Content
                        </a>
                    </div>
                </div>
            </div>
        </div>

        <!-- Reports -->
        <div class="accordion-item">
            <h2 class="accordion-header" id="headingReports">
                <button class="accordion-button collapsed" type="button" data-target="#collapseReports">
                    <i class="fas fa-chart-bar me-2"></i> Reports
                </button>
            </h2>
            <div id="collapseReports" class="accordion-collapse collapse">
                <div class="accordion-body p-0">
                    <div class="list-group list-group-flush">
                        <a href="#" class="list-group-item list-group-item-action sidebar-item">
                            <i class="fas fa-dollar-sign me-2"></i> Monthly Revenue
                        </a>
                    </div>
                </div>
            </div>
        </div>

    </div>
</div>

<!-- ✅ JavaScript xử lý accordion xổ ra -->
<script>
document.addEventListener("DOMContentLoaded", function () {
    const accordionButtons = document.querySelectorAll(".accordion-button");

    accordionButtons.forEach(button => {
        button.addEventListener("click", function () {
            const targetId = button.getAttribute("data-target");
            const target = document.querySelector(targetId);
            const isExpanded = target.classList.contains("show");

            // Đóng tất cả các mục
            document.querySelectorAll(".accordion-collapse").forEach(collapse => collapse.classList.remove("show"));
            document.querySelectorAll(".accordion-button").forEach(btn => btn.classList.add("collapsed"));

            // Nếu đang đóng thì mở ra
            if (!isExpanded) {
                target.classList.add("show");
                button.classList.remove("collapsed");
            }
        });
    });
});
</script>
