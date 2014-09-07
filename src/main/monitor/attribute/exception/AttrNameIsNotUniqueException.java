package main.monitor.attribute.exception;

/**
 * Attribute with defined name declared more than once.
 *
 * @author Evgenii Morenkov
 */
public class AttrNameIsNotUniqueException extends Exception {

    public AttrNameIsNotUniqueException(){
        super();
    }

    public AttrNameIsNotUniqueException(String message) {
        super(message);
    }
}
