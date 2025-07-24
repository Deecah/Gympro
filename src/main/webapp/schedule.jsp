
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="model.User" %>
<%@ page import="model.Customer" %>
<%@ page import="model.Trainer" %>
<!DOCTYPE html>
<%
    User user = (User) session.getAttribute("user");
    if (user == null) {
        response.sendRedirect("login.jsp");
        return;
    }
    String role = user.getRole();
    Customer customer = null;
    Trainer trainer = null;

    if ("Customer".equalsIgnoreCase(role)) {
        customer = (Customer) session.getAttribute("customer");
    } else if ("Trainer".equalsIgnoreCase(role)) {
        trainer = (Trainer) session.getAttribute("trainer");
    }

%>
<html lang="en">
    <head>
        <meta charset="UTF-8" />
        <meta name="viewport" content="width=device-width, initial-scale=1.0" />
        <title>Weekly Timetable</title>
        <link rel="stylesheet" href="stylecss/schedule.css" />
        <style>
            /* Modal styling */
            .modal {
                position: fixed;
                top: 0;
                left: 0;
                width: 100%;
                height: 100%;
                background-color: rgba(0,0,0,0.6);
                display: none;
                justify-content: center;
                align-items: center;
                z-index: 9999;
            }

            .modal-content {
                background-color: #fff;
                padding: 30px;
                width: 700px;
                border-radius: 8px;
                position: relative;
                max-height: 90vh;
                overflow-y: auto;
            }

            .close-button {
                position: absolute;
                top: 10px;
                right: 20px;
                font-size: 26px;
                cursor: pointer;
            }
        </style>

    </head>
    <body>
        <div class="layout">
            <div class="sidebar">
                <div class="sidebar-top">
                    <button class="sidebar-home">Home</button>
                </div>
                <div class="sidebar-menu">
                    <button>My Schedule</button>
                    <button>Requests</button>
                </div>
            </div>
            <div class="timetable-wrapper">
                <div class="timetable-border-fade"></div>
                <table class="timetable">
                    <thead>
                        <tr>
                            <th>THỨ 2<br><span>29/07</span></th>
                            <th>THỨ 3<br><span>30/07</span></th>
                            <th>THỨ 4<br><span>31/07</span></th>
                            <th>THỨ 5<br><span>01/08</span></th>
                            <th>THỨ 6<br><span>02/08</span></th>
                            <th>THỨ 7<br><span>03/08</span></th>
                            <th>CHỦ NHẬT<br><span>04/08</span></th>
                        </tr>
                    </thead>
                    <tbody>
                        <tr>
                            <%
                                String className1 = "Vinyasa Yoga", instructor1 = "Ms. Liên", studentName1 = "Nguyễn Văn A";
                                String className2 = "Hip Opening", instructor2 = "Mr. Lalit", studentName2 = "Trần Thị B";
                                String className3 = "Back Bend Yoga", instructor3 = "Ms. Liên", studentName3 = "Phạm Văn C";
                                String className4 = "Vinyasa Yoga", instructor4 = "Mr. Satya Sai", studentName4 = "Lê Thị D";
                                String className5 = "Power Yoga", instructor5 = "Ms. Liên", studentName5 = "Nguyễn Văn E";
                                String className6 = "", instructor6 = "", studentName6 = "";
                                String className7 = "Yin Yoga", instructor7 = "Ms. Dương Vy", studentName7 = "Lê Văn F";
                            %>
                            <td data-slot-id="1"><strong><%= className1 %></strong> (<%= "Trainer".equalsIgnoreCase(role) ? studentName1 : instructor1 %>)<br><span class="time">9:00 - 10:00</span></td>
                            <td data-slot-id="2"><strong><%= className2 %></strong> (<%= "Trainer".equalsIgnoreCase(role) ? studentName2 : instructor2 %>)<br><span class="time">9:00 - 10:00</span></td>
                            <td data-slot-id="12"><strong><%= className3 %></strong> (<%= "Trainer".equalsIgnoreCase(role) ? studentName3 : instructor3 %>)<br><span class="time">9:00 - 10:00</span></td>
                            <td data-slot-id="15"><strong><%= className4 %></strong> (<%= "Trainer".equalsIgnoreCase(role) ? studentName4 : instructor4 %>)<br><span class="time">9:00 - 10:00</span></td>
                            <td data-slot-id="19"><strong><%= className5 %></strong> (<%= "Trainer".equalsIgnoreCase(role) ? studentName5 : instructor5 %>)<br><span class="time">9:00 - 10:00</span></td>
                            <td></td>
                            <td data-slot-id="24"><strong><%= className7 %></strong> (<%= "Trainer".equalsIgnoreCase(role) ? studentName7 : instructor7 %>)<br><span class="time">9:00 - 10:00</span></td>
                        </tr>

                        <tr>
                            <%
                                String className8 = "Power Yoga", instructor8 = "Ms. Liên", studentName8 = "Học viên G";
                                String className9 = "Dynamic Yoga", instructor9 = "Ms. Dương Vy", studentName9 = "Học viên H";
                                String className10 = "Hatha Yoga", instructor10 = "Ms. Liên", studentName10 = "Học viên I";
                                String className11 = "Detox Yoga", instructor11 = "Ms. Dương Vy", studentName11 = "Học viên J";
                                String className12 = "Hip Opening", instructor12 = "Ms. Liên", studentName12 = "Học viên K";
                                String className13 = "Yin Yoga", instructor13 = "Ms. Dương Vy", studentName13 = "Học viên L";
                            %>
                            <td data-slot-id="4"><strong><%= className8 %></strong> (<%= "Trainer".equalsIgnoreCase(role) ? studentName8 : instructor8 %>)<br><span class="time">10:00 - 11:00</span></td>
                            <td data-slot-id="8"><strong><%= className9 %></strong> (<%= "Trainer".equalsIgnoreCase(role) ? studentName9 : instructor9 %>)<br><span class="time">10:00 - 11:00</span></td>
                            <td data-slot-id="3"><strong><%= className10 %></strong> (<%= "Trainer".equalsIgnoreCase(role) ? studentName10 : instructor10 %>)<br><span class="time">10:00 - 11:00</span></td>
                            <td data-slot-id="16"><strong><%= className11 %></strong> (<%= "Trainer".equalsIgnoreCase(role) ? studentName11 : instructor11 %>)<br><span class="time">10:00 - 11:00</span></td>
                            <td data-slot-id="20"><strong><%= className12 %></strong> (<%= "Trainer".equalsIgnoreCase(role) ? studentName12 : instructor12 %>)<br><span class="time">10:00 - 11:00</span></td>
                            <td data-slot-id="23"><strong><%= className13 %></strong> (<%= "Trainer".equalsIgnoreCase(role) ? studentName13 : instructor13 %>)<br><span class="time">10:00 - 11:00</span></td>
                            <td data-slot-id="25"><strong><%= className11 %></strong> (<%= "Trainer".equalsIgnoreCase(role) ? studentName11 : instructor11 %>)<br><span class="time">10:00 - 11:00</span></td>
                        </tr>

                        <tr>
                            <%
                                String className14 = "Yoga Mix", instructor14 = "Mr. Kiran", studentName14 = "HV M";
                                String className15 = "Vinyasa Yoga", instructor15 = "Mr. Satya Sai", studentName15 = "HV N";
                                String className16 = "Shoulder Opening", instructor16 = "Mr. Kiran", studentName16 = "HV O";
                                String className17 = "Back Bending", instructor17 = "Mr. Satya Sai", studentName17 = "HV P";
                                String className18 = "Twist Yoga", instructor18 = "Mr. Ajay", studentName18 = "HV Q";
                            %>
                            <td data-slot-id="5"><strong><%= className14 %></strong> (<%= "Trainer".equalsIgnoreCase(role) ? studentName14 : instructor14 %>)<br><span class="time">18:00 - 19:00</span></td>
                            <td data-slot-id="9"><strong><%= className15 %></strong> (<%= "Trainer".equalsIgnoreCase(role) ? studentName15 : instructor15 %>)<br><span class="time">18:00 - 19:00</span></td>
                            <td data-slot-id="13"><strong><%= className16 %></strong> (<%= "Trainer".equalsIgnoreCase(role) ? studentName16 : instructor16 %>)<br><span class="time">18:00 - 19:00</span></td>
                            <td data-slot-id="17"><strong><%= className17 %></strong> (<%= "Trainer".equalsIgnoreCase(role) ? studentName17 : instructor17 %>)<br><span class="time">18:00 - 19:00</span></td>
                            <td data-slot-id="21"><strong><%= className18 %></strong> (<%= "Trainer".equalsIgnoreCase(role) ? studentName18 : instructor18 %>)<br><span class="time">18:00 - 19:00</span></td>
                            <td></td>
                            <td></td>
                        </tr>

                        <tr>
                            <%
                                String className19 = "Flexibility Yoga", instructor19 = "Ms. My", studentName19 = "HV R";
                                String className20 = "Dynamic Yoga", instructor20 = "Ms. My", studentName20 = "HV S";
                                String className21 = "Hip Opening", instructor21 = "Ms. My", studentName21 = "HV T";
                                String className22 = "Vinyasa Yoga", instructor22 = "Ms. My", studentName22 = "HV U";
                            %>
                            <td data-slot-id="3"><strong><%= className19 %></strong> (<%= "Trainer".equalsIgnoreCase(role) ? studentName19 : instructor19 %>)<br><span class="time">19:15 - 20:15</span></td>
                            <td data-slot-id="3"><strong><%= className20 %></strong> (<%= "Trainer".equalsIgnoreCase(role) ? studentName20 : instructor20 %>)<br><span class="time">19:15 - 20:15</span></td>
                            <td data-slot-id="3"><strong><%= className21 %></strong> (<%= "Trainer".equalsIgnoreCase(role) ? studentName21 : instructor21 %>)<br><span class="time">19:15 - 20:15</span></td>
                            <td data-slot-id="3"><strong><%= className22 %></strong> (<%= "Trainer".equalsIgnoreCase(role) ? studentName22 : instructor22 %>)<br><span class="time">19:15 - 20:15</span></td>
                            <td data-slot-id="3"><strong><%= className19 %></strong> (<%= "Trainer".equalsIgnoreCase(role) ? studentName19 : instructor19 %>)<br><span class="time">19:15 - 20:15</span></td>
                            <td></td>
                            <td></td>
                        </tr>

                        <tr>
                            <%
                                String className23 = "Zumba", instructor23 = "Ms. Thảo", studentName23 = "HV V";
                            %>
                            <td class="highlight" data-slot-id="7"><strong><%= className23 %></strong> (<%= "Trainer".equalsIgnoreCase(role) ? studentName23 : instructor23 %>)<br><span class="time">20:30 - 21:30</span></td>
                            <td class="highlight" data-slot-id="11"><strong><%= className23 %></strong> (<%= "Trainer".equalsIgnoreCase(role) ? studentName23 : instructor23 %>)<br><span class="time">20:30 - 21:30</span></td>
                            <td></td>
                            <td></td>
                            <td></td>
                            <td></td>
                            <td></td>
                        </tr>
                    </tbody>

                </table>
            </div>
            <button class="request-button" title="New Request">+</button>
        </div>

        <!-- Context Menu -->
        <ul id="contextMenu" class="context-menu" style="display: none;">
            <li id="editOption">Edit</li>
            <li id="addOption">Add</li>
        </ul>

        <script>
            window.addEventListener('DOMContentLoaded', () => {
                const modal = document.getElementById('slotModal');
                const modalBody = document.getElementById('modal-body');
                const closeButton = document.querySelector('.close-button');
                const menu = document.getElementById('contextMenu');
                let targetCell = null;

                function openSlotModal(slotId) {
                    if (!slotId || slotId.trim() === "") {
                        console.error("Không có slotId hợp lệ để fetch");
                        return;
                    }

                    console.log("Đang gửi fetch với slotId =", slotId);

                    fetch('/SWP391/slotContent.jsp?slotId=' + encodeURIComponent(slotId))
                            .then(res => {
                                if (!res.ok)
                                    throw new Error("Lỗi khi tải slotContent.jsp");
                                return res.text();
                            })
                            .then(html => {
                                modalBody.innerHTML = html;
                                modal.style.display = 'flex';

                                // ✅ Gắn lại sự kiện cho nút toggle sau khi chèn nội dung
                                const toggleBtn = modalBody.querySelector("#toggleExerciseBtn");
                                const detailsDiv = modalBody.querySelector("#exerciseDetails");

                                if (toggleBtn && detailsDiv) {
                                    toggleBtn.addEventListener("click", function () {
                                        const isVisible = detailsDiv.style.display === "block";
                                        detailsDiv.style.display = isVisible ? "none" : "block";
                                        toggleBtn.textContent = isVisible
                                                ? "📋 Xem danh sách bài tập"
                                                : "🔽 Ẩn danh sách bài tập";
                                    });
                                }
                            })
                            .catch(err => {
                                console.error("Lỗi khi tải nội dung slot:", err);
                                document.getElementById('modal-body').innerHTML = "<p>Lỗi tải nội dung slot.</p>";
                                document.getElementById('slotModal').style.display = 'flex';
                            });
                }


                // Right click context menu
                document.addEventListener('contextmenu', function (e) {
                    if (e.target.closest('td')) {
                        e.preventDefault();
                        targetCell = e.target.closest('td');
                        const hasContent = targetCell.textContent.trim() !== '';
                        document.getElementById('editOption').style.display = hasContent ? 'block' : 'none';
                        document.getElementById('addOption').style.display = hasContent ? 'none' : 'block';
                        menu.style.top = `${e.pageY}px`;
                        menu.style.left = `${e.pageX}px`;
                        menu.style.display = 'block';
                    } else {
                        menu.style.display = 'none';
                    }
                });

                document.addEventListener('click', function () {
                    menu.style.display = 'none';
                });

                document.getElementById('editOption').onclick = () => {
                    const slotId = targetCell?.getAttribute("data-slot-id");
                    openSlotModal(slotId);
                };

                document.getElementById('addOption').onclick = () => {
                    alert('Add new event');
                };

                document.querySelectorAll('td[data-slot-id]').forEach(cell => {
                    cell.addEventListener('click', () => {
                        const slotId = cell.getAttribute('data-slot-id');
                        console.log("SlotId click truyền vào:", slotId);  // log chắc chắn
                        openSlotModal(slotId); // truyền thẳng vào hàm
                    });
                });

                if (closeButton) {
                    closeButton.onclick = () => {
                        modal.style.display = 'none';
                    };
                }

                window.onclick = function (event) {
                    if (event.target === modal) {
                        modal.style.display = "none";
                    }
                };
            });


        </script>
        <!-- Modal -->
        <div id="slotModal" class="modal" style="display:none;">
            <div class="modal-content">
                <span class="close-button">&times;</span>
                <div id="modal-body">
                    <!-- Nội dung slot.jsp sẽ được tải vào đây -->
                </div>
            </div>
        </div>
    </body>
</html>
