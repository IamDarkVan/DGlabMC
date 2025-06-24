package shirohaNya.dglabmc.commands;

public class CommandException extends Exception {
    public CommandException(String message) {
        super(message);
    }

    public CommandException(Exception e) {
        super(e.getMessage());
    }
}
