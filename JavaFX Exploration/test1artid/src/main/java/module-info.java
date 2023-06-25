module com.testgroupid {
    requires javafx.controls;
    requires javafx.fxml;

    opens com.testgroupid to javafx.fxml;
    exports com.testgroupid;
}
