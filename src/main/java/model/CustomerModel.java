package model;

import dto.CustomerDto;

import java.sql.SQLException;
import java.util.List;

public interface CustomerModel {

    boolean saveCustomer(CustomerDto customerDto) throws SQLException, ClassNotFoundException;
    boolean updateCustomer(CustomerDto customerDto) throws SQLException, ClassNotFoundException;
    boolean deleteCustomer(String id) throws SQLException, ClassNotFoundException;
    List<CustomerDto> allCustomer() throws SQLException, ClassNotFoundException;
    CustomerDto searchCustomer(String id) throws SQLException, ClassNotFoundException;
}

