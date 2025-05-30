<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Confirm password</title>
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
                font-size: 14px;
                color: #333;
                margin-bottom: 5px;
                display: block;
            }
            input[type="email"] {
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
            <h1>Enter your old password</h1>
            <form action="ChangePasswordServlet" method="post">
                <div class="form-group">
                    <input type="password" id="oldPassword" name="oldPassword" placeholder="Enter your old password" required>
                </div>
                <button type="submit" class="reset-btn" name="action" value="confirm" >CONFIRM PASSWORD</button>
            </form>
        </div>
    </body>
</html>
