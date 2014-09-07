package monitor.attribute.exception;

/**
 * Attribute with defined name declared more than once.
 *
 * @author Evgenii Morenkov
 */
public class AttrNameIsNotUniqueException extends Exception {

    /**
     * Construct a new ConnectException with no detailed message.
     */
    public AttrNameIsNotUniqueException() {
        super();
    }

    /**
     * Constructs a new AttrNameIsNotUniqueException with the specified detail
     * message as to why the error occurred.
     * A detail message is a String that gives a specific
     * description of this error.
     *
     * @param message the detail message
     */
    public AttrNameIsNotUniqueException(String message) {
        super(message);
    }
}
