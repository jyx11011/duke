package duke.parser;

import duke.command.Command;
import duke.command.DeadlineCommand;
import duke.command.DeleteCommand;
import duke.command.DoneCommand;
import duke.command.EventCommand;
import duke.command.ExitCommand;
import duke.command.FindCommand;
import duke.command.ListCommand;
import duke.command.ToDoCommand;
import duke.exception.IllegalCommandException;
import duke.exception.IllegalDescriptionException;

import java.time.LocalDateTime;

/**
 * A class representing a parser.
 */
public class Parser {
    /**
     * Returns the command by parsing the command string that typed by the user.
     * @param command the command typed by the user
     * @return the command by parsing the command string that typed by the user.
     * @throws IllegalDescriptionException If the description of the task is illegal.
     * @throws IllegalCommandException If the command is illegal.
     */
    public Command parseCommand(String command)
            throws IllegalDescriptionException, IllegalCommandException {
        Command cmd;
        if (command.equals(ExitCommand.COMMAND_WORD)) {
            cmd = new ExitCommand();
        } else if (command.equals(ListCommand.COMMAND_WORD)) {
            cmd = new ListCommand();
        } else {
            int indexOfSpace = command.indexOf(' ');
            //if there is no space, assume that the string is a command type
            if (indexOfSpace == -1) {
                indexOfSpace = command.length();
            }
            //seperate command and description of the task
            String type = command.substring(0, indexOfSpace);
            String description = command.substring(indexOfSpace).strip();

            if (type.equals(DoneCommand.COMMAND_WORD)) {
                cmd = new DoneCommand(Integer.parseInt(description));
            } else if (type.equals(DeleteCommand.COMMAND_WORD)) {
                cmd = new DeleteCommand(Integer.parseInt(description));
            } else if (type.equals(ToDoCommand.COMMAND_WORD)) {
                cmd = new ToDoCommand(description.strip());
            } else if (type.equals(DeadlineCommand.COMMAND_WORD)) {
                int sep = description.indexOf("/by");
                if (sep == -1) {
                    throw new IllegalDescriptionException("The format of deadline task is wrong.");
                }
                cmd = new DeadlineCommand(description.substring(0, sep).strip(),
                                parseDate(description.substring(sep + 3).strip()));
            } else if (type.equals(EventCommand.COMMAND_WORD)) {
                int sep = description.indexOf("/at");
                if (sep == -1) {
                    throw new IllegalDescriptionException("The format of event task is wrong.");
                }
                cmd = new EventCommand(description.substring(0, sep).strip(),
                                parseDate(description.substring(sep + 3).strip()));
            } else if (type.equals(FindCommand.COMMAND_WORD)) {
                cmd = new FindCommand(description);
            } else {
                throw new IllegalCommandException(
                        "I'm sorry, but I don't know what that means :-(");
            }
        }
        return cmd;
    }

    /**
     * Returns a LocalDateTime object representing the date and time of String date.
     * @param date a String that is to be parsed into date and time
     * @return a LocalDateTime object representing the date and time of String date.
     * @throws IllegalDescriptionException If the date or time is illegal.
     */
    private LocalDateTime parseDate(String date) throws IllegalDescriptionException {
        int indexOfSpace = date.indexOf(" ");
        String time = "";
        try {
            if (indexOfSpace != -1) {
                time = date.substring(indexOfSpace + 1).strip();
                date = date.substring(0, indexOfSpace).strip();
            } else {
                throw new IllegalDescriptionException("The format of date and time is wrong!");
            }

            String[] dayMonthYear = date.split("/");
            LocalDateTime dateTime = LocalDateTime.of(Integer.parseInt(dayMonthYear[2]),
                    Integer.parseInt(dayMonthYear[1]),
                    Integer.parseInt(dayMonthYear[0]),
                    Integer.parseInt(time.substring(0, 2)),
                    Integer.parseInt(time.substring(2)));

            return dateTime;
        } catch (IndexOutOfBoundsException e) {
            throw new IllegalDescriptionException("The format of date and time is wrong");
        }
    }
}
