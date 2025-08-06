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
                margin-top: 20px; 
            }
            .reset-btn:hover {
                background: linear-gradient(to bottom, #ff9966, #ff5e62);
            }

            .error-message {
                color: red;
                font-weight: bold;
                margin-top: 10px; 
                margin-bottom: 10px;
                min-height: 1.2em;
            }
        </style>
    </head>
    <body>
        <div class="container">
            <h1>Change Password</h1>
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
                <button type="submit" class="reset-btn">Confirm</button>
                
                <p class="error-message">
                     ${mess}
                </p>
            </form>
        </div>
    </body>
</html>

