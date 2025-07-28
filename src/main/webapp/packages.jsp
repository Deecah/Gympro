<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="model.User" %>
<%@ taglib uri="jakarta.tags.core" prefix="c"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<%
    User user = (User) session.getAttribute("user");
%>
<!DOCTYPE html>
<html lang="en">

    <head>
        <meta charset="UTF-8">
        <meta name="description" content="Gympro Template">
        <meta name="keywords" content="Gympro, unica, creative, html">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <meta http-equiv="X-UA-Compatible" content="ie=edge">
        <title>Gympro</title>
        <!-- Google Font -->
        <link href="https://fonts.googleapis.com/css?family=Poppins:300,400,500,600,700,800,900&display=swap" rel="stylesheet">
        <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/4.7.0/css/font-awesome.min.css">
        <!-- Css Styles -->
        <link rel="stylesheet" href="css/bootstrap.min.css" type="text/css">
        <link rel="stylesheet" href="css/font-awesome.min.css" type="text/css">
        <link rel="stylesheet" href="css/owl.carousel.min.css" type="text/css">
        <link rel="stylesheet" href="css/magnific-popup.css" type="text/css">
        <link rel="stylesheet" href="css/slicknav.min.css" type="text/css">
        <link rel="stylesheet" href="css/style.css" type="text/css">
        <link rel="stylesheet" href="${pageContext.request.contextPath}/stylecss/header.css" type="text/css">
        <link rel="stylesheet" href="${pageContext.request.contextPath}/stylecss/packages.css" type="text/css">
    </head>
    <body>

        <!-- Page Preloder -->
        <div id="preloder">
            <div class="loader"></div>
        </div>

        <jsp:include page="header.jsp" />

        <!-- Breadcrumb Section Begin -->
        <section class="breadcrumb-section set-bg" data-setbg="img/breadcrumb/classes-breadcrumb.jpg">
            <div class="container">
                <div class="row justify-content-center">
                    <div class="col-lg-10 text-center">
                        <div class="breadcrumb-text">
                            <h2 class="text-white fw-bold">Package</h2>
                            <div class="breadcrumb-option mb-3">
                                <a href="./index.jsp"><i class="fa fa-home"></i> Home</a>
                                <span>Packages</span>
                            </div>

                            <!-- Search Bar tích hợp -->
                            <form action="SearchServlet" method="get" class="d-flex justify-content-center">
                                <input type="text" name="keyword"
                                       class="form-control w-50 me-2 rounded-pill px-4"
                                       placeholder="Find Trainer or Package..." required />
                                <button type="submit" class="btn btn-light text-primary rounded-pill px-4 fw-semibold">
                                    Find
                                </button>
                            </form>
                            <!-- End Search Bar -->
                        </div>
                    </div>
                </div>
            </div>
        </section>

        <!-- Breadcrumb Section End -->
        


        <!-- Package Section Begin -->
        <section class="blog-section spad">
            <div class="container">
                <div class="row">
                    <c:choose>
                        <c:when test="${not empty packages}">
                            <c:forEach var="p" items="${packages}">
                                <c:set var="imageUrl" value="${empty p.imageUrl ? 'images/default.jpg' : (fn:startsWith(p.imageUrl, 'http') ? p.imageUrl : pageContext.request.contextPath.concat(p.imageUrl))}" />
                                <div class="col-lg-4 col-md-6 mb-4">
                                    <a href="${pageContext.request.contextPath}/PackageDetailServlet?packageId=${p["packageID"]}" 
                                       style="text-decoration: none; color: inherit;">
                                        <div class="single-blog-item card h-100 shadow-sm border-0 package-card"
                                             style="transition: transform 0.3s ease, box-shadow 0.3s ease;">
                                            <img src="${imageUrl}" class="card-img-top"
                                                 alt="${p.name}"
                                                 style="height: 200px; object-fit: cover; border-top-left-radius: 0.5rem; border-top-right-radius: 0.5rem;" />
                                            <div class="card-body">
                                                <div class="blog-widget mb-2">
                                                    <div class="bw-date text-muted">Duration: ${p.duration} days</div>
                                                    <span class="tag badge bg-primary text-white">#${p.price} VNĐ</span>
                                                </div>
                                                <h5 class="card-title mt-2">${p.name}</h5>
                                                <p class="card-text text-muted">${p.description}</p>
                                            </div>
                                            <div class="card-footer bg-transparent border-0 text-end">
                                                <c:choose>
                                                    <c:when test="${purchaseStatus[p.packageID]}">
                                                        <button class="btn btn-secondary btn-sm" disabled>Already Purchased</button>
                                                    </c:when>
                                                    <c:otherwise>
                                                        <a href="${pageContext.request.contextPath}/purchase?packageId=${p.packageID}" 
                                                           class="btn btn-outline-success btn-sm">Purchase Package</a>
                                                    </c:otherwise>
                                                </c:choose>
                                            </div>
                                        </div>
                                    </a>
                                </div>
                            </c:forEach>
                        </c:when>
                        <c:otherwise>
                            <div class="col-12 text-center py-5">
                                <p class="text-muted">No packages available.</p>
                            </div>
                        </c:otherwise>
                    </c:choose>
                </div>
            </div>
        </section>

        <%-- Đảm bảo các biến phân trang luôn có giá trị mặc định --%>
        <c:set var="currentPage" value="${currentPage != null ? currentPage : 1}" />
        <c:set var="totalPages" value="${totalPages != null ? totalPages : 1}" />

        <!-- Pagination Begin -->
        <c:if test="${totalPages > 1}">
            <div class="d-flex justify-content-center mt-4">
                <nav>
                    <ul class="pagination">
                        <c:if test="${currentPage > 1}">
                            <li class="page-item">
                                <a class="page-link" href="CustomerPackageServlet?page=${currentPage - 1}">Previous</a>
                            </li>
                        </c:if>
                        
                        <%-- Logic hiển thị tối đa 5 trang --%>
                        <c:set var="maxVisiblePages" value="5" />
                        <c:set var="startPage" value="1" />
                        <c:set var="endPage" value="${totalPages}" />
                        
                        <%-- Tính toán range hiển thị --%>
                        <c:choose>
                            <c:when test="${totalPages <= maxVisiblePages}">
                                <%-- Hiển thị tất cả nếu tổng số trang <= 5 --%>
                                <c:set var="startPage" value="1" />
                                <c:set var="endPage" value="${totalPages}" />
                            </c:when>
                            <c:when test="${currentPage <= 3}">
                                <%-- Hiển thị trang 1-5 nếu đang ở trang đầu --%>
                                <c:set var="startPage" value="1" />
                                <c:set var="endPage" value="5" />
                            </c:when>
                            <c:when test="${currentPage >= totalPages - 2}">
                                <%-- Hiển thị 5 trang cuối nếu đang ở cuối --%>
                                <c:set var="startPage" value="${totalPages - 4}" />
                                <c:set var="endPage" value="${totalPages}" />
                            </c:when>
                            <c:otherwise>
                                <%-- Hiển thị 2 trang trước và 2 trang sau trang hiện tại --%>
                                <c:set var="startPage" value="${currentPage - 2}" />
                                <c:set var="endPage" value="${currentPage + 2}" />
                            </c:otherwise>
                        </c:choose>
                        
                        <%-- Hiển thị dấu "..." nếu có trang bị ẩn ở đầu --%>
                        <c:if test="${startPage > 1}">
                            <li class="page-item">
                                <a class="page-link" href="CustomerPackageServlet?page=1">1</a>
                            </li>
                            <c:if test="${startPage > 2}">
                                <li class="page-item disabled">
                                    <span class="page-link">...</span>
                                </li>
                            </c:if>
                        </c:if>
                        
                        <%-- Hiển thị các trang trong range --%>
                        <c:forEach begin="${startPage}" end="${endPage}" var="i">
                            <li class="page-item ${i == currentPage ? 'active' : ''}">
                                <a class="page-link" href="CustomerPackageServlet?page=${i}">${i}</a>
                            </li>
                        </c:forEach>
                        
                        <%-- Hiển thị dấu "..." nếu có trang bị ẩn ở cuối --%>
                        <c:if test="${endPage < totalPages}">
                            <c:if test="${endPage < totalPages - 1}">
                                <li class="page-item disabled">
                                    <span class="page-link">...</span>
                                </li>
                            </c:if>
                            <li class="page-item">
                                <a class="page-link" href="CustomerPackageServlet?page=${totalPages}">${totalPages}</a>
                            </li>
                        </c:if>
                        
                        <c:if test="${currentPage < totalPages}">
                            <li class="page-item">
                                <a class="page-link" href="CustomerPackageServlet?page=${currentPage + 1}">Next</a>
                            </li>
                        </c:if>
                    </ul>
                </nav>
            </div>
        </c:if>
        <!-- Pagination End -->
        
        <!-- Package Section End -->

        <jsp:include page="footer.jsp" />
        <script>
            function toggleMenu() {
                const menu = document.getElementById("dropdownMenu");
                menu.style.display = (menu.style.display === "block") ? "none" : "block";
            }
            window.addEventListener("click", function (e) {
                const menu = document.getElementById("dropdownMenu");
                const avatar = document.querySelector(".header-avatar img");
                if (!menu.contains(e.target) && !avatar.contains(e.target)) {
                    menu.style.display = "none";
                }
            });
        </script>

        <!-- Js Plugins -->
        <script src="js/jquery-3.3.1.min.js"></script>
        <script src="js/bootstrap.min.js"></script>
        <script src="js/jquery.magnific-popup.min.js"></script>
        <script src="js/mixitup.min.js"></script>
        <script src="js/jquery.slicknav.js"></script>
        <script src="js/masonry.pkgd.min.js"></script>
        <script src="js/owl.carousel.min.js"></script>
        <script src="js/main.js"></script>
    </body>
</html>
