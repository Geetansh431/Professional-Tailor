package org.example.project.measurementExplorer;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import org.example.project.enrollment.MySqlConnectionKlass;

import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ResourceBundle;

public class MeasurementExplorerController {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private TableView<Order> ordersTable;

    @FXML
    private TableColumn<Order, Integer> OrderColumn;

    @FXML
    private TableColumn<Order, String> MobileColumn;

    @FXML
    private TableColumn<Order, String> DressColumn;

    @FXML
    private TableColumn<Order, String> PicColumn;

    @FXML
    private TableColumn<Order, String> DOBColumn;

    @FXML
    private TableColumn<Order, Integer> QuantityColumn;

    @FXML
    private TableColumn<Order, Integer> BillColumn;

    @FXML
    private TableColumn<Order, String> WorkersColumn;

    @FXML
    private ComboBox<String> OrderStatusID;

    @FXML
    private ComboBox<String> WorkersID;

    private Connection con;

    @FXML
    void initialize() {
        con = MySqlConnectionKlass.doConnect();
        if (con == null) {
            System.out.println("Connection Did Not Establish");
        } else {
            System.out.println("Connection Done");
        }

        setupTableColumns();
        populateOrderStatusComboBox();
        populateWorkersComboBox();
    }

    private void setupTableColumns() {
        OrderColumn.setCellValueFactory(new PropertyValueFactory<>("orderId"));
        MobileColumn.setCellValueFactory(new PropertyValueFactory<>("mobileNumber"));
        DressColumn.setCellValueFactory(new PropertyValueFactory<>("dress"));
        PicColumn.setCellValueFactory(new PropertyValueFactory<>("pic"));
        DOBColumn.setCellValueFactory(new PropertyValueFactory<>("dodelDate"));
        QuantityColumn.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        BillColumn.setCellValueFactory(new PropertyValueFactory<>("bill"));
        WorkersColumn.setCellValueFactory(new PropertyValueFactory<>("worker"));
    }

    private void populateOrderStatusComboBox() {
        ObservableList<String> statusItems = FXCollections.observableArrayList("1", "2", "3");
        OrderStatusID.setItems(statusItems);
    }

    private void populateWorkersComboBox() {
        ObservableList<String> workerItems = FXCollections.observableArrayList();
        String query = "SELECT wname FROM workers";

        try {
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery(query);

            while (rs.next()) {
                String workerName = rs.getString("wname");
                workerItems.add(workerName);
            }

            WorkersID.setItems(workerItems);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    void FetchOnAction(ActionEvent event) {
        fetchAllOrders();
    }

    private void fetchAllOrders() {
        ObservableList<Order> orderList = FXCollections.observableArrayList();

        String query = "SELECT * FROM orders";
        try {
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery(query);

            while (rs.next()) {
                int orderId = rs.getInt("order_id");
                String mobileNumber = rs.getString("mobile_number");
                String dress = rs.getString("dress");
                String pic = rs.getString("pic");
                String dodelDate = rs.getString("dodel_date");
                int quantity = rs.getInt("qty");
                int bill = rs.getInt("bill");
                String worker = rs.getString("worker");

                Order order = new Order(orderId, mobileNumber, dress, pic, dodelDate, quantity, bill, worker);
                orderList.add(order);
            }

            ordersTable.setItems(orderList);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    void MobileNumberAction(ActionEvent event) {
        // Handle Mobile Number Action
    }

    @FXML
    void OrderStatusOnAction(ActionEvent event) {
        String selectedStatus = OrderStatusID.getValue();
        fetchOrdersByStatus(selectedStatus);
    }

    private void fetchOrdersByStatus(String status) {
        ObservableList<Order> orderList = FXCollections.observableArrayList();

        String query = "SELECT * FROM orders WHERE status = '" + status + "'";
        try {
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery(query);

            while (rs.next()) {
                int orderId = rs.getInt("order_id");
                String mobileNumber = rs.getString("mobile_number");
                String dress = rs.getString("dress");
                String pic = rs.getString("pic");
                String dodelDate = rs.getString("dodel_date");
                int quantity = rs.getInt("qty");
                int bill = rs.getInt("bill");
                String worker = rs.getString("worker");

                Order order = new Order(orderId, mobileNumber, dress, pic, dodelDate, quantity, bill, worker);
                orderList.add(order);
            }

            ordersTable.setItems(orderList);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    void WorkersOnAction(ActionEvent event) {
        // Handle Workers Action
    }

    public void ExportOnAction(ActionEvent actionEvent) {

    }
}
