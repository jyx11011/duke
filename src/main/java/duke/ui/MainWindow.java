package duke.ui;

import duke.handler.Duke;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;

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
    }

    /**
     * Initializes the main window with duke handler.
     * @param duke the duke handler to handle request.
     */
    public void init(Duke duke) {
        this.duke = duke;
        try {
            duke.loadTask();
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
            Platform.exit();
        }
        userInput.clear();
    }
}