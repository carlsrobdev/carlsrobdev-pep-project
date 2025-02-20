package DAO;
import java.sql.*;

import Model.Account;
import Util.ConnectionUtil;

public class AccountDAO {
    Connection connection;

    public AccountDAO() {
        connection = ConnectionUtil.getConnection();
    }
    
    public Account createAccount(Account account){
        try {
            String sql = "INSERT INTO account (username, password) VALUES (?, ?)";
            PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, account.getUsername());
            ps.setString(2, account.getPassword());
            ps.executeUpdate();

            ResultSet pkeyResultSet = ps.getGeneratedKeys();
            if(pkeyResultSet.next()){
                int account_id = (int) pkeyResultSet.getInt(1);
                return new Account(account_id, account.getUsername(), account.getPassword());
            }
        }catch(SQLException e){
            e.printStackTrace();
        }
        return null;
    }

    public Account getAccountByUsername(String username){
        Account account = null;
        try {
            String sql = "SELECT * FROM account WHERE username = ?";
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setString(1, username);
            ResultSet rs = ps.executeQuery();
            if(rs.next()) {
                account = new Account(rs.getInt("account_id"), rs.getString("username"), rs.getString("password"));
            }
        }catch(SQLException e){
            e.printStackTrace();
        }
        return account;
    }

    public Account verifyAccount(String username, String password){
        Account account = null;
        try {
            String sql = "SELECT * FROM account WHERE username = ? AND password = ?";
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setString(1, username);
            ps.setString(2, password);
            ResultSet rs = ps.executeQuery();
            if(rs.next()) {
                account = new Account(rs.getInt("account_id"), rs.getString("username"), rs.getString("password"));
            }
        }catch(SQLException e){
            e.printStackTrace();
        }
        return account;
    }
}
