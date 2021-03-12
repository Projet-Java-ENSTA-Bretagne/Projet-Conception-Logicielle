package database;

public class UserNotLoggedException extends Exception {
    public UserNotLoggedException(String errorMessage) {
        super(errorMessage);
    }
}