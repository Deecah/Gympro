<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<%@ taglib uri="jakarta.tags.core" prefix="c"  %>
<%@ taglib uri="jakarta.tags.fmt"  prefix="fmt"%>   
<%@ page import="model.User" %>

<%
    /* --- kiểm tra đăng nhập --- */
    HttpSession httpSession = request.getSession(false);
    if (httpSession == null || httpSession.getAttribute("user") == null) {
        response.sendRedirect("login.jsp");
        return;
    }
    User currentUser = (User) httpSession.getAttribute("user");
    int userId = currentUser.getUserId();

    int chatId = request.getAttribute("chatId") == null
                 ? -1
                 : (Integer) request.getAttribute("chatId");
%>

<!DOCTYPE html>
<html>
    <head>
        <meta charset="UTF-8">
        <title>GymPro Chat</title>

        <!-- Bootstrap 5 -->
        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
        <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.10.5/font/bootstrap-icons.css">

        <!-- custom -->
        <link href="${pageContext.request.contextPath}/stylecss/chat.css" rel="stylesheet">
    </head>

    <body data-ctx="${pageContext.request.contextPath}" 
      data-chat="<%= chatId %>"
      data-uid="<%= userId %>">
      
      <!-- Toast container -->
      <div id="toastContainer" class="toast-container"></div>

    <div class="container py-3">
    <div class="chat-wrapper">

        <!-- Sidebar -->
        <div class="chat-sidebar">
            <div class="p-3">
                <a href="Navigate?target=backtohome" class="btn btn-outline-secondary mb-3">&larr; Back to home</a>
                <h5 class="p-3 mb-0">
                    <a href="ChatServlet" class="text-decoration-none text-dark fw-bold">
                        <i class="bi bi-chat-left-text me-2"></i> Chats
                    </a>
                </h5>
            </div>
            <c:forEach var="chat" items="${chatList}">
                <div class="chat-summary" onclick="location.href='ChatServlet?chatId=${chat.chatId}'">
                    <div class="d-flex align-items-center">
                        <img src="${empty chat.partnerAvatar ? 'default-avatar.jpg' : chat.partnerAvatar}"
                             class="chat-avatar me-3"/>
                        <div class="flex-grow-1">
                            <div class="partner-name">
                                <i class="bi bi-person-circle"></i> ${chat.partnerName}
                            </div>
                            <div class="partner-role">${chat.partnerRole}</div>
                        </div>
                    </div>
                </div>
            </c:forEach>
        </div>

        <!-- Main chat area -->
        <div class="chat-main">
            <div id="messages" class="chat-messages">
                <c:forEach var="m" items="${messageList}">
                    <div class="${m.senderUserId == userId ? 'text-end' : 'text-start'}">
                        <div class="p-2 bg-primary text-white rounded mb-2 d-inline-block message-box">
                            <div><c:out value="${m.messageContent}" /></div>

                            <c:if test="${not empty m.imageUrl}">
                                <img src="${m.imageUrl}" class="message-image" onclick="showImageModal('${m.imageUrl}')"/>
                            </c:if>

                            <c:if test="${not empty m.fileUrl}">
                                <div class="mt-1">
                                    <a href="${m.fileUrl}" class="btn btn-sm btn-light text-dark" target="_blank">
                                        <i class="bi bi-download me-1"></i>Tải file
                                    </a>
                                </div>
                            </c:if>

                            <div class="message-time">
                                <fmt:formatDate value="${m.sentAtDate}" pattern="HH:mm dd/MM/yy"/>
                            </div>
                        </div>
                    </div>
                </c:forEach>
            </div>

            <div class="chat-input d-flex align-items-center">
                <input id="imageInput" type="file" accept="image/*" hidden>
                <input id="fileInput"  type="file" hidden>

                <button class="btn btn-light me-2" onclick="insertEmoji('😊')" title="Emoji">😊</button>
                <button class="btn btn-light me-2" onclick="imageInput.click()" title="Send Image">📷</button>
                <button class="btn btn-light me-2" onclick="fileInput.click()" title="Send File">📎</button>

                <input id="messageInput" type="text" class="form-control me-2" placeholder="Type a message…" onkeypress="handleKeyPress(event)">
                <button id="sendButton" class="btn btn-primary" onclick="sendMessage()">
                    <span id="sendText">Send</span>
                    <span id="sendLoading" style="display: none;">⏳</span>
                </button>
            </div>
            
            <!-- Preview area -->
            <div id="previewArea" class="preview-area" style="display: none;">
                <div class="preview-content">
                    <div class="preview-header">
                        <span>Preview</span>
                        <button class="btn btn-sm btn-outline-danger" onclick="clearPreview()">✕</button>
                    </div>
                    <div id="previewContent"></div>
                </div>
            </div>
        </div>

    </div>
</div>

        <!-- Image Modal -->
        <div id="imageModal" class="modal fade" tabindex="-1">
            <div class="modal-dialog modal-lg modal-dialog-centered">
                <div class="modal-content">
                    <div class="modal-header">
                        <h5 class="modal-title">Image Preview</h5>
                        <button type="button" class="btn-close" data-bs-dismiss="modal"></button>
                    </div>
                    <div class="modal-body text-center">
                        <img id="modalImage" src="" class="img-fluid" style="max-height: 70vh;">
                    </div>
                </div>
            </div>
        </div>

        <!-- ===================== JS ===================== -->
        <script>
        const currentUserId = "${user.getUserId()}";
        const fullName = "${user.getUserName()}";
        let ws = null;
        let currentRoomId = null;

        function openChat(friendId) {
            if (ws) {
                ws.close();
            }
            currentRoomId =
                "room_" +
                Math.min(currentUserId, friendId) +
                "_" +
                Math.max(currentUserId, friendId);
            ws = new WebSocket(
                "ws://localhost:8080/TripsExeWeb/chatendpoint/" + currentRoomId
            );
            ws.onopen = function () {
                let initobj = {
                    "load": {
                        "roomId": currentRoomId
                    }
                }
                ws.send(JSON.stringify(initobj));
            };
            ws.onmessage = function (event) {
                let dataobj = JSON.parse(event.data);
                const chatBox = document.getElementById("messages");
                const isCurrentUser = dataobj.senderUserId == currentUserId;
                const wrap = document.createElement("div");
                wrap.className = isCurrentUser ? "text-end" : "text-start";
                const b = document.createElement("div");
                b.className = "p-2 bg-primary text-white rounded mb-2 d-inline-block message-box";
                const content = document.createElement("div");
                content.textContent = dataobj.messageContent || "";
                b.appendChild(content);
                if (dataobj.imageUrl) {
                    const img = document.createElement("img");
                    img.src = dataobj.imageUrl;
                    img.className = "message-image";
                    img.onclick = function() {
                        showImageModal(dataobj.imageUrl);
                    };
                    b.appendChild(img);
                }
                if (dataobj.fileUrl) {
                    const fileLink = document.createElement("a");
                    fileLink.href = dataobj.fileUrl;
                    fileLink.target = "_blank";
                    fileLink.className = "btn btn-sm btn-light text-dark mt-1";
                    fileLink.innerHTML = '<i class="bi bi-download me-1"></i>Tải file';
                    b.appendChild(fileLink);
                }
                const time = document.createElement("div");
                time.className = "message-time";
                time.textContent = new Date(dataobj.sentAt).toLocaleTimeString([], {hour: '2-digit', minute: '2-digit'});
                b.appendChild(time);
                wrap.appendChild(b);
                chatBox.appendChild(wrap);
                chatBox.scrollTop = chatBox.scrollHeight;
            };
            ws.onerror = function (error) {
                console.error("WebSocket error: ", error);
            };
            ws.onclose = function () {
                console.log("WebSocket closed");
            };
        }

        async function sendMessage() {
            if (!ws || ws.readyState !== WebSocket.OPEN) {
                alert("WebSocket not connected");
                return;
            }
            const txt = document.getElementById("messageInput").value.trim();
            let imageUrl = "", fileUrl = "";
            // Add upload logic if needed
            if (!txt && !imageUrl && !fileUrl) return;
            let messageobj = {
                "message": {
                    "userId": currentUserId,
                    "roomId": currentRoomId,
                    "fullName": fullName,
                    "content": txt,
                    "imageUrl": imageUrl,
                    "fileUrl": fileUrl
                }
            }
            ws.send(JSON.stringify(messageobj));
            document.getElementById("messageInput").value = "";
        }
        </script>
    </body>
</html>
