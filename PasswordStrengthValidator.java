package application;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class PasswordStrengthValidator extends Application {
    private static final String PASSWORD_FILE = "src/application/password.txt";
    

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Password Validation");

        Label passwordStrengthLabel = new Label();
        Label passwordSuggestionsLabel = new Label();
        Label Name =new Label("Username:");
        TextField text=new TextField();
        Label passwordLabel =new Label("Password:");

        PasswordField passwordField = new PasswordField();
        passwordField.setOnKeyReleased(event -> {
            String password = passwordField.getText();
            String passwordStrength = validatePasswordStrength(password);
            passwordStrengthLabel.setText("Password Strength: " + passwordStrength);
            if (passwordStrength.equals("Weak")) {
                passwordStrengthLabel.setTextFill(Color.RED);
            } else {
                passwordStrengthLabel.setTextFill(Color.GREEN);
            }

            String passwordSuggestions = getPasswordSuggestions(password);
            passwordSuggestionsLabel.setText("Suggestions: " + passwordSuggestions);
            passwordSuggestionsLabel.setTextFill(Color.RED);
        });

        Button submitButton = new Button("Submit");
        submitButton.setDisable(false); // Disable the button by default
        submitButton.setOnAction(event -> {
            String enteredPassword = passwordField.getText();
            boolean passwordExists = checkPasswordInFile(enteredPassword);
            if (passwordExists) {
                showAlert("Password Check", "The password is in the file!");
            } else {
                showAlert("Password Check", "The password is not in the file.");
            }
        });

        VBox vbox = new VBox(10);
        vbox.setPadding(new Insets(10));
        vbox.getChildren().addAll(Name,text,passwordLabel, passwordField, passwordStrengthLabel, passwordSuggestionsLabel, submitButton);

        Scene scene = new Scene(vbox, 500, 500);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private String validatePasswordStrength(String password) {
        boolean hasMinimumLength = password.length() >= 12;
        boolean startsWithChar = password.length() > 0 && Character.isLetter(password.charAt(0));
        boolean hasSpecialChar = password.matches(".*[!@#$%^&*()_+=\\[\\]{}|:;\"'<>,.?/\\\\].*");
        boolean hasCapitalLetter = password.matches(".*[A-Z].*");
        boolean hasNumber = password.matches(".*\\d.*");

        if (!hasMinimumLength || !startsWithChar || !hasSpecialChar || !hasCapitalLetter || !hasNumber) {
            return "Weak";
        } else {
            return "Strong";
        }
    }

    private String getPasswordSuggestions(String password) {
        StringBuilder suggestions = new StringBuilder();

        if (password.length() < 12) {
            suggestions.append("- Password should contain at least 12 characters.\n");
        }
        if (password.length() > 0 && !Character.isLetter(password.charAt(0))) {
            suggestions.append("- Password should start with a character.\n");
        }
        if (!password.matches(".*[!@#$%^&*()_+=\\[\\]{}|:;\"'<>,.?/\\\\].*")) {
            suggestions.append("- Password should contain at least one special character.\n");
        }
        if (!password.matches(".*[A-Z].*")) {
            suggestions.append("- Password should contain at least one capital letter.\n");
        }
        if (!password.matches(".*\\d.*")) {
            suggestions.append("- Password should contain at least one number.\n");
        }

        return suggestions.toString();
    }

    private boolean checkPasswordInFile(String password) {
        try (BufferedReader reader = new BufferedReader(new FileReader(PASSWORD_FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.equals(password)) {
                    return true;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
