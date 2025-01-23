package org.example.project.orderDeliveryPanel;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.SelectionMode;
import org.example.project.enrollment.MySqlConnectionKlass;

public class orderDeliveryPanelController {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private TableColumn<Order, String> AddressColumn;

    @FXML
    private TableColumn<Order, Integer> BillColumn;

    @FXML
    private TextField MobileNumberFeild;

    @FXML
    private TableColumn<Order, Integer> OrderIDColumn;

    @FXML
    private TableColumn<Order, String> StatusColumn;

    @FXML
    private TableView<Order> TableID;

    @FXML
    private TextField TotalBill;

    @FXML
    void DeliveredAllOnAction(ActionEvent event) {
        ObservableList<Order> selectedOrders = TableID.getSelectionModel().getSelectedItems();
        if (selectedOrders.isEmpty()) {
            System.out.println("No orders selected");
            return;
        }

        String updateQuery = "UPDATE orders SET status = ? WHERE order_id = ?";
        try {
            PreparedStatement preparedStatement = con.prepareStatement(updateQuery);
            for (Order order : selectedOrders) {
                preparedStatement.setString(1, "3");
                preparedStatement.setInt(2, order.getOrderId());
                preparedStatement.addBatch();
            }
            preparedStatement.executeBatch();

            // Refresh the table to show updated status
            FindOrderOnAction(null); // Assuming mobile number is still in the field
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    void FindOrderOnAction(ActionEvent event) {
        String mobileNumber = MobileNumberFeild.getText();
        if (mobileNumber.isEmpty()) {
            System.out.println("Please enter a mobile number");
            return;
        }

        String query = "SELECT * FROM orders WHERE mobile_number = ?";
        try {
            PreparedStatement preparedStatement = con.prepareStatement(query);
            preparedStatement.setString(1, mobileNumber);
            ResultSet resultSet = preparedStatement.executeQuery();

            ObservableList<Order> orderList = FXCollections.observableArrayList();
            int totalBill = 0;

            while (resultSet.next()) {
                int orderId = resultSet.getInt("order_id");
                String address = resultSet.getString("dress");
                int bill = resultSet.getInt("bill");
                String status = resultSet.getString("status");

                totalBill += bill;

                orderList.add(new Order(orderId, address, bill, status));
            }

            TableID.setItems(orderList);
            TotalBill.setText(String.valueOf(totalBill));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    Connection con;

    @FXML
    void initialize() {
        con = MySqlConnectionKlass.doConnect();
        if (con == null) {
            System.out.println("Connection Did Not Establish");
        } else {
            System.out.println("Connection Done");
        }

        OrderIDColumn.setCellValueFactory(new PropertyValueFactory<>("orderId"));
        AddressColumn.setCellValueFactory(new PropertyValueFactory<>("address"));
        BillColumn.setCellValueFactory(new PropertyValueFactory<>("bill"));
        StatusColumn.setCellValueFactory(new PropertyValueFactory<>("status"));

        // Enable multiple selection in the table
        TableID.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
    }

    public static class Order {
        private int orderId;
        private String address;
        private int bill;
        private String status;

        public Order(int orderId, String address, int bill, String status) {
            this.orderId = orderId;
            this.address = address;
            this.bill = bill;
            this.status = status;
        }

        public int getOrderId() {
            return orderId;
        }

        public void setOrderId(int orderId) {
            this.orderId = orderId;
        }

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public int getBill() {
            return bill;
        }

        public void setBill(int bill) {
            this.bill = bill;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }
    }
}
