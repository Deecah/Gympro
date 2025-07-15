<%@ page contentType="text/html;charset=UTF-8" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<style>
    #toast-container {
        position: fixed;
        top: 20px;
        right: 20px;
        z-index: 9999;
    }

    .toast {
        background-color: #ffffff;
        color: #333;
        padding: 16px 24px;
        margin-bottom: 12px;
        border-radius: 8px;
        box-shadow: 0 4px 12px rgba(0, 0, 0, 0.15);
        position: relative;
        opacity: 1;
        animation: fadein 0.5s ease-in-out;
        transition: opacity 0.5s ease;
        min-width: 250px;
        font-family: sans-serif;
    }

    .toast .close-btn {
        position: absolute;
        top: 50%;
        right: 12px;
        transform: translateY(-50%);
        font-size: 16px;
        color: #888;
        cursor: pointer;
        transition: color 0.2s;
    }


    .toast .close-btn:hover {
        color: #ff0000;
    }

    @keyframes fadein {
        from {
            opacity: 0;
            right: 0;
        }
        to {
            opacity: 1;
            right: 20px;
        }
    }
</style>

<div id="toast-container"></div>

<script>
    const socket = new WebSocket("ws://localhost:8080/SWP391/notification");

    socket.onmessage = function (event) {
        const msg = event.data;
        console.log("🔔 Message received:", msg);
        showToast(msg);
    };

    socket.onopen = () => console.log("✅ WebSocket connected");
    socket.onerror = e => console.error("❌ WebSocket error", e);

function showToast(message) {
    const container = document.getElementById("toast-container");

    const toast = document.createElement("div");
    toast.className = "toast";

    // Tạo thẻ chứa nội dung thông báo
    const msgSpan = document.createElement("span");
    msgSpan.className = "toast-message";
    msgSpan.textContent = message;

    // Tạo nút đóng "×"
    const closeSpan = document.createElement("span");
    closeSpan.className = "close-btn";
    closeSpan.textContent = "×";
    closeSpan.onclick = () => toast.remove();

    // Gắn vào toast
    toast.appendChild(msgSpan);
    toast.appendChild(closeSpan);
    container.appendChild(toast);

    // Tự động biến mất sau 6 giây
    setTimeout(() => {
        toast.style.opacity = '0';
        setTimeout(() => toast.remove(), 500);
    }, 6000);
}
</script>
