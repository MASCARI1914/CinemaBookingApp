package com.example.demo;

import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import java.awt.*;
import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

public class WelcomeScreen {

    private final Stage primaryStage;
    private final Stage welcomeStage = new Stage();
    private Runnable onLoginSuccess;

    // Αποθήκευση χρηστών (email -> password)
    private final Map<String, String> users = new HashMap<>();
    private static final String USERS_FILE = "users.txt"; // Αρχείο αποθήκευσης χρηστών

    // Θυμάται το τελευταίο email που έκανε Login
    private String lastLoggedInEmail = null;

    public void setLoginSuccessCallback(Runnable callback) {
        this.onLoginSuccess = callback;
    }

    public WelcomeScreen(Stage stage) {
        this.primaryStage = stage;
        loadUsersFromFile(); // Φόρτωση χρηστών με βάση το αρχείο
    }

    public void show() {
        VBox layout = new VBox(20);
        layout.setAlignment(javafx.geometry.Pos.CENTER);

        // Ρύθμιση σταθερού φόντου
        BackgroundImage backgroundImage = new BackgroundImage(
                new Image("file:src/Photos/sxedio/WelcomeScreen.png",
                        400, 300, // Σταθερό πλάτος και ύψος
                        false, true),
                BackgroundRepeat.NO_REPEAT,
                BackgroundRepeat.NO_REPEAT,
                BackgroundPosition.CENTER,
                BackgroundSize.DEFAULT
        );
        layout.setBackground(new Background(backgroundImage));

        Button signInButton = new Button("Sign In");
        Button logInButton = new Button("Log In");

        signInButton.setOnAction(e -> showSignInForm());
        logInButton.setOnAction(e -> showLogInForm());

        layout.getChildren().addAll(signInButton, logInButton);

        // Ορισμός σκηνής με σταθερό μέγεθος
        Scene scene = new Scene(layout, 400, 300);
        welcomeStage.setScene(scene);
        welcomeStage.setTitle("Welcome Screen");
        welcomeStage.setResizable(false); // Απενεργοποίηση αλλαγής μεγέθους
        welcomeStage.show();
    }

    private void showSignInForm() {
        //Δημιουργία της φόρμας SignIn
        VBox formLayout = createForm("Sign In");
        formLayout.setStyle("-fx-background-color: darkred;-fx-font-smoothing-type: 100");
        Button backButton = new Button("Back");
        backButton.setStyle("-fx-background-color: wheat;-fx-font-smoothing-type: 100");
        backButton.setOnAction(e -> show());
        formLayout.getChildren().add(backButton);

        // Ορισμός σκηνής με σταθερό μέγεθος
        Scene signInScene = new Scene(formLayout, 400, 400);
        welcomeStage.setScene(signInScene);
        welcomeStage.setResizable(false); // Απενεργοποίηση αλλαγής μεγέθους
    }

    private void showLogInForm() {
        //Δημιουργία της φόρμας LogIn
        VBox formLayout = createForm("Log In");
        formLayout.setStyle("-fx-background-color: darkred;-fx-font-smoothing-type: 100");
        Button backButton = new Button("Back");
        backButton.setStyle("-fx-background-color: wheat;-fx-font-smoothing-type: 100");
        backButton.setOnAction(e -> show());
        formLayout.getChildren().add(backButton);

        // Ορισμός σκηνής με σταθερό μέγεθος
        Scene logInScene = new Scene(formLayout, 400, 400);
        welcomeStage.setScene(logInScene);
        welcomeStage.setResizable(false); // Απενεργοποίηση αλλαγής μεγέθους
    }

    private VBox createForm(String titleText) {
        VBox form = new VBox(15);
        form.setAlignment(javafx.geometry.Pos.CENTER);

        Label title = new Label(titleText);
        title.setStyle("-fx-font-size: 30; -fx-font-weight: bold;-fx-text-fill: black;-fx-smooth: 500;-fx-font-family: Arial");

        TextField emailField = new TextField();
        emailField.setPromptText("Email");

        // Προ-συμπλήρωση του τελευταίου email αν πρόκειται για Login
        if (titleText.equals("Log In") && lastLoggedInEmail != null) {
            emailField.setText(lastLoggedInEmail);
        }

        PasswordField passwordField = new PasswordField();
        passwordField.setPromptText("Password");

        TextField visiblePasswordField = new TextField();
        visiblePasswordField.setManaged(false); // Αρχικά κρυφό
        visiblePasswordField.setVisible(false); // Αρχικά κρυφό
        visiblePasswordField.setPromptText("Password");

        // Διασύνδεση πεδίων
        passwordField.textProperty().bindBidirectional(visiblePasswordField.textProperty());

        PasswordField confirmPasswordField;
        TextField visibleConfirmPasswordField = null;

        if (titleText.equals("Sign In")) {
            confirmPasswordField = new PasswordField();
            confirmPasswordField.setPromptText("Confirm Password");

            visibleConfirmPasswordField = new TextField();
            visibleConfirmPasswordField.setManaged(false); // Αρχικά κρυφό
            visibleConfirmPasswordField.setVisible(false); // Αρχικά κρυφό
            visibleConfirmPasswordField.setPromptText("Confirm Password");

            // Διασύνδεση πεδίων
            confirmPasswordField.textProperty().bindBidirectional(visibleConfirmPasswordField.textProperty());
        } else {
            confirmPasswordField = null;
        }

        CheckBox showPasswordCheckBox = new CheckBox("Show Password");
        showPasswordCheckBox.setStyle("-fx-text-fill: white;");

        // Εναλλαγή ορατότητας πεδίων
        TextField finalVisibleConfirmPasswordField = visibleConfirmPasswordField;
        PasswordField finalConfirmPasswordField = confirmPasswordField;
        showPasswordCheckBox.setOnAction(e -> {
            if (showPasswordCheckBox.isSelected()) {
                visiblePasswordField.setVisible(true);
                visiblePasswordField.setManaged(true);
                passwordField.setVisible(false);
                passwordField.setManaged(false);

                if (finalVisibleConfirmPasswordField != null && finalConfirmPasswordField != null) {
                    finalVisibleConfirmPasswordField.setVisible(true);
                    finalVisibleConfirmPasswordField.setManaged(true);
                    finalConfirmPasswordField.setVisible(false);
                    finalConfirmPasswordField.setManaged(false);
                }
            } else {
                visiblePasswordField.setVisible(false);
                visiblePasswordField.setManaged(false);
                passwordField.setVisible(true);
                passwordField.setManaged(true);

                if (finalVisibleConfirmPasswordField != null && finalConfirmPasswordField != null) {
                    finalVisibleConfirmPasswordField.setVisible(false);
                    finalVisibleConfirmPasswordField.setManaged(false);
                    finalConfirmPasswordField.setVisible(true);
                    finalConfirmPasswordField.setManaged(true);
                }
            }
        });

        Button submitButton = new Button("Submit");
        submitButton.setStyle("-fx-background-color: wheat;-fx-smooth: 100;-fx-font-smoothing-type: 100");
        submitButton.setOnAction(e -> {
            if (titleText.equals("Sign In")) {
                handleSignIn(emailField, passwordField, confirmPasswordField);
            } else if (titleText.equals("Log In")) {
                handleLogIn(emailField, passwordField);
            }
        });

        // Προσθέτουμε τα στοιχεία της φόρμας με τη σωστή σειρά
        form.getChildren().addAll(title, emailField, passwordField, visiblePasswordField);
        if (titleText.equals("Sign In")) {
            form.getChildren().addAll(confirmPasswordField, visibleConfirmPasswordField);
        }
        form.getChildren().add(showPasswordCheckBox); // Το CheckBox μπαίνει κάτω από τα πεδία Password
        form.getChildren().add(submitButton); // Το κουμπί Submit μπαίνει πάντα τελευταίο

        return form;
    }


    private void handleSignIn(TextField emailField, PasswordField passwordField, PasswordField confirmPasswordField) {
        String email = emailField.getText().trim();
        String password = passwordField.getText().trim();
        String confirmPassword = confirmPasswordField.getText().trim();

        // Έλεγχοι εγκυρότητας για τον κωδικό
        if (!email.matches("^[a-zA-Z0-9._%+-]+@(gmail\\.com|yahoo''.com)$")) {
            showAlert("Invalid Email", "Please enter a valid email address.");
            return;
        }

        if (password.length() < 6) {
            showAlert("Weak Password", "Password must be at least 6 characters long.");
            return;
        }

        if (!password.equals(confirmPassword)) {
            showAlert("Passwords Do Not Match", "The passwords you entered do not match.");
            return;
        }

        // Έλεγχος αν το email υπάρχει ήδη
        if (users.containsKey(email)) {
            showAlert("Email Already Exists", "This email is already registered.");
            return;
        }

        // Αποθήκευση χρήστη
        users.put(email, password);
        saveUsersToFile(); // Αποθήκευση στο αρχείο
        showAlert("Sign In Successful", "Your account has been created!");

        // Callback για επιτυχημένη εγγραφή
        show();
    }


    private void handleLogIn(TextField emailField, PasswordField passwordField) {
        String email = emailField.getText().trim();
        String password = passwordField.getText().trim();
        if (!email.matches("^[a-zA-Z0-9._%+-]+@(gmail\\.com|yahoo\\.com)")) {
            showAlert("Invalid Email", "Email must end with @gmail.com or @yahoo.com.");
            return;
        }

        // Έλεγχοι εγκυρότητας
        if (!users.containsKey(email)) {
            showAlert("User Not Found", "This email is not registered.");
            return;
        }

        if (!users.get(email).equals(password)) {
            showAlert("Incorrect Password", "The password you entered is incorrect.");
            return;
        }

        lastLoggedInEmail = email;

        showAlert("Login Successful", "Welcome back!");

        // Για επιτυχημένο login
        if (onLoginSuccess != null) {
            onLoginSuccess.run();
        }
        welcomeStage.close();
    }



    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    // Αποθήκευση χρηστών σε αρχείο
    private void saveUsersToFile() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(USERS_FILE))) {
            for (Map.Entry<String, String> entry : users.entrySet()) {
                writer.write(entry.getKey() + "," + entry.getValue());
                writer.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Φόρτωση χρηστών από αρχείο
    private void loadUsersFromFile() {
        File file = new File(USERS_FILE);
        if (!file.exists()) return;

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 2) {
                    users.put(parts[0], parts[1]); // email -> password
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}