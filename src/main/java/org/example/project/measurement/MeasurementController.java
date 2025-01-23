package org.example.project.measurement;

import java.io.File;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import org.example.project.enrollment.MySqlConnectionKlass;

public class MeasurementController {

    @FXML
    private ResourceBundle resources;

    @FXML
    private ComboBox<String> ORDERID;

    @FXML
    private URL location;

    @FXML
    private DatePicker DateID;

    @FXML
    private ComboBox<String> SelectStatus;

    @FXML
    private ComboBox<String> DressID;

    @FXML
    private TextArea MeasurementsID;

    @FXML
    private TextField MobileNumber;

    @FXML
    private TextField PriceID;

    @FXML
    private ComboBox<String> QuantityID;

    @FXML
    private TextField TotalBill;

    @FXML
    private ImageView UploadedImage;

    @FXML
    private ComboBox<String> WorkersID;

    private String picturePath;
    private Connection con;

    @FXML
    void DoClose(ActionEvent event) {
        clearForm();
        deleteOrder();
    }

    @FXML
    void DoSave(ActionEvent event) {
        String mobileNumber = MobileNumber.getText();
        String dress = DressID.getSelectionModel().getSelectedItem();
        String measurements = MeasurementsID.getText();
        String worker = WorkersID.getSelectionModel().getSelectedItem();
        String doOrderDate = String.valueOf(java.time.LocalDate.now());
        int quantity = Integer.parseInt(QuantityID.getSelectionModel().getSelectedItem());
        int pricePerUnit = Integer.parseInt(PriceID.getText());
        int bill = Integer.parseInt(TotalBill.getText());
        String dodelDate = DateID.getValue().toString();
        String selectedStatus = SelectStatus.getValue();
        String query = "INSERT INTO orders (mobile_number, dress, pic, dodel_date, qty, bill, measurements, worker, doorder_date,status) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?,?)";

        try (PreparedStatement pst = con.prepareStatement(query, PreparedStatement.RETURN_GENERATED_KEYS)) {
            pst.setString(1, mobileNumber);
            pst.setString(2, dress);
            pst.setString(3, picturePath);
            pst.setString(4, dodelDate);
            pst.setInt(5, quantity);
            pst.setInt(6, bill);
            pst.setString(7, measurements);
            pst.setString(8, worker);
            pst.setString(9, doOrderDate);
            pst.setString(10,selectedStatus);
            pst.executeUpdate();

            // Get the generated order ID
            ResultSet generatedKeys = pst.getGeneratedKeys();
            if (generatedKeys.next()) {
                int orderId = generatedKeys.getInt(1);
                // Add the new order ID to the ComboBox
                ORDERID.getItems().add(String.valueOf(orderId));
                ORDERID.getSelectionModel().select(String.valueOf(orderId)); // Select the newly added ID
                System.out.println("New order saved successfully with ID: " + orderId);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void DoUpdate(ActionEvent event) {
        // Handle the update event if needed
    }

    @FXML
    void DoUpload(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open Image File");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg")
        );
        File selectedFile = fileChooser.showOpenDialog(UploadedImage.getScene().getWindow());
        if (selectedFile != null) {
            Image image = new Image(selectedFile.toURI().toString());
            UploadedImage.setImage(image);
            picturePath = selectedFile.getAbsolutePath();
        }
    }

    @FXML
    void NewData(ActionEvent event) {
        clearForm();
    }

    @FXML
    void SelectID(ActionEvent event) {
        populateOrderDetails();
    }

    @FXML
    void SelectQuantity(ActionEvent event) {
        updateTotalBill();
    }

    private void clearForm() {
        MobileNumber.clear();
        DressID.getSelectionModel().clearSelection();
        MeasurementsID.clear();
        PriceID.clear();
        QuantityID.getSelectionModel().clearSelection();
        TotalBill.clear();
        UploadedImage.setImage(null);
        WorkersID.getSelectionModel().clearSelection();
        DateID.setValue(null);
        picturePath = null;
        SelectStatus.getSelectionModel().clearSelection();
    }

    private void deleteOrder() {
        String selectedOrderID = ORDERID.getSelectionModel().getSelectedItem();
        if (selectedOrderID != null) {
            String query = "DELETE FROM orders WHERE order_id = ?";
            try (PreparedStatement pst = con.prepareStatement(query)) {
                pst.setInt(1, Integer.parseInt(selectedOrderID));
                int affectedRows = pst.executeUpdate();
                if (affectedRows > 0) {
                    System.out.println("Order deleted successfully: " + selectedOrderID);
                    ORDERID.getItems().remove(selectedOrderID); // Remove from ComboBox
                    clearForm();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    private void populateWorkers() {
        WorkersID.getItems().clear();
        String selectedDress = DressID.getSelectionModel().getSelectedItem();
        if (selectedDress != null) {
            String query = "SELECT wname FROM workers WHERE splz LIKE ?";
            try (PreparedStatement pst = con.prepareStatement(query)) {
                pst.setString(1, "%" + selectedDress + "%");
                try (ResultSet rs = pst.executeQuery()) {
                    while (rs.next()) {
                        WorkersID.getItems().add(rs.getString("wname"));
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void updateTotalBill() {
        try {
            int price = Integer.parseInt(PriceID.getText());
            String quantityString = QuantityID.getSelectionModel().getSelectedItem();
            int quantity = quantityString != null ? Integer.parseInt(quantityString) : 0;

            int totalBill = price * quantity;
            TotalBill.setText(String.valueOf(totalBill));
        } catch (NumberFormatException e) {
            TotalBill.setText("Invalid input");
        }
    }

    private void populateOrderIDs() {
        ORDERID.getItems().clear();
        String query = "SELECT order_id FROM orders";
        try (PreparedStatement pst = con.prepareStatement(query);
             ResultSet rs = pst.executeQuery()) {
            while (rs.next()) {
                ORDERID.getItems().add(String.valueOf(rs.getInt("order_id")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void populateOrderDetails() {
        String selectedOrderID = ORDERID.getSelectionModel().getSelectedItem();
        if (selectedOrderID != null) {
            String query = "SELECT * FROM orders WHERE order_id = ?";
            try (PreparedStatement pst = con.prepareStatement(query)) {
                pst.setInt(1, Integer.parseInt(selectedOrderID));
                try (ResultSet rs = pst.executeQuery()) {
                    if (rs.next()) {
                        MobileNumber.setText(rs.getString("mobile_number"));
                        DressID.getSelectionModel().select(rs.getString("dress"));
                        picturePath = rs.getString("pic");
                        DateID.setValue(rs.getDate("dodel_date").toLocalDate());
                        int quantity = rs.getInt("qty");
                        QuantityID.getSelectionModel().select(String.valueOf(quantity));
                        int bill = rs.getInt("bill");
                        TotalBill.setText(String.valueOf(bill));
                        MeasurementsID.setText(rs.getString("measurements"));
                        WorkersID.getSelectionModel().select(rs.getString("worker"));
                        int pricePerUnit = (quantity != 0) ? bill / quantity : 0;
                        PriceID.setText(String.valueOf(pricePerUnit));

                        // Load the image if the path is available
                        if (picturePath != null && !picturePath.isEmpty()) {
                            Image image = new Image(new File(picturePath).toURI().toString());
                            UploadedImage.setImage(image);
                        }
                    }
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    @FXML
    void initialize() {
        con = MySqlConnectionKlass.doConnect();
        if (con == null) {
            System.out.println("Connection Did Not Establish");
        } else {
            System.out.println("Connection Done");
        }

        DressID.getItems().addAll("pent", "shirt", "coat");

        for (int i = 1; i <= 100; i++) {
            QuantityID.getItems().add(String.valueOf(i));
        }

        DressID.setOnAction(event -> populateWorkers());

        PriceID.textProperty().addListener((observable, oldValue, newValue) -> updateTotalBill());
        QuantityID.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> updateTotalBill());

        populateOrderIDs();

        SelectStatus.getItems().addAll("1","2","3");

    }
}
