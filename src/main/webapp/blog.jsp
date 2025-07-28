<%@page import="model.User"%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ page import="java.util.List" %>
<%@ page import="model.Blog" %>
<%@ page import="dao.BlogDAO" %>
<%
    User user = (User) session.getAttribute("user");
%>
<!DOCTYPE html>
<html lang="zxx">
 
<head>
    <meta charset="UTF-8">
    <meta name="description" content="Gutim Template">
    <meta name="keywords" content="Gutim, unica, creative, html">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <meta http-equiv="X-UA-Compatible" content="ie=edge">
    <title>Gutim | Template</title>

    <!-- Google Font -->
    <link href="https://fonts.googleapis.com/css?family=Poppins:300,400,500,600,700,800,900&display=swap" rel="stylesheet">
    
    <!-- Css Styles -->
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/bootstrap.min.css" type="text/css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/font-awesome.min.css" type="text/css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/owl.carousel.min.css" type="text/css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/magnific-popup.css" type="text/css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/slicknav.min.css" type="text/css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css" type="text/css">
            <link rel="stylesheet" href="${pageContext.request.contextPath}/stylecss/header.css" type="text/css">
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
            <div class="row">
                <div class="col-lg-12">
                    <div class="breadcrumb-text">
                        <h2>Blog</h2>
                        <div class="breadcrumb-option">
                            <a href="${pageContext.request.contextPath}/index.jsp"><i class="fa fa-home"></i> Home</a>
                            <span>Blog</span>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </section>
    <!-- Breadcrumb Section End -->

   <!-- Blog Section Begin -->
<section class="blog-section spad">
    <div class="container">
        <div class="row align-items-center">
    <div class="col-lg-6 text-left">
        <h2>Blogs</h2>
    </div>
    <div class="col-lg-6 text-right">
        <c:if test="${user != null && (user.role == 'Admin' || user.role == 'Trainer')}">
            <button class="btn btn-primary mr-2" onclick="showAddForm()">Add</button>
            <button class="btn btn-secondary" onclick="showEditForm()">Edit</button>
        </c:if>
    </div>
</div>

        <!-- Danh sách blog -->
        <div class="row">
            <c:forEach var="blog" items="${blogs}">
                <div class="col-lg-4 col-md-6">
                    <div class="single-blog-item">
                        <a href="${pageContext.request.contextPath}/BlogDetailServlet?action=view&id=${blog.id}" class="thumbnail-wrapper">
                            <img src="${blog.thumbnail}" alt="${blog.title}"
                                 onerror="this.onerror=null;this.src='${pageContext.request.contextPath}/img/default-thumbnail.jpg';">
                        </a>
                        <div class="blog-widget">
                            <div class="bw-date">
                                <fmt:formatDate value="${blog.createdAt}" pattern="dd-MM-yyyy"/>
                            </div>
                            <a href="#" class="tag">#${blog.tag != null ? blog.tag : "General"}</a>
                        </div>
                        <h4>
                            <a href="${pageContext.request.contextPath}/BlogDetailServlet?action=view&id=${blog.id}">
                                ${blog.title}
                            </a>
                        </h4>
                    </div>
                </div>
            </c:forEach>
        </div>

        <!-- Pagination -->
        <div class="row">
            <div class="col-lg-12 text-center">
                <div class="pagination">
                    <c:forEach begin="1" end="${totalPages}" var="i">
                        <c:choose>
                            <c:when test="${i == currentPage}">
                                <span>${i}</span>
                            </c:when>
                            <c:otherwise>
                                <a href="${pageContext.request.contextPath}/BlogServlet?page=${i}">${i}</a>
                            </c:otherwise>
                        </c:choose>
                    </c:forEach>
                </div>
            </div>
        </div>
    </div>
</section>

<!-- Blog Section End -->


    <!-- Popup Add Form -->
<div id="addForm" style="display:none; position:fixed; top:50%; left:50%; transform:translate(-50%, -50%);
    width:50%; background:#fff; border:1px solid #ccc; padding:20px; z-index:9999; box-shadow: 0 0 10px rgba(0,0,0,0.2);">
    <h4>Add New Blog</h4>
    <form action="AddBlogServlet" method="post">
        <div class="form-group">
            <label>Title:</label>
            <input type="text" name="title" class="form-control" required />
        </div>
        <div class="form-group">
            <label>Thumbnail URL:</label>
            <input type="text" name="thumbnail" class="form-control" />
        </div>
        <div class="form-group">
            <label>Tag:</label>
            <input type="text" name="tag" class="form-control" />
        </div>
        <button type="submit" class="btn btn-success">Submit</button>
        <button type="button" class="btn btn-danger" onclick="hideAddForm()">Cancel</button>
    </form>
</div>

<!-- Popup Edit Form -->
<div id="editForm" style="display:none; position:fixed; top:50%; left:50%; transform:translate(-50%, -50%);
    width:50%; background:#fff; border:1px solid #ccc; padding:20px; z-index:9999; box-shadow: 0 0 10px rgba(0,0,0,0.2);">
    <h4>Edit Blog</h4>
    <form action="EditBlogServlet" method="get">
        <div class="form-group">
            <label>Enter Blog ID to Edit:</label>
            <input type="number" name="id" class="form-control" required />
        </div>
        <button type="submit" class="btn btn-warning">Edit</button>
        <button type="button" class="btn btn-danger" onclick="hideEditForm()">Cancel</button>
    </form>
</div>

<script>
    function showAddForm() {
        document.getElementById("addForm").style.display = "block";
    }
    function hideAddForm() {
        document.getElementById("addForm").style.display = "none";
    }

    function showEditForm() {
        document.getElementById("editForm").style.display = "block";
    }
    function hideEditForm() {
        document.getElementById("editForm").style.display = "none";
    }
</script>

    
    <!-- Js Plugins -->
    <script src="${pageContext.request.contextPath}/js/jquery-3.3.1.min.js"></script>
    <script src="${pageContext.request.contextPath}/js/bootstrap.min.js"></script>
    <script src="${pageContext.request.contextPath}/js/jquery.magnific-popup.min.js"></script>
    <script src="${pageContext.request.contextPath}/js/mixitup.min.js"></script>
    <script src="${pageContext.request.contextPath}/js/jquery.slicknav.js"></script>
    <script src="${pageContext.request.contextPath}/js/masonry.pkgd.min.js"></script>
    <script src="${pageContext.request.contextPath}/js/owl.carousel.min.js"></script>
    <script src="${pageContext.request.contextPath}/js/main.js"></script>
    
   <jsp:include page="footer.jsp" />
</body>

</html>