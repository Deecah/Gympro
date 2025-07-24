<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.Map" %>
<%@ page import="java.util.List" %>
<%@ page import="model.WorkoutSlotDTO" %>
<%@ page import="model.User" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%
    User user = (User) session.getAttribute("user");
    if (user == null) {
        response.sendRedirect("login.jsp");
        return;
    }
    String role = user.getRole();
%>
<!DOCTYPE html>
<html>
    <head>
        <meta charset="UTF-8">
        <title>Weekly Timetable</title>
        <link rel="stylesheet" href="stylecss/schedule.css">
        <style>
            td {
                min-height: 90px;
                vertical-align: top;
                padding: 6px;
            }
            .workout-box {
                background: #e9f9e9;
                border-left: 4px solid #28a745;
                padding: 4px 8px;
                margin-bottom: 6px;
                border-radius: 4px;
            }
            .workout-box strong {
                display: block;
                font-weight: bold;
            }
            .program-name {
                font-size: 12px;
                color: #555;
                font-style: italic;
            }
        </style>
    </head>
    <body>
        <div class="layout">
            <div class="sidebar">
                <div class="sidebar-top">
                    <a href="index.jsp" class="sidebar-home">Home</a>
                </div>

                <div class="sidebar-menu">
                    <button>My Schedule</button>
                    <button>Requests</button>
                </div>
            </div>

            <div class="timetable-wrapper">
                <form action="timetable" method="get" style="margin-bottom: 15px;">
                    <label for="weekRangeSelect"><strong>Chọn tuần:</strong></label>
                    <select id="weekRangeSelect" name="weekRange" onchange="this.form.submit()">
                        <c:forEach var="opt" items="${weekOptions}">
                            <option value="${opt}" ${opt == currentWeekRange ? "selected" : ""}>${opt}</option>
                        </c:forEach>
                    </select>
                </form>

                <table class="timetable">
                    <thead>
                        <tr>
                            <th>THỨ 2</th>
                            <th>THỨ 3</th>
                            <th>THỨ 4</th>
                            <th>THỨ 5</th>
                            <th>THỨ 6</th>
                            <th>THỨ 7</th>
                            <th>CHỦ NHẬT</th>
                        </tr>
                    </thead>
                    <tbody>
                        <%
                            Map<Integer, List<WorkoutSlotDTO>> slotMap = (Map<Integer, List<WorkoutSlotDTO>>) request.getAttribute("slotMap");
                            for (int block = 1; block <= 7; block++) {
                        %>
                        <tr>
                            <%
                                for (int day = 0; day < 7; day++) {
                                    int slotId = day * 10 + block;
                                    List<WorkoutSlotDTO> slots = slotMap.get(slotId);
                            %>
                            <td data-slot-id="<%=slotId%>">
                                <%
                                    if (slots != null) {
                                        for (WorkoutSlotDTO slot : slots) {
                                %>
                                <div class="workout-box">
                                    <strong><%= slot.getTitle() %></strong>
                                    <span><%= slot.getStartStr() %> - <%= slot.getEndStr() %></span><br/>
                                    <span class="program-name">👤 <%= slot.getDisplayName() %></span><br/>
                                    <span class="program-name">📘 <%= slot.getProgramName() %></span>
                                </div>
                                <%
                                        }
                                    }
                                %>
                            </td>
                            <%
                                }
                            %>
                        </tr>
                        <%
                            }
                        %>
                    </tbody>

                </table>
            </div>

            <button class="request-button" title="New Request">+</button>
        </div>
    </body>
</html>
