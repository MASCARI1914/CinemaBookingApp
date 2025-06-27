package com.example.demo;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;



import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CinemaTicketBookingApp extends Application {
    private static final double TICKET_PRICE = 7.5;
    private VBox vbox3;
    private TextField nameField, surnameField, phoneField;
    private Button purchaseButton;
    private Label totalCostLabel;
    private ComboBox<String> movieComboBox, dayComboBox, timeComboBox;
    private ImageView movieImageView;
    private Label movieInfoLabel;
    private final Map<String, List<Button>> movieSeatSelections = new HashMap<>();
    private String currentMovie = null;
    private MenuBar menuBar;

    @Override
    public void start(Stage primaryStage) {

        Image image = new Image("file:C:\\Users\\chris\\demo\\src\\Photos\\sxedio\\backmain.png");  // Εισαγωγή φωτογραφίας στην αρχική σελίδα
        ImageView imageView = new ImageView(image);


        imageView.setPreserveRatio(false);
        imageView.setFitWidth(900);
        imageView.setFitHeight(600);
        imageView.fitWidthProperty().bind(primaryStage.widthProperty());
        imageView.fitHeightProperty().bind(primaryStage.heightProperty());//ρυθμίσεις για την τοποθέτηση της φωτογραφίας

        MenuBar menuBar = createMenuBar(primaryStage);
        BorderPane rootLayout = new BorderPane();

        VBox vbox1 = new VBox(20);
        vbox1.setPadding(new Insets(60));

//δημιουργία Vbox1 για τα Labels ταινίας,ημέρας και ώρας-προβολής
        Label movieLabel = new Label("Select Movie:");
        movieComboBox = new ComboBox<>();
        movieComboBox.getItems().addAll("Godfather", "Interstellar", "Scarface");
        movieComboBox.setOnAction(_ -> updateMovieInfo());
        movieComboBox.setStyle("-fx-font-size: 18;");
        movieLabel.setStyle("-fx-font-size: 18; -fx-font-weight: bold;");

        movieImageView = new ImageView();
        movieImageView.setFitWidth(200);
        movieImageView.setPreserveRatio(true);

        movieInfoLabel = new Label();
        movieInfoLabel.setWrapText(true);
        movieInfoLabel.setMaxWidth(200);
        //σωστή τοποθέτηση του Vbox1 ώστε να ειναι στην σωστή θέση
        VBox movieInfoBox = new VBox(10, movieImageView, movieInfoLabel);
        movieInfoBox.setAlignment(Pos.CENTER);
        movieInfoBox.setPadding(new Insets(10));

        Label dayLabel = new Label("Select Day:");
        dayComboBox = new ComboBox<>();
        dayComboBox.getItems().addAll("Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday");
        dayComboBox.setStyle("-fx-font-size: 18;");
        dayComboBox.setOnAction(e -> updateMovieInfo());
        dayLabel.setStyle("-fx-font-size: 18; -fx-font-weight: bold;");

        Label timeLabel = new Label("Select Time:");
        timeComboBox = new ComboBox<>();
        timeComboBox.getItems().addAll("17:15", "21:00");
        timeComboBox.setStyle("-fx-font-size: 18;");
        timeComboBox.setOnAction(e -> updateMovieInfo());
        timeLabel.setStyle("-fx-font-size: 18; -fx-font-weight: bold;");
        vbox1.setPrefWidth(500);

        vbox1.getChildren().addAll(movieLabel, movieComboBox, dayLabel, dayComboBox, timeLabel, timeComboBox, movieInfoBox);
//Δημιουργία Vbox2(θέσεις)
        VBox vbox2 = new VBox(70);
        vbox2.setPadding(new Insets(30));
        vbox2.setFillWidth(true);
        vbox2.setStyle("-fx-smooth: 100px;-fx-font-size: 600px ");
        vbox2.setAlignment(Pos.TOP_CENTER);
        vbox2.setStyle("-fx-background-color: black ");
        vbox2.setPrefWidth(800);
//Το ορθογώνιο παραλληλόγραμμο για να ενταχθεί η λέξη Screen
        Rectangle screenRectangle = new Rectangle(500, 40);
        screenRectangle.setFill(Color.rgb(200, 200, 200, 0.3));
        screenRectangle.setStroke(Color.LIGHTGRAY);

        Label screenLabel = new Label("SCREEN");
        screenLabel.setStyle("-fx-font-size: 18; -fx-font-weight: bold;fx-text-fill: darkgreen");
        StackPane screenPane = new StackPane(screenRectangle, screenLabel);
        screenPane.setAlignment(Pos.CENTER);
        screenPane.setStyle("-fx-background-color:  lightGrey;");

        GridPane seatGrid = new GridPane();
        seatGrid.setHgap(10);
        seatGrid.setVgap(10);
        seatGrid.setPadding(new Insets(40));
        seatGrid.setAlignment(Pos.CENTER);

        for (int row = 0; row < 10; row++) {
            for (int col = 0; col < 7; col++) {
                Button seatButton = new Button(String.valueOf((char) ('A' + row)) + (col + 1));
                seatButton.setPrefSize(40, 40);
                seatButton.setStyle("-fx-background-color: #90ee90;");
                seatButton.setOnAction(e -> handleSeatSelection(seatButton));
                seatGrid.add(seatButton, col, row);
            }
        }

        vbox2.getChildren().addAll(screenPane, seatGrid);
//Δημιουργία Vbox3 και ταυτόχρονα όταν επιλέγεται θέση εμφανίζεται και αντιστοίχα όταν 'ξεπιλέγεται' φεύγει
        vbox3 = createFormBox();
        vbox3.setVisible(false);
        //ρυθμίσεις ώστε να είναι στην σωστή θέση
        HBox mainLayout = new HBox(20, vbox1, vbox2, vbox3, menuBar);
        mainLayout.setPadding(new Insets(20));

        StackPane root = new StackPane(imageView, mainLayout); // Βάζουμε την εικόνα φόντου κάτω και το layout από πάνω
        Scene scene = new Scene(root, 900, 600);

        primaryStage.setTitle("Cinema Ticket Booking System");
        primaryStage.setScene(scene);
        primaryStage.setMaximized(true);
        // primaryStage.show();

        vbox3.setPadding(new Insets(70, 30, 30, 230));
        vbox3.setPrefWidth(450);

        VBox centerLayout = new VBox(500);
        centerLayout.setPadding(new Insets(20));
        centerLayout.setAlignment(Pos.TOP_LEFT);
        centerLayout.getChildren().add(createPlaceholderContent());
        rootLayout.setCenter(centerLayout);

        scene.widthProperty().addListener((obs, oldVal, newVal) -> adjustLayout(mainLayout));
        scene.heightProperty().addListener((obs, oldVal, newVal) -> adjustLayout(mainLayout));
//Η αρχική σελίδα καλοσωρίσματος
        WelcomeScreen welcomeScreen = new WelcomeScreen(primaryStage);
        welcomeScreen.setLoginSuccessCallback(() -> {
            primaryStage.show();
        });

        welcomeScreen.show();
    }

    private MenuBar createMenuBar(Stage primaryStage) {
        MenuBar menuBar = new MenuBar();

        // Δημιουργία της επιλογής About
        Menu aboutMenu = new Menu("About");
        aboutMenu.setStyle("-fx-font-size:20;-fx-text-size:10");
        MenuItem aboutItem = new MenuItem("About");
        aboutItem.setOnAction(e -> showAboutDialog());

        // Δημιουργία Log Out
        Menu logOutMenu = new Menu("Log Out");
        logOutMenu.setStyle("-fx-font-size:20;-fx-text-size:10");
        MenuItem logoutItem = new MenuItem("Log Out");
        logoutItem.setOnAction(e -> handleLogout(primaryStage));
        //H πορσθήκη
        aboutMenu.getItems().add(aboutItem);
        logOutMenu.getItems().add(logoutItem);
        menuBar.getMenus().addAll(aboutMenu, logOutMenu);
        menuBar.setPrefWidth(300);
        return menuBar;
    }
    //Διαχείριση του LogOut ώστε να είναι σίγουρος ο πελάτης αν θέλει να βγει ή όχι
    private void handleLogout(Stage primaryStage) {
        Alert logoutAlert = new Alert(Alert.AlertType.CONFIRMATION);
        logoutAlert.setTitle("Log Out");
        logoutAlert.setHeaderText(null);
        logoutAlert.setContentText("Are you sure you want to log out?");

        // Εάν ο χρήστης επιλέξει "OK", κλείσε το κύριο παράθυρο και επέστρεψε στο WelcomeScreen
        logoutAlert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                primaryStage.hide(); // Κρύβουμε το κύριο παράθυρο

                // Δημιουργία νέου WelcomeScreen
                WelcomeScreen welcomeScreen = new WelcomeScreen(primaryStage);
                welcomeScreen.setLoginSuccessCallback(() -> {
                    primaryStage.show(); // Επανεμφάνιση του κύριου παραθύρου
                });
                welcomeScreen.show();
            }
        });
    }

    //Όταν επιλεχθεί το About τότε εμφανίζεται το μήνυμα όπου παρέχει βοήθειες και οδηγίες στον πελάτη
    private void showAboutDialog() {
        Alert aboutAlert = new Alert(Alert.AlertType.INFORMATION);
        aboutAlert.setTitle("About");
        aboutAlert.setHeaderText(null);
        aboutAlert.setContentText("GREEN:AVAILABLE\nRED:SELECTED\nGREY:NOT AVAILABLE\nTo purchase tickets you have to select first movie,day and time.");
        aboutAlert.showAndWait();

    }

    private VBox createPlaceholderContent() {
        VBox placeholder = new VBox(40);
        placeholder.setAlignment(Pos.TOP_LEFT);
        placeholder.getChildren().addAll();
        return placeholder;
    }
    //Εδώ είναι η δημιουργία του vbox3 όπου εμφανίζονται τα στοιχεία:όνομα,επίθετο τηλέφωνο ,κόστος και ολοκλήρωση αγοράς
    private VBox createFormBox() {
        VBox vbox = new VBox(15);
        vbox.setPadding(new Insets(20));
        vbox.setAlignment(Pos.TOP_LEFT);
//συμπλήρωση ονόματος
        Label nameLabel = new Label("Name:");
        nameField = new TextField();
        nameLabel.setStyle("-fx-font-size: 18");
        nameField.setStyle("-fx-font-size: 15");
//συμπλήρωση επιθέτου
        Label surnameLabel = new Label("Surname:");
        surnameField = new TextField();
        surnameLabel.setStyle("-fx-font-size: 18");
        surnameField.setStyle("-fx-font-size: 15");
//συμπλήρωση τηλεφώνου
        Label phoneLabel = new Label("Phone:");
        phoneField = new TextField();
        phoneField.setStyle("-fx-font-size: 15");
        phoneLabel.setStyle("-fx-font-size: 18");


        totalCostLabel = new Label("Total Cost: €0.0");
//κουμπι αγοράς
        purchaseButton = new Button("Purchase");
        purchaseButton.setStyle("-fx-background-color: grey;");
        purchaseButton.setDisable(true);
        purchaseButton.setStyle("-fx-font-size: 18;-fx-font-weight: bold;");
        purchaseButton.setOnAction(e -> completePurchase());

        vbox.getChildren().addAll(nameLabel, nameField, surnameLabel, surnameField, phoneLabel, phoneField, totalCostLabel, purchaseButton);
        return vbox;
    }

    //Σε περίτπωση που ο χρήστης πάει να επιλέξει θέσεις χωρίς να έχει διαλέξει τα 3 απαραίτητα πεδία από το Vbox1(τανία,ημέρα και ώρα)
    private void handleSeatSelection(Button seatButton) {
        if (movieComboBox.getValue() == null || dayComboBox.getValue() == null || timeComboBox.getValue() == null) {
            showAlert(Alert.AlertType.ERROR, "Selection Error", "Please select a movie, day, and time before choosing seats.");
            return;
        }

        String key = getMovieSessionInfo();

        movieSeatSelections.putIfAbsent(key, new ArrayList<>());
        List<Button> selectedSeatsForMovie = movieSeatSelections.get(key);

        String currentStyle = seatButton.getStyle();
//Διαδικασία για τις επιλεγμένες θέσεις
        if (currentStyle.contains("#90ee90")) {
            seatButton.setStyle("-fx-background-color: #ff6347;");
            selectedSeatsForMovie.add(seatButton);
        } else if (currentStyle.contains("#ff6347")) {
            seatButton.setStyle("-fx-background-color: #90ee90;");//Διαδικασία για τις διαθέσιμες θέσεις
            selectedSeatsForMovie.remove(seatButton);
        }

        updateFormVisibility();
        updateTotalCost();//καλείται η συνάρτηση κόστους
    }

    private void updateFormVisibility() {

        String key = getMovieSessionInfo();

        if (key == null) {
            vbox3.setVisible(false);
            purchaseButton.setDisable(true);
            return;
        }

        // Ανακτώ τις επιλεγμένες θέσεις για την τρέχουσα ταινία
        List<Button> selectedSeatsForMovie = movieSeatSelections.getOrDefault(key, new ArrayList<>());

        vbox3.setVisible(!selectedSeatsForMovie.isEmpty());
        purchaseButton.setDisable(selectedSeatsForMovie.isEmpty());
    }

    private void updateTotalCost() { //συναρτηση κοστους

        String key = getMovieSessionInfo();

        if (key == null) {
            totalCostLabel.setText("Total Cost: €0.0");
            return;
        }

        List<Button> selectedSeatsForMovie = movieSeatSelections.getOrDefault(key, new ArrayList<>());

        // Υπολογισμός κόστους
        double totalCost = selectedSeatsForMovie.size() * TICKET_PRICE;

        // Συνεχής ενημέρωση για το κόστος
        totalCostLabel.setText(String.format("Total Cost: €%.2f", totalCost));
    }

    private boolean isFormValid() { //Έλεγχος για την ορθή συμπλήρωση της φόρμας στα συγκεκριμένα πεδία
        String name = nameField.getText();
        String surname = surnameField.getText();
        String phone = phoneField.getText();

        //Έλεγχος για το αν εισάγονται οι σωστοί χαρακτήρες στις φόρμες ανάλογα με τους περιορισμούς τους,υπάρχουν και χαρακτήρες με τόνους
        boolean isNameValid = name.matches("[a-zA-Zα-ωΑ-Ωάέήίόύώ]*");
        boolean isSurnameValid = surname.matches("[a-zA-Zα-ωΑ-Ωάέήίόύώ]*");
        boolean isPhoneValid = phone.matches("69\\d{8}");

        return isNameValid && isSurnameValid && isPhoneValid;
    }

    private void updateMovieInfo() {
        String key = getMovieSessionInfo();//Κάλεσμα κλήσης για τις πληροφορίες των ταινιών

        if (key == null) return;

        if (currentMovie != null && !currentMovie.equals(key)) {
            List<Button> previousMovieSeats = movieSeatSelections.getOrDefault(currentMovie, new ArrayList<>());
            for (Button seat : previousMovieSeats) {
                seat.setStyle("-fx-background-color: #90ee90;");
                seat.setDisable(false);
            }
        }

        // Ενημέρωση για τις αγορασμένες θέσεις
        List<Button> newMovieSeats = movieSeatSelections.getOrDefault(key, new ArrayList<>());
        for (Button seat : newMovieSeats) {
            seat.setStyle("-fx-background-color: #808080;");
            seat.setDisable(true);
        }

        currentMovie = key;

        String selectedMovie = movieComboBox.getValue();
        switch (selectedMovie) {
            case "Godfather":
                movieImageView.setImage(new Image("file:src/Photos/Tainies/godfather.png"));
                movieInfoLabel.setText("The aging patriarch of an organized crime dynasty transfers control of his clandestine empire to his reluctant son.\n 1972 * Crime-Drama * 2h 55m " );
                movieInfoLabel.setStyle("-fx-font-size: 14");
                break;
            case "Interstellar":
                movieImageView.setImage(new Image("file:src/Photos/Tainies/Intrerstellar.png"));
                movieInfoLabel.setText("When Earth becomes uninhabitable in the future, a farmer and ex-NASA pilot, Joseph Cooper, is tasked to pilot a spacecraft, along with a team of researchers, to find a new planet for humans. \n 2014 * Sci-fi * 2h 49m");
                movieInfoLabel.setStyle("-fx-font-size: 14");
                break;
            case "Scarface":
                movieImageView.setImage(new Image("file:src/Photos/Tainies/scarface.png"));
                movieInfoLabel.setText("Miami in the 1980s: a determined criminal-minded Cuban immigrant becomes the biggest drug smuggler in Florida, and is eventually undone by his own drug addiction.\n1983 * Crime * 2h 50m");
                movieInfoLabel.setStyle("-fx-font-size: 14");
                break;
        }
    }

    private void completePurchase() {
        if (!isFormValid()) {
            showAlert(Alert.AlertType.ERROR, "Invalid Input", generateErrorMessage());
            return;
        }//Έλεγχος για ολοκληρωθεί η αγορά

        String name = nameField.getText();
        String surname = surnameField.getText();
        String phone = phoneField.getText();
//Παίρνει τα δεδομένα
        String key = getMovieSessionInfo();

        String movie = movieComboBox.getValue();
        if (movie == null) {
            showAlert(Alert.AlertType.ERROR, "Purchase Error", "No movie selected.");
            return;
        }//Αν ο χρήστης πατήσει κατευθείαν το Purchase χωρίς να έχει επιλέξει ταινία

        List<Button> selectedSeatsForMovie = movieSeatSelections.getOrDefault(key, new ArrayList<>());
        List<String> purchasedSeats = new ArrayList<>();

        //Οι αγορασμένες θέσεις γίνονται γκρι-μη διαθέσιμες
        List<Button> newMovieSeats = movieSeatSelections.getOrDefault(key, new ArrayList<>());
        for (Button seat : newMovieSeats) {
            purchasedSeats.add(seat.getText());
            seat.setStyle("-fx-background-color: #808080;");
            seat.setDisable(true);
        }

        movieSeatSelections.put(key, selectedSeatsForMovie);

        updateFormVisibility();
        updateTotalCost();

        String day = dayComboBox.getValue();
        String time = timeComboBox.getValue();
        //Η απόδειξη καλείται
        showReceiptWindow(name, surname, phone, movie, day, time, purchasedSeats);

        nameField.clear();
        surnameField.clear();
        phoneField.clear();

        vbox3.setVisible(false);
    }


    //Αποδειξη πελατη
    private void showReceiptWindow(String name, String surname, String phone, String movie, String day, String time, List<String> seats) {
        Stage receiptStage = new Stage();
        receiptStage.setTitle("Purchase Receipt");

        VBox receiptBox = new VBox(10);
        receiptBox.setStyle("-fx-background-color: darkred");
        receiptBox.setPadding(new Insets(20));
        receiptBox.setAlignment(Pos.CENTER);

        Label receiptTitle = new Label("Purchase Receipt");
        receiptTitle.setStyle("-fx-font-size: 18; -fx-font-weight: bold;-fx-text-fill: wheat; ");

        Label nameLabel = new Label("Name: " + name);
        nameLabel.setStyle("-fx-font-size: 14;-fx-font-weight: bold;-fx-text-fill: black;");
        Label surnameLabel = new Label("Surname: " + surname);
        surnameLabel.setStyle("-fx-font-size: 14;-fx-text-fill: black;-fx-font-weight: bold");
        Label phoneLabel = new Label("Phone: " + phone);
        phoneLabel.setStyle("-fx-font-size: 14;-fx-text-fill: black;-fx-font-weight: bold");
        Label movieLabel = new Label("Movie: " + movie);
        movieLabel.setStyle("-fx-font-size: 14;-fx-text-fill: black;-fx-font-weight: bold");
        Label dayLabel = new Label("Day: " + day);
        dayLabel.setStyle("-fx-font-size: 14;-fx-text-fill: black;-fx-font-weight: bold");
        Label timeLabel = new Label("Time: " + time);
        timeLabel.setStyle("-fx-font-size: 14;-fx-text-fill: black;-fx-font-weight: bold");
        Label seatsLabel = new Label("Seats: " + String.join(", ", seats));
        seatsLabel.setStyle("-fx-font-size: 14;-fx-text-fill: black;-fx-font-weight: bold");
        Label totalCostLabel = new Label(String.format("Total Cost: €%.2f", seats.size() * TICKET_PRICE));
        totalCostLabel.setStyle("-fx-font-size: 14;-fx-text-fill: black;-fx-font-weight: bold");

        receiptBox.getChildren().addAll(receiptTitle, nameLabel, surnameLabel, phoneLabel, movieLabel, dayLabel, timeLabel, seatsLabel, totalCostLabel);

        Scene receiptScene = new Scene(receiptBox, 400, 300);
        receiptStage.setScene(receiptScene);
        receiptStage.show();
    }

    //Ελεγχος για εισαγωγη σωστων χαρακτηρων στην φορμα για στοιχεια
    private String generateErrorMessage() {
        StringBuilder errorMessage = new StringBuilder();

        if (!nameField.getText().matches("[a-zA-Zα-ωΑ-Ωάέήίόύώ]*")) {
            errorMessage.append("Invalid Name: Only letters (with or without accents) are allowed.\n");
        }
        if (!surnameField.getText().matches("[a-zA-Zα-ωΑ-Ωάέήίόύώ]*")) {
            errorMessage.append("Invalid Surname: Only letters (with or without accents) are allowed.\n");
        }        if (!phoneField.getText().matches("69\\d{8}")) {
            errorMessage.append("Invalid Phone: Must start with '69' and contain exactly 10 digits.\n");
        }

        return errorMessage.toString().trim();
    }

    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void adjustLayout(HBox mainLayout) {
        double width = mainLayout.getScene().getWidth();
        mainLayout.setSpacing(width * 0.02);
    }

    private String getMovieSessionInfo() {
        String movie = movieComboBox.getValue();
        String day = dayComboBox.getValue();
        String time = timeComboBox.getValue();
        return movie + "|" + day + "|" + time;
    }

    public static void main(String[] args) {
        launch(args);
    }
}

