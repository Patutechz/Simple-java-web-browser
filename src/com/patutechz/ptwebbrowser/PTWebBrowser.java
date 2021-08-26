package com.patutechz.ptwebbrowser;

import java.io.File;
import java.net.MalformedURLException;
import java.util.Optional;
import javafx.beans.value.ChangeListener;
import javafx.application.Application;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.print.PrinterJob;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.layout.VBox;
import javafx.scene.text.FontSmoothingType;
import javafx.scene.web.*;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Callback;

public class PTWebBrowser extends Application {

    // Create the FileChooser
    private FileChooser fileChooser = new FileChooser();

    @Override
    public void start(Stage stage) {
        ToolBar bar = new ToolBar();

        WebView wv = new WebView();

        WebEngine we = wv.getEngine();

        we.titleProperty().addListener(new ChangeListener<String>() {
            public void changed(ObservableValue<? extends String> ov,
                    final String oldvalue, final String newvalue) {
                // Set the Title of the Stage
                stage.setTitle(newvalue + " PT Web Browser");
            }
        });

        String homePage = "http://www.google.com";

        // Menu starts from here
        MenuBar menuBar = new MenuBar();

        Menu file = new Menu("_File");
        Menu view = new Menu("_View");
        CheckMenuItem changeTheme = new CheckMenuItem("Change Theme");
        changeTheme.setSelected(true);
        Menu help = new Menu("_Help");

        Menu option = new Menu("_Option");

        Menu historym = new Menu("_History");

        MenuItem helpp = new MenuItem("Help");
        MenuItem about = new MenuItem("About");

        MenuItem open = new MenuItem("Open");
        MenuItem print = new MenuItem("Print");
        MenuItem exit = new MenuItem("Exit");

        CheckMenuItem ctxMenu = new CheckMenuItem("Enable Context Menu");
        ctxMenu.setSelected(true);

        MenuItem normalFontMenu = new MenuItem("Normal");
        MenuItem biggerFontMenu = new MenuItem("10% Bigger");
        MenuItem smallerFontMenu = new MenuItem("10% Smaller");

        MenuItem normalZoomMenu = new MenuItem("Normal");
        MenuItem biggerZoomMenu = new MenuItem("10% Bigger");
        MenuItem smallerZoomMenu = new MenuItem("10% Smaller");

        RadioMenuItem grayMenu = new RadioMenuItem("GRAY");
        grayMenu.setSelected(true);
        RadioMenuItem lcdMenu = new RadioMenuItem("LCD");

        // Create the Menus
        Menu scalingMenu = new Menu("Font Scale");
        scalingMenu.textProperty().bind(new SimpleStringProperty("Font Scale ").concat(wv.fontScaleProperty().multiply(100.0)).concat("%"));

        Menu smoothingMenu = new Menu("Font Smoothing");

        Menu zoomMenu = new Menu("Zoom");
        zoomMenu.textProperty().bind(new SimpleStringProperty("Zoom ").concat(wv.zoomProperty().multiply(100.0)).concat("%"));

        // Add  the Items to the corresponding Menu
        scalingMenu.getItems().addAll(normalFontMenu, biggerFontMenu, smallerFontMenu);
        smoothingMenu.getItems().addAll(grayMenu, lcdMenu);
        zoomMenu.getItems().addAll(normalZoomMenu, biggerZoomMenu, smallerZoomMenu);
        view.getItems().add(changeTheme);

        // Create the ToggleGroup
        new ToggleGroup().getToggles().addAll(lcdMenu, grayMenu);

        // Define the Event Handler
        normalFontMenu.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                wv.setFontScale(1.0);
            }
        });

        biggerFontMenu.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                wv.setFontScale(wv.getFontScale() + 0.10);
            }
        });

        smallerFontMenu.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                wv.setFontScale(wv.getFontScale() - 0.10);
            }
        });

        grayMenu.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                wv.setFontSmoothingType(FontSmoothingType.GRAY);
            }
        });

        lcdMenu.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                wv.setFontSmoothingType(FontSmoothingType.LCD);
            }
        });

        normalZoomMenu.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                wv.setZoom(1.0);
            }
        });

        biggerZoomMenu.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                wv.setZoom(wv.getZoom() + 0.10);
            }
        });

        smallerZoomMenu.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                wv.setZoom(wv.getZoom() - 0.10);
            }
        });

        wv.contextMenuEnabledProperty().bind(ctxMenu.selectedProperty());

        // Enabled JavaScript option
        CheckMenuItem scriptMenu = new CheckMenuItem("Enable JavaScript");
        scriptMenu.setSelected(true);
        wv.getEngine().javaScriptEnabledProperty().bind(scriptMenu.selectedProperty());

        option.getItems().addAll(ctxMenu, scalingMenu, smoothingMenu, zoomMenu, new SeparatorMenuItem(), scriptMenu);
        help.getItems().add(about);
        file.getItems().addAll(open, print, exit);

        // menu ends here
        //navugation starts here
        // Create the TextField
        TextField pageUrl = new TextField();
        pageUrl.setPrefColumnCount(70);
        pageUrl.setPromptText("Enter an Address");
        // Create the navigation Buttons
        Button refreshButton = new Button("Refresh");
        Button goButton = new Button("Go");
        Button homeButton = new Button("Home");

        about.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent e) {
                AboutBox ab = new AboutBox();
                ab.show();
            }
        });

        // Add an ActionListener to navigate to the entered URL
        pageUrl.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {

                we.load(pageUrl.getText());
            }
        });

        // Update the stage title when a new web page title is available
        we.locationProperty().addListener(new ChangeListener<String>() {
            public void changed(ObservableValue<? extends String> ov,
                    final String oldvalue, final String newvalue) {
                // Set the Title of the Stage
                pageUrl.setText(newvalue);
            }
        });
        // Add an ActionListener for the Refresh Button
        refreshButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                we.reload();
            }
        });

        // Add an ActionListener for the Go Button
        goButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                we.load(pageUrl.getText());
            }
        });

        // Add an ActionListener for the Home Button
        homeButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                we.load(homePage);
            }
        });

        open.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                File selectedFile = fileChooser.showOpenDialog(wv.getScene().getWindow());

                if (selectedFile != null) {
                    try {
                        we.load(selectedFile.toURI().toURL().toExternalForm());
                    } catch (MalformedURLException ex) {
                        ex.printStackTrace();
                    }
                }
            }
        });

        exit.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent e) {
                Alert a = new Alert(Alert.AlertType.INFORMATION, "Are you sure you want to quit?", ButtonType.YES, ButtonType.NO);
                a.setTitle("Quit?");
                Optional<ButtonType> r = a.showAndWait();
                if (r.isPresent() && r.get() == ButtonType.YES) {
                    stage.close();
                }
            }
        });

        print.setOnAction((ActionEvent e) -> {
            PrinterJob job = PrinterJob.createPrinterJob();
            if (job != null) {
                we.print(job);
                job.endJob();
            }
        });

        we.load(homePage);

        // Create the WebHistory
        WebHistory history = we.getHistory();

        // Create the Buttons
        Button backButton = new Button("<");
        backButton.setDisable(true);
        Button forwardButton = new Button(">");
        forwardButton.setDisable(true);

        // Add an ActionListener to the Back and Forward Buttons
        backButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                history.go(-1);
            }
        });

        forwardButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                history.go(1);
            }
        });

        // Add an ChangeListener to the currentIndex property
        history.currentIndexProperty().addListener(new ChangeListener<Number>() {
            public void changed(ObservableValue<? extends Number> ov,
                    final Number oldvalue, final Number newvalue) {
                int currentIndex = newvalue.intValue();

                if (currentIndex <= 0) {
                    backButton.setDisable(true);
                } else {
                    backButton.setDisable(false);
                }

                if (currentIndex >= history.getEntries().size()) {
                    forwardButton.setDisable(true);
                } else {
                    forwardButton.setDisable(false);
                }
            }
        });

        // Create the ComboBox for the History List
        ComboBox<WebHistory.Entry> historyList = new ComboBox<>();
        historyList.setPrefWidth(150);
        historyList.setItems(history.getEntries());

        // Set a cell factory to to show only the page title in the history list
        historyList.setCellFactory(new Callback<ListView<WebHistory.Entry>, ListCell<WebHistory.Entry>>() {
            @Override
            public ListCell<WebHistory.Entry> call(ListView<WebHistory.Entry> list) {
                ListCell<WebHistory.Entry> cell = new ListCell<WebHistory.Entry>() {
                    @Override
                    public void updateItem(WebHistory.Entry item, boolean empty) {
                        super.updateItem(item, empty);

                        if (empty) {
                            this.setText(null);
                            this.setGraphic(null);
                        } else {
                            String pageTitle = item.getTitle();
                            this.setText(pageTitle);
                        }
                    }
                };

                return cell;
            }
        });

        historyList.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                int currentIndex = history.getCurrentIndex();

                WebHistory.Entry selectedEntry = historyList.getValue();

                int selectedIndex = historyList.getItems().indexOf(selectedEntry);
                int offset = selectedIndex - currentIndex;
                history.go(offset);
            }
        });

        CustomMenuItem menuItemName = new CustomMenuItem(historyList);
        menuItemName.setHideOnClick(false);

        historym.getItems().addAll(menuItemName);
        menuBar.getMenus().addAll(file, view, option, historym, help);

        bar.getItems().addAll(backButton, forwardButton, homeButton, refreshButton, pageUrl, goButton);

        VBox root = new VBox(menuBar, bar, wv);
        Scene scene = new Scene(root);
        //scene.getStylesheets().add(getClass().getResource("style.css").toExternalForm());
        changeTheme.setOnAction(e -> {
            if (changeTheme.isSelected()) {
                setUserAgentStylesheet(STYLESHEET_MODENA);
            } else {
                setUserAgentStylesheet(STYLESHEET_CASPIAN);
            }
        });
        stage.setScene(scene);
        //set icon
        //File f = new File("src/com/patutechz/ptwebbrowser/patutechz.png");
        //Image img = new Image(f.toURI().toString());
        Image img = new Image(getClass().getResourceAsStream("africa.png"));
        stage.getIcons().add(img);
        stage.setTitle("PT Web Browser");
        stage.show();

    }

    public static void main(String[] args) {
        launch(args);
    }

}
