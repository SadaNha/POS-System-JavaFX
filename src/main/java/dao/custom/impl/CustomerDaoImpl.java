package dao.custom.impl;

import dao.util.CrudUtil;
import db.DBConnection;
import dto.CustomerDto;
import dao.custom.CustomerDao;
import entity.Customer;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CustomerDaoImpl implements CustomerDao {

//    @Override
//    public boolean saveCustomer(CustomerDto customerDto) throws SQLException, ClassNotFoundException {
//
//
//        String sql = "INSERT INTO customer VALUES(?,?,?,?)";
//
//
//        PreparedStatement pstm = DBConnection.getInstance().getConnection().prepareStatement(sql);
//        pstm.setString(1,customerDto.getId());
//        pstm.setString(2,customerDto.getName());
//        pstm.setString(3,customerDto.getAddress());
//        pstm.setDouble(4,customerDto.getSalary());
//        return pstm.executeUpdate()>0;
//    }

//    @Override
//    public boolean updateCustomer(CustomerDto customerDto) throws SQLException, ClassNotFoundException {
//        String sql = "UPDATE customer SET name =?, address=?, salary=? WHERE id=?";
//        PreparedStatement pstm = DBConnection.getInstance().getConnection().prepareStatement(sql);
//        pstm.setString(1,customerDto.getName());
//        pstm.setString(2,customerDto.getAddress());
//        pstm.setDouble(3,customerDto.getSalary());
//        pstm.setString(4,customerDto.getId());
//        return pstm.executeUpdate()>0;
//    }

//    @Override
//    public boolean deleteCustomer(String id) throws SQLException, ClassNotFoundException {
//
//        String sql = "DELETE FROM customer WHERE id=?";
//        PreparedStatement pstm = DBConnection.getInstance().getConnection().prepareStatement(sql);
//        pstm.setString(1,id);
//        return pstm.executeUpdate()>0;
//    }

//    @Override
//    public List<CustomerDto> allCustomer() throws SQLException, ClassNotFoundException {
//        List<CustomerDto> list = new ArrayList<>();
//        String sql = "SELECT * FROM customer";
//        PreparedStatement pstm = DBConnection.getInstance().getConnection().prepareStatement(sql);
//        ResultSet rst = pstm.executeQuery();
//        while (rst.next()){
//
//            list.add(new CustomerDto(
//                    rst.getString(1),
//                    rst.getString(2),
//                    rst.getString(3),
//                    rst.getDouble(4)
//            ));
//
//        }
//        return list;
//    }

    @Override
    public CustomerDto searchCustomer(String id) throws SQLException, ClassNotFoundException {

        String sql = "SELECT * FROM customer WHERE id=?";
//        PreparedStatement pstm = DBConnection.getInstance().getConnection().prepareStatement(sql);
//        pstm.setString(1,id);
//        ResultSet rst = pstm.executeQuery();

        ResultSet rst = CrudUtil.execute(sql,id);
        if (rst.next()){
            return new CustomerDto(
                    rst.getString(1),
                    rst.getString(2),
                    rst.getString(3),
                    rst.getDouble(4)
            );
        }
        return null;
    }

    @Override
    public boolean save(Customer entity) throws SQLException, ClassNotFoundException {
        String sql = "INSERT INTO customer VALUES(?,?,?,?)";


//        PreparedStatement pstm = DBConnection.getInstance().getConnection().prepareStatement(sql);
//        pstm.setString(1,entity.getId());
//        pstm.setString(2,entity.getName());
//        pstm.setString(3,entity.getAddress());
//        pstm.setDouble(4,entity.getSalary());
//        return pstm.executeUpdate()>0;
        return CrudUtil.execute(sql,entity.getId(),entity.getName(),entity.getAddress(),entity.getSalary());
    }

    @Override
    public boolean update(Customer entity) throws SQLException, ClassNotFoundException {
        String sql = "UPDATE customer SET name =?, address=?, salary=? WHERE id=?";
//        PreparedStatement pstm = DBConnection.getInstance().getConnection().prepareStatement(sql);
//        pstm.setString(1,entity.getName());
//        pstm.setString(2,entity.getAddress());
//        pstm.setDouble(3,entity.getSalary());
//        pstm.setString(4,entity.getId());
//        return pstm.executeUpdate()>0;

        return CrudUtil.execute(sql,entity.getName(),entity.getAddress(),entity.getSalary(),entity.getId());
    }

    @Override
    public boolean delete(String value) throws SQLException, ClassNotFoundException {
        String sql = "DELETE FROM customer WHERE id=?";
//        PreparedStatement pstm = DBConnection.getInstance().getConnection().prepareStatement(sql);
//        pstm.setString(1,value);
//        return pstm.executeUpdate()>0;

        return CrudUtil.execute(sql,value);
    }

    @Override
    public List<Customer> getAll() throws SQLException, ClassNotFoundException {
        List<Customer> list = new ArrayList<>();
        String sql = "SELECT * FROM customer";
//        PreparedStatement pstm = DBConnection.getInstance().getConnection().prepareStatement(sql);
//        ResultSet rst = pstm.executeQuery();
        ResultSet rst = CrudUtil.execute(sql);
        while (rst.next()){

            list.add(new Customer(
                    rst.getString(1),
                    rst.getString(2),
                    rst.getString(3),
                    rst.getDouble(4)
            ));

        }
        return list;
    }
}
