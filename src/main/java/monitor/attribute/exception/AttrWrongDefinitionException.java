package monitor.attribute.exception;

/**
 * Can't understand attribute.
 *
 * @author Evgenii Morenkov
 */
public class AttrWrongDefinitionException extends Exception {

    /**
     * Construct a new ConnectException with no detailed message.
     */
    public AttrWrongDefinitionException() {
        super();
    }

    /**
     * Constructs a new AttrWrongDefinitionException with the specified detail
     * message as to why the error occurred.
     * A detail message is a String that gives a specific
     * description of this error.
     *
     * @param message the detail message
     */
    public AttrWrongDefinitionException(String message) {
        super(message);
    }
}
