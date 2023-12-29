package dao.custom.impl;

import dao.util.CrudUtil;
import db.DBConnection;
import dto.ItemDto;
import dao.custom.ItemDao;
import entity.Item;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ItemDaoImpl implements ItemDao {
//    @Override
//    public boolean saveItem(ItemDto itemDto) throws SQLException, ClassNotFoundException {
//        String sql = "INSERT INTO item VALUES(?,?,?,?)";
//        PreparedStatement pstm = DBConnection.getInstance().getConnection().prepareStatement(sql);
//        pstm.setString(1,itemDto.getCode());
//        pstm.setString(2,itemDto.getDesc());
//        pstm.setDouble(3,itemDto.getUnitPrice());
//        pstm.setInt(4,itemDto.getQty());
//
//        return pstm.executeUpdate()>0;
//    }

//    @Override
//    public boolean updateItem(ItemDto itemDto) throws SQLException, ClassNotFoundException {
//        String sql = "UPDATE item SET description=?, unitPrice=?, qtyOnHand=? WHERE code=?";
//        PreparedStatement pstm = DBConnection.getInstance().getConnection().prepareStatement(sql);
//        pstm.setString(1,itemDto.getDesc());
//        pstm.setDouble(2, itemDto.getUnitPrice());
//        pstm.setInt(3,itemDto.getQty());
//        pstm.setString(4,itemDto.getCode());
//
//        return pstm.executeUpdate()>0;
//    }

//    @Override
//    public boolean deleteItem(String code) throws SQLException, ClassNotFoundException {
//        String sql = "DELETE FROM item WHERE code=?";
//        PreparedStatement pstm = DBConnection.getInstance().getConnection().prepareStatement(sql);
//        pstm.setString(1,code);
//
//        return pstm.executeUpdate()>0;
//    }

    @Override
    public ItemDto getItem(String code) throws SQLException, ClassNotFoundException {

        String sql = "SELECT * FROM item WHERE code=?";
//        PreparedStatement pstm = DBConnection.getInstance().getConnection().prepareStatement(sql);
//        pstm.setString(1,code);
//        ResultSet rst = pstm.executeQuery();

        ResultSet rst = CrudUtil.execute(sql,code);
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

//    @Override
//    public List<ItemDto> allItems() throws SQLException, ClassNotFoundException {
//
//        List<ItemDto> list = new ArrayList<>();
//        String sql = "SELECT * FROM item";
//        PreparedStatement pstm = DBConnection.getInstance().getConnection().prepareStatement(sql);
//        ResultSet rst = pstm.executeQuery();
//        while (rst.next()){
//
//            list.add(new ItemDto(
//                    rst.getString(1),
//                    rst.getString(2),
//                    rst.getDouble(3),
//                    rst.getInt(4)
//            ));
//
//        }
//        return list;
//    }

    @Override
    public boolean save(Item entity) throws SQLException, ClassNotFoundException {
        String sql = "INSERT INTO item VALUES(?,?,?,?)";
//        PreparedStatement pstm = DBConnection.getInstance().getConnection().prepareStatement(sql);
//        pstm.setString(1,entity.getCode());
//        pstm.setString(2,entity.getDescription());
//        pstm.setDouble(3,entity.getUnitPrice());
//        pstm.setInt(4,entity.getQtyOnHand());
//
//        return pstm.executeUpdate()>0;

        return CrudUtil.execute(sql,entity.getCode(),entity.getDescription(),entity.getUnitPrice(),entity.getQtyOnHand());
    }

    @Override
    public boolean update(Item entity) throws SQLException, ClassNotFoundException {
        String sql = "UPDATE item SET description=?, unitPrice=?, qtyOnHand=? WHERE code=?";
//        PreparedStatement pstm = DBConnection.getInstance().getConnection().prepareStatement(sql);
//        pstm.setString(1,entity.getDescription());
//        pstm.setDouble(2, entity.getUnitPrice());
//        pstm.setInt(3,entity.getQtyOnHand());
//        pstm.setString(4,entity.getCode());
//
//        return pstm.executeUpdate()>0;

        return CrudUtil.execute(sql,entity.getDescription(),entity.getUnitPrice(),entity.getQtyOnHand(),entity.getCode());
    }

    @Override
    public boolean delete(String value) throws SQLException, ClassNotFoundException {
        String sql = "DELETE FROM item WHERE code=?";
//        PreparedStatement pstm = DBConnection.getInstance().getConnection().prepareStatement(sql);
//        pstm.setString(1,value);
//
//        return pstm.executeUpdate()>0;

        return CrudUtil.execute(sql,value);
    }

    @Override
    public List<Item> getAll() throws SQLException, ClassNotFoundException {

        List<Item> itemEntityList = new ArrayList<>();
        String sql = "SELECT * FROM item";
//        PreparedStatement pstm = DBConnection.getInstance().getConnection().prepareStatement(sql);
//        ResultSet rst = pstm.executeQuery();

        ResultSet rst = CrudUtil.execute(sql);
        while (rst.next()){

            itemEntityList.add(new Item(
                    rst.getString(1),
                    rst.getString(2),
                    rst.getDouble(3),
                    rst.getInt(4)
            ));

        }

        return itemEntityList;
    }
}
