package dao.custom.impl;

import db.DBConnection;
import dto.ItemDto;
import dao.custom.ItemDao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ItemDaoImpl implements ItemDao {
    @Override
    public boolean saveItem(ItemDto itemDto) throws SQLException, ClassNotFoundException {
        String sql = "INSERT INTO item VALUES(?,?,?,?)";
        PreparedStatement pstm = DBConnection.getInstance().getConnection().prepareStatement(sql);
        pstm.setString(1,itemDto.getCode());
        pstm.setString(2,itemDto.getDesc());
        pstm.setDouble(3,itemDto.getUnitPrice());
        pstm.setInt(4,itemDto.getQty());

        return pstm.executeUpdate()>0;
    }

    @Override
    public boolean updateItem(ItemDto itemDto) throws SQLException, ClassNotFoundException {
        String sql = "UPDATE item SET description=?, unitPrice=?, qtyOnHand=? WHERE code=?";
        PreparedStatement pstm = DBConnection.getInstance().getConnection().prepareStatement(sql);
        pstm.setString(1,itemDto.getDesc());
        pstm.setDouble(2, itemDto.getUnitPrice());
        pstm.setInt(3,itemDto.getQty());
        pstm.setString(4,itemDto.getCode());

        return pstm.executeUpdate()>0;
    }

    @Override
    public boolean deleteItem(String code) throws SQLException, ClassNotFoundException {
        String sql = "DELETE FROM item WHERE code=?";
        PreparedStatement pstm = DBConnection.getInstance().getConnection().prepareStatement(sql);
        pstm.setString(1,code);

        return pstm.executeUpdate()>0;
    }

    @Override
    public ItemDto getItem(String code) throws SQLException, ClassNotFoundException {

        String sql = "SELECT * FROM item WHERE code=?";
        PreparedStatement pstm = DBConnection.getInstance().getConnection().prepareStatement(sql);
        pstm.setString(1,code);
        ResultSet rst = pstm.executeQuery();
           if (rst.next()){
               return new ItemDto(
                       rst.getString(1),
                       rst.getString(2),
                       rst.getDouble(3),
                       rst.getInt(4)
               );
           }
        return null;
    }

    @Override
    public List<ItemDto> allItems() throws SQLException, ClassNotFoundException {

        List<ItemDto> list = new ArrayList<>();
        String sql = "SELECT * FROM item";
        PreparedStatement pstm = DBConnection.getInstance().getConnection().prepareStatement(sql);
        ResultSet rst = pstm.executeQuery();
        while (rst.next()){

            list.add(new ItemDto(
                    rst.getString(1),
                    rst.getString(2),
                    rst.getDouble(3),
                    rst.getInt(4)
            ));

        }
        return list;
    }
}
