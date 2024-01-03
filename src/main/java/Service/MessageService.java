package Service;

import Model.Message;
import Util.ConnectionUtil;
import DAO.AccountDAO;
import DAO.MessageDAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

public class MessageService {
    private MessageDAO messageDAO;

    public MessageService() {
        this.messageDAO = new MessageDAO();
    }

    public MessageService(MessageDAO newMessageDAO) {
        this.messageDAO = newMessageDAO;
    }

    // Retrieve //////////////////////

    public List<Message> getAllMessages() {
        return this.messageDAO.getAllMessages();
    }

    public Message getMessage(int targetMessageID) {
        return this.messageDAO.getMessage(targetMessageID);
    }

}
