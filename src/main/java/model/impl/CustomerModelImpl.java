package model.impl;

import db.DBConnection;
import dto.CustomerDto;
import model.CustomerModel;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

public class CustomerModelImpl implements CustomerModel {

    @Override
    public boolean saveCustomer(CustomerDto customerDto) throws SQLException, ClassNotFoundException {
//        CustomerDto customerDto = new CustomerDto(customerDto.getId()
//                ,customerDto.getName()
//                , customerDto.getAddress()
//                ,customerDto.getSalary()
//        );

        String sql = "INSERT INTO customer VALUES(?,?,?,?)";


        PreparedStatement pstm = DBConnection.getInstance().getConnection().prepareStatement(sql);
        pstm.setString(1,customerDto.getId());
        pstm.setString(2,customerDto.getName());
        pstm.setString(3,customerDto.getAddress());
        pstm.setDouble(4,customerDto.getSalary());
        return pstm.executeUpdate()>0;
    }

    @Override
    public boolean updateCustomer(CustomerDto customerDto) throws SQLException, ClassNotFoundException {
        String sql = "UPDATE customer SET name =?, address=?, salary=? WHERE id=?";
        PreparedStatement pstm = DBConnection.getInstance().getConnection().prepareStatement(sql);
        pstm.setString(1,customerDto.getName());
        pstm.setString(2,customerDto.getAddress());
        pstm.setDouble(3,customerDto.getSalary());
        pstm.setString(4,customerDto.getId());
        return pstm.executeUpdate()>0;
    }

    @Override
    public boolean deleteCustomer(String id) throws SQLException, ClassNotFoundException {

        String sql = "DELETE FROM customer WHERE id=?";
        PreparedStatement pstm = DBConnection.getInstance().getConnection().prepareStatement(sql);
        pstm.setString(1,id);
        return pstm.executeUpdate()>0;
    }

    @Override
    public List<CustomerDto> allCustomer() throws SQLException, ClassNotFoundException {
        return null;
    }

    @Override
    public CustomerDto searchCustomer(String id) {
        return null;
    }
}
