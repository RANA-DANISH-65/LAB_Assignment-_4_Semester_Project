module com.example.crypto_price_tracker {
    requires javafx.controls;
    requires javafx.fxml;
    requires okhttp3;
    requires org.json;
    requires javafx.graphics;
    requires org.jfree.jfreechart;
    requires java.desktop;
    requires firebase.admin;
    requires com.google.auth.oauth2;
    requires java.sql;


    opens com.example.crypto_price_tracker to javafx.fxml;
    exports com.example.crypto_price_tracker;
}