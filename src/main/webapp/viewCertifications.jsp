<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.List" %>
<%@ page import="model.Certification" %>

<%
    List<Certification> certList = (List<Certification>) request.getAttribute("certList");
%>

<!DOCTYPE html>
<html>
    <head>
        <meta charset="UTF-8">
        <title>View Certifications</title>
        <style>
            body {
                font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
                background: linear-gradient(to right, #4AA6CE,#A2C8D3);
                margin: 0;
                padding: 0;
            }

            h2 {
                text-align: center;
                margin: 40px 0 20px;
                color: #333;
                font-size: 28px;
            }

            .action-bar {
                text-align: center;
                margin-bottom: 30px;
            }

            .action-bar a {
                display: inline-block;
                padding: 12px 24px;
                background: linear-gradient(135deg, #42a5f5, #ab47bc);
                color: white;
                text-decoration: none;
                font-weight: bold;
                border-radius: 25px;
                box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1);
                transition: background 0.3s, transform 0.2s;
            }

            .action-bar a:hover {
                background: linear-gradient(135deg, #1e88e5, #8e24aa);
                transform: scale(1.05);
            }

            table {
                border-collapse: collapse;
                width: 85%;
                margin: 0 auto 50px auto;
                background-color: #ffffff;
                border-radius: 12px;
                overflow: hidden;
                box-shadow: 0 8px 20px rgba(0, 0, 0, 0.1);
            }

            th, td {
                padding: 14px 16px;
                text-align: left;
            }

            th {
                background-color: #f5f5f5;
                color: #333;
                font-weight: bold;
                border-bottom: 2px solid #ddd;
            }

            td {
                border-bottom: 1px solid #eee;
            }

            td a {
                color: #1976d2;
                text-decoration: none;
                font-weight: bold;
                transition: color 0.2s;
            }

            td a:hover {
                color: #0d47a1;
                text-decoration: underline;
            }

            .no-data {
                text-align: center;
                padding: 30px;
                color: #e53935;
                font-weight: bold;
            }
        </style>
    </head>
    <body>

        <h2>Trainer's Certifications</h2>

        <div class="action-bar">
            <a href="addCertification.jsp">➕ Add New Certification</a>
        </div>

        <table>
            <tr>
                <th>ID</th>
                <th>Name</th>
                <th>Description</th>
                <th>Expire Date</th>
                <th>Actions</th>
            </tr>

            <%
                if (certList != null && !certList.isEmpty()) {
                    for (Certification cert : certList) {
            %>
            <tr>
                <td><%= cert.getCertificationID() %></td>
                <td><%= cert.getName() %></td>
                <td><%= cert.getDescription() %></td>
                <td><%= cert.getExpireDate() != null ? cert.getExpireDate().toLocalDate() : "N/A" %></td>
                <td>
                    <a href="EditCertificationServlet?id=<%= cert.getCertificationID() %>">Edit</a> |
                    <a href="DeleteCertificationServlet?id=<%= cert.getCertificationID() %>"
                       onclick="return confirm('Are you sure you want to delete this certification?');">
                        Delete
                    </a>
                </td>
            </tr>
            <%
                    }
                } else {
            %>
            <tr>
                <td colspan="5" class="no-data">No certifications found.</td>
            </tr>
            <%
                }
            %>
        </table>

    </body>
</html>
