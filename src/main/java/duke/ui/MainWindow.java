package duke.ui;

import duke.handler.Duke;
import javafx.animation.PauseTransition;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.util.Duration;

import java.io.FileNotFoundException;

/**
 * A class representing the main window of the app.
 */
public class MainWindow extends AnchorPane {
    @FXML
    private TextField userInput;
    @FXML
    private ScrollPane scrollPane;
    @FXML
    private VBox dialogContainer;
    @FXML
    private Button sendButton;

    private Image userImage = new Image(this.getClass().getResourceAsStream("/images/user.jpg"));
    private Image dukeImage = new Image(this.getClass().getResourceAsStream("/images/duke.jpg"));
    private Duke duke;

    /**
     * Initializes the main window.
     */
    @FXML
    public void initialize() {
        scrollPane.vvalueProperty().bind(dialogContainer.heightProperty());
        scrollPane.setFitToWidth(true);
        sendButton.setOnMouseClicked(event -> {
            handleUserInput();
        });
    }

    /**
     * Initializes the main window with duke handler.
     * @param duke the duke handler to handle request.
     */
    public void init(Duke duke) {
        this.duke = duke;
        try {
            duke.loadTasks();
        } catch (FileNotFoundException e) {
            addDukeDialogBox(Ui.showLoadingError(e));
        } finally {
            addDukeDialogBox(Ui.greet());
        }
    }

    /**
     * Adds a duke dialog box to main window.
     * @param text the text to be shown in the dialog box.
     */
    public void addDukeDialogBox(String text) {
        dialogContainer.getChildren().addAll(DialogBox.getDukeDialog(text, dukeImage));
    }

    /**
     * Adds a user dialog box to main window.
     * @param text the text to be shown in the dialog box.
     */
    public void addUserDialogBox(String text) {
        dialogContainer.getChildren().addAll(DialogBox.getUserDialog(text, userImage));
    }

    @FXML
    private void handleUserInput() {
        String input = userInput.getText();
        if (input.isEmpty()) {
            return;
        }
        addUserDialogBox(input);
        addDukeDialogBox(duke.parse(input));
        if (input.equals("bye")) {
            exit();
        }
        userInput.clear();
    }

    /**
     * Exits the application after 3 seconds.
     */
    public void exit() {
        userInput.setDisable(true);
        sendButton.setDisable(true);
        PauseTransition delay = new PauseTransition(Duration.seconds(3));
        delay.setOnFinished(event -> Platform.exit());
        delay.play();
    }
}