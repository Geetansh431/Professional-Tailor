package org.example.project.workersconsole;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import org.example.project.enrollment.MySqlConnectionKlass;

public class WorkersConsoleController {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private TextField Address;

    @FXML
    private TextArea FillSpecialization;

    @FXML
    private TextField MobileNumber;

    @FXML
    private ListView<String> Specialization;

    @FXML
    private TextField WorkerName;

    @FXML
    void ClickedSpecialization(MouseEvent event) {
        if (event.getClickCount() == 2) {
            String selectedItem = Specialization.getSelectionModel().getSelectedItem();
            if (selectedItem != null) {
                FillSpecialization.appendText(selectedItem + ",");
            }
        }
    }

    PreparedStatement stmt;

    @FXML
    void DoSave(ActionEvent event) {
        try {
            // Prepare SQL insert statement
            stmt = con.prepareStatement("INSERT INTO workers (wname, address, mobile, splz) VALUES (?, ?, ?, ?)");
            stmt.setString(1, WorkerName.getText());
            stmt.setString(2, Address.getText());
            stmt.setString(3, MobileNumber.getText());
            stmt.setString(4, FillSpecialization.getText());

            // Execute the insert statement
            int rowsInserted = stmt.executeUpdate();
            if (rowsInserted > 0) {
                System.out.println("A new worker was inserted successfully!");
                showAlert(Alert.AlertType.INFORMATION, "Success", "Worker saved successfully!");
            } else {
                showAlert(Alert.AlertType.ERROR, "Error", "Worker could not be saved!");
            }
        } catch (SQLException exp) {
            exp.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Error", "An error occurred while saving the worker: " + exp.getMessage());
        }
    }

    @FXML
    void NewConsole(ActionEvent event) {
        WorkerName.clear();
        Address.clear();
        MobileNumber.clear();
        FillSpecialization.clear();
        Specialization.getSelectionModel().clearSelection();
    }

    @FXML
    void DoDelete(ActionEvent event) {
        try {
            stmt = con.prepareStatement("DELETE FROM workers WHERE wname = ?");
            stmt.setString(1, WorkerName.getText());
            int rowsDeleted = stmt.executeUpdate();
            if (rowsDeleted > 0) {
                System.out.println("The worker was deleted successfully!");
                showAlert (Alert.AlertType.INFORMATION, "Success", "Worker deleted successfully!");
            } else {
                showAlert(Alert.AlertType.ERROR, "Error", "Worker could not be found or deleted!");
            }
        } catch (SQLException exp) {
            exp.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Error", "An error occurred while deleting the worker: " + exp.getMessage());
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
        ObservableList<String> items = FXCollections.observableArrayList(
                "Pent", "Shirt", "Coat"
        );
        Specialization.setItems(items);
    }

    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
