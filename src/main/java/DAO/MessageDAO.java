package DAO;

import java.sql.*;
import java.util.Collection;
import java.util.LinkedList;

import Model.Message;
import Util.ConnectionUtil;

public class MessageDAO {
    Connection connection;

    public MessageDAO() {
        connection = ConnectionUtil.getConnection();
    }
    
    public Message createMessage(Message message){
        try {
            String sql = "INSERT INTO message (posted_by, message_text, time_posted_epoch) VALUES (?, ?, ?)";
            PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setInt(1, message.getPosted_by());
            ps.setString(2, message.getMessage_text());
            ps.setLong(3, message.getTime_posted_epoch());
            ps.executeUpdate();

            ResultSet pkeyResultSet = ps.getGeneratedKeys();
            if(pkeyResultSet.next()){
                int message_id = (int) pkeyResultSet.getInt(1);
                return new Message(message_id, message.getPosted_by(), message.getMessage_text(), message.getTime_posted_epoch());
            }
        }catch(SQLException e){
            e.printStackTrace();
        }
        return null;
    }

    public Collection<Message> getAllMessages(){
        Collection<Message> messages = new LinkedList<>();
        try {
            
            String sql = "SELECT * FROM message";
            Statement ps = connection.createStatement();
            ResultSet result = ps.executeQuery(sql);

            while (result.next()) {
                messages.add(new Message(result.getInt(1), result.getInt(2), result.getString(3), result.getLong(4)));
            }
            
        }catch(SQLException e){
            e.printStackTrace();
        }
        return messages;
    }

    public Message getMessageById(int id){
        Message message = null;
        try {
            
            String sql = "SELECT * FROM message WHERE message_id = ?";
            PreparedStatement ps = connection.prepareStatement(sql);

            ps.setInt(1, id);
            ResultSet result = ps.executeQuery();

            while (result.next()) {
                message = new Message(result.getInt(1), result.getInt(2), result.getString(3), result.getLong(4));
            }
            
        }catch(SQLException e){
            e.printStackTrace();
        }
        return message;
    }

    public boolean updateMessageText(int messageId, String message){
        try {
            String sql = "UPDATE message SET message_text = ? WHERE message_id = ?";
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setString(1, message);
            ps.setInt(2, messageId);
            int rowsUpdated = ps.executeUpdate();
            return rowsUpdated > 0;
        }catch(SQLException e){
            e.printStackTrace();
        }
        return false;
    }

    public Collection<Message> getMessagesByAccount(int postedBy){
        Collection<Message> messages = new LinkedList<>();
        try {
            
            String sql = "SELECT * FROM message WHERE posted_by = ?";
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setInt(1, postedBy);
            ResultSet result = ps.executeQuery();

            while (result.next()) {
                messages.add(new Message(result.getInt(1), result.getInt(2), result.getString(3), result.getLong(4)));
            }
            
        }catch(SQLException e){
            e.printStackTrace();
        }
        return messages;
    }

    public void deleteMessageText(int messageId){
        try {
            String sql = "DELETE FROM message WHERE message_id = ?";
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setInt(1, messageId);
            ps.executeUpdate();
        }catch(SQLException e){
            e.printStackTrace();
        }
    }
}
