package exception;

public class DbConnectorException extends RuntimeException {
    public DbConnectorException(String message) {
        super(message);
    }
}
