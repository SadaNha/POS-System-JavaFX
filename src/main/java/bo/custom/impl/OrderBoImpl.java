package bo.custom.impl;

import bo.custom.OrderBo;
import dao.DaoFactory;
import dao.custom.OrderDao;
import dao.custom.impl.OrderDaoImpl;
import dao.util.DaoType;
import dto.OrderDto;
import entity.Orders;

import java.sql.SQLException;

public class OrderBoImpl implements OrderBo {

    private OrderDao orderDao = DaoFactory.getInstance().getDao(DaoType.ORDER);
    @Override
    public boolean saveOrder(OrderDto orderDto) throws SQLException, ClassNotFoundException {
        return orderDao.saveOrder(orderDto);
    }

    @Override
    public String generateId() throws SQLException, ClassNotFoundException {
        try {
            OrderDto dto = orderDao.lastOrder();
            if(dto!=null){
                String lastOrderId = dto.getOrderId();
                int number = Integer.parseInt(lastOrderId.split("[D]")[1]);
                return String.format("D%03d",++number);
            }
            else{
                return "D001";
            }
        } catch (SQLException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

}
