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
            // Configuration
            const rawCtx = document.body.dataset.ctx || "";
            const ctx = rawCtx.endsWith("/") ? rawCtx.slice(0, -1) : rawCtx;
            const altCtx = window.location.pathname.substring(0, window.location.pathname.indexOf('/', 1)) || '';
            const finalCtx = ctx || altCtx || '/SWP391';
            
            const chatId = Number(document.body.dataset.chat);
            const userId = Number(document.body.dataset.uid);

            // Upload function
            async function doUpload(file, endPoint) {
                const fd = new FormData();
                fd.append("file", file);
                
                const uploadUrl = finalCtx + '/chat-upload/' + endPoint;
                console.log("Upload URL:", uploadUrl);
                
                try {
                    const res = await fetch(uploadUrl, {method: "POST", body: fd});
                    
                    if (!res.ok) {
                        if (res.status === 401) {
                            throw new Error("Unauthorized - Please login again");
                        }
                        if (res.status === 500) {
                            throw new Error("Server error - Please try again later");
                        }
                        const errorData = await res.json();
                        throw new Error(errorData.error || "Upload failed");
                    }
                    
                    const data = await res.json();
                    if (!data.success && data.error) {
                        throw new Error(data.error);
                    }
                    
                    return data.url;
                } catch (error) {
                    console.error("Upload error:", error);
                    if (error.message.includes("JSON")) {
                        throw new Error("Server returned invalid response. Please try again.");
                    }
                    throw error;
                }
            }

            // WebSocket
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

            // UI helper
            const box = document.getElementById("messages");

            function appendMsg(m) {
                const wrap = document.createElement("div");
                wrap.className = Number(m.senderUserId) === userId ? "text-end" : "text-start";

                const b = document.createElement("div");
                b.className = "p-2 bg-primary text-white rounded mb-2 d-inline-block message-box";

                const content = document.createElement("div");
                content.textContent = m.messageContent || "";
                b.appendChild(content);

                if (m.imageUrl) {
                    const img = document.createElement("img");
                    img.src = m.imageUrl;
                    img.className = "message-image";
                    img.onclick = function() {
                        showImageModal(m.imageUrl);
                    };
                    b.appendChild(img);
                }

                if (m.fileUrl) {
                    const fileLink = document.createElement("a");
                    fileLink.href = m.fileUrl;
                    fileLink.target = "_blank";
                    fileLink.className = "btn btn-sm btn-light text-dark mt-1";
                    fileLink.innerHTML = '<i class="bi bi-download me-1"></i>Tải file';
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

            // Send message
            async function sendMessage() {
                if (!socket || socket.readyState !== WebSocket.OPEN) {
                    alert("WebSocket chưa kết nối");
                    return;
                }

                const txt = document.getElementById("messageInput").value.trim();
                let imageUrl = "", fileUrl = "";

                // Show loading
                const sendButton = document.getElementById('sendButton');
                const sendText = document.getElementById('sendText');
                const sendLoading = document.getElementById('sendLoading');
                
                sendButton.disabled = true;
                sendText.style.display = 'none';
                sendLoading.style.display = 'inline';

                try {
                    if (imageInput.files.length) {
                        imageUrl = await doUpload(imageInput.files[0], "image");
                    }
                    if (fileInput.files.length) {
                        fileUrl = await doUpload(fileInput.files[0], "file");
                    }
                } catch (e) {
                    showErrorToast(e.message);
                    // Reset loading state
                    sendButton.disabled = false;
                    sendText.style.display = 'inline';
                    sendLoading.style.display = 'none';
                    return;
                }

                if (!txt && !imageUrl && !fileUrl) {
                    // Reset loading state
                    sendButton.disabled = false;
                    sendText.style.display = 'inline';
                    sendLoading.style.display = 'none';
                    return;
                }

                socket.send(JSON.stringify({chatId, messageContent: txt, imageUrl, fileUrl}));

                // reset form
                document.getElementById("messageInput").value = "";
                clearPreview();
                
                // Reset loading state
                sendButton.disabled = false;
                sendText.style.display = 'inline';
                sendLoading.style.display = 'none';
            }
            
            function handleKeyPress(event) {
                if (event.key === 'Enter') {
                    sendMessage();
                }
            }
            
            function showImageModal(imageUrl) {
                document.getElementById('modalImage').src = imageUrl;
                const modal = new bootstrap.Modal(document.getElementById('imageModal'));
                modal.show();
            }
            
            function showErrorToast(message) {
                const container = document.getElementById('toastContainer');
                const toast = document.createElement('div');
                toast.className = 'toast-message';
                toast.textContent = message;
                
                container.appendChild(toast);
                
                // Auto remove after 5 seconds
                setTimeout(() => {
                    if (toast.parentNode) {
                        toast.parentNode.removeChild(toast);
                    }
                }, 5000);
            }

            function insertEmoji(e) {
                const inp = document.getElementById("messageInput");
                inp.value += e;
                inp.focus();
            }
            
            // Preview functions
            function showPreview(file, type) {
                const previewArea = document.getElementById('previewArea');
                const previewContent = document.getElementById('previewContent');
                
                previewContent.innerHTML = '';
                
                if (type === 'image') {
                    const img = document.createElement('img');
                    img.src = URL.createObjectURL(file);
                    img.className = 'preview-image';
                    previewContent.appendChild(img);
                } else if (type === 'file') {
                    const fileDiv = document.createElement('div');
                    fileDiv.className = 'preview-file';
                    
                    const icon = document.createElement('div');
                    icon.className = 'preview-file-icon';
                    icon.textContent = '📎';
                    
                    const info = document.createElement('div');
                    info.className = 'preview-file-info';
                    
                    const name = document.createElement('div');
                    name.className = 'preview-file-name';
                    name.textContent = file.name;
                    
                    const size = document.createElement('div');
                    size.className = 'preview-file-size';
                    size.textContent = formatFileSize(file.size);
                    
                    info.appendChild(name);
                    info.appendChild(size);
                    fileDiv.appendChild(icon);
                    fileDiv.appendChild(info);
                    previewContent.appendChild(fileDiv);
                }
                
                previewArea.style.display = 'block';
            }
            
            function clearPreview() {
                document.getElementById('previewArea').style.display = 'none';
                document.getElementById('previewContent').innerHTML = '';
                imageInput.value = '';
                fileInput.value = '';
            }
            
            function formatFileSize(bytes) {
                if (bytes === 0) return '0 Bytes';
                const k = 1024;
                const sizes = ['Bytes', 'KB', 'MB', 'GB'];
                const i = Math.floor(Math.log(bytes) / Math.log(k));
                return parseFloat((bytes / Math.pow(k, i)).toFixed(2)) + ' ' + sizes[i];
            }
            
            // Add event listeners for file inputs
            document.addEventListener('DOMContentLoaded', function() {
                imageInput.addEventListener('change', function(e) {
                    if (e.target.files.length > 0) {
                        showPreview(e.target.files[0], 'image');
                    }
                });
                
                fileInput.addEventListener('change', function(e) {
                    if (e.target.files.length > 0) {
                        showPreview(e.target.files[0], 'file');
                    }
                });
            });
        </script>
    </body>
</html>
