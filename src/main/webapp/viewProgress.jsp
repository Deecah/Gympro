<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<html>
    <head>
        <title>Progress</title>
        <style>
            table {
                width: 100%;
                border-collapse: collapse;
                margin-top: 20px;
            }
            th, td {
                border: 1px solid #ccc;
                padding: 10px;
                text-align: center;
            }
            th {
                background-color: #f0f0f0;
            }
        </style>
    </head>
    <body>
        <h2>Progress Tracking</h2>

        <c:choose>
            <c:when test="${not empty progressList}">
                <table>
                    <tr>
                        <th>Date</th>
                        <th>Weight (kg)</th>
                        <th>Body Fat (%)</th>
                        <th>Muscle Mass (kg)</th>
                        <th>Notes</th>
                    </tr>
                    <c:forEach var="p" items="${progressList}">
                        <tr>
                            <td>${p.recordedAt}</td>
                            <td>${p.weight}</td>
                            <td>${p.bodyFatPercent}</td>
                            <td>${p.muscleMass}</td>
                            <td>${p.notes}</td>
                            <c:if test="${user.role == 'Trainer'}">
                                <td><a href="editProgress.jsp?id=${p.progressID}">Edit</a></td>
                            </c:if>
                        </tr>
                    </c:forEach>
                </table>
            </c:when>
            <c:otherwise>
                <p>No progress data found.</p>
            </c:otherwise>
        </c:choose>

    </body>
</html>
