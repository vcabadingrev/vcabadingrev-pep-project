package DAO;

import Model.Message;
import Util.ConnectionUtil;

import java.util.List;
import java.util.ArrayList;
import java.sql.*;

public class MessageDAO {
    
    // RETRIEVE ////////////////////////

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
}
