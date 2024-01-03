package DAO;

import Model.Message;
import Util.ConnectionUtil;

import java.util.List;
import java.util.ArrayList;
import java.sql.*;

public class MessageDAO {
    
    // CREATE //////////////////////////

    /**
     * Insert new message into database
     * 
     * @param newMessage
     * @return posted message if successful
     */
    public Message addMessage (Message newMessage) {
        Connection connection = ConnectionUtil.getConnection();
        Message postedMessage = null;
        try {
            String sql = "INSERT INTO message (posted_by, message_text, time_posted_epoch) VALUES (?, ?, ?)";
            PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setInt(1, newMessage.getPosted_by());
            ps.setString(2, newMessage.getMessage_text());
            ps.setLong(3, newMessage.getTime_posted_epoch());
            ps.executeUpdate();
            ResultSet pkeyResultSet = ps.getGeneratedKeys();
            if (pkeyResultSet.next()) {
                int generatedMessageId = (int) pkeyResultSet.getLong(1);
                postedMessage = new Message(generatedMessageId, newMessage.getPosted_by(), newMessage.getMessage_text(), newMessage.getTime_posted_epoch());
            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return postedMessage;
    }

    // RETRIEVE ////////////////////////

    /**
     * Get All Messages from Database
     * @return List of Messages
     */
    public List<Message> getAllMessages() {
        Connection connection = ConnectionUtil.getConnection();
        List<Message> messages = new ArrayList<>();
        try {
            String sql = "SELECT * FROM message";  
            PreparedStatement ps = connection.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Message curMessage = new Message(rs.getInt("message_id"), rs.getInt("posted_by"), rs.getString("message_text"), rs.getLong("time_posted_epoch") );
                messages.add(curMessage);
            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return messages;
    }

    public List<Message> getAllMessages(int targetPostedByID) {
        Connection connection = ConnectionUtil.getConnection();
        List<Message> foundMessages = new ArrayList<>();
        try {
            String sql = "SELECT * FROM message WHERE posted_by = ?";  
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setInt(1, targetPostedByID);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Message curMessage = new Message(rs.getInt("message_id"), rs.getInt("posted_by"), rs.getString("message_text"), rs.getLong("time_posted_epoch") );
                foundMessages.add(curMessage);
            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return foundMessages;
    }

    /**
     * Get a message according to a given message ID
     * 
     * @param targetMessageID
     * @return Message if found (null if not found)
     */
    public Message getMessage(int targetMessageID) {
        Connection connection = ConnectionUtil.getConnection();
        Message foundMessage = null;
        try {
            String sql = "SELECT * FROM message WHERE message_id = ?";  
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setInt(1, targetMessageID);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                foundMessage = new Message(rs.getInt("message_id"), rs.getInt("posted_by"), rs.getString("message_text"), rs.getLong("time_posted_epoch") );
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return foundMessage;
    }

    // UPDATE //////////////////////////

    /**
     * Update a Message Text for message associated with given Message ID
     * 
     * @param targetMessageID
     * @param targetMessageText
     * @return Updated Message if update was successful (null if update failed)
     */
    public Message updateMessageText(int targetMessageID, String targetMessageText) {
        Message updatedMessage = null;
        try {
            Connection connection = ConnectionUtil.getConnection();
            // Update Message
            String sql = "UPDATE message SET message_text = ? WHERE message_id = ?";
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setString(1, targetMessageText);
            ps.setInt(2, targetMessageID);
            int wasUpdated = ps.executeUpdate();

            if (wasUpdated >= 1) {
                // Retrieve updated Message
                String sql2 = "Select * FROM message WHERE message_id = ?";
                PreparedStatement ps2 = connection.prepareStatement(sql2);
                ps2.setInt(1, targetMessageID);
                ResultSet rs = ps2.executeQuery();
                if (rs.next()) {
                    updatedMessage = new Message(rs.getInt("message_id"), rs.getInt("posted_by"), rs.getString("message_text"), rs.getLong("time_posted_epoch"));
                }
            } else {
                // Message Update failed
                updatedMessage = null;
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return updatedMessage;
    }

    // DELETE //////////////////////////

    /**
     * Deletes a message according to its message ID
     * 
     * @param targetMessage
     * @return deletedMessage
     */
    public Message deleteMessage(Message targetMessage) {
        Connection connection = ConnectionUtil.getConnection();
        Message deletedMessage = null;
        try {
            String sql = "DELETE FROM message WHERE message_id = ?";
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setInt(1, targetMessage.getMessage_id());
            ps.executeUpdate();
            deletedMessage = new Message(targetMessage.getMessage_id(), targetMessage.getPosted_by(), targetMessage.getMessage_text(), targetMessage.getTime_posted_epoch());
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return deletedMessage;
    }
}
