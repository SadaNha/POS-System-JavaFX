package model.impl;

import db.DBConnection;
import dto.OrderDto;
import model.OrderDetailsModel;
import model.OrderModel;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class OrderModelImpl implements OrderModel {
    OrderDetailsModel orderDetailsModel = new OrderDetailsDtoModelImpl();
    @Override
    public boolean saveOrder(OrderDto orderDto) throws SQLException {
        Connection connection=null;
        try{
            connection = DBConnection.getInstance().getConnection();
            connection.setAutoCommit(false);
            String sql = "INSERT INTO orders VALUES(?,?,?)";
            PreparedStatement pstm = connection.prepareStatement(sql);
            pstm.setString(1,orderDto.getOrderId());
            pstm.setString(2,orderDto.getDate());
            pstm.setString(3,orderDto.getCustId());
            if(pstm.executeUpdate()>0){
                boolean isDetailsSaved = orderDetailsModel.saveOrderDetails(orderDto.getList());
                if (isDetailsSaved){
                    connection.commit();
                    return true;
                }
            }

        }catch ( SQLException | ClassNotFoundException exception){
            connection.rollback();
        }finally {
            connection.setAutoCommit(true);
        }
        return false;
    }

    @Override
    public OrderDto lastOrder() throws SQLException, ClassNotFoundException {
        String sql = "SELECT * from orders ORDER BY id DESC LIMIT 1";
        PreparedStatement pstm = DBConnection.getInstance().getConnection().prepareStatement(sql);
        ResultSet resultSet=pstm.executeQuery();
        if(resultSet.next()){
            return new OrderDto(
                    resultSet.getString(1),
                    resultSet.getString(2),
                    resultSet.getString(3),
                    null
            );
        }
        return null;
    }
}
