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
import duke.exception.IllegalIndexOfTaskException;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

/**
 * A class representing a parser.
 */
public class Parser {
    private String getFirstWord(String string) {
        int indexOfSpace = string.indexOf(' ');
        if (indexOfSpace == -1) {
            return string;
        }
        return string.substring(0, indexOfSpace);
    }

    private String removeFirstWord(String string) {
        int indexOfSpace = string.indexOf(' ');
        if (indexOfSpace == -1) {
            return "";
        }
        return string.substring(indexOfSpace + 1).stripLeading();
    }

    /**
     * Returns the command by parsing the command string that typed by the user.
     * @param commandString the command typed by the user
     * @return the command by parsing the command string that typed by the user.
     * @throws IllegalDescriptionException If the description of the task is illegal.
     * @throws IllegalCommandException If the command is illegal.
     */
    public Command parseCommand(String commandString)
            throws IllegalDescriptionException, IllegalCommandException, IllegalIndexOfTaskException {
        String stringWithoutLeadingSpaces = commandString.stripLeading();
        String commandType = getFirstWord(stringWithoutLeadingSpaces);
        String description = removeFirstWord(stringWithoutLeadingSpaces);
        switch (commandType) {
        //Ignore words after the exit or list command
        case ExitCommand.COMMAND_WORD:
            return new ExitCommand();
        case ListCommand.COMMAND_WORD:
            return new ListCommand();
        case DoneCommand.COMMAND_WORD:
            if (description.isEmpty()) {
                throw new IllegalIndexOfTaskException("Please provide a valid index.");
            }
            return new DoneCommand(Integer.parseInt(description));
        case DeleteCommand.COMMAND_WORD:
            if (description.isEmpty()) {
                throw new IllegalIndexOfTaskException("Please provide a valid index.");
            }
            return new DeleteCommand(Integer.parseInt(description));
        case ToDoCommand.COMMAND_WORD:
            return new ToDoCommand(description);
        case FindCommand.COMMAND_WORD:
            return new FindCommand(description);
        case DeadlineCommand.COMMAND_WORD:
            return parseDeadlineTask(description);
        case EventCommand.COMMAND_WORD:
            return parseEventTask(description);
        default:
            throw new IllegalCommandException(
                    "I'm sorry, but I don't know what that means :-(");
        }
    }

    private DeadlineCommand parseDeadlineTask(String description) throws IllegalDescriptionException {
        int indexOfTime = description.indexOf("/by");
        if (indexOfTime == -1) {
            throw new IllegalDescriptionException("The format of deadline description is wrong.");
        }
        assert indexOfTime <= description.length() && indexOfTime + 3 <= description.length() :
                        "String length: " + description.length() + " seperator index: " + indexOfTime;
        String taskDescription = description.substring(0, indexOfTime).strip();
        String dateTime = description.substring(indexOfTime + 3).strip();

        return new DeadlineCommand(taskDescription, parseDateTime(dateTime));
    }

    private EventCommand parseEventTask(String description) throws IllegalDescriptionException {
        int indexOfTime = description.indexOf("/at");
        if (indexOfTime == -1) {
            throw new IllegalDescriptionException("The format of deadline description is wrong.");
        }
        assert indexOfTime <= description.length() && indexOfTime + 3 <= description.length() :
                        "String length: " + description.length() + " seperator index: " + indexOfTime;
        String taskDescription = description.substring(0, indexOfTime).strip();
        String dateTime = description.substring(indexOfTime + 3).strip();

        return new EventCommand(taskDescription, parseDateTime(dateTime));
    }

    private LocalDate parseDate(String dateString) throws IllegalDescriptionException {
        if (dateString.isEmpty()) {
            throw new IllegalDescriptionException("Date cannot be empty.");
        }
        String[] dayMonthYear = dateString.split("/");
        if (dayMonthYear.length != 3) {
            throw new IllegalDescriptionException("Format of date is wrong.");
        }
        LocalDate date = LocalDate.of(Integer.parseInt(dayMonthYear[2]),
                Integer.parseInt(dayMonthYear[1]),
                Integer.parseInt(dayMonthYear[0]));
        return date;
    }

    private LocalTime parseTime(String timeString) throws IllegalDescriptionException {
        if (timeString.isEmpty()) {
            throw new IllegalDescriptionException("Time cannot be empty.");
        }
        String[] hourMinute = timeString.split(":");
        if (hourMinute.length != 2) {
            throw new IllegalDescriptionException("Format of time is wrong.");
        }
        LocalTime time = LocalTime.of(
                Integer.parseInt(hourMinute[0]),
                Integer.parseInt(hourMinute[1]));
        return time;
    }

    private LocalDateTime parseDateTime(String dateTimeString) throws IllegalDescriptionException {
        String dateString = getFirstWord(dateTimeString);
        String timeString = removeFirstWord(dateTimeString);
        LocalDate date = parseDate(dateString);
        LocalTime time = parseTime(timeString);
        return LocalDateTime.of(date, time);
    }
}
