package Service;

import Model.Account;
import Util.ConnectionUtil;
import DAO.AccountDAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

public class AccountService {
    private AccountDAO accountDAO;

    public AccountService() {
        this.accountDAO = new AccountDAO();
    }

    public AccountService(AccountDAO newAccountDAO) {
        this.accountDAO = newAccountDAO;
    }

    // Create

    public Account insertAccount(Account accountToInsert) {
        Connection connection = ConnectionUtil.getConnection();
        try {
            String sql = "INSERT INTO account (username, password) VALUES (?, ?)";
            PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, accountToInsert.getUsername());
            ps.setString(2, accountToInsert.getPassword());
            ps.executeUpdate();
            ResultSet pkeyResultSet = ps.getGeneratedKeys();
            if (pkeyResultSet.next()) {
                int generatedAccountId = (int) pkeyResultSet.getLong(1);
                return new Account(generatedAccountId, accountToInsert.getUsername(), accountToInsert.getPassword());
            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return null;
    }

    // Retrieve

    public List<Account> getAllAccounts() {
        return this.accountDAO.getAllAccounts();
    }

    public boolean checkAccountExist(String targetUserName) {
        if (this.accountDAO.getAccountByUsername(targetUserName) != null) {
            return true;
        }
        return false;
    }

    public Account getAccount(String targetUsername, String targetPassword) {
        Account foundAccount = accountDAO.getAccount(targetUsername, targetPassword);
        return foundAccount;
    }
}