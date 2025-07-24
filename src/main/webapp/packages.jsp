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
        <link rel="stylesheet" href="${pageContext.request.contextPath}/stylecss/chat.css" type="text/css">
        <style>
            .package-card:hover {
                transform: scale(1.02);
                box-shadow: 0 8px 20px rgba(0, 0, 0.5, 0.5) !important;
            }
            .search-banner {
                background-color: #f8f9fa;
            }

            .search-banner h2 {
                font-size: 26px;
                font-weight: 600;
                color: #1a73e8;
            }

            .search-banner input::placeholder {
                color: #888;
            }

            .search-banner input:focus {
                border-color: #1a73e8;
                box-shadow: 0 0 0 0.2rem rgba(26, 115, 232, 0.25);
            }

        </style>
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
        <jsp:include page="footer.jsp" />
    </body>
</html>
