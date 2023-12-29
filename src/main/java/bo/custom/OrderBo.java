package bo.custom;

import bo.SuperBo;
import dao.custom.OrderDao;
import dao.custom.impl.OrderDaoImpl;
import dto.OrderDto;

import java.sql.SQLException;

public interface OrderBo extends SuperBo {



    boolean saveOrder(OrderDto orderDto) throws SQLException, ClassNotFoundException;

    String generateId() throws SQLException, ClassNotFoundException;
}
