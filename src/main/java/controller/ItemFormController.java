package controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import com.jfoenix.controls.JFXTreeTableView;
import com.jfoenix.controls.RecursiveTreeItem;
import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;
import db.DBConnection;
import dto.ItemDto;
import dto.tm.CustomerTm;
import dto.tm.ItemTm;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeTableColumn;
import javafx.scene.control.cell.TreeItemPropertyValueFactory;
import javafx.stage.Stage;
import model.ItemModel;
import model.impl.ItemModelImpl;

import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Objects;
import java.util.function.Predicate;

public class ItemFormController {
    public JFXTextField txtUnitPrice;
    public JFXTextField txtCode;
    public JFXTextField txtDesc;
    public JFXTextField txtQtyOnHand;
    public JFXTreeTableView<ItemTm> tblItem;

    public ItemModel itemModel = new ItemModelImpl();
    public TreeTableColumn colItemCode;
    public JFXTextField txtItemSearch;
    public TreeTableColumn colItemUnitPrice;
    public TreeTableColumn colQtyOnHand;
    public TreeTableColumn colOption;
    public TreeTableColumn colItemDescription;

    public void initialize(){

        colItemCode.setCellValueFactory(new TreeItemPropertyValueFactory<>("code"));
        colItemDescription.setCellValueFactory(new TreeItemPropertyValueFactory<>("desc"));
        colItemUnitPrice.setCellValueFactory(new TreeItemPropertyValueFactory<>("unitPrice"));
        colQtyOnHand.setCellValueFactory(new TreeItemPropertyValueFactory<>("qty"));
        colOption.setCellValueFactory(new TreeItemPropertyValueFactory<>("btn"));
        loadItemTable();

        txtItemSearch.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observableValue, String s, String newValue) {
                tblItem.setPredicate(new Predicate<TreeItem<ItemTm>>() {
                    @Override
                    public boolean test(TreeItem<ItemTm> treeItem) {
                        return treeItem.getValue().getCode().contains(newValue) ||
                                treeItem.getValue().getDesc().contains(newValue);
                    }
                });
            }
        });

        tblItem.getSelectionModel().selectedItemProperty().addListener((ObservableValue, oldValue, newValue)->{
            setData(newValue.getValue());
        });
    }

    private void setData(ItemTm value) {

        if(value !=null){
            txtCode.setEditable(false);
            txtCode.setText(value.getCode());
            txtDesc.setText(value.getDesc());
            txtUnitPrice.setText(String.valueOf(value.getUnitPrice()));
            txtQtyOnHand.setText(String.valueOf(value.getQty()));
        }
    }

    private void loadItemTable() {

        ObservableList<ItemTm> itemList = FXCollections.observableArrayList();
        String sql = "SELECT * FROM item";
        try {
            Statement stm = DBConnection.getInstance().getConnection().createStatement();
            ResultSet result = stm.executeQuery(sql);
            while (result.next()){
                JFXButton btn = new JFXButton("DELETE");
                ItemTm c = new ItemTm(
                        result.getString(1),
                        result.getString(2),
                        result.getDouble(3),
                        result.getInt(4),
                        btn
                );
                btn.setOnAction(actionEvent -> {
                    deleteItem(c.getCode());
                });
                itemList.add(c);
            }
            TreeItem<ItemTm> treeItem = new RecursiveTreeItem<ItemTm>(itemList, RecursiveTreeObject :: getChildren);
            tblItem.setRoot(treeItem);
            tblItem.setShowRoot(false);
        } catch (ClassNotFoundException | SQLException e) {
            throw new RuntimeException(e);
        }
        catch (NullPointerException e){
            new Alert(Alert.AlertType.INFORMATION,"Null").show();
        }
    }

    private void deleteItem(String code) {

        try {
                boolean isDeleted = itemModel.deleteItem(code);
            if (isDeleted){
                new Alert(Alert.AlertType.INFORMATION,"Item deleted successfully").show();
                loadItemTable();
                clearFields();
            }
            else{
                new Alert(Alert.AlertType.ERROR,"Something went wrong").show();
            }
        } catch (ClassNotFoundException | SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private void clearFields() {
        tblItem.refresh();
        txtCode.clear();
        txtDesc.clear();
        txtUnitPrice.clear();
        txtQtyOnHand.clear();
    }

    public void saveButtonOnAction(ActionEvent actionEvent) {
        try {
                boolean isSaved = itemModel.saveItem(new ItemDto(
                    txtCode.getText()
                    ,txtDesc.getText()
                    ,Double.parseDouble(txtUnitPrice.getText())
                    ,Integer.parseInt(txtQtyOnHand.getText())
            ));

            if (isSaved){
                new Alert(Alert.AlertType.INFORMATION,"Item saved successfully").show();
                loadItemTable();
                clearFields();
            }
        } catch (SQLException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public void updateButtonOnAction(ActionEvent actionEvent) {
        try {
                boolean isUpdated = itemModel.updateItem(new ItemDto(
                    txtCode.getText()
                    ,txtDesc.getText()
                    ,Double.parseDouble(txtUnitPrice.getText())
                    ,Integer.parseInt(txtQtyOnHand.getText())
            ));

            if (isUpdated){
                new Alert(Alert.AlertType.INFORMATION,"Item updated successfully").show();
                loadItemTable();
                clearFields();
            }
        } catch (SQLException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public void backButtonOnAction(ActionEvent actionEvent) {
        Stage stage = (Stage)tblItem.getScene().getWindow();
        try {
            stage.setScene(new Scene(FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/view/DashboardForm.fxml")))));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        stage.show();
    }
}
