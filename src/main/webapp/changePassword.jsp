<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Change Password</title>
        <style>
            body {
                margin: 0;
                height: 100vh;
                display: flex;
                justify-content: center;
                align-items: center;
                background: linear-gradient(to bottom, #ff5e62, #ff9966);
                font-family: Arial, sans-serif;
            }
            .container {
                background-color: white;
                padding: 30px;
                border-radius: 10px;
                box-shadow: 0 4px 8px rgba(0, 0, 0, 0.2);
                width: 300px;
                text-align: center;
            }
            h1 {
                font-size: 24px;
                margin-bottom: 20px;
                color: #333;
            }
            .form-group {
                margin-bottom: 15px;
                text-align: left;
            }
            label {
                display: block;
                font-size: 14px;
                color: #333;
                margin-bottom: 5px;
            }
            input[type="password"] {
                width: 100%;
                padding: 10px;
                border: 1px solid #ccc;
                border-radius: 5px;
                font-size: 14px;
                box-sizing: border-box; /* Đảm bảo padding và border không làm tăng kích thước tổng thể */
            }
            .reset-btn {
                width: 100%;
                padding: 10px;
                font-size: 16px;
                color: white;
                background: linear-gradient(to bottom, #ff5e62, #ff9966);
                border: none;
                border-radius: 25px;
                cursor: pointer;
                transition: background 0.3s;
                margin-top: 20px; /* Thêm khoảng cách phía trên nút */
            }
            .reset-btn:hover {
                background: linear-gradient(to bottom, #ff9966, #ff5e62);
            }
            /* CSS MỚI cho thông báo lỗi */
            .error-message {
                color: red; /* Màu đỏ */
                font-weight: bold; /* In đậm */
                margin-top: 10px; /* Khoảng cách trên */
                margin-bottom: 10px; /* Khoảng cách dưới */
                min-height: 1.2em; /* Giữ chỗ để tránh layout nhảy khi lỗi xuất hiện */
            }
        </style>
    </head>
    <body>
        <div class="container">
            <h1>Reset Password</h1>
            <form action="ChangePasswordServlet" method="post">
                <input type="hidden" name="action" value="changePassword">

                <div class="form-group">
                    <label for="password1">New Password</label>
                    <input type="password" id="password1" name="password1" placeholder="Enter new password" required>
                </div>

                <div class="form-group">
                    <label for="password2">Confirm New Password</label>
                    <input type="password" id="password2" name="password2" placeholder="Re-enter new password" required>
                </div>

                <p class="error-message">
                    <%= request.getAttribute("error") != null ? request.getAttribute("error") : "" %>
                </p>

                <button type="submit" class="reset-btn">Confirm</button>
            </form>
        </div>
    </body>
</html>