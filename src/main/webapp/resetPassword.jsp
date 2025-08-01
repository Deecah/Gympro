<%-- 
    Document   : forgotPassword
    Created on : May 27, 2025, 5:42:26 PM
    Author     : gamer
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Reset password</title>
        <style>
            body {
                margin: 0;
                height: 100vh;
                display: flex;
                justify-content: center;
                align-items: center;
                background: linear-gradient(to right, #4AA6CE,#A2C8D3);
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
            input[type="text"],
            input[type="password"],
            input[type="email"] { 
                width: 100%;
                padding: 12px 15px;
                margin: 8px 0;
                display: inline-block;
                border: 1px solid #ccc;
                border-radius: 25px;
                box-sizing: border-box;
                font-size: 16px;
                line-height: 1.5;
            }

           
            input[type="text"]:focus,
            input[type="password"]:focus,
            input[type="email"]:focus {
                border-color: #88c0ff;
                outline: none;
                box-shadow: 0 0 5px rgba(136, 192, 255, 0.5); 
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
            }
            .reset-btn:hover {
                background: linear-gradient(to bottom, #ff9966, #ff5e62);
            }
        </style>
    </head>
    <body>
        <div class="container">
            <h1>Reset Password</h1>
            <form action="ResetPasswordServlet" method="post">
                <div class="form-group">
                    <label for="password">Password</label>
                    <input type="password" id="password" name="password" placeholder="Enter new password" required>
                </div>
                <div class="form-group">
                    <label for="confirmPassword">Confirm Password</label>
                    <input type="password" id="confirmPassword" name="confirmPassword" placeholder="Confirm new password" required>
                </div>
                <button type="submit" class="reset-btn" name="action" >RESET PASSWORD</button>
                <p class="error-message">
                     ${mess}
                </p>
            </form>
        </div>
    </body>
</html>

