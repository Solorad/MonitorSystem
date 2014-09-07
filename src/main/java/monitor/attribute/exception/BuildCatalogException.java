package monitor.attribute.exception;

/**
 * Exception for problems with catalog build.
 *
 * @author Evgenii Morenkov
 */
public class BuildCatalogException extends Exception {

    /**
     * Construct a new ConnectException with no detailed message.
     */
    public BuildCatalogException() {
        super();
    }

    /**
     * Constructs a new BuildCatalogException with the specified detail
     * message as to why the error occurred.
     * A detail message is a String that gives a specific
     * description of this error.
     *
     * @param message the detail message
     */
    public BuildCatalogException(String message) {
        super(message);
    }
}
