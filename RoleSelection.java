package com.example.bistrobliss;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.stage.Stage;

public class RoleSelection {

    private BistroBliss bistroBliss;

    public void setBistroBliss(BistroBliss bistroBliss) {
        this.bistroBliss = bistroBliss;
    }

    @FXML
    private Button AdminButton;

    @FXML
    private Button UserButton;

    @FXML
    public void setupButtonHandlers(Stage primaryStage) {
        // Set up event handler for the Admin button
        AdminButton.setOnAction(event -> {
            if (bistroBliss != null) {
                bistroBliss.showAdminLoginRegister(primaryStage);
            } else {
                System.out.println("BistroBliss reference is null.");
            }
        });

        // Set up event handler for the User button
        UserButton.setOnAction(event -> {
            if (bistroBliss != null) {
                bistroBliss.showUserLoginRegister(primaryStage);
            } else {
                System.out.println("BistroBliss reference is null.");
            }
        });
    }
}