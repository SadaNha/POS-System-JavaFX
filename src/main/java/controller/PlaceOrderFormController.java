package controller;

import bo.BoFactory;
import bo.custom.CustomerBo;
import bo.custom.ItemBo;
import bo.custom.OrderBo;
import bo.custom.impl.CustomerBoImpl;
import bo.custom.impl.ItemBoImpl;
import bo.custom.impl.OrderBoImpl;
import com.jfoenix.controls.*;
import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;
import dao.util.BoType;
import dto.CustomerDto;
import dto.OrderDetailDto;
import dto.OrderDto;
import dto.ItemDto;
import dto.tm.OrderTm;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeTableColumn;
import javafx.scene.control.cell.TreeItemPropertyValueFactory;
import javafx.stage.Stage;
import dao.custom.CustomerDao;
import dao.custom.ItemDao;
import dao.custom.OrderDao;
import dao.custom.impl.CustomerDaoImpl;
import dao.custom.impl.ItemDaoImpl;
import dao.custom.impl.OrderDaoImpl;

import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class PlaceOrderFormController {
    public JFXComboBox cmbCustId;
    public JFXComboBox cmbItemCode;
    public JFXTextField txtCustName;
    public JFXTextField txtDescription;
    public JFXTextField txtUnitPrice;
    public JFXTextField txtQtyOnHand;
    public JFXTreeTableView<OrderTm> tblOrder;
    public TreeTableColumn colCode;
    public TreeTableColumn colDesc;
    public TreeTableColumn colQty;
    public TreeTableColumn colAmount;
    public TreeTableColumn colOption;
    public Label lblOrderId;
    public Label lblTotal;

    //private OrderDao orderDao = new OrderDaoImpl();

    private double total=0;
    private List<CustomerDto> customers;
    private List<ItemDto> items;

    private ObservableList<OrderTm> tmList = FXCollections.observableArrayList();

    private CustomerBo customerBo = BoFactory.getInstance().getBo(BoType.CUSTOMER);
    private ItemBo itemBo= BoFactory.getInstance().getBo(BoType.ITEM);

    private OrderBo orderBo = BoFactory.getInstance().getBo(BoType.ORDER);

    public void initialize(){

        colCode.setCellValueFactory(new TreeItemPropertyValueFactory<>("code"));
        colDesc.setCellValueFactory(new TreeItemPropertyValueFactory<>("desc"));
        colQty.setCellValueFactory(new TreeItemPropertyValueFactory<>("qty"));
        colAmount.setCellValueFactory(new TreeItemPropertyValueFactory<>("amount"));
        colOption.setCellValueFactory(new TreeItemPropertyValueFactory<>("btn"));

        generateId();

        loadCustomers();
        loadItems();
        cmbCustId.getSelectionModel().selectedItemProperty().addListener(((observableValue, oldValue, id) -> {
            for (CustomerDto customer:customers) {
                if(customer.getId().equals(id)){
                    txtCustName.setText(customer.getName());
                }
            }
        }));

        cmbItemCode.getSelectionModel().selectedItemProperty().addListener(((observableValue, oldValue, code) -> {
            for (ItemDto itemDto:items) {
                if(itemDto.getCode().equals(code)){
                    txtDescription.setText(itemDto.getDesc());
                    txtUnitPrice.setText(String.valueOf(itemDto.getUnitPrice()));
                    txtQtyOnHand.setText(String.valueOf(itemDto.getQty()));
                }
            }
        }));
    }

    private void loadItems() {
        try {
            items = itemBo.allItems();
            ObservableList<String> list = FXCollections.observableArrayList();
            for (ItemDto dto: items) {
                list.add(dto.getCode());
            }
            cmbItemCode.setItems(list);
        } catch (SQLException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    private void loadCustomers() {
        try {
            customers = customerBo.allCustomer();
            ObservableList<String> list = FXCollections.observableArrayList();
            for (CustomerDto dto : customers) {
                list.add(dto.getId());
            }
            cmbCustId.setItems(list);
        } catch (SQLException | ClassNotFoundException e) {

        }
    }

    public void generateId(){
        try {
            lblOrderId.setText(orderBo.generateId());
        } catch (SQLException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public void addToCartOnAction(ActionEvent actionEvent) throws SQLException, ClassNotFoundException {

        double amount = itemBo.getItem(cmbItemCode.getValue().toString()).getUnitPrice()*Integer.parseInt(txtQtyOnHand.getText());
        JFXButton btn = new JFXButton("Delete");
        OrderTm orderTm = new OrderTm(
                cmbItemCode.getValue().toString(),
                txtDescription.getText(),
                Double.parseDouble(txtUnitPrice.getText()),
                Integer.parseInt(txtQtyOnHand.getText()),
                amount,
                btn
        );
        boolean isExist = false;

        btn.setOnAction(actionEvent1 -> {
            total-=orderTm.getAmount();
            tmList.remove(orderTm);
            tblOrder.refresh();
            lblTotal.setText(String.format("%.2f",total));
        });
        for (OrderTm order: tmList) {
            if (order.getCode().equals(orderTm.getCode())){
                order.setQty(orderTm.getQty()+order.getQty());
                order.setAmount(orderTm.getAmount()+order.getAmount());
                isExist=true;
                total+=orderTm.getAmount();
            }
        }
        if (!isExist){
            tmList.add(orderTm);
            total+=orderTm.getAmount();
        }
        TreeItem<OrderTm> treeObject = new RecursiveTreeItem<OrderTm>(tmList, RecursiveTreeObject:: getChildren);
        tblOrder.setRoot(treeObject);
        tblOrder.setShowRoot(false);
        lblTotal.setText(String.format("%.2f",total));
    }

    public void placeOrderOnAction(ActionEvent actionEvent) throws SQLException, ClassNotFoundException {

        List<OrderDetailDto> list = new ArrayList<>();
        for (OrderTm tm:tmList) {
            list.add(new OrderDetailDto(
                    lblOrderId.getText(),
                    tm.getCode(),
                    tm.getQty(),
                    tm.getAmount()/tm.getQty()
            ));
        }
        if(! tmList.isEmpty()){
            boolean isSaved = orderBo.saveOrder(new OrderDto(
                            lblOrderId.getText(),
                            LocalDateTime.now().format(DateTimeFormatter.ofPattern("YYYY-MM-dd")),
                            cmbCustId.getValue().toString(),
                            list
                    )
            );
            if(isSaved){
                new Alert(Alert.AlertType.INFORMATION,"Order Saved").show();
            }
            else{
                new Alert(Alert.AlertType.ERROR,"Something Wrong").show();
            }
        }
    }

    public void backButtonOnAction(ActionEvent actionEvent) {
        Stage stage = (Stage)tblOrder.getScene().getWindow();
        try {
            stage.setScene(new Scene(FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/view/DashboardForm.fxml")))));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        stage.show();
    }
}
