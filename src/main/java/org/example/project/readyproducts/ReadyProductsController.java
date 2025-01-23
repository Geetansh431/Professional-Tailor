package org.example.project.readyproducts;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import org.example.project.enrollment.MySqlConnectionKlass;

public class ReadyProductsController {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private ListView<String> DateOfDelivery;

    @FXML
    private ListView<String> DressID;

    @FXML
    private ListView<String> OrderID;

    @FXML
    private Button Recieved;

    @FXML
    private ComboBox<String> SelectWorker;

    private Connection con;

    @FXML
    void initialize() {
        con = MySqlConnectionKlass.doConnect();
        if (con == null) {
            System.out.println("Connection Did Not Establish");
        } else {
            System.out.println("Connection Done");
            // Add event listener to populate ComboBox when clicked
            SelectWorker.setOnMouseClicked(event -> populateWorkersWithStatusOne()); // To Fill the combo box with workers
            // Add event listener to ComboBox selection change
            SelectWorker.setOnAction(event -> populateOrderDetailsForSelectedWorker());

            // Add double-click event listener for ListView items
            OrderID.setOnMouseClicked(event -> handleOrderIDDoubleClick(event));
            Recieved.setOnAction(event -> handleReceiveAll());
        }
    }

    private void populateWorkersWithStatusOne() {
        SelectWorker.getItems().clear(); // Clear previous items
        String query = "SELECT DISTINCT worker FROM orders WHERE status = '1'";
        try (PreparedStatement pst = con.prepareStatement(query);
             ResultSet rs = pst.executeQuery()) {
            while (rs.next()) {
                SelectWorker.getItems().add(rs.getString("worker"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void populateOrderDetailsForSelectedWorker() {
        String selectedWorker = SelectWorker.getSelectionModel().getSelectedItem();
        if (selectedWorker != null) {
            OrderID.getItems().clear();
            DressID.getItems().clear();
            DateOfDelivery.getItems().clear();

            String query = "SELECT order_id, dress, dodel_date FROM orders WHERE worker = ? AND status = '1'";
            try (PreparedStatement pst = con.prepareStatement(query)) {
                pst.setString(1, selectedWorker);
                try (ResultSet rs = pst.executeQuery()) {
                    while (rs.next()) {
                        OrderID.getItems().add(String.valueOf(rs.getInt("order_id")));
                        DressID.getItems().add(rs.getString("dress"));
                        DateOfDelivery.getItems().add(rs.getDate("dodel_date").toString());
                    }
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    private void handleOrderIDDoubleClick(MouseEvent event) {
        if (event.getButton() == MouseButton.PRIMARY && event.getClickCount() == 2) {
            String selectedOrderID = OrderID.getSelectionModel().getSelectedItem();
            if (selectedOrderID != null) {
                updateOrderStatusTo2(Integer.parseInt(selectedOrderID));
                removeOrderFromListView(selectedOrderID);
            }
        }
    }

    private void handleReceiveAll() {
        String selectedWorker = SelectWorker.getSelectionModel().getSelectedItem();
        if (selectedWorker != null) {
            updateAllOrdersStatusTo2ForWorker(selectedWorker);
            clearListView();
        }
    }

    private void updateOrderStatusTo2(int orderId) {
        String query = "UPDATE orders SET status = '2' WHERE order_id = ?";
        try (PreparedStatement pst = con.prepareStatement(query)) {
            pst.setInt(1, orderId);
            pst.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void updateAllOrdersStatusTo2ForWorker(String worker) {
        String query = "UPDATE orders SET status = '2' WHERE worker = ? AND status = '1'";
        try (PreparedStatement pst = con.prepareStatement(query)) {
            pst.setString(1, worker);
            pst.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void removeOrderFromListView(String orderId) {
        int index = OrderID.getItems().indexOf(orderId);
        if (index >= 0) {
            OrderID.getItems().remove(index);
            DressID.getItems().remove(index);
            DateOfDelivery.getItems().remove(index);
        }
    }

    private void clearListView() {
        OrderID.getItems().clear();
        DressID.getItems().clear();
        DateOfDelivery.getItems().clear();
    }
}
