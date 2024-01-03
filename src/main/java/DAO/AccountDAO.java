package DAO;

import Model.Account;
import Util.ConnectionUtil;

import java.util.List;
import java.util.ArrayList;
import java.sql.*;

public class AccountDAO {
    
    public List<Account> getAllAccounts() {
        Connection connection = ConnectionUtil.getConnection();
        List<Account> accounts = new ArrayList<>();
        try {
            String sql = "SELECT * from account";
            PreparedStatement ps = connection.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Account curAccount = new Account(rs.getInt("account_id"), rs.getString("username"), rs.getString("password"));
                accounts.add(curAccount);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return accounts;
    }

    public Account getAccountByUsername(String targetUserName) {
        Connection connection = ConnectionUtil.getConnection();
        Account foundAccount = null;
        try {
            String sql = "SELECT (account_id, username) FROM account WHERE username = ?";
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setString(1, targetUserName);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                foundAccount = new Account(rs.getInt("account_id"), rs.getString("username"), "****");
            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return foundAccount;
    }

    public Account getAccount(String targetUserName, String targetPassword) {
        Connection connection = ConnectionUtil.getConnection();
        Account foundAccount = null;
        try {
            String sql = "SELECT * FROM account WHERE username = ? AND password = ?";
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setString(1, targetUserName);
            ps.setString(2, targetPassword);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                foundAccount = new Account(rs.getInt("account_id"), rs.getString("username"), rs.getString("password"));
            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return foundAccount;
    }

    public Account geAccount(int targetAccountID) {
        Connection connection = ConnectionUtil.getConnection();
        Account foundAccount = null;
        try {
            String sql = "SELECT * FROM account WHERE account_id = ?";
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setInt(1, targetAccountID);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                foundAccount = new Account(rs.getInt("account_id"), rs.getString("username"), rs.getString("password"));
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return foundAccount;
    }
}
