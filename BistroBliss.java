package com.example.bistrobliss;

import javafx.application.Application;
import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.Window;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class BistroBliss extends Application {

    private static final String ADMIN_FILE = "admin.txt";
    private static final String USER_FILE = "user.txt";
    private static final int MIN_PASSWORD_LENGTH = 8;

    //regular expressions for password validation
    private static final String PASSWORD_PATTERN = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=?])(?=\\S+$).{8,}$";

    private ObservableList<Ingredient> ingredients = FXCollections.observableArrayList();
    private List<FoodItem> availableItems;
    private Order currentOrder;

    //method to check if a password meets the criteria
    private boolean isPasswordValid(String password) {
        Pattern pattern = Pattern.compile(PASSWORD_PATTERN);
        Matcher matcher = pattern.matcher(password);
        return matcher.matches();
    }

    public void start(Stage stage) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("RoleSelection.fxml"));
            Parent root = loader.load();

            //controller instance
            RoleSelection controller = loader.getController();
            //event handlers for the Admin and User buttons
            controller.setupButtonHandlers(stage);
            controller.setBistroBliss(this);
            initialiseAvailableItems();

            Image icon = new Image(getClass().getResourceAsStream("/com/example/bistrobliss/logo.jpg"));
            stage.getIcons().addAll(icon);

            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.setTitle("Role Selection");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        //create empty files
        createEmptyFile(ADMIN_FILE);
        createEmptyFile(USER_FILE);
        launch();
    }

    static void createEmptyFile(String fileName) {
        try {
            File file = new File(fileName);
            if (file.createNewFile()) {
                System.out.println("File created: " + file.getName());
            } else {
                System.out.println("File already exists.");
            }
        } catch (IOException e) {
            System.out.println("An error occurred while creating the file.");
            e.printStackTrace();
        }
    }

    private void initialiseAvailableItems() {
        availableItems = new ArrayList<>();
        Image shawarmaImage = new Image(getClass().getResourceAsStream("/com/example/bistrobliss/Shawarma.jpg"));
        FoodItem shawarma = new FoodItem("Chicken Shawarma", shawarmaImage, "Made with thinly sliced, marinated chicken grilled on a rotisserie, served in pita bread with tahini sauce, hummus, and veggies", 13.99);
        availableItems.add(shawarma);

        Image maqloobaImage = new Image(getClass().getResourceAsStream("/com/example/bistrobliss/Maqlooba.jpg"));
        FoodItem maqlooba = new FoodItem("Maqlooba", maqloobaImage, "Made with rice, meat, and fried vegetables. The ingredients are layered in a pot, cooked together, and then flipped upside down when served", 27.99);
        availableItems.add(maqlooba);

        Image hummusImage = new Image(getClass().getResourceAsStream("/com/example/bistrobliss/Hummus.jpg"));
        FoodItem hummus = new FoodItem("Hummus", hummusImage,"A dip made from cooked, mashed chickpeas blended with tahini, lemon juice, garlic, and olive oil", 2.99);
        availableItems.add(hummus);

        Image falafelImage = new Image(getClass().getResourceAsStream("/com/example/bistrobliss/Falafel.jpg"));
        FoodItem falafel = new FoodItem("Falefel", falafelImage, "Deep-fried balls  made from ground chickpeas",4.99);
        availableItems.add(falafel);

        Image mahshiImage = new Image(getClass().getResourceAsStream("/com/example/bistrobliss/Mahshi.jpg"));
        FoodItem mahshi = new FoodItem("Mahshi", mahshiImage, "Vine leaves stuffed with a flavorful mixture of rice, herbs, spices, and meat", 14.99);
        availableItems.add(mahshi);

        Image tabboulehImage = new Image(getClass().getResourceAsStream("/com/example/bistrobliss/Tabbouleh.jpg"));
        FoodItem tabbouleh = new FoodItem("Tabbouleh", tabboulehImage, "Salad made with finely chopped parsley, tomatoes, mint, onion, and soaked bulgur", 7.99);
        availableItems.add(tabbouleh);

        Image chickenmandiImage = new Image(getClass().getResourceAsStream("/com/example/bistrobliss/ChickMandi.jpg"));
        FoodItem chickenmandi = new FoodItem("Chicken Mandi", chickenmandiImage, "Tender chicken marinated in aromatic spices, slow-cooked to perfection, and served over fragrant basmati rice, creating a flavorful dish with a blend of savory and aromatic notes", 17.99);
        availableItems.add(chickenmandi);

        Image meatmandiImage = new Image(getClass().getResourceAsStream("/com/example/bistrobliss/MeatMandi.jpg"));
        FoodItem meatmandi = new FoodItem("Meat Mandi", meatmandiImage, "Slow-cooked tender meat, infused with spices and served atop fragrant basmati rice, garnished with nuts and raisins", 22.99);
        availableItems.add(meatmandi);

        Image chickenkabsaImage = new Image(getClass().getResourceAsStream("/com/example/bistrobliss/ChickKabsa.jpg"));
        FoodItem chickenkabsa = new FoodItem("Chicken Kabsa", chickenkabsaImage, "Long-grain rice cooked with tender chicken and a mix of vegetables, seasoned with a blend of spices with savory chicken and fragrant rice", 17.99);
        availableItems.add(chickenkabsa);

        Image meatkabsaImage = new Image(getClass().getResourceAsStream("/com/example/bistrobliss/MeatKabsa.jpg"));
        FoodItem meatkabsa = new FoodItem("Meat Kabsa", meatkabsaImage, "Long-grain rice cooked with tender meat and a mix of vegetables, seasoned with spices with savory meat and fragrant rice", 22.99);
        availableItems.add(meatkabsa);

        Image shakshukaImage = new Image(getClass().getResourceAsStream("/com/example/bistrobliss/Shakshuka.jpg"));
        FoodItem shakshuka = new FoodItem("Shakshuka", shakshukaImage, "Poached eggs in a sauce made from tomatoes, chili peppers, onions, and various spices", 6.99);
        availableItems.add(shakshuka);

        Image fattoushImage = new Image(getClass().getResourceAsStream("/com/example/bistrobliss/Fattoush.jpg"));
        FoodItem fattoush = new FoodItem("Fattoush", fattoushImage, "Fresh vegetables garnished with toasted pita bread, and dressed with vinaigrette", 8.99);
        availableItems.add(fattoush);

        Image knafehImage = new Image(getClass().getResourceAsStream("/com/example/bistrobliss/Knafeh.jpg"));
        FoodItem knafeh = new FoodItem("Knafeh", knafehImage, "Shredded phyllo dough filled with cheese, and soaked in sweet syrup", 10.99);
        availableItems.add(knafeh);

        Image waterImage = new Image(getClass().getResourceAsStream("/com/example/bistrobliss/Water.jpg"));
        FoodItem water = new FoodItem("Water", waterImage, "Water", 0.99);
        availableItems.add(water);

        Image cocktailImage = new Image(getClass().getResourceAsStream("/com/example/bistrobliss/Cocktail.jpg"));
        FoodItem cocktailjuice = new FoodItem("Cocktail Juice", cocktailImage, "Made from freshly blended banana, apple, kiwi, and watermelon", 4.99);
        availableItems.add(cocktailjuice);
    }

    //method to change password
    void changePassword(String username, String newPassword, String fileName) {
        try {
            File file = new File(fileName);
            File tempFile = new File("temp.txt");
            Scanner scanner = new Scanner(file);
            FileWriter writer = new FileWriter(tempFile);
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                String[] parts = line.split(",");
                if (parts.length == 2 && parts[0].equals(username)) {
                    //update the password
                    writer.write(parts[0] + "," + newPassword + "\n");
                } else {
                    writer.write(line + "\n");
                }
            }
            scanner.close();
            writer.close();
            file.delete();
            tempFile.renameTo(new File(fileName));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //method to display message
    private void showMessage(Label messageLabel, String message, Color color) {
        messageLabel.setText(message);
        messageLabel.setTextFill(color);
    }

    //method to show admin login/register view
    public void showAdminLoginRegister(Stage primaryStage) {
        //close previous stage
        primaryStage.close();
        //create new stage
        Stage adminStage = new Stage();
        adminStage.setTitle("Admin Login/Register");

        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(25, 25, 25, 25));

        adminStage.setFullScreen(true);

        Button exitButton = new Button("X");
        exitButton.setOnAction(event -> adminStage.close());
        HBox xbuttonBox = new HBox(10, exitButton);
        xbuttonBox.setAlignment(Pos.TOP_RIGHT);
        grid.add(xbuttonBox, 1, 0);

        Label passwordDescriptionLabel = new Label("Please note: Password must contain at least one digit, one uppercase letter, one lowercase letter, one special character (@#$%^&+=), and no whitespaces.");
        passwordDescriptionLabel.setTextFill(Color.RED);
        grid.add(passwordDescriptionLabel, 0, 10, 2, 1);

        Label usernameLabel = new Label("Username:");
        grid.add(usernameLabel, 0, 2);

        TextField usernameTextField = new TextField();
        usernameTextField.setMaxWidth(200);
        grid.add(usernameTextField, 1, 2);

        Label passwordLabel = new Label("Password:");
        grid.add(passwordLabel, 0, 3);

        PasswordField passwordField = new PasswordField();
        passwordField.setMaxWidth(200);
        grid.add(passwordField, 1, 3);

        Button loginButton = new Button("Login");
        grid.add(loginButton, 0, 4);

        Button registerButton = new Button("Register");
        grid.add(registerButton, 1, 4);

        Text descriptionText = new Text("Welcome, Admin!\n\nPlease enter a username and a password, then click on the 'Register' button to register. After registering you can login by clicking the 'Login' button.");
        descriptionText.setFont(Font.font("Arial", FontWeight.NORMAL, 14));
        descriptionText.setTextAlignment(TextAlignment.CENTER);
        grid.add(descriptionText, 0, 1, 2, 1);

        Label messageLabel = new Label();
        grid.add(messageLabel, 0, 5, 2, 4);

        Button backButton = new Button("Back");
        grid.add(backButton, 0, 12);
        backButton.setOnAction(e -> {
            adminStage.close();
            primaryStage.show();
        });

        //event handlers for login and register
        loginButton.setOnAction(e -> {
            String username = usernameTextField.getText();
            String password = passwordField.getText();
            if (username.isEmpty() && password.isEmpty()) {
                showMessage(messageLabel, "Please enter your username and password to login.", Color.RED);
            } else if (username.isEmpty()) {
                showMessage(messageLabel, "Please enter your username as well to login.", Color.RED);
            } else if (password.isEmpty()) {
                showMessage(messageLabel, "Please enter your password as well to login.", Color.RED);
            } else {
                if (checkCredentials(username, password, ADMIN_FILE)) {
                    showMessage(messageLabel, "Admin login successful", Color.GREEN);
                    showAdminDashboard(adminStage, primaryStage);
                } else {
                    showMessage(messageLabel, "Invalid admin credentials", Color.RED);
                }
            }
        });

        registerButton.setOnAction(e -> {
            String username = usernameTextField.getText();
            String password = passwordField.getText();
            if (username.isEmpty() && password.isEmpty()) {
                showMessage(messageLabel, "Please enter your username and password to register.", Color.RED);
            } else if (username.isEmpty()) {
                showMessage(messageLabel, "Please enter your username as well to register.", Color.RED);
            } else if (password.isEmpty()) {
                showMessage(messageLabel, "Please enter your password as well to register.", Color.RED);
            } else if (password.length() < MIN_PASSWORD_LENGTH) {
                showMessage(messageLabel, "Password should be at least " + MIN_PASSWORD_LENGTH + " characters long.", Color.RED);
            } else if (!isPasswordValid(password)){
                showMessage(messageLabel, "Password must contain at least one digit, one uppercase letter, one lowercase letter, one special character (@#$%^&+=), and no whitespaces.", Color.RED);
            } else {
                //call registerCredentials method with messageLabel as parameter
                if (registerCredentials(username, password, ADMIN_FILE, messageLabel)) {
                    //registration successful clear fields
                    usernameTextField.clear();
                    passwordField.clear();
                }
            }
        });

        //button to handle forgot password functionality
        Button forgotPasswordButton = new Button("Forgot Password?");
        grid.add(forgotPasswordButton, 0, 8, 2, 2);

        forgotPasswordButton.setOnAction(e -> {
            String username = usernameTextField.getText();
            if (username.isEmpty()) {
                showMessage(messageLabel, "Please enter your username to change password.", Color.RED);
            } else {
                // Check if username exists in the file before proceeding
                if (isUserRegistered(username, ADMIN_FILE)) {
                    //create a dialog for changing password
                    Stage dialog = new Stage();
                    dialog.initOwner(adminStage);
                    dialog.initModality(Modality.APPLICATION_MODAL);
                    dialog.setTitle("Change Password");
                    GridPane dialogGrid = new GridPane();
                    dialogGrid.setAlignment(Pos.CENTER);
                    dialogGrid.setHgap(10);
                    dialogGrid.setVgap(10);
                    dialogGrid.setPadding(new Insets(25, 25, 25, 25));

                    //label for displaying password change messages
                    Label changeMessageLabel = new Label();
                    dialogGrid.add(changeMessageLabel, 0, 4, 2, 1);

                    Label newPasswordLabel = new Label("New Password:");
                    dialogGrid.add(newPasswordLabel, 0, 1);
                    PasswordField newPasswordField = new PasswordField();
                    dialogGrid.add(newPasswordField, 1, 1);

                    Label confirmPasswordLabel = new Label("Confirm New Password:");
                    dialogGrid.add(confirmPasswordLabel, 0, 2);
                    PasswordField confirmPasswordField = new PasswordField();
                    dialogGrid.add(confirmPasswordField, 1, 2);

                    Button changeButton = new Button("Change");
                    dialogGrid.add(changeButton, 0, 3, 2, 1);
                    changeButton.setOnAction(event -> {
                        String newPassword = newPasswordField.getText();
                        String confirmPassword = confirmPasswordField.getText();
                        if (newPassword.isEmpty() || confirmPassword.isEmpty()) {
                            changeMessageLabel.setText("Please enter and confirm the new password.");
                            changeMessageLabel.setTextFill(Color.RED);
                        } else if (!newPassword.equals(confirmPassword)) {
                            changeMessageLabel.setText("Passwords do not match.");
                            changeMessageLabel.setTextFill(Color.RED);
                        } else if (newPassword.length() < MIN_PASSWORD_LENGTH) {
                            changeMessageLabel.setText("Password should be at least " + MIN_PASSWORD_LENGTH + " characters long.");
                            changeMessageLabel.setTextFill(Color.RED);
                        } else if (!isPasswordValid(newPassword)) {
                            changeMessageLabel.setText("Password must contain at least one digit, one uppercase letter, one lowercase letter, one special character (@#$%^&+=), and no whitespaces.");
                            changeMessageLabel.setTextFill(Color.RED);
                        } else {
                            changePassword(username, newPassword, ADMIN_FILE);
                            changeMessageLabel.setText("Password changed successfully.");
                            changeMessageLabel.setTextFill(Color.GREEN);
                            dialog.close();
                        }
                    });

                    Scene dialogScene = new Scene(dialogGrid, 400, 200);
                    dialog.setScene(dialogScene);
                    dialog.showAndWait();
                } else {
                    showMessage(messageLabel, "Username not found. Please register first.", Color.RED);
                }
            }
        });

        Scene scene = new Scene(grid, 600, 400);
        adminStage.setScene(scene);
        adminStage.show();
    }
    private String loggedInUserName;


    //method to show user login/register view
    public void showUserLoginRegister(Stage primaryStage) {
        //close previous stage
        primaryStage.close();
        //create new stage
        Stage userStage = new Stage();
        userStage.setTitle("User Login/Register");

        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(25, 25, 25, 25));

        Button exitButton = new Button("X");
        exitButton.setOnAction(event -> userStage.close());
        HBox xbuttonBox = new HBox(10, exitButton);
        xbuttonBox.setAlignment(Pos.TOP_RIGHT);
        grid.add(xbuttonBox,1,0);

        userStage.setFullScreen(true);

        Label usernameLabel = new Label("Username:");
        grid.add(usernameLabel, 0, 2);

        TextField usernameTextField = new TextField();
        usernameTextField.setMaxWidth(200);
        grid.add(usernameTextField, 1, 2);

        Label passwordDescriptionLabel = new Label("Please note: Password must contain at least one digit, one uppercase letter, one lowercase letter, one special character (@#$%^&+=), and no whitespaces.");
        passwordDescriptionLabel.setTextFill(Color.RED);
        grid.add(passwordDescriptionLabel, 0, 10, 2, 1);

        Label passwordLabel = new Label("Password:");
        grid.add(passwordLabel, 0, 3);

        PasswordField passwordField = new PasswordField();
        passwordField.setMaxWidth(200);
        grid.add(passwordField, 1, 3);

        Button loginButton = new Button("Login");
        grid.add(loginButton, 0, 4);

        Button registerButton = new Button("Register");
        grid.add(registerButton, 1, 4);

        Text descriptionText = new Text("Welcome, User!\n\nPlease enter a username and a password, then click on the 'Register' button to register. After registering you can login by clicking the 'Login' button.");
        descriptionText.setFont(Font.font("Arial", FontWeight.NORMAL, 14));
        descriptionText.setTextAlignment(TextAlignment.CENTER);
        grid.add(descriptionText, 0, 1, 2, 1);

        Label messageLabel = new Label();
        grid.add(messageLabel, 0, 5, 2, 4);


        Button backButton = new Button("Back");
        grid.add(backButton, 0, 12);
        backButton.setOnAction(e -> {
            userStage.close();
            primaryStage.show();
        });

        //event handlers for login and register
        loginButton.setOnAction(e -> {
            String username = usernameTextField.getText();
            String password = passwordField.getText();
            if (username.isEmpty() && password.isEmpty()) {
                showMessage(messageLabel, "Please enter your username and password to login.", Color.RED);
            } else if (username.isEmpty()) {
                showMessage(messageLabel, "Please enter your username as well to login.", Color.RED);
            } else if (password.isEmpty()) {
                showMessage(messageLabel, "Please enter your password as well to login.", Color.RED);
            } else {
                if (checkCredentials(username, password, USER_FILE)) {
                    loggedInUserName = username;
                    showMessage(messageLabel, "User login successful", Color.GREEN);
                    Order currentOrder = new Order();
                    List<OrderItem> reviewOrderItems = new ArrayList<>();
                    showUserCRUDInterface(userStage, availableItems, currentOrder, primaryStage, reviewOrderItems);
                } else {
                    showMessage(messageLabel, "Invalid user credentials", Color.RED);
                }
            }
        });

        registerButton.setOnAction(e -> {
            String username = usernameTextField.getText();
            String password = passwordField.getText();
            if (username.isEmpty() && password.isEmpty()) {
                showMessage(messageLabel, "Please enter your username and password to register.", Color.RED);
            } else if (username.isEmpty()) {
                showMessage(messageLabel, "Please enter your username as well to register.", Color.RED);
            } else if (password.isEmpty()) {
                showMessage(messageLabel, "Please enter your password as well to register.", Color.RED);
            } else if (password.length() < MIN_PASSWORD_LENGTH) {
                showMessage(messageLabel, "Password should be at least " + MIN_PASSWORD_LENGTH + " characters long.", Color.RED);
            } else if (!isPasswordValid(password)){
                showMessage(messageLabel, "Password must contain at least one digit, one uppercase letter, one lowercase letter, one special character (@#$%^&+=), and no whitespaces.", Color.RED);
            } else {
                //call registerCredentials method with messageLabel as parameter
                if (registerCredentials(username, password, USER_FILE, messageLabel)) {
                    //registration successful, clear fields
                    usernameTextField.clear();
                    passwordField.clear();
                }
            }
        });

        //button to handle forgot password functionality
        Button forgotPasswordButton = new Button("Forgot Password?");
        grid.add(forgotPasswordButton, 0, 8, 2, 2);

        forgotPasswordButton.setOnAction(e -> {
            String username = usernameTextField.getText();
            if (username.isEmpty()) {
                showMessage(messageLabel, "Please enter your username to change password.", Color.RED);
                //dialog for changing password
            } else {
                //check if username exists in the file before proceeding
                if (isUserRegistered(username, USER_FILE)) {
                    Stage dialog = new Stage();
                    dialog.initOwner(userStage);
                    dialog.initModality(Modality.APPLICATION_MODAL);
                    dialog.setTitle("Change Password");
                    GridPane dialogGrid = new GridPane();
                    dialogGrid.setAlignment(Pos.CENTER);
                    dialogGrid.setHgap(10);
                    dialogGrid.setVgap(10);
                    dialogGrid.setPadding(new Insets(25, 25, 25, 25));

                    //label for displaying password change messages
                    Label changeMessageLabel = new Label();
                    dialogGrid.add(changeMessageLabel, 0, 4, 2, 1);

                    Label newPasswordLabel = new Label("New Password:");
                    dialogGrid.add(newPasswordLabel, 0, 1);
                    PasswordField newPasswordField = new PasswordField();
                    dialogGrid.add(newPasswordField, 1, 1);

                    Label confirmPasswordLabel = new Label("Confirm New Password:");
                    dialogGrid.add(confirmPasswordLabel, 0, 2);
                    PasswordField confirmPasswordField = new PasswordField();
                    dialogGrid.add(confirmPasswordField, 1, 2);

                    Button changeButton = new Button("Change");
                    dialogGrid.add(changeButton, 0, 3, 2, 1);
                    changeButton.setOnAction(event -> {
                        String newPassword = newPasswordField.getText();
                        String confirmPassword = confirmPasswordField.getText();
                        if (newPassword.isEmpty() || confirmPassword.isEmpty()) {
                            changeMessageLabel.setText("Please enter and confirm the new password.");
                            changeMessageLabel.setTextFill(Color.RED);
                        } else if (!newPassword.equals(confirmPassword)) {
                            changeMessageLabel.setText("Passwords do not match.");
                            changeMessageLabel.setTextFill(Color.RED);
                        } else if (newPassword.length() < MIN_PASSWORD_LENGTH) {
                            changeMessageLabel.setText("Password should be at least " + MIN_PASSWORD_LENGTH + " characters long.");
                            changeMessageLabel.setTextFill(Color.RED);
                        } else if (!isPasswordValid(newPassword)) {
                            changeMessageLabel.setText("Password must contain at least one digit, one uppercase letter, one lowercase letter, one special character (@#$%^&+=), and no whitespaces.");
                            changeMessageLabel.setTextFill(Color.RED);
                        } else {
                            changePassword(username, newPassword, USER_FILE);
                            changeMessageLabel.setText("Password changed successfully.");
                            changeMessageLabel.setTextFill(Color.GREEN);
                            dialog.close();
                        }
                    });

                    //width and height of the scene
                    Scene dialogScene = new Scene(dialogGrid, 400, 200);
                    dialog.setScene(dialogScene);
                    dialog.showAndWait();
                } else {
                    showMessage(messageLabel, "Username not found. Please register first.", Color.RED);
                }
            }
        });

        Scene scene = new Scene(grid, 400, 300);
        userStage.setScene(scene);
        userStage.show();
    }

    private boolean isUserRegistered(String username, String filename) {
        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] userData = line.split(",");
                if (userData.length > 1 && userData[0].equals(username)) {
                    return true;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }
    boolean checkCredentials(String username, String password, String fileName) {
        try {
            File file = new File(fileName);
            Scanner scanner = new Scanner(file);
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                String[] parts = line.split(",");
                if (parts.length == 2 && parts[0].equals(username) && parts[1].equals(password)) {
                    scanner.close();
                    return true;
                }
            }
            scanner.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    boolean registerCredentials(String username, String password, String fileName, Label messageLabel) {
        try {
            File file = new File(fileName);
            //check if the username already exists
            if (checkUsernameExists(username, file)) {
                showMessage(messageLabel, "Username already exists.", Color.RED);
                return false;
            }
            //if username doesnt exist, proceed with registration
            FileWriter writer = new FileWriter(file, true);
            writer.write(username + "," + password + "\n");
            writer.close();
            showMessage(messageLabel, "Registration successful", Color.GREEN);
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    private void showMessageAddItem(String itemName, int quantity) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Item Added");
        alert.setHeaderText(null);
        alert.setContentText("Added " + quantity + " " + itemName + " to your order.");
        alert.showAndWait();
    }
    public void showUserCRUDInterface(Stage userStage, List<FoodItem> availableItems, Order currentOrder, Stage primaryStage, List<OrderItem> reviewOrderItems) {
        userStage.close();

        //stage for the CRUD interface
        Stage crudStage = new Stage();
        crudStage.setTitle("Order");
        crudStage.setFullScreen(true);

        //gridpane to hold the items
        GridPane gridPane = new GridPane();
        gridPane.setAlignment(Pos.CENTER);
        gridPane.setHgap(10);
        gridPane.setVgap(10);

        int columnIndex = 0;
        int rowIndex = 0;

        for (FoodItem item : availableItems) {
            VBox itemLayout = new VBox();
            itemLayout.setAlignment(Pos.CENTER);

            //imageview for the food items picture
            ImageView imageView = new ImageView(item.getImage());
            imageView.setFitWidth(200);
            imageView.setFitHeight(200);

            //tooltip for the image to show the description
            Tooltip tooltip = new Tooltip(item.getDescription());
            Tooltip.install(imageView, tooltip);

            //label for the food items name
            Label nameLabel = new Label(item.getName());
            nameLabel.setAlignment(Pos.CENTER);
            nameLabel.setWrapText(true);

            //label for the food items price
            Label priceLabel = new Label("Price: MYR " + String.format("%.2f", item.getPrice()));
            priceLabel.setAlignment(Pos.CENTER);

            //button to add the item to the order
            Button addButton = new Button("Add to Order");
            addButton.setOnAction(event -> {
                TextInputDialog dialog = new TextInputDialog("1");
                dialog.setTitle("Enter Quantity");
                dialog.setHeaderText("Enter the quantity for " + item.getName() + ":");
                dialog.setContentText("Quantity:");

                Optional<String> result = dialog.showAndWait();
                result.ifPresent(quantityStr -> {
                    try {
                        int quantity = Integer.parseInt(quantityStr);
                        if (quantity <= 0) {
                            showMessageAddItem("Invalid quantity. Please enter a positive integer.", 0);
                            return;
                        }
                        OrderItem orderItem = new OrderItem(item, quantity);
                        currentOrder.addItem(orderItem);
                        //add the order item to the review list
                        reviewOrderItems.add(orderItem);
                        showMessageAddItem(item.getName(), quantity);
                    } catch (NumberFormatException e) {
                        showMessageAddItem("Invalid quantity format. Please enter a valid integer.", 0);
                    }
                });
            });

            //add the elements to the vbox
            itemLayout.getChildren().addAll(imageView, nameLabel, priceLabel,addButton);

            //add the vbox to the GridPane
            gridPane.add(itemLayout, columnIndex, rowIndex);

            //increment the column index if it reaches 4 reset it to 0 and increment the row index
            columnIndex++;
            if (columnIndex == 4) {
                columnIndex = 0;
                rowIndex++;
            }
        }

        //scrollpane to hold the gridpane
        ScrollPane scrollPane = new ScrollPane(gridPane);
        scrollPane.setFitToWidth(true);

        //button to review and place the order
        Button placeOrderButton = new Button("Review and Place Order");
        placeOrderButton.setOnAction(event -> {
            showReviewOrderStage(reviewOrderItems, primaryStage);
        });

        Button cancelOrderButton = new Button("Cancel Order");
        cancelOrderButton.setOnAction(event ->{
            showUserOrders(loggedInUserName);
        });

        //button to sign out
        Button signoutButton = new Button("Sign Out");
        signoutButton.setOnAction(event -> {
            crudStage.close();
            showUserLoginRegister(primaryStage);
        });

        //button to exit
        Button exitButton = new Button("X");
        exitButton.setOnAction(event -> crudStage.close());
        HBox xbuttonBox = new HBox(10, exitButton);
        xbuttonBox.setAlignment(Pos.CENTER_RIGHT);

        //vbox to hold the buttons
        VBox buttonVBox = new VBox(10, placeOrderButton, cancelOrderButton,signoutButton);
        buttonVBox.setAlignment(Pos.CENTER);

        //vbox to hold the ScrollPane and buttons
        VBox layout = new VBox(10, xbuttonBox, scrollPane, buttonVBox);
        layout.setAlignment(Pos.CENTER);

        //scene to display the layout
        Scene scene = new Scene(layout, 800, 600);
        crudStage.setScene(scene);
        crudStage.show();
    }
    private void showAlert(String message, Alert.AlertType alertType) {
        Alert alert = new Alert(alertType);
        alert.setTitle("Alert");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void showUserOrders(String userName) {
        //stage to display user orders
        Stage orderStage = new Stage();
        orderStage.setTitle("Your Orders");

        //tableview to display orders
        TableView<String[]> orderTableView = new TableView<>();
        TableColumn<String[], String> orderNumberColumn = new TableColumn<>("Order Number");
        orderNumberColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue()[0]));

        TableColumn<String[], String> userNameColumn = new TableColumn<>("Username");
        userNameColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue()[1]));

        TableColumn<String[], String> orderItemsColumn = new TableColumn<>("Ordered Items");
        orderItemsColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue()[2]));

        TableColumn<String[], String> totalQuantityColumn = new TableColumn<>("Total Quantity");
        totalQuantityColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue()[3]));

        TableColumn<String[], String> totalPriceColumn = new TableColumn<>("Total Price");
        totalPriceColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue()[4]));

        //add columns to the TableView
        orderTableView.getColumns().addAll(orderNumberColumn, userNameColumn, orderItemsColumn, totalQuantityColumn, totalPriceColumn);

        //read orders from file and filter orders for the current user
        List<String[]> userOrders = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader("Orders.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] orderData = line.split(", ");
                if (orderData.length >= 5 && orderData[1].equals(userName)) {
                    userOrders.add(orderData);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        //populate tableview with user orders
        ObservableList<String[]> data = FXCollections.observableArrayList(userOrders);
        orderTableView.setItems(data);

        //cancel order button
        Button cancelOrderButton = new Button("Cancel Selected Order");
        cancelOrderButton.setOnAction(event -> {
            String[] selectedOrder = orderTableView.getSelectionModel().getSelectedItem();
            if (selectedOrder != null) {
                removeOrder(selectedOrder[0]);
                orderTableView.getItems().remove(selectedOrder);
                showAlert("Order canceled successfully", Alert.AlertType.INFORMATION);
            } else {
                showAlert("Please select an order to cancel", Alert.AlertType.WARNING);
            }
        });

        //vbox to hold TableView and Cancel Order button
        VBox vbox = new VBox();
        vbox.setSpacing(10);
        vbox.setAlignment(Pos.CENTER);
        vbox.getChildren().addAll(orderTableView, cancelOrderButton);

        //create scene and set it to the stage
        Scene scene = new Scene(vbox, 600, 400);
        orderStage.setScene(scene);
        orderStage.show();
    }

    private void removeOrder(String orderId) {
        //remove order with given ID from the file
        try {
            List<String> lines = Files.readAllLines(Paths.get("Orders.txt"));
            lines.removeIf(line -> line.startsWith(orderId + ","));
            Files.write(Paths.get("Orders.txt"), lines);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private void showReviewOrderStage(List<OrderItem> reviewOrderItems, Stage primaryStage) {
    Stage reviewOrderStage = new Stage();
    reviewOrderStage.setTitle("Review Order");

    VBox layout = new VBox(10);
    layout.setPadding(new Insets(10));

    Label descriptionLabel = new Label("Please select an item to update or delete");
    layout.getChildren().add(descriptionLabel);

    //display review order items with quantity
    ListView<String> orderListView = new ListView<>();
    double totalPrice = 0;

    for (OrderItem orderItem : reviewOrderItems) {
        FoodItem item = orderItem.getFoodItem();
        int quantity = orderItem.getQuantity();
        double price = item.getPrice();
        totalPrice += price * quantity;
        orderListView.getItems().add(item.getName() + " - Quantity: " + quantity + " - MYR " + String.format("%.2f", price) + "(each)");
    }

    //display total price
    Label totalPriceLabel = new Label("Total Price: MYR " + String.format("%.2f", totalPrice));

    //buttons
    Button confirmOrderButton = new Button("Confirm Order");
    Button updateOrderButton = new Button("Update Order");
    Button removeItemButton = new Button("Delete Item");

    confirmOrderButton.setOnAction(event -> {
        //check if user is logged in
        if (loggedInUserName != null) {
            //save order to the file with the logged-in username
            saveOrderToFile(reviewOrderItems, loggedInUserName);
            updateFoodStock(reviewOrderItems);

            //clear table
            reviewOrderItems.clear();

            //close review order stage
            reviewOrderStage.close();

            //confirmation alert
            Alert confirmAlert = new Alert(Alert.AlertType.INFORMATION);
            confirmAlert.setTitle("Order Confirmed");
            confirmAlert.setHeaderText(null);
            confirmAlert.setContentText("Your order has been confirmed. Enjoy your meal :)");
            confirmAlert.showAndWait();
        } else {
            showAlert("Please log in before confirming the order.", Alert.AlertType.ERROR);
        }
    });

    updateOrderButton.setOnAction(event -> {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Update Quantity");
        dialog.setHeaderText("Enter the new quantity for the selected item:");
        dialog.setContentText("Quantity:");
        Optional<String> result = dialog.showAndWait();

        result.ifPresent(newQuantityStr -> {
            try {
                int newQuantity = Integer.parseInt(newQuantityStr);
                if (newQuantity <= 0 || newQuantity > 500) {
                    showAlert("Invalid quantity. Please enter a positive integer less than or equal to 500.", Alert.AlertType.ERROR);
                    return;
                }

                //get selected item from listview
                String selectedOrderItem = orderListView.getSelectionModel().getSelectedItem();
                if (selectedOrderItem != null) {
                    //extract item name from the selected string
                    String itemName = selectedOrderItem.split(" - ")[0];
                    //find corresponding OrderItem and update its quantity
                    for (OrderItem orderItem : reviewOrderItems) {
                        if (orderItem.getFoodItem().getName().equals(itemName)) {
                            orderItem.setQuantity(newQuantity);
                            break;
                        }
                    }

                    //update listview and total price label
                    orderListView.getItems().clear();
                    double updatedTotalPrice = 0;
                    for (OrderItem orderItem : reviewOrderItems) {
                        FoodItem item = orderItem.getFoodItem();
                        int quantity = orderItem.getQuantity();
                        double price = item.getPrice();
                        orderListView.getItems().add(item.getName() + " - Quantity: " + quantity + " - MYR " + String.format("%.2f", price) + " (each)");
                        updatedTotalPrice += price * quantity;
                    }
                    totalPriceLabel.setText("Total Price: MYR " + String.format("%.2f", updatedTotalPrice));

                    showAlert("Quantity updated successfully.", Alert.AlertType.INFORMATION);
                } else {
                    showAlert("Please select an item to update.", Alert.AlertType.ERROR);
                }
            } catch (NumberFormatException e) {
                showAlert("Invalid quantity format. Please enter a valid integer.", Alert.AlertType.ERROR);
            }
        });
    });

    removeItemButton.setOnAction(event -> {
        //get selected item from listview
        String selectedOrderItem = orderListView.getSelectionModel().getSelectedItem();
        if (selectedOrderItem != null) {
            //extract item name from the selected string
            String itemName = selectedOrderItem.split(" - ")[0];
            //find orderitem and remove it from list
            Iterator<OrderItem> iterator = reviewOrderItems.iterator();
            while (iterator.hasNext()) {
                OrderItem orderItem = iterator.next();
                if (orderItem.getFoodItem().getName().equals(itemName)) {
                    iterator.remove();
                    break;
                }
            }

            //refresh ListView after removal
            orderListView.getItems().remove(selectedOrderItem);

            //recalculate total price
            double updatedTotalPrice = 0;
            for (OrderItem orderItem : reviewOrderItems) {
                FoodItem item = orderItem.getFoodItem();
                int quantity = orderItem.getQuantity();
                double price = item.getPrice();
                updatedTotalPrice += price * quantity;
            }
            totalPriceLabel.setText("Total Price: MYR " + String.format("%.2f", updatedTotalPrice));

            showAlert("Item removed successfully.", Alert.AlertType.INFORMATION);
        } else {
            showAlert("Please select an item to remove.", Alert.AlertType.ERROR);
        }
    });

    layout.getChildren().addAll(orderListView, totalPriceLabel, confirmOrderButton, updateOrderButton, removeItemButton);

    Scene scene = new Scene(layout);
    reviewOrderStage.setScene(scene);
    reviewOrderStage.show();
}

private Map<String, StringBuilder> orderDataMap = new HashMap<>();
    private void saveOrderToFile(List<OrderItem> reviewOrderItems, String userName) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("Orders.txt", true))) {
            StringBuilder orderData = new StringBuilder();

            orderData.append(orderDataMap.size() + 1).append(", ");
            orderData.append(userName).append(", ");

            for (OrderItem orderItem : reviewOrderItems) {
                FoodItem item = orderItem.getFoodItem();
                int quantity = orderItem.getQuantity();
                orderData.append(item.getName()).append(": ").append(quantity).append("-");
            }

            if (orderData.length() > 0 && orderData.charAt(orderData.length() - 1) == '-') {
                orderData.deleteCharAt(orderData.length() - 1);
            }

            //calculate total quantity
            int totalQuantity = reviewOrderItems.stream().mapToInt(OrderItem::getQuantity).sum();
            orderData.append(", ").append(totalQuantity).append(", ");

            double totalPrice = reviewOrderItems.stream().mapToDouble(orderItem -> orderItem.getFoodItem().getPrice() * orderItem.getQuantity()).sum();
            orderData.append(totalPrice);

            writer.write(orderData.toString());
            writer.newLine();

            orderDataMap.put(userName, orderData);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void decrementIngredientQuantity(String itemName, int decrement) {
        for (Ingredient ingredient : ingredients) {
            if (ingredient.getItemName().equals(itemName)) {
                int currentQuantity = ingredient.getItemQuantity();
                int newQuantity = Math.max(currentQuantity - decrement, 0);
                ingredient.setItemQuantity(newQuantity);
                break;
            }
        }
    }

    public void updateFoodStock(List<OrderItem> orderItems) {
        for (OrderItem orderItem : orderItems) {
            FoodItem foodItem = orderItem.getFoodItem();
            int quantityOrdered = orderItem.getQuantity();

            switch (foodItem.getName()) {
                case "Chicken Shawarma":
                    decrementIngredientQuantity("Shawarma wrap", quantityOrdered);
                    decrementIngredientQuantity("Chicken", quantityOrdered);
                    decrementIngredientQuantity("Hummus", quantityOrdered);
                    decrementIngredientQuantity("Potato", quantityOrdered);
                    break;
                case "Maqlooba":
                    decrementIngredientQuantity("Rice", quantityOrdered);
                    decrementIngredientQuantity("Eggplant", quantityOrdered);
                    decrementIngredientQuantity("Onion", quantityOrdered);
                    decrementIngredientQuantity("Potato", quantityOrdered);
                    decrementIngredientQuantity("Meat", quantityOrdered);
                    decrementIngredientQuantity("Hummus", quantityOrdered);
                    decrementIngredientQuantity("Tomato", quantityOrdered);
                    break;
                case "Hummus":
                    decrementIngredientQuantity("Hummus", quantityOrdered);
                    break;
                case "Falafel":
                    decrementIngredientQuantity("Chickpeas", quantityOrdered);
                    decrementIngredientQuantity("Garlic", quantityOrdered);
                    break;
                case "Mahshi":
                    decrementIngredientQuantity("Vine leaves", quantityOrdered);
                    decrementIngredientQuantity("Rice", quantityOrdered);
                    decrementIngredientQuantity("Meat", quantityOrdered);
                    break;
                case "Tabbouleh":
                    decrementIngredientQuantity("Parsley", quantityOrdered);
                    decrementIngredientQuantity("Tomato", quantityOrdered);
                    decrementIngredientQuantity("Mint Leaves", quantityOrdered);
                    decrementIngredientQuantity("Onion", quantityOrdered);
                    decrementIngredientQuantity("Bulgur", quantityOrdered);
                    break;
                case "Chicken Mandi":
                    decrementIngredientQuantity("Chicken", quantityOrdered);
                    decrementIngredientQuantity("Rice", quantityOrdered);
                    decrementIngredientQuantity("Spices", quantityOrdered);
                    break;
                case "Meat Mandi":
                    decrementIngredientQuantity("Meat", quantityOrdered);
                    decrementIngredientQuantity("Rice", quantityOrdered);
                    decrementIngredientQuantity("Spices", quantityOrdered);
                    decrementIngredientQuantity("Nuts", quantityOrdered);
                    decrementIngredientQuantity("Raisins", quantityOrdered);
                    break;
                case "Chicken Kabsa":
                    decrementIngredientQuantity("Chicken", quantityOrdered);
                    decrementIngredientQuantity("Rice", quantityOrdered);
                    decrementIngredientQuantity("Vegetables", quantityOrdered);
                    decrementIngredientQuantity("Spices", quantityOrdered);
                    break;
                case "Meat Kabsa":
                    decrementIngredientQuantity("Meat", quantityOrdered);
                    decrementIngredientQuantity("Rice", quantityOrdered);
                    decrementIngredientQuantity("Vegetables", quantityOrdered);
                    decrementIngredientQuantity("Spices", quantityOrdered);
                    break;
                case "Shakshuka":
                    decrementIngredientQuantity("Egg", quantityOrdered);
                    decrementIngredientQuantity("Tomato", quantityOrdered);
                    decrementIngredientQuantity("Onion", quantityOrdered);
                    decrementIngredientQuantity("Bell Pepper", quantityOrdered);
                    decrementIngredientQuantity("Garlic", quantityOrdered);
                    decrementIngredientQuantity("Chili Peppers", quantityOrdered);
                    break;
                case "Fattoush":
                    decrementIngredientQuantity("Lettuce", quantityOrdered);
                    decrementIngredientQuantity("Tomato", quantityOrdered);
                    decrementIngredientQuantity("Cucumber", quantityOrdered);
                    decrementIngredientQuantity("Onion", quantityOrdered);
                    decrementIngredientQuantity("Bell Pepper", quantityOrdered);
                    decrementIngredientQuantity("Pita Bread", quantityOrdered);
                    decrementIngredientQuantity("Olive Oil", quantityOrdered);
                    decrementIngredientQuantity("Sumac", quantityOrdered);
                    break;
                case "Knafeh":
                    decrementIngredientQuantity("Phyllo Dough", quantityOrdered);
                    decrementIngredientQuantity("Akawi Cheese", quantityOrdered);
                    decrementIngredientQuantity("Sugar", quantityOrdered);
                    decrementIngredientQuantity("Rose Water", quantityOrdered);
                    decrementIngredientQuantity("Butter", quantityOrdered);
                    break;
                default:
                    break;
            }
        }
    }

    public static class FoodItem {
        private String name;
        private Image image;
        private String description;
        private double price;

        public FoodItem(String name, Image image, String description, double price) {
            this.name = name;
            this.image = image;
            this.description = description;
            this.price = price;
        }
        public String getName() {
            return name;
        }
        public Image getImage() {
            return image;
        }
        public String getDescription() {
            return description;
        }
        public double getPrice() {
            return price;
        }
    }

    public class OrderItem {
        private FoodItem foodItem;
        private int quantity;

        public OrderItem(FoodItem foodItem, int quantity) {
            this.foodItem = foodItem;
            this.quantity = quantity;
        }

        public FoodItem getFoodItem() {
            return foodItem;
        }
        public int getQuantity() {
            return quantity;
        }

        public void setQuantity(int quantity) {
            this.quantity = quantity;
        }
    }

    public static class Order {
        private List<OrderItem> items;

        public Order() {
            this.items = new ArrayList<>();
        }

        public void addItem(OrderItem item) {
            items.add(item);
        }
    }

    public class Ingredient {
        private static int nextId = 1;
        private final StringProperty itemId;
        private final StringProperty itemName;
        private final IntegerProperty itemQuantity;
        private final DoubleProperty itemPrice;

        public Ingredient(String itemName, int itemQuantity, double itemPrice) {
            this.itemId = new SimpleStringProperty("" + nextId++);
            this.itemName = new SimpleStringProperty(itemName);
            this.itemQuantity = new SimpleIntegerProperty(itemQuantity);
            this.itemPrice = new SimpleDoubleProperty(itemPrice);
        }
        public String getItemId() {
            return itemId.get();
        }

        public String getItemName() {
            return itemName.get();
        }
        public int getItemQuantity() {
            return itemQuantity.get();
        }
        public double getItemPrice() {
            return itemPrice.get();
        }
        public void setItemName(String newName) {
            this.itemName.set(newName);
        }
        public void setItemQuantity(int newQuantity) {
            this.itemQuantity.set(newQuantity);
        }
        public void setItemPrice(double newPrice) {
            this.itemPrice.set(newPrice);
        }
    }

    private void generateReport() {
        //read data from Orders.txt and populate the report table
        List<String[]> orders = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader("Orders.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] orderData = line.split(", ");
                if (orderData.length >= 5) {
                    orders.add(orderData);
                } else {

                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        //create the report table
        TableView<String[]> reportTable = new TableView<>();
        TableColumn<String[], String> orderNumberCol = new TableColumn<>("Order Number");
        orderNumberCol.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue()[0]));

        TableColumn<String[], String> usernameCol = new TableColumn<>("Username");
        usernameCol.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue()[1]));

        TableColumn<String[], String> orderItemsCol = new TableColumn<>("Order Items");
        orderItemsCol.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue()[2]));

        TableColumn<String[], String> totalQuantityCol = new TableColumn<>("Total Quantity");
        totalQuantityCol.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue()[3]));

        TableColumn<String[], String> totalPriceCol = new TableColumn<>("Total Price");
        totalPriceCol.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue()[4]));

        reportTable.getColumns().addAll(orderNumberCol, usernameCol, orderItemsCol, totalQuantityCol, totalPriceCol);
        reportTable.getItems().addAll(orders);

        //report stage
        Stage reportStage = new Stage();
        reportStage.setTitle("Report");
        reportStage.setScene(new Scene(new VBox(reportTable), 800, 600));
        reportStage.show();
    }

    //global variable to keep track of the ingredient ID counter
    private int ingredientIdCounter = 1;

    private void resetIngredientIdCounter() {
        ingredientIdCounter = 1;
    }
    private int generateIngredientId() {
        return ingredientIdCounter++;
    }

    private void signOut() {
        resetIngredientIdCounter();
    }
    private void showAdminDashboard(Stage adminStage, Stage primaryStage) {
        //close the admin login/register stage
        adminStage.close();

        //new stage for the admin dashboard
        Stage adminDashboardStage = new Stage();
        adminDashboardStage.setTitle("Admin Dashboard");

        //vbox to hold the dashboard components
        VBox vbox = new VBox();
        vbox.setAlignment(Pos.CENTER);
        vbox.setSpacing(10);

        Button signOutButton = new Button("Sign Out");
        signOutButton.setOnAction(event -> {
            signOut();
            adminDashboardStage.close();
            showAdminLoginRegister(primaryStage);
        });
        
        HBox signOutBox = new HBox(10, signOutButton);
        signOutBox.setAlignment(Pos.CENTER_RIGHT);
        vbox.getChildren().addAll(signOutBox);

        Button exitButton = new Button("X");
        exitButton.setOnAction(event -> adminDashboardStage.close());
        HBox xbuttonBox = new HBox(10, exitButton);
        xbuttonBox.setAlignment(Pos.CENTER_RIGHT);
        vbox.getChildren().addAll(xbuttonBox);

        Label titleLabel = new Label("Stock");
        titleLabel.setStyle("-fx-font-weight: bold; -fx-font-size: 18px;");
        vbox.getChildren().add(titleLabel);

        Label descriptionLabel = new Label("Select an ingredient to update or delete. Click 'Create' to add a new ingredient.");
        descriptionLabel.setStyle("-fx-font-size: 12px;");
        vbox.getChildren().add(descriptionLabel);

        //add CRUD options
        HBox crudOptions = new HBox();
        crudOptions.setAlignment(Pos.CENTER);
        crudOptions.setSpacing(10);
        Button createButton = new Button("Create");
        Button updateButton = new Button("Update");
        Button deleteButton = new Button("Delete");
        crudOptions.getChildren().addAll(createButton, updateButton, deleteButton);

        Button reportButton = new Button("Report");
        reportButton.setOnAction(event -> generateReport());

        Button salesButton = new Button("Sales");
        salesButton.setOnAction(event -> {
            try {
                //load the FXML file
                FXMLLoader loader = new FXMLLoader(getClass().getResource("Sales.fxml"));
                Parent salesRoot = loader.load();
                Stage salesStage = new Stage();
                salesStage.setTitle("Sales");
                Scene salesScene = new Scene(salesRoot);
                salesStage.setScene(salesScene);
                salesStage.show();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        //display ingredient information
        TableView<Ingredient> tableView = new TableView<>();
        TableColumn<Ingredient, String> itemIdCol = new TableColumn<>("Ingredient ID");
        itemIdCol.setCellValueFactory(new PropertyValueFactory<>("itemId"));

        TableColumn<Ingredient, String> itemNameCol = new TableColumn<>("Ingredient");
        itemNameCol.setCellValueFactory(new PropertyValueFactory<>("itemName"));

        TableColumn<Ingredient, Integer> itemQuantityCol = new TableColumn<>("Quantity");
        itemQuantityCol.setCellValueFactory(new PropertyValueFactory<>("itemQuantity"));

        TableColumn<Ingredient, Double> itemPriceCol = new TableColumn<>("Price");
        itemPriceCol.setCellValueFactory(new PropertyValueFactory<>("itemPrice"));

        //set column header fonts to bold and larger
        itemIdCol.setStyle("-fx-font-weight: bold; -fx-font-size: 14px;");
        itemNameCol.setStyle("-fx-font-weight: bold; -fx-font-size: 14px;");
        itemQuantityCol.setStyle("-fx-font-weight: bold; -fx-font-size: 14px;");
        itemPriceCol.setStyle("-fx-font-weight: bold; -fx-font-size: 14px;");

        tableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        //column width
        double columnWidth = tableView.getWidth() / 4;
        itemIdCol.setPrefWidth(columnWidth);
        itemNameCol.setPrefWidth(columnWidth);
        itemQuantityCol.setPrefWidth(columnWidth);
        itemPriceCol.setPrefWidth(columnWidth);

        //set column alignment to center
        itemIdCol.setStyle("-fx-alignment: CENTER;");
        itemNameCol.setStyle("-fx-alignment: CENTER;");
        itemQuantityCol.setStyle("-fx-alignment: CENTER;");
        itemPriceCol.setStyle("-fx-alignment: CENTER;");

        tableView.getColumns().addAll(itemIdCol, itemNameCol, itemQuantityCol, itemPriceCol);

        //create ObservableList to store ingredients
        ObservableList<Ingredient> ingredients = FXCollections.observableArrayList();

        //add ingredients
        ingredients.add(new Ingredient("Rice", 194, 453.69));
        ingredients.add(new Ingredient("Chicken", 86, 353.04));
        ingredients.add(new Ingredient("Meat", 77, 683.78));
        ingredients.add(new Ingredient("Potato", 97, 149.0));
        ingredients.add(new Ingredient("Onion",103, 121.23));
        ingredients.add(new Ingredient("Eggplant", 134,189.81));
        ingredients.add(new Ingredient("Hummus",106,76.20));
        ingredients.add(new Ingredient("Tomato", 129, 150.69));
        ingredients.add(new Ingredient("Bell Pepper",87,61.77));
        ingredients.add(new Ingredient("Tahini",181,152.3));
        ingredients.add(new Ingredient("Shawarma wrap", 115, 76.32));
        ingredients.add(new Ingredient("Chickpeas", 178, 88.31));
        ingredients.add(new Ingredient("Garlic", 176,100.94));
        ingredients.add(new Ingredient("Egg", 188, 120.99));
        ingredients.add(new Ingredient("Chili Peppers", 99, 31.50));
        ingredients.add(new Ingredient("Spices", 108, 88.20));
        ingredients.add(new Ingredient("Nuts", 114, 93.41));
        ingredients.add(new Ingredient("Raisins", 123, 155.00));
        ingredients.add(new Ingredient("Lettuce", 100, 94.53));
        ingredients.add(new Ingredient("Cucumber", 94, 106.34));
        ingredients.add(new Ingredient("Radishes", 66, 84.90));
        ingredients.add(new Ingredient("Green Peppers", 91, 32.80));
        ingredients.add(new Ingredient("Red Peppers", 78, 71.83));
        ingredients.add(new Ingredient("Mint Leaves", 106, 81.43));
        ingredients.add(new Ingredient("Pita Bread", 191, 90.70));
        ingredients.add(new Ingredient("Olive Oil", 41, 117.01));
        ingredients.add(new Ingredient("Lemon Juice", 69, 41.80));
        ingredients.add(new Ingredient("Phyllo Dough", 200, 241.52));
        ingredients.add(new Ingredient("Akawi Cheese", 176, 164.87));
        ingredients.add(new Ingredient("Sugar", 100, 153.22));
        ingredients.add(new Ingredient("Rose Water", 103, 177.61));
        ingredients.add(new Ingredient("Butter", 89, 146.83));
        ingredients.add(new Ingredient("Sumac", 86,62.00));
        ingredients.add(new Ingredient("Syrup", 104, 150.22));


        //set items of the tableview to the observablelist
        tableView.setItems(ingredients);

        vbox.getChildren().addAll(crudOptions, tableView);

        HBox buttonBox = new HBox();
        buttonBox.setSpacing(10);


        Region leftSpacer = new Region();
        HBox.setHgrow(leftSpacer, Priority.ALWAYS);

        Region rightSpacer = new Region();
        HBox.setHgrow(rightSpacer, Priority.ALWAYS);
        buttonBox.getChildren().addAll(leftSpacer, reportButton, salesButton, rightSpacer);

        vbox.getChildren().addAll(buttonBox);

        Scene adminDashboardScene = new Scene(vbox, 800, 600);

        //set the scene to the admin dashboard stage
        adminDashboardStage.setScene(adminDashboardScene);
        adminDashboardStage.setFullScreen(true);
        adminDashboardStage.show();

        Label messageLabel = new Label();

        //event handlers for CRUD buttons
        createButton.setOnAction(event -> {
            //open a dialog for adding a new ingredient
            showAddDialog(tableView, ingredients);
        });

        updateButton.setOnAction(event -> {
            //get the selected ingredient and open a dialog for updating its properties
            Ingredient selectedIngredient = tableView.getSelectionModel().getSelectedItem();
            if (selectedIngredient != null) {
                //open a dialog for updating the ingredient
                showUpdateDialog(selectedIngredient, tableView);
            } else {
                showMessage(messageLabel, "Please select an ingredient to update.", Color.RED);
            }
        });

        deleteButton.setOnAction(event -> {
            // Remove the selected ingredient from the table
            Ingredient selectedIngredient = tableView.getSelectionModel().getSelectedItem();
            if (selectedIngredient != null) {
                ingredients.remove(selectedIngredient);
            } else {
                showMessage(messageLabel, "Please select an ingredient to delete.", Color.RED);
            }
        });
    }
    public void showAddDialog(TableView<Ingredient> tableView, ObservableList<Ingredient> ingredients) {
        //new stage for the dialog
        Stage dialogStage = new Stage();
        dialogStage.initModality(Modality.WINDOW_MODAL);
        Window adminDashboardStage = null;
        dialogStage.initOwner(adminDashboardStage);

        //gridpane for the dialog layout
        GridPane dialogPane = new GridPane();
        dialogPane.setAlignment(Pos.CENTER);
        dialogPane.setHgap(10);
        dialogPane.setVgap(10);
        dialogPane.setPadding(new Insets(20));

        //controls for adding ingredient properties
        Label nameLabel = new Label("Ingredient:");
        TextField nameTextField = new TextField();
        Label quantityLabel = new Label("Quantity:");
        Spinner<Integer> quantitySpinner = new Spinner<>(0, 200, 0);
        Label priceLabel = new Label("Price:");
        TextField priceTextField = new TextField();

        //controls to the dialog pane
        dialogPane.addRow(0, nameLabel, nameTextField);
        dialogPane.addRow(1, quantityLabel, quantitySpinner);
        dialogPane.addRow(2, priceLabel, priceTextField);

        //create buttons for adding and canceling
        Button addButton = new Button("Add");
        Button cancelButton = new Button("Cancel");

        ObservableList<Ingredient> newIngredients = FXCollections.observableArrayList();

        //event handler for add button
        addButton.setOnAction(event -> {
            //get ingredient properties from dialog controls
            String itemName = nameTextField.getText();
            int itemQuantity = quantitySpinner.getValue();
            String priceString = priceTextField.getText();

            if (itemName.isEmpty()) {
                //alert for missing name
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Missing Ingredient Name");
                alert.setContentText("Please enter the ingredient name to add the new item.");
                alert.showAndWait();
                return;
            }

            if (priceString.isEmpty()) {
                //alert for missing price
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Missing Price");
                alert.setContentText("Please enter the price to add the new item.");
                alert.showAndWait();
                return;
            }

            try {
                double itemPrice = Double.parseDouble(priceString);
                if (itemPrice < 0) {
                    //alert for negative price
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Invalid Price");
                    alert.setContentText("The price of the ingredient cannot be negative.");
                    alert.showAndWait();
                    return;
                }
                //add the new ingredient to the table
                ingredients.add(new Ingredient(itemName, itemQuantity, itemPrice));
                dialogStage.close();
            } catch (NumberFormatException e) {
                //show alert for invalid price format
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Invalid Price");
                alert.setContentText("The entered price is not a valid number.");
                alert.showAndWait();
            }
        });

        //event handler for cancel button
        cancelButton.setOnAction(event -> {
            dialogStage.close();
        });

        //add buttons to the dialog pane
        HBox buttonBox = new HBox(10, addButton, cancelButton);
        buttonBox.setAlignment(Pos.CENTER);
        dialogPane.add(buttonBox, 0, 3, 2, 1);

        Scene dialogScene = new Scene(dialogPane);
        dialogStage.setScene(dialogScene);
        dialogStage.show();
    }

    private void showUpdateDialog(Ingredient ingredient, TableView<Ingredient> tableView) {
        //new stage for the dialog
        Stage dialogStage = new Stage();
        dialogStage.initModality(Modality.WINDOW_MODAL);
        Window adminDashboardStage = null;
        dialogStage.initOwner(adminDashboardStage);

        //gridpane for the dialog layout
        GridPane dialogPane = new GridPane();
        dialogPane.setAlignment(Pos.CENTER);
        dialogPane.setHgap(10);
        dialogPane.setVgap(10);
        dialogPane.setPadding(new Insets(20));

        //controls for updating ingredient properties
        Label nameLabel = new Label("Ingredient:");
        TextField nameTextField = new TextField(ingredient.getItemName());
        Label quantityLabel = new Label("Quantity:");
        Spinner<Integer> quantitySpinner = new Spinner<>(0, 200, ingredient.getItemQuantity());
        Label priceLabel = new Label("Price:");
        TextField priceTextField = new TextField(String.valueOf(ingredient.getItemPrice()));

        dialogPane.addRow(0, nameLabel, nameTextField);
        dialogPane.addRow(1, quantityLabel, quantitySpinner);
        dialogPane.addRow(2, priceLabel, priceTextField);

        //buttons for updating and canceling
        Button updateButton = new Button("Update");
        Button cancelButton = new Button("Cancel");

        //event handler for update button
        updateButton.setOnAction(event -> {
            Ingredient selectedIngredient = ingredient;
            if (nameTextField.getText().isEmpty()) {
                //alert for missing name
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Missing Ingredient Name");
                alert.setContentText("Please enter the ingredient name to update the selected item.");
                alert.showAndWait();
                return;
            }

            if (priceTextField.getText().isEmpty()) {
                //alert for missing price
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Missing Price");
                alert.setContentText("Please enter the price to update the selected item.");
                alert.showAndWait();
                return;
            }

            try {
                double itemPrice = Double.parseDouble(priceTextField.getText());
                if (itemPrice < 0) {
                    //alert for negative price
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Invalid Price");
                    alert.setContentText("The price of an ingredient cannot be negative.");
                    alert.showAndWait();
                    return;
                }

                //update the ingredient properties
                selectedIngredient.setItemName(nameTextField.getText());
                selectedIngredient.setItemQuantity(quantitySpinner.getValue());
                selectedIngredient.setItemPrice(itemPrice);

                //close the dialog
                dialogStage.close();
            } catch (NumberFormatException e) {
                //alert for invalid price format
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Invalid Price");
                alert.setContentText("The entered price is not a valid number.");
                alert.showAndWait();
            }
        });

        //event handler for cancel button
        cancelButton.setOnAction(event -> {
            dialogStage.close();
        });

        //add buttons to the dialog pane
        HBox buttonBox = new HBox(10, updateButton, cancelButton);
        buttonBox.setAlignment(Pos.CENTER);
        dialogPane.add(buttonBox, 0, 3, 2, 1);

        //create a scene with the dialog pane
        Scene dialogScene = new Scene(dialogPane);

        //set the scene and show the dialog
        dialogStage.setScene(dialogScene);
        dialogStage.show();
    }

    //method to check if username exists in the file
    public boolean checkUsernameExists(String username, File file) {
        try {
            Scanner scanner = new Scanner(file);
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                String[] parts = line.split(",");
                if (parts.length >= 1 && parts[0].equals(username)) {
                    scanner.close();
                    return true;
                }
            }
            scanner.close();
        } catch (IOException e) {

            e.printStackTrace();
        }
        return false;
    }
}