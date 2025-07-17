package dao;

import connectDB.ConnectDatabase;
import java.sql.*;
import model.ChatSummary;
import model.Message;

import java.util.ArrayList;
import java.util.List;

public class ChatDAO {

    public int createChatIfNotExists(int userId1, int userId2) {
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            conn = ConnectDatabase.getInstance().openConnection();

            String checkSql = "SELECT ChatID FROM Chats WHERE (UserID1 = ? AND UserID2 = ?) OR (UserID1 = ? AND UserID2 = ?)";
            ps = conn.prepareStatement(checkSql);
            ps.setInt(1, userId1);
            ps.setInt(2, userId2);
            ps.setInt(3, userId2);
            ps.setInt(4, userId1);
            rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt("ChatID");
            }

            String insertSql = "INSERT INTO Chats (UserID1, UserID2, MessageID) VALUES (?, ?, NULL)";
            ps = conn.prepareStatement(insertSql, Statement.RETURN_GENERATED_KEYS);
            ps.setInt(1, userId1);
            ps.setInt(2, userId2);
            ps.executeUpdate();

            rs = ps.getGeneratedKeys();
            if (rs.next()) {
                return rs.getInt(1);
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            close(rs);
            close(ps);
            close(conn);
        }
        return -1;
    }

    public void saveMessage(Message msg) {
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            conn = ConnectDatabase.getInstance().openConnection();
            String insert = "INSERT INTO Messages (ChatID, SenderUserID, MessageContent, ImageUrl, FileUrl, SentAt) VALUES (?, ?, ?, ?, ?, ?)";
            ps = conn.prepareStatement(insert, Statement.RETURN_GENERATED_KEYS);
            ps.setInt(1, msg.getChatId());
            ps.setInt(2, msg.getSenderUserId());
            ps.setString(3, msg.getMessageContent());
            ps.setString(4, msg.getImageUrl());
            ps.setString(5, msg.getFileUrl());
            ps.setTimestamp(6, Timestamp.valueOf(msg.getSentAt()));
            ps.executeUpdate();

            rs = ps.getGeneratedKeys();
            if (rs.next()) {
                int messageId = rs.getInt(1);
                PreparedStatement update = conn.prepareStatement("UPDATE Chats SET MessageID = ? WHERE ChatID = ?");
                update.setInt(1, messageId);
                update.setInt(2, msg.getChatId());
                update.executeUpdate();
                update.close();
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            close(rs);
            close(ps);
            close(conn);
        }
    }

    public boolean isChatAllowed(int userA, int userB) {
        String sql = "SELECT 1 FROM Contracts WHERE ((CustomerID = ? AND TrainerID = ?) OR (CustomerID = ? AND TrainerID = ?)) AND Status IN ('active', 'pending')";
        try (Connection conn = ConnectDatabase.getInstance().openConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, userA);
            ps.setInt(2, userB);
            ps.setInt(3, userB);
            ps.setInt(4, userA);
            ResultSet rs = ps.executeQuery();
            return rs.next();

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public int getOtherParticipant(int chatId, int senderId) {
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        int otherId = -1;

        try {
            conn = ConnectDatabase.getInstance().openConnection();
            String sql = "SELECT CASE WHEN UserID1 = ? THEN UserID2 ELSE UserID1 END AS OtherUser FROM Chats WHERE ChatID = ?";
            ps = conn.prepareStatement(sql);
            ps.setInt(1, senderId);
            ps.setInt(2, chatId);
            rs = ps.executeQuery();

            if (rs.next()) {
                otherId = rs.getInt("OtherUser");
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            close(rs);
            close(ps);
            close(conn);
        }

        return otherId;
    }

    public List<ChatSummary> getChatsForUser(int userId) {
        List<ChatSummary> list = new ArrayList<>();
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        String sql = "SELECT c.ChatID, " +
                "       CASE WHEN c.UserID1 = ? THEN c.UserID2 ELSE c.UserID1 END AS partnerId, " +
                "       u.Name AS partnerName, " +
                "       u.Role AS partnerRole, " +
                "       u.AvatarUrl, " +
                "       m.MessageContent AS lastMessage, " +
                "       m.SenderUserID " +
                "FROM Chats c " +
                "JOIN Users u ON u.Id = CASE WHEN c.UserID1 = ? THEN c.UserID2 ELSE c.UserID1 END " +
                "LEFT JOIN Messages m ON m.MessageID = c.MessageID " +
                "WHERE (c.UserID1 = ? OR c.UserID2 = ?) AND c.UserID1 <> c.UserID2 " +
                "ORDER BY m.SentAt DESC";

        try {
            conn = ConnectDatabase.getInstance().openConnection();
            ps = conn.prepareStatement(sql);
            ps.setInt(1, userId); // CASE
            ps.setInt(2, userId); // JOIN
            ps.setInt(3, userId); // WHERE
            ps.setInt(4, userId); // WHERE

            rs = ps.executeQuery();

            while (rs.next()) {
                int partnerId = rs.getInt("partnerId");

                if (partnerId == userId) continue;

                ChatSummary chat = new ChatSummary();
                chat.setChatId(rs.getInt("ChatID"));
                chat.setPartnerId(partnerId);
                chat.setPartnerName(rs.getString("partnerName"));
                chat.setPartnerRole(rs.getString("partnerRole"));
                chat.setPartnerAvatar(rs.getString("AvatarUrl"));
                chat.setLastMessage(rs.getString("lastMessage"));
                list.add(chat);
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            close(rs);
            close(ps);
            close(conn);
        }

        return list;
    }

    public List<Message> getMessagesByChatId(int chatId, int offset, int limit) {
        List<Message> list = new ArrayList<>();
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        String sql = "SELECT MessageID, ChatID, SenderUserID, MessageContent, ImageUrl, FileUrl, SentAt " +
             "FROM Messages WHERE ChatID = ? ORDER BY SentAt ASC OFFSET ? ROWS FETCH NEXT ? ROWS ONLY";
        try {
            conn = ConnectDatabase.getInstance().openConnection();
            ps = conn.prepareStatement(sql);
            ps.setInt(1, chatId);
            ps.setInt(2, offset);
            ps.setInt(3, limit);
            rs = ps.executeQuery();

            while (rs.next()) {
                Message msg = new Message();
                msg.setMessageId(rs.getInt("MessageID"));
                msg.setChatId(rs.getInt("ChatID"));
                msg.setSenderUserId(rs.getInt("SenderUserID"));
                msg.setMessageContent(rs.getString("MessageContent"));
                msg.setImageUrl(rs.getString("ImageUrl"));
                msg.setFileUrl(rs.getString("FileUrl"));
                msg.setSentAt(rs.getTimestamp("SentAt").toLocalDateTime());
                // KHÔNG gọi msg.setIsRead(...) nữa
                list.add(msg);
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            close(rs);
            close(ps);
            close(conn);
        }

        return list;
    }

    private void close(AutoCloseable ac) {
        try {
            if (ac != null) {
                ac.close();
            }
        } catch (Exception ignored) {}
    }
}
