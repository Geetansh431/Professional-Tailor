package org.example.project.workersdata;

import java.net.URL;
import java.sql.*;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import org.example.project.enrollment.MySqlConnectionKlass;



public class WorkersDataController {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private TableView<Worker> workersTable;

    @FXML
    private TableColumn<Worker, String> addressColumn;

    @FXML
    private TableColumn<Worker, String> numberColumn;

    @FXML
    private ComboBox<String> SpecializationCombo;

    @FXML
    private TableColumn<Worker, String> specializationColumn;

    @FXML
    private TableColumn<Worker, String> nameColumn;

    private Connection con;

    @FXML
    void ShowWorkersData(ActionEvent event) {
        ObservableList<Worker> workersList = FXCollections.observableArrayList();
        String query = "SELECT * FROM workers";

        try (Statement stmt = con.createStatement(); ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next()) {
                workersList.add(new Worker(
                        rs.getString("wname"),
                        rs.getString("address"),
                        rs.getString("mobile"),
                        rs.getString("splz")
                ));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        workersTable.setItems(workersList);
    }

    @FXML
    void SlowSpec(ActionEvent event) {
        String selectedSpec = SpecializationCombo.getValue();
        ObservableList<Worker> workersList = FXCollections.observableArrayList();
        String query = "SELECT * FROM workers WHERE splz like ?";

        try (PreparedStatement stmt = con.prepareStatement(query)) {
            stmt.setString(1, "%" +selectedSpec+ "%");
            ResultSet rs = stmt.executeQuery();

            while (rs.next())
            {
                workersList.add(new Worker(
                        rs.getString("wname"),
                        rs.getString("address"),
                        rs.getString("mobile"),
                        rs.getString("splz")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        workersTable.setItems(workersList);
    }

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
        SpecializationCombo.setItems(items);
        // Set up the table columns
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("wname"));
        addressColumn.setCellValueFactory(new PropertyValueFactory<>("address"));
        numberColumn.setCellValueFactory(new PropertyValueFactory<>("mobile"));
        specializationColumn.setCellValueFactory(new PropertyValueFactory<>("splz"));
    }

}
