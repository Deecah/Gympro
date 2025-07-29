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
    <title>Blog</title>

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
    
    <!-- CKEditor CDN -->
    <script src="https://cdn.ckeditor.com/ckeditor5/40.0.0/classic/ckeditor.js"></script>
    
    <style>
        /* Image Upload Styles */
        .thumbnail-upload-container {
            position: relative;
        }
        
        .image-preview {
            text-align: center;
            padding: 15px;
            background: #f8f9fa;
            border-radius: 8px;
            border: 2px dashed #dee2e6;
        }
        
        .image-preview img {
            box-shadow: 0 2px 8px rgba(0,0,0,0.1);
            transition: transform 0.2s ease;
        }
        
        .image-preview img:hover {
            transform: scale(1.05);
        }
        
        /* File Input Styling */
        input[type="file"] {
            padding: 8px;
            border: 2px dashed #007bff;
            border-radius: 8px;
            background: #f8f9fa;
            transition: all 0.3s ease;
        }
        
        input[type="file"]:hover {
            border-color: #0056b3;
            background: #e3f2fd;
        }
        
        input[type="file"]:focus {
            outline: none;
            border-color: #007bff;
            box-shadow: 0 0 0 3px rgba(0, 123, 255, 0.1);
        }
        
        /* Modal Improvements */
        #addForm {
            background: linear-gradient(135deg, #ffffff 0%, #f8f9fa 100%);
        }
        
        .form-control {
            border: 2px solid #e9ecef;
            border-radius: 8px;
            padding: 12px 15px;
            transition: all 0.3s ease;
        }
        
        .form-control:focus {
            border-color: #007bff;
            box-shadow: 0 0 0 3px rgba(0, 123, 255, 0.1);
        }
        
        .btn {
            border-radius: 8px;
            padding: 10px 20px;
            font-weight: 500;
            transition: all 0.3s ease;
        }
        
        .btn:hover {
            transform: translateY(-1px);
            box-shadow: 0 4px 12px rgba(0,0,0,0.15);
        }
        
        /* CKEditor Styling */
        .ck-editor__editable {
            min-height: 300px !important;
            background: #ffffff !important;
            color: #333333 !important;
            font-size: 16px !important;
            line-height: 1.6 !important;
            padding: 20px !important;
            border: 2px solid #e9ecef !important;
            border-radius: 8px !important;
        }
        
        .ck-editor__editable:focus {
            border-color: #007bff !important;
            box-shadow: 0 0 0 3px rgba(0, 123, 255, 0.1) !important;
        }
        
        /* Bullet Points Styling */
        .ck-editor__editable ul {
            list-style-type: none !important;
            padding-left: 0 !important;
        }
        
        .ck-editor__editable ul li {
            margin-bottom: 12px !important;
            line-height: 1.8 !important;
            position: relative !important;
            padding-left: 25px !important;
        }
        
        .ck-editor__editable ul li::before {
            content: "●";
            color: #007bff;
            font-weight: bold;
            font-size: 18px;
            position: absolute;
            left: 0;
            top: 0;
            line-height: 1.8;
        }
        
        .ck-editor__editable ol {
            list-style-type: decimal !important;
            padding-left: 25px !important;
        }
        
        .ck-editor__editable ol li {
            margin-bottom: 12px !important;
            line-height: 1.8 !important;
        }
        
        .ck-editor__editable ol li::marker {
            color: #007bff !important;
            font-weight: bold !important;
            font-size: 16px !important;
        }
        
        /* CKEditor Toolbar Styling */
        .ck.ck-toolbar {
            background: linear-gradient(135deg, #f8f9fa 0%, #e9ecef 100%) !important;
            border: 2px solid #dee2e6 !important;
            border-radius: 8px 8px 0 0 !important;
            border-bottom: none !important;
        }
        
        .ck.ck-button {
            border-radius: 4px !important;
            transition: all 0.2s ease !important;
        }
        
        .ck.ck-button:hover {
            background: #007bff !important;
            color: white !important;
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
            <button class="btn btn-primary mr-2" onclick="showAddForm()">Write new blog</button>
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
    width:80%; max-width:800px; background:#fff; border:1px solid #ccc; padding:30px; z-index:9999; 
    box-shadow: 0 0 20px rgba(0,0,0,0.3); border-radius: 10px; max-height: 90vh; overflow-y: auto;">
    
    <div class="d-flex justify-content-between align-items-center mb-4">
        <h4 class="mb-0">Add New Blog</h4>
        <button type="button" class="btn-close" onclick="hideAddForm()" style="background: none; border: none; font-size: 24px; cursor: pointer;">&times;</button>
    </div>
    
    <form action="AddBlogServlet" method="post" id="blogForm">
        <div class="form-group mb-3">
            <label for="blogTitle" class="form-label"><strong>Title:</strong></label>
            <input type="text" id="blogTitle" name="title" class="form-control" required>
        </div>
        
        <div class="form-group mb-3">
            <label for="blogContent" class="form-label"><strong>Content:</strong></label>
            <textarea id="blogContent" name="content" class="form-control" rows="10" required></textarea>
        </div>
        
        <div class="form-group mb-3">
            <label for="blogThumbnail" class="form-label"><strong>Thumbnail:</strong></label>
            <div class="thumbnail-upload-container">
                <input type="file" id="blogThumbnail" name="thumbnail" class="form-control" 
                       accept="image/*" onchange="previewImage(this)" required>
                <div id="imagePreview" class="image-preview" style="display: none; margin-top: 10px;">
                    <img id="previewImg" src="" alt="Preview" style="max-width: 200px; max-height: 150px; border-radius: 5px; border: 2px solid #ddd;">
                    <button type="button" class="btn btn-sm btn-danger mt-2" onclick="removeImage()">Remove Image</button>
                </div>
            </div>
        </div>
        
        <div class="form-group mb-4">
            <label for="blogTag" class="form-label"><strong>Tag:</strong></label>
            <input type="text" id="blogTag" name="tag" class="form-control" 
                   placeholder="e.g., Fitness, Nutrition, Training" required>
        </div>
        
        <div class="d-flex gap-2">
            <button type="submit" class="btn btn-success">Submit</button>
            <button type="button" class="btn btn-secondary" onclick="hideAddForm()">Cancel</button>
        </div>
    </form>
</div>

<!-- Overlay -->
<div id="overlay" style="display:none; position:fixed; top:0; left:0; width:100%; height:100%; 
    background:rgba(0,0,0,0.5); z-index:9998;"></div>

<script>
    let editor;
    
    // Image preview function
    function previewImage(input) {
        const file = input.files[0];
        const preview = document.getElementById('imagePreview');
        const previewImg = document.getElementById('previewImg');
        
        if (file) {
            // Validate file type
            if (!file.type.startsWith('image/')) {
                alert('Please select an image file!');
                input.value = '';
                return;
            }
            
            // Validate file size (max 15MB)
            if (file.size > 15 * 1024 * 1024) {
                alert('Image size should be less than 15MB!');
                input.value = '';
                return;
            }
            
            const reader = new FileReader();
            reader.onload = function(e) {
                previewImg.src = e.target.result;
                preview.style.display = 'block';
            };
            reader.readAsDataURL(file);
        } else {
            preview.style.display = 'none';
        }
    }
    
    // Remove image function
    function removeImage() {
        const input = document.getElementById('blogThumbnail');
        const preview = document.getElementById('imagePreview');
        const previewImg = document.getElementById('previewImg');
        
        input.value = '';
        previewImg.src = '';
        preview.style.display = 'none';
    }
    
    function showAddForm() {
        document.getElementById("overlay").style.display = "block";
        document.getElementById("addForm").style.display = "block";
        
        // Initialize CKEditor after form is shown
        setTimeout(() => {
            if (!editor) {
                ClassicEditor
                    .create(document.querySelector('#blogContent'), {
                        toolbar: {
                            items: [
                                'heading',
                                '|',
                                'bold',
                                'italic',
                                'underline',
                                'strikethrough',
                                '|',
                                'link',
                                '|',
                                'alignment',
                                '|',
                                'bulletedList',
                                'numberedList',
                                '|',
                                'indent',
                                'outdent',
                                '|',
                                'undo',
                                'redo'
                            ]
                        },
                        heading: {
                            options: [
                                { model: 'paragraph', title: 'Paragraph', class: 'ck-heading_paragraph' },
                                { model: 'heading1', view: 'h1', title: 'Heading 1', class: 'ck-heading_heading1' },
                                { model: 'heading2', view: 'h2', title: 'Heading 2', class: 'ck-heading_heading2' },
                                { model: 'heading3', view: 'h3', title: 'Heading 3', class: 'ck-heading_heading3' },
                                { model: 'heading4', view: 'h4', title: 'Heading 4', class: 'ck-heading_heading4' },
                                { model: 'heading5', view: 'h5', title: 'Heading 5', class: 'ck-heading_heading5' },
                                { model: 'heading6', view: 'h6', title: 'Heading 6', class: 'ck-heading_heading6' }
                            ]
                        },
                        removePlugins: ['Title'],
                        placeholder: 'Write your blog content here...'
                    })
                    .then(newEditor => {
                        editor = newEditor;
                        console.log('CKEditor loaded successfully:', editor);
                    })
                    .catch(error => {
                        console.error('There was a problem initializing the editor:', error);
                    });
            }
        }, 100);
    }
    
    function hideAddForm() {
        document.getElementById("overlay").style.display = "none";
        document.getElementById("addForm").style.display = "none";
        
        // Destroy editor when form is hidden
        if (editor) {
            editor.destroy().then(() => {
                editor = null;
                console.log('Editor destroyed');
            });
        }
        
        // Reset form and image preview
        document.getElementById('blogForm').reset();
        removeImage();
    }
    
    // Close form when clicking overlay
    document.getElementById('overlay').addEventListener('click', hideAddForm);
    
    // Prevent form from closing when clicking inside form
    document.getElementById('addForm').addEventListener('click', function(e) {
        e.stopPropagation();
    });
    
    // Form submission handling
    document.getElementById('blogForm').addEventListener('submit', function(e) {
        e.preventDefault();
        
        // Get form data
        const formData = new FormData(this);
        
        // Get CKEditor content
        if (editor) {
            formData.set('content', editor.getData());
        }
        
        // Validate image
        const thumbnailInput = document.getElementById('blogThumbnail');
        if (!thumbnailInput.files[0]) {
            alert('Please select a thumbnail image!');
            return;
        }
        
        // Submit form
        fetch('AddBlogServlet', {
            method: 'POST',
            body: formData
        })
        .then(response => {
            if (response.ok) {
                alert('Blog added successfully!');
                hideAddForm();
                // Reload page to show new blog
                window.location.reload();
            } else {
                throw new Error('Failed to add blog');
            }
        })
        .catch(error => {
            console.error('Error:', error);
            alert('Failed to add blog. Please try again.');
        });
    });
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