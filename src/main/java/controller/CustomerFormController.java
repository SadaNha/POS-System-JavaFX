package controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import com.jfoenix.controls.JFXTreeTableView;
import com.jfoenix.controls.RecursiveTreeItem;
import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;
import db.DBConnection;
import dto.CustomerDto;
import dto.tm.CustomerTm;
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
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import model.CustomerModel;
import model.impl.CustomerModelImpl;

import java.io.IOException;
import java.sql.*;
import java.util.Objects;
import java.util.function.Predicate;

public class CustomerFormController {

    public JFXTextField txtCustAddress;
    public JFXTextField txtId;
    public JFXTextField txtCustName;
    public JFXTextField txtCustSalary;
    public JFXTextField txtCustSearch;
    public TreeTableColumn colCustId;
    public TreeTableColumn colCustName;
    public TreeTableColumn colSalary;
    public TreeTableColumn colOption;
    public JFXTreeTableView<CustomerTm> tblCustomer;
    public TreeTableColumn colCustAddress;
    public CustomerModel customerModel = new CustomerModelImpl();


    public void initialize(){

        colCustId.setCellValueFactory(new TreeItemPropertyValueFactory<>("id"));
        colCustName.setCellValueFactory(new TreeItemPropertyValueFactory<>("name"));
        colCustAddress.setCellValueFactory(new TreeItemPropertyValueFactory<>("address"));
        colSalary.setCellValueFactory(new TreeItemPropertyValueFactory<>("salary"));
        colOption.setCellValueFactory(new TreeItemPropertyValueFactory<>("deleteBtn"));
        loadCustomerTable();



        txtCustSearch.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observableValue, String s, String newValue) {
                tblCustomer.setPredicate(new Predicate<TreeItem<CustomerTm>>() {
                    @Override
                    public boolean test(TreeItem<CustomerTm> treeItem) {
                        return treeItem.getValue().getId().contains(newValue) ||
                                treeItem.getValue().getName().contains(newValue);
                    }
                });
            }
        });
        tblCustomer.getSelectionModel().selectedItemProperty().addListener((ObservableValue, oldValue, newValue)->{

                setData(newValue.getValue());

        });
    }

    private void setData(CustomerTm newValue) {
        if(newValue !=null){
            txtId.setEditable(false);
            txtId.setText(newValue.getId());
            txtCustName.setText(newValue.getName());
            txtCustAddress.setText(newValue.getAddress());
            txtCustSalary.setText(String.valueOf(newValue.getSalary()));
        }

    }
    private void loadCustomerTable() {

        ObservableList<CustomerTm> customerList = FXCollections.observableArrayList();
        String sql = "SELECT * FROM customer";
        try {
            Statement stm = DBConnection.getInstance().getConnection().createStatement();
            ResultSet result = stm.executeQuery(sql);
            while (result.next()){
                JFXButton btn = new JFXButton("DELETE");
                CustomerTm c = new CustomerTm(
                        result.getString(1),
                        result.getString(2),
                        result.getString(3),
                        result.getDouble(4),
                        btn
                );
                btn.setOnAction(actionEvent -> {
                    deleteItem(c.getId());
                });
                customerList.add(c);
            }
            TreeItem<CustomerTm> treeItem = new RecursiveTreeItem<CustomerTm>(customerList, RecursiveTreeObject:: getChildren);
            tblCustomer.setRoot(treeItem);
            tblCustomer.setShowRoot(false);
        } catch (ClassNotFoundException | SQLException e) {
            throw new RuntimeException(e);
        }
        catch (NullPointerException e){
            new Alert(Alert.AlertType.INFORMATION,"Null").show();
        }
    }

    private void deleteItem(String id) {
        try {
            Boolean isDeleted = customerModel.deleteCustomer(id);
            if (isDeleted){
                new Alert(Alert.AlertType.INFORMATION,"Customer deleted successfully").show();
                loadCustomerTable();
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
        tblCustomer.refresh();
        txtId.clear();
        txtCustName.clear();
        txtCustAddress.clear();
        txtCustSalary.clear();
        txtCustSearch.clear();
    }

    public void saveButtonOnAction(ActionEvent actionEvent) {
        try {
            Boolean isSaved = customerModel.saveCustomer( new CustomerDto(
                    txtId.getText()
                    ,txtCustName.getText()
                    , txtCustAddress.getText()
                    ,Double.parseDouble(txtCustSalary.getText())
                    )
            );
            if (isSaved){
                new Alert(Alert.AlertType.INFORMATION,"Customer saved successfully").show();
                loadCustomerTable();
                clearFields();
            }
        }catch(SQLIntegrityConstraintViolationException ex){
            new Alert(Alert.AlertType.ERROR,"Customer already added").show();
        }catch (NumberFormatException | ClassNotFoundException | SQLException e) {
            new Alert(Alert.AlertType.ERROR,"Error").show();
            //throw new RuntimeException(e);
        }
    }

    public void updateButtonOnAction(ActionEvent actionEvent) {

        try {

            Boolean isUpdated = customerModel.updateCustomer( new CustomerDto(
                            txtId.getText()
                            ,txtCustName.getText()
                            , txtCustAddress.getText()
                            ,Double.parseDouble(txtCustSalary.getText())
                    )
            );
            if (isUpdated){
                new Alert(Alert.AlertType.INFORMATION,"Customer updated successfully").show();
                loadCustomerTable();
                clearFields();
            }
        } catch (SQLException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }

    }

    public void relodButtonOnAction(ActionEvent actionEvent) {
        loadCustomerTable();
        tblCustomer.refresh();
    }

    public void backButtonOnAction(ActionEvent actionEvent) {
        Stage stage = (Stage)tblCustomer.getScene().getWindow();
        try {
            stage.setScene(new Scene(FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/view/DashboardForm.fxml")))));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        stage.show();
    }
}
