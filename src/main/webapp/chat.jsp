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

        <style>
            .chat-avatar{
                width:45px;
                height:45px;
                border-radius:50%;
                object-fit:cover
            }
            .message-box img.message-image{
                max-width:250px;
                border-radius:8px;
                margin-top:5px
            }
            .message-time{
                font-size:.75rem;
                color:#dee2e6;
                margin-top:4px
            }
        </style>
    </head>

    <body data-ctx="${pageContext.request.contextPath}" 
      data-chat="<%= chatId %>"
      data-uid="<%= userId %>">

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
                                <img src="${m.imageUrl}" class="message-image"/>
                            </c:if>

                            <c:if test="${not empty m.fileUrl}">
                                <div class="mt-1">
                                    <a href="${m.fileUrl}" class="btn btn-sm btn-light text-dark" target="_blank">Tải file</a>
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

                <button class="btn btn-light me-2" onclick="insertEmoji('😊')">😊</button>
                <button class="btn btn-light me-2" onclick="imageInput.click()">📷</button>
                <button class="btn btn-light me-2" onclick="fileInput.click()">📎</button>

                <input id="messageInput" type="text" class="form-control me-2" placeholder="Type a message…">
                <button class="btn btn-primary" onclick="sendMessage()">Send</button>
            </div>
        </div>

    </div>
</div>

        <!-- ===================== JS ===================== -->
        <script>
            /* ---------- cấu hình ---------- */
            const rawCtx = document.body.dataset.ctx || "";
            const ctx = rawCtx.endsWith("/") ? rawCtx.slice(0, -1) : rawCtx;

            const chatId = Number(document.body.dataset.chat);
            const userId = Number(document.body.dataset.uid);

            /* ---------- upload (REST) ---------- */
            async function doUpload(file, endPoint) {
                const fd = new FormData();
                fd.append("file", file);
                const res = await fetch(`${ctx}/upload/${endPoint}`, {method: "POST", body: fd});
                        if (!res.ok)
                            throw new Error("Upload failed");
                        return (await res.json()).url;          
                    }

                    /* ---------- WebSocket ---------- */
                    let socket;
                    function wsUrl() {
                        const proto = location.protocol === "https:" ? "wss" : "ws";
                        return `${proto}${location.host}${ctx}ws/chat/${chatId}/${userId}`;
                            }
                            function connectWs() {
                                if (!chatId || !userId) {
                                    console.warn("Thiếu chatId/userId");
                                    return;
                                }
                                socket = new WebSocket(wsUrl());

                                socket.onopen = () => console.log("[WS] open");
                                socket.onclose = () => {
                                    console.warn("[WS] closed – reconnect in 2 s");
                                    setTimeout(connectWs, 2000);
                                };
                                socket.onerror = e => console.error("[WS] error", e);
                                socket.onmessage = e => appendMsg(JSON.parse(e.data));
                            }
                            window.addEventListener("DOMContentLoaded", connectWs);

                            /* ---------- UI helper ---------- */
                            const box = document.getElementById("messages");

                            function appendMsg(m) {
                                const wrap = document.createElement("div");
                                wrap.className = Number(m.senderUserId) === userId ? "text-end" : "text-start";

                                const b = document.createElement("div");
                                b.className = "p-2 bg-primary text-white rounded mb-2 d-inline-block message-box";

                                // Tạo inner content chắc chắn không bị lỗi
                                const content = document.createElement("div");
                                content.textContent = m.messageContent || "";
                                b.appendChild(content);

                                if (m.imageUrl) {
                                    const img = document.createElement("img");
                                    img.src = m.imageUrl;
                                    img.className = "message-image";
                                    b.appendChild(img);
                                }

                                if (m.fileUrl) {
                                    const fileLink = document.createElement("a");
                                    fileLink.href = m.fileUrl;
                                    fileLink.target = "_blank";
                                    fileLink.className = "btn btn-sm btn-light text-dark mt-1";
                                    fileLink.textContent = "Tải file";
                                    b.appendChild(fileLink);
                                }

                                const time = document.createElement("div");
                                time.className = "message-time";
                                time.textContent = new Date(m.sentAt).toLocaleTimeString([], {hour: '2-digit', minute: '2-digit'});
                                b.appendChild(time);

                                wrap.appendChild(b);
                                document.getElementById("messages").appendChild(wrap);
                                box.scrollTop = box.scrollHeight;
                            }

                            /* ---------- gửi tin ---------- */
                            async function sendMessage() {
                                if (!socket || socket.readyState !== WebSocket.OPEN) {
                                    alert("WebSocket chưa kết nối");
                                    return;
                                }

                                const txt = document.getElementById("messageInput").value.trim();
                                let imageUrl = "", fileUrl = "";

                                try {
                                    if (imageInput.files.length) {
                                        imageUrl = await doUpload(imageInput.files[0], "image");
                                    }
                                    if (fileInput.files.length) {
                                        fileUrl = await doUpload(fileInput.files[0], "file");
                                    }
                                } catch (e) {
                                    alert(e.message);
                                    return;
                                }

                                if (!txt && !imageUrl && !fileUrl)
                                    return;

                                socket.send(JSON.stringify({chatId, messageContent: txt, imageUrl, fileUrl}));
                                console.log(JSON.stringify({chatId, messageContent: txt, imageUrl, fileUrl}))

                                // reset form
                                document.getElementById("messageInput").value = "";
                                imageInput.value = "";
                                fileInput.value = "";
                            }

                            function insertEmoji(e) {
                                const inp = document.getElementById("messageInput");
                                inp.value += e;
                                inp.focus();
                            }
        </script>
    </body>
</html>
