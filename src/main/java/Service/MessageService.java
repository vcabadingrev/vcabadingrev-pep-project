package Service;

import Model.Message;
import Util.ConnectionUtil;
import Service.AccountService;
import DAO.MessageDAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

public class MessageService {
    private MessageDAO messageDAO;
    private AccountService accountService;

    public MessageService() {
        this.messageDAO = new MessageDAO();
        this.accountService = new AccountService();
    }

    public MessageService(MessageDAO newMessageDAO) {
        this.messageDAO = newMessageDAO;
        this.accountService = new AccountService();
    }

    // Insert ////////////////////////

    public Message addMessage(Message newMessage) {
        // Check is new messate exists
        if (newMessage == null) {
            return null;
        }

        // Validate parameters of new message
        // Check for empty message text
        if (newMessage.getMessage_text() == null || newMessage.getMessage_text().length()<1) {
            return null;
        }
        // Check for too long message text
        if (newMessage.getMessage_text().length() > 255) {
            return null;
        }
        // Check if message poster's account exists in the database
        if (accountService.checkAccountExist(newMessage.getPosted_by()) == false) {
            return null;
        }

        Message postedMessage = messageDAO.addMessage(newMessage);
        return postedMessage;
    }

    // Retrieve //////////////////////

    public List<Message> getAllMessages() {
        return this.messageDAO.getAllMessages();
    }

    public Message getMessage(int targetMessageID) {
        return this.messageDAO.getMessage(targetMessageID);
    }

    // Delete ////////////////////////

    public Message deleteMessage(int targetMessageID) {
        // Verify if message exists
        Message targetMessage = getMessage(targetMessageID);
        if (targetMessage == null) {
            return null;
        }
        Message deletedMessage = this.messageDAO.deleteMessage(targetMessage);
        return deletedMessage;
    }

}
