package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;

public class DashBoardFormController {
    public AnchorPane dashBoardPane;

    public void customerButtonOnAction(ActionEvent actionEvent) {
        Stage stage = (Stage)dashBoardPane.getScene().getWindow();
        try {
            stage.setScene(new Scene(FXMLLoader.load(Objects.requireNonNull(getClass().getResource("../view/CustomerForm.fxml")))));
            stage.setTitle("Customer Form");
            stage.centerOnScreen();
            stage.setResizable(false);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        stage.show();
    }

    public void itemButtonOnAction(ActionEvent actionEvent) {
        Stage stage = (Stage)dashBoardPane.getScene().getWindow();
        try {
            stage.setScene(new Scene(FXMLLoader.load(Objects.requireNonNull(getClass().getResource("../view/ItemForm.fxml")))));
            stage.setTitle("Customer Form");
            stage.centerOnScreen();
            stage.setResizable(false);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        stage.show();
    }

    public void placeOrderButtonOnAction(ActionEvent actionEvent) {
        Stage stage = (Stage)dashBoardPane.getScene().getWindow();
        try {
            stage.setScene(new Scene(FXMLLoader.load(Objects.requireNonNull(getClass().getResource("../view/PlaceOrderForm.fxml")))));
            stage.setTitle("Customer Form");
            stage.centerOnScreen();
            stage.setResizable(false);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        stage.show();
    }

}
