package main.monitor.attribute.exception;

/**
 * Can't understand attribute.
 *
 * @author Evgenii Morenkov
 */
public class AttrWrongDefinitionException extends Exception{

    public AttrWrongDefinitionException(){
        super();
    }

    public AttrWrongDefinitionException(String message) {
        super(message);
    }
}
