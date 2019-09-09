package duke.ui;

import duke.command.CommandResult;
import duke.task.Task;

import java.io.IOException;

/**
 * A class deals with user interface.
 */
public class Ui {
    /**
     * Returns a string to be printed.
     * @param text an array of strings to be composed.
     * @return a string to be printed.
     */
    private static String composeBlock(String... text) {
        assert text.length > 0 : "empty dialog box";
        StringBuilder strb = new StringBuilder();
        for (String line: text) {
            strb.append(line + "\n");
        }
        return strb.toString();
    }

    /**
     * Returns a string representation of the command result.
     * @param result a command result to be shown in UI.
     * @return a string representation of the command result.
     */
    public String composeResult(CommandResult result) {
        switch (result.getCommandType()) {
        case Exit:
            return sayGoodBye();
        case Add:
            return showAddedTask(result.getTasks()[0], result.getTaskListSize());
        case Delete:
            return showRemovedTask(result.getTasks()[0], result.getTaskListSize());
        case Find:
            return showMatchingTasks(result.getTasks());
        case List:
            return showTasks(result.getTasks());
        case Done:
            return showDoneTask(result.getTasks()[0]);
        default:
            assert false : "unknown result type";
        }
        return "";
    }

    /**
     * Returns a string to greet the user.
     * @return a string to greet the user.
     */
    public static String greet() {
        String greeting = "Hello! I'm Duke";
        String question = "What can I do for you?";
        return composeBlock(greeting, question);
    }

    /**
     * Returns a string of all tasks in the task list.
     * @param tasks the task list to be shown.
     * @return a string of all tasks in the task list.
     */
    public String showTasks(Task[] tasks) {
        String[] text = new String[tasks.length + 1];
        text[0] = "Here are the tasks in your list:";
        for (int i = 0; i < tasks.length; i++) {
            text[i + 1] = (i + 1) + "." + tasks[i];
        }
        return composeBlock(text);
    }

    /**
     * Returns a string representation of a message that the task has been marked as done.
     * @param task the task that has been marked as done.
     * @return a string representation of a message that the task has been marked as done.
     */
    public String showDoneTask(Task task) {
        return composeBlock("Nice! I've marked this task as done:", "  " + task);
    }

    /**
     * Returns a string representation of a message that the task has been removed from the list.
     * @param task the task that has been removed.
     * @param taskListSize the number of tasks in the task list.
     * @return a string representation of a message that the task has been removed from the list.
     */
    public String showRemovedTask(Task task, int taskListSize) {
        return composeBlock("Noted. I've removed this task:", "  " + task,
                String.format("Now you have %d task%s in the list.",
                        taskListSize, taskListSize > 1 ? "s" : ""));
    }

    /**
     * Returns a string representation of the message that the task has been added to the list.
     * @param task the task that has been added.
     * @param taskListSize the number of tasks in the task list.
     * @return a string representation of the message that the task has been added to the list.
     */
    public String showAddedTask(Task task, int taskListSize) {
        return composeBlock("Got it. I've added this task:", "  " + task,
                String.format("Now you have %d task%s in the list.",
                        taskListSize, taskListSize > 1 ? "s" : ""));
    }

    /**
     * Returns a string representation of all tasks that matches a keyword typed by the user.
     * @param taskList a list tasks that matches a keyword.
     * @return a string representation of all tasks that matches a keyword typed by the user.
     */
    public String showMatchingTasks(Task[] taskList) {
        String[] text = new String[taskList.length + 1];
        text[0] = "Here are the matching tasks in your list:";
        for (int i = 0; i < taskList.length; i++) {
            text[i + 1] = (i + 1) + "." + taskList[i];
        }
        return composeBlock(text);
    }

    /**
     * Returns a string representation of saying good bye to the user.
     * @return a string representation of saying good bye to the user.
     */
    public String sayGoodBye() {
        return composeBlock("Bye. Hope to see you again soon!");
    }

    /**
     * Returns a string representation of errors that occurs when parsing commands.
     * @param exception the exception thrown when parsing commands.
     * @return a string representation of errors that occurs when parsing commands.
     */
    public String showParsingError(Exception exception) {
        return composeBlock("OPPS!!! " + exception.getMessage());
    }

    /**
     * Returns a string representation of errors that occurs when loading tasks.
     * @param exception the exception thrown when loading tasks.
     * @return a string representation of errors that occurs when loading tasks.
     */
    public static String showLoadingError(Exception exception) {
        return composeBlock("OPPS!!! Fails to load your tasks.", exception.getMessage());
    }

    /**
     * Returns a string representation of errors that occurs when storing tasks.
     * @param exception the exception thrown when storing tasks.
     * @return a string representation of errors that occurs when storing tasks.
     */
    public String showStoringError(IOException exception) {
        return composeBlock("OPPS!!! Fails to store your tasks.", exception.getMessage());
    }
}