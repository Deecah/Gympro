package controller;

import dao.ChatDAO;
import model.ChatSummary;
import model.Message;
import model.User;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@WebServlet(name = "ChatServlet", urlPatterns = {"/ChatServlet"})
public class ChatServlet extends HttpServlet {

    private final ChatDAO dao = new ChatDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        /* -------- kiểm tra đăng nhập -------- */
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            response.sendRedirect("login.jsp");
            return;
        }
        User currentUser = (User) session.getAttribute("user");
        int userId = currentUser.getUserId();

        try {
            /* -------- danh sách chat -------- */
            List<ChatSummary> chatList = dao.getChatsForUser(userId);
            request.setAttribute("chatList", chatList);

            /* -------- nếu chọn chat cụ thể -------- */
            String chatIdParam = request.getParameter("chatId");
            if (chatIdParam != null && !chatIdParam.isBlank()) {

                int chatId = Integer.parseInt(chatIdParam);

                ChatSummary selected = chatList.stream()
                        .filter(c -> c.getChatId() == chatId)
                        .findFirst()
                        .orElse(null);

                /* kiểm tra quyền */
                if (selected != null && dao.isChatAllowed(userId, selected.getPartnerId())) {

                    int page   = request.getParameter("page") != null
                            ? Integer.parseInt(request.getParameter("page")) : 0;
                    int limit  = 20;
                    int offset = page * limit;

                    List<Message> messages = dao.getMessagesByChatId(chatId, offset, limit);

                    request.setAttribute("messageList", messages);
                    request.setAttribute("chatId", chatId); // <-- JSP dùng biến này
                    request.setAttribute("page", page);

                } else {
                    System.out.printf("User %d không có quyền truy cập chat %d%n", userId, chatId);
                }
            }

            request.getRequestDispatcher("chat.jsp").forward(request, response);

        } catch (Exception e) {
            e.printStackTrace();
            response.sendError(500, "Server error khi tải trò chuyện.");
        }
    }
}
