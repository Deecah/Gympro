<%@page import="model.Comment"%>
<%@page import="java.util.List"%>
<%@page import="model.Blog"%>
<%@page import="model.User"%>
<%@ page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

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
        <link href="https://fonts.googleapis.com/css?family=Poppins:300,400,500,600,700,800,900&display=swap"
              rel="stylesheet">

        <!-- Css Styles -->
        <link rel="stylesheet" href="${pageContext.request.contextPath}/css/bootstrap.min.css" type="text/css">
        <link rel="stylesheet" href="${pageContext.request.contextPath}/css/font-awesome.min.css" type="text/css">
        <link rel="stylesheet" href="${pageContext.request.contextPath}/css/owl.carousel.min.css" type="text/css">
        <link rel="stylesheet" href="${pageContext.request.contextPath}/css/magnific-popup.css" type="text/css">
        <link rel="stylesheet" href="${pageContext.request.contextPath}/css/slicknav.min.css" type="text/css">
        <link rel="stylesheet" href="${pageContext.request.contextPath}/css/style.css" type="text/css">
    </head>

    <body>
        <!-- Page Preloder -->
        <div id="preloder">
            <div class="loader"></div>
        </div>

        <jsp:include page="header.jsp" />

        <!-- Blog Details Hero Section Begin -->
        <section class="blog-details-hero set-bg" data-setbg="https://images.unsplash.com/photo-1581009146145-b5ef050c2e1e?q=80&w=1170&auto=format&fit=crop&ixlib=rb-4.1.0&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D">
            <div class="container">
                <div class="row">
                    <div class="col-lg-12">
                        <div class="bd-hero-text">
                            <h2>${blog.title}</h2>
                        </div>
                    </div>
                </div>
            </div>
        </section>
        <!-- Blog Details Hero Section End -->

        <!-- Blog Details Section Begin -->
        <section class="blog-details spad">
            <div class="container">
                <div class="row">
                    <div class="col-lg-10 offset-lg-1">
                        <div class="bd-text">
                            <!-- Tiêu đề blog -->
                            <div class="bd-title">
                                <h2>${blog.title}</h2>
                                <div class="blog-meta">
                                    <span><fmt:formatDate value="${blog.createdAt}" pattern="dd/MM/yyyy"/></span>
                                </div>
                            </div>

                            <!-- Hình ảnh (nếu có) -->
                            <c:if test="${not empty blog.imagesUrl}">
                                <div class="bd-pic">
                                    <div class="row">
                                        <c:forEach var="img" items="${blog.imagesUrl}">
                                            <div class="col-lg-6">
                                                <img src="${img.imageUrl}" alt="" style="width:100%; height:auto;">
                                            </div>
                                        </c:forEach>
                                    </div>
                                </div>
                            </c:if>

                            <!-- Nội dung -->
                            <div class="bd-more-text">
                                <div class="bm-item">
                                    <c:if test="${not empty blog.content}">
                                        <p>${blog.content}</p>
                                    </c:if>
                                </div>
                            </div>

                            <hr class="separator-line">

                            <!-- Danh sách bình luận -->
                            <div class="leave-comment">
                                <h3>Comment</h3>
                                <div class="comment-list">
                                    <c:forEach var="cmt" items="${commentList}">
                                        <div class="comment-item">
                                            <img src="${commentAvaMap[cmt.userId]}" alt="Avatar" class="avatar">
                                            <div>
                                                <div class="comment-content">
                                                    <div class="comment-header">
                                                        <strong>${cmt.userName}</strong>
                                                        <c:if test="${currentUser != null && currentUser.id == cmt.userId}">
                                                            <div class="comment-options-wrapper">
                                                                <span class="comment-options" onclick="toggleOptions(this)">...</span>
                                                                <ul class="comment-menu">
                                                                    <li onclick="editComment(${cmt.commentId}, '${cmt.content}')">Edit</li>
                                                                    <li onclick="deleteComment(${cmt.commentId})">Delete</li>
                                                                </ul>
                                                            </div>
                                                        </c:if>
                                                    </div>
                                                    <p>${cmt.content}</p>
                                                </div>
                                                <div class="comment-meta">
                                                    <span><fmt:formatDate value="${cmt.createdAt}" pattern="dd/MM/yyyy HH:mm"/></span>
                                                    <c:if test="${not empty cmt.createdAt}">
                                                        <fmt:formatDate value="${cmt.createdAt}" pattern="dd/MM/yyyy HH:mm"/>
                                                    </c:if>
                                                </div>
                                            </div>
                                        </div>
                                    </c:forEach>
                                </div>

                                <!-- Form bình luận -->
                                <form action="CommentServlet" method="POST" id="commentForm">
                                    <input type="hidden" name="commentId" id="commentIdInput" value="">
                                    <textarea class="comment-box" id="commentBox" name="content" placeholder="Write your comment..." oninput="autoResize(this)"></textarea>
                                    <button type="submit" class="send-comment-btn" id="submitBtn" name="action" value="send">Gửi</button>
                                </form>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </section>
        <!-- Blog Details Section End -->


        <!-- Latest Blog Section Begin -->
        <section class="latest-blog-section recommend spad">
            <div class="container">
                <div class="row">
                    <div class="col-lg-12">
                        <h3>Recommended</h3>
                    </div>
                </div>
                <div class="row">
                    <div class="col-lg-4">
                        <div class="single-blog-item">
                            <img src="img/blog/blog-1.jpg" alt="">
                            <div class="blog-widget">
                                <div class="bw-date">February 17, 2019</div>
                                <a href="#" class="tag">#Gym</a>
                            </div>
                            <h4><a href="#">10 States At Risk of Rural Hospital Closures</a></h4>
                        </div>
                    </div>
                    <div class="col-lg-4">
                        <div class="single-blog-item">
                            <img src="img/blog/blog-2.jpg" alt="">
                            <div class="blog-widget">
                                <div class="bw-date">February 17, 2019</div>
                                <a href="#" class="tag">#Sport</a>
                            </div>
                            <h4><a href="#">Diver who helped save Thai soccer team</a></h4>
                        </div>
                    </div>
                    <div class="col-lg-4">
                        <div class="single-blog-item">
                            <img src="img/blog/blog-3.jpg" alt="">
                            <div class="blog-widget">
                                <div class="bw-date">February 17, 2019</div>
                                <a href="#" class="tag">#Body</a>
                            </div>
                            <h4><a href="#">Man gets life in prison for stabbing</a></h4>
                        </div>
                    </div>
                </div>
            </div>
        </section>
        <!-- Latest Blog Section End -->

        <!-- Footer Section Begin -->
        <footer class="footer-section">
            <div class="container">
                <div class="row">
                    <div class="col-lg-4">
                        <div class="contact-option">
                            <span>Phone</span>
                            <p>(0905) 000 666 - (0905) 666 000</p>
                        </div>
                    </div>
                    <div class="col-lg-4">
                        <div class="contact-option">
                            <span>Address</span>
                            <p>Khu do thi FPT City, Ngu Hanh Son, Da Nang</p>
                        </div>
                    </div>
                    <div class="col-lg-4">
                        <div class="contact-option">
                            <span>Email</span>
                            <p>swptest391@gmail.com</p>
                        </div>
                    </div>
                </div>
                <br>
                <div class="copyright-text">
                    <p>&copy;<p><!-- Link back to Colorlib can't be removed. Template is licensed under CC BY 3.0. -->
                        Copyright &copy;<script>document.write(new Date().getFullYear());</script> All rights reserved | This template is made with <i class="fa fa-heart" aria-hidden="true"></i> by D02-RT01
                        <!-- Link back to Colorlib can't be removed. Template is licensed under CC BY 3.0. --></p></p>
                    <div class="footer-social">
                        <a href="#"><i class="fa fa-facebook"></i></a>
                        <a href="#"><i class="fa fa-instagram"></i></a>
                    </div>
                </div>
            </div>
        </footer>
        <!-- Footer Section End -->

        <!-- Js Plugins -->
        <script src="${pageContext.request.contextPath}/js/jquery-3.3.1.min.js"></script>
        <script src="${pageContext.request.contextPath}/js/bootstrap.min.js"></script>
        <script src="${pageContext.request.contextPath}/js/jquery.magnific-popup.min.js"></script>
        <script src="${pageContext.request.contextPath}/js/mixitup.min.js"></script>
        <script src="${pageContext.request.contextPath}/js/jquery.slicknav.js"></script>
        <script src="${pageContext.request.contextPath}/js/owl.carousel.min.js"></script>
        <script src="${pageContext.request.contextPath}/js/main.js"></script>
        <script>
                            function autoResize(textarea) {
                                textarea.style.height = 'auto'; // reset v? auto
                                textarea.style.height = textarea.scrollHeight + 'px'; // gán l?i theo n?i dung
                            }
        </script>
        <script>
            function toggleOptions(el) {
                const menu = el.nextElementSibling;
                menu.style.display = menu.style.display === 'block' ? 'none' : 'block';
                // Ẩn menu khác
                document.querySelectorAll('.comment-menu').forEach(m => {
                    if (m !== menu)
                        m.style.display = 'none';
                });
            }

            // Ẩn khi click ngoài menu
            document.addEventListener('click', function (e) {
                if (!e.target.closest('.comment-options-wrapper')) {
                    document.querySelectorAll('.comment-menu').forEach(m => m.style.display = 'none');
                }
            });

            function openReportModal() {
                document.getElementById('reportModal').style.display = 'flex';
            }

            function closeReportModal() {
                document.getElementById('reportModal').style.display = 'none';
            }

            function submitReport() {
                const reason = document.querySelector('#reportModal textarea').value;
                if (reason.trim() === '') {
                    alert('Vui lòng nhập lý do.');
                    return;
                }
                // Xử lý gửi dữ liệu tại đây
                alert('Báo cáo đã được gửi!');
                closeReportModal();
            }
            function editComment(id, content) {
                document.getElementById('commentBox').value = content;
                document.getElementById('commentIdInput').value = id;
                document.getElementById('submitBtn').innerText = 'Save';
                document.getElementById('submitBtn').value = 'edit';
            }

        </script>

        <div class="modal-overlay" id="reportModal">
            <div class="report-modal">
                <h4>Báo cáo bình luận</h4>
                <textarea placeholder="Enter reason..."></textarea><br>
                <button onclick="submitReport()">Send Report</button>
                <button class="close-btn" onclick="closeReportModal()">Cancel</button>
            </div>
        </div>

    </body>

</html>