module com.grupo8.indicehash {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires net.synedra.validatorfx;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.bootstrapfx.core;
    requires com.almasb.fxgl.all;
    requires java.desktop;
    opens com.grupo8.indicehash to javafx.fxml;
    opens com.grupo8.indicehash.gui to javafx.graphics, javafx.fxml;
    opens com.grupo8.indicehash.controller to javafx.fxml;
    exports com.grupo8.indicehash;
    exports com.grupo8.indicehash.gui;
    exports com.grupo8.indicehash.controller;
}