package main.monitor.attribute.exception;

/**
 * Attribute with defined name was not found.
 *
 * @author Evgenii Morenkov
 */
public class AttrTypeNotFoundException extends Exception {

    public AttrTypeNotFoundException(){
        super();
    }

    public AttrTypeNotFoundException(String message) {
        super(message);
    }
}
