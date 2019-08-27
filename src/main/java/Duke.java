import duke.command.Command;
import duke.exception.IllegalDescriptionException;
import duke.parser.Parser;
import duke.storage.Storage;
import duke.task.TaskList;
import duke.ui.Ui;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Scanner;

public class Duke {
    private static final String FILEPATH = "/Users/jiangyuxin/Documents/sem1/cs2103/duke/data/duke.txt";
    private Storage storage;
    private TaskList tasks;
    private Parser parser;
    private Ui ui;

    /**
     * Class constructor specifying the file path for storage.
     * @param filePath the file path for storage.
     */
    public Duke(String filePath) {
        ui = new Ui();
        storage = new Storage(filePath);
        parser = new Parser();
        try {
            tasks = new TaskList(storage.load());
        } catch (FileNotFoundException | IllegalDescriptionException e) {
            ui.showLoadingError(e);
            tasks = new TaskList();
        }
    }

    /**
     * Runs the app.
     */
    public void run() {
        ui.greet();
        Scanner in = new Scanner(System.in);
        while (true) {
            String command = ui.readCommand(in);
            if (parser.isTerminatingCommand(command)) {
                try {
                    storage.store(tasks);
                } catch (IOException e) {
                    ui.showStoringError(e);
                }
                ui.sayGoodBye();
                break;
            } else {
                try {
                    Command cmd = parser.parseCommand(command, tasks, ui);
                    cmd.execute(tasks, ui);
                } catch (Exception e) {
                    ui.showParsingError(e);
                }
            }
        }
    }

    public static void main(String[] args) {
        new Duke(FILEPATH).run();
    }
}