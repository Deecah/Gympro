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
        <title>Blog Detail</title>

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
        
        <style>
            /* Comment List Styling */
            .comment-list {
                margin-bottom: 30px;
            }
            
            .comment-item {
                display: flex;
                margin-bottom: 20px;
                padding: 15px;
                background: #ffffff;
                border-radius: 8px;
                box-shadow: 0 2px 8px rgba(0,0,0,0.1);
                transition: transform 0.2s ease;
            }
            
            .comment-item:hover {
                transform: translateY(-2px);
                box-shadow: 0 4px 12px rgba(0,0,0,0.15);
            }
            
            .comment-item .avatar {
                width: 50px;
                height: 50px;
                border-radius: 50%;
                margin-right: 15px;
                object-fit: cover;
                border: 2px solid #e9ecef;
            }
            
            .comment-content {
                flex: 1;
            }
            
            .comment-header {
                display: flex;
                justify-content: space-between;
                align-items: center;
                margin-bottom: 8px;
            }
            
            .comment-header strong {
                color: #333;
                font-size: 16px;
            }
            
            .comment-content p {
                color: #555;
                line-height: 1.6;
                margin: 0;
            }
            
            .comment-meta {
                margin-top: 8px;
            }
            
            .comment-meta span {
                font-size: 12px;
                color: #888;
            }
            
            /* Comment Options */
            .comment-options-wrapper {
                position: relative;
            }
            
            .comment-options {
                cursor: pointer;
                padding: 5px 10px;
                border-radius: 4px;
                transition: background 0.2s ease;
            }
            
            .comment-options:hover {
                background: #f8f9fa;
            }
            
            .comment-menu {
                position: absolute;
                right: 0;
                top: 100%;
                background: white;
                border: 1px solid #ddd;
                border-radius: 4px;
                box-shadow: 0 2px 8px rgba(0,0,0,0.1);
                list-style: none;
                padding: 0;
                margin: 0;
                display: none;
                z-index: 1000;
            }
            
            .comment-menu li {
                padding: 8px 15px;
                cursor: pointer;
                transition: background 0.2s ease;
            }
            
            .comment-menu li:hover {
                background: #f8f9fa;
            }
            
            /* No comments styling */
            .no-comments {
                text-align: center;
                color: #6c757d;
                font-size: 18px;
                font-weight: 500;
                margin: 30px 0;
                font-style: italic;
            }
            
            /* Ensure comment section always shows */
            .leave-comment {
                display: block !important;
            }
            
            .comment-list {
                display: block !important;
            }
            
            /* Hide empty comment items */
            .comment-item[style*="display: none"] {
                display: none !important;
            }
        </style>
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
                                <h3>Comments</h3>
                                                                <div class="comment-list">
                                    <!-- Debug: Kiểm tra dữ liệu commentList -->
                                    <script>
                                        console.log("=== DEBUG: JSP Comment Data ===");
                                        console.log("CommentList size: ${commentList.size()}");
                                        console.log("CommentList empty: ${empty commentList}");
                                        console.log("CommentList: ${commentList}");
                                    </script>
                                    
                                    <c:if test="${empty commentList}">
                                        <h4 class="no-comments">No comments yet</h4>
                                    </c:if>
                                    <c:forEach var="cmt" items="${commentList}">
                                        <!-- Debug: In ra thông tin từng comment -->
                                        <script>
                                            console.log("Comment ID: ${cmt.id}");
                                            console.log("User ID: ${cmt.userId}");
                                            console.log("User Name: ${cmt.userName}");
                                            console.log("Content: ${cmt.content}");
                                            console.log("Created At: ${cmt.createdAt}");
                                        </script>
                                        <div class="comment-item">
                                            <img src="${commentAvaMap[cmt.userId]}" alt="Avatar" class="avatar">
                                            <div>
                                                <div class="comment-content">
                                                    <div class="comment-header">
                                                        <strong>${cmt.userName != null ? cmt.userName : 'Anonymous'}</strong>
                                                                                                                    <c:if test="${currentUser != null && currentUser.userId == cmt.userId}">
                                                            <div class="comment-options-wrapper">
                                                                <span class="comment-options" onclick="toggleOptions(this)">...</span>
                                                                <ul class="comment-menu">
                                                                    <li data-comment-id="${cmt.id}" data-comment-content="${cmt.content}" class="edit-comment">Edit</li>
                                                                    <li data-comment-id="${cmt.id}" class="delete-comment">Delete</li>
                                                                </ul>
                                                            </div>
                                                        </c:if>
                                                    </div>
                                                    <p>${cmt.content}</p>
                                                </div>
                                                <div class="comment-meta">
                                                    <span><fmt:formatDate value="${cmt.createdAt}" pattern="dd/MM/yyyy HH:mm"/></span>
                                                </div>
                                            </div>
                                        </div>
                                    </c:forEach>
                                </div>

                                <!-- Form bình luận -->
                                <form action="CommentServlet" method="POST" id="commentForm">
                                    <input type="hidden" name="commentId" id="commentIdInput" value="">
                                    <input type="hidden" name="blogId" value="${blog.id}">
                                    <textarea class="comment-box" id="commentBox" name="content" placeholder="Write your comment..." oninput="autoResize(this)"></textarea>
                                    <button type="submit" class="send-comment-btn" id="submitBtn" name="action" value="send">Send</button>
                                </form>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </section>
        <!-- Blog Details Section End -->

        <!-- Footer Section Begin -->
       
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
            
            // Handle comment display and empty comments
            document.addEventListener('DOMContentLoaded', function() {
                const commentList = document.querySelector('.comment-list');
                const noCommentsElement = document.querySelector('.no-comments');
                
                if (commentList) {
                    const commentItems = commentList.querySelectorAll('.comment-item');
                    let validComments = 0;
                    
                    // Check each comment item
                    commentItems.forEach((item) => {
                        const content = item.querySelector('.comment-content p');
                        const userName = item.querySelector('.comment-header strong');
                        
                        // Check if comment is empty or has no content
                        if (!content || !content.textContent || content.textContent.trim() === '') {
                            console.log('Hiding empty comment item');
                            item.style.display = 'none';
                        } else {
                            validComments++;
                        }
                    });
                    
                    // If no valid comments, show "No comments yet"
                    if (validComments === 0) {
                        // Remove existing "No comments yet" if exists
                        if (noCommentsElement) {
                            noCommentsElement.remove();
                        }
                        
                        // Create and add "No comments yet" message
                        const noCommentsMsg = document.createElement('h4');
                        noCommentsMsg.className = 'no-comments';
                        noCommentsMsg.textContent = 'No comments yet';
                        commentList.appendChild(noCommentsMsg);
                    } else {
                        // Remove "No comments yet" if we have valid comments
                        if (noCommentsElement) {
                            noCommentsElement.remove();
                        }
                    }
                }
            });
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
            
            // Event listeners for comment menu items
            document.addEventListener('click', function(e) {
                if (e.target.classList.contains('edit-comment')) {
                    const commentId = e.target.getAttribute('data-comment-id');
                    const commentContent = e.target.getAttribute('data-comment-content');
                    editComment(commentId, commentContent);
                }
                
                if (e.target.classList.contains('delete-comment')) {
                    const commentId = e.target.getAttribute('data-comment-id');
                    deleteComment(commentId);
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
            
            function deleteComment(id) {
                if (confirm('Are you sure you want to delete this comment?')) {
                    // Redirect to delete comment
                    window.location.href = 'CommentServlet?action=delete&commentId=' + id;
                }
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
 <jsp:include page="footer.jsp" />
    </body>

</html>