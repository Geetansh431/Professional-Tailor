module org.example.project {
    requires javafx.controls;
    requires javafx.fxml;

    requires com.dlsc.formsfx;
    requires java.sql;
    requires mysql.connector.j;
    requires java.mail;

    opens org.example.project to javafx.fxml;
    exports org.example.project;

    exports org.example.project.enrollment;
    opens org.example.project.enrollment to javafx.fxml;

    exports org.example.project.workersconsole;
    opens org.example.project.workersconsole to javafx.fxml;

    exports  org.example.project.measurement;
    opens  org.example.project.measurement to javafx.fxml;

    exports org.example.project.readyproducts;
    opens org.example.project.readyproducts to javafx.fxml;


    exports org.example.project.workersdata;
    opens org.example.project.workersdata to javafx.fxml;

    exports org.example.project.measurementExplorer;
    opens org.example.project.measurementExplorer to javafx.fxml;

    exports org.example.project.orderDeliveryPanel;
    opens org.example.project.orderDeliveryPanel to javafx.fxml;
}