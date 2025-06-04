<%-- 
    Document   : requestChangePassword
    Created on : May 27, 2025, 8:24:08 PM
    Author     : gamer
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Request Change Password</title>
                <style>
            body {
                margin: 0;
                height: 100vh;
                display: flex;
                justify-content: center;
                align-items: center;
                background: linear-gradient(to bottom, #ff5e62, #ff9966);
                font-family: Arial, sans-serif;
                p{
                    text-align: center;
                }
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
            input[type="email"],
            input[type="password"] {
                width: 100%;
                padding: 10px;
                border: 1px solid #ccc;
                border-radius: 5px;
                font-size: 14px;
                box-sizing: border-box;
            }
            .reset-btn {
                width: 100%;
                padding: 10px;
                font-size: 16px;
                color: white; /* Chữ trắng để dễ đọc trên nền gradient hồng */
                background: linear-gradient(to bottom, #ff5e62, #ff9966); /* Gradient giống background */
                border: none;
                border-radius: 25px;
                cursor: pointer;
                transition: background 0.3s;
            }
            .reset-btn:hover {
                background: linear-gradient(to bottom, #ff9966, #ff5e62); /* Đảo ngược gradient khi hover */
            }
            
        </style>
    </head>
    <body>
       <div class="container">
            <h1>Enter your email</h1>
            <form action="RequestPasswordServlet" method="post">
                <div class="form-group">
                    <label for="email">Email</label>
                    <input type="email" id="email" name="email" placeholder="Enter your email" required>
                </div>
                <button type="submit" class="reset-btn">Send Request</button>
            </form>
        </div>
    </body>
</html>