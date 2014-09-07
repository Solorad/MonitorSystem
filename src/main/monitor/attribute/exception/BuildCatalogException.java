package main.monitor.attribute.exception;

/**
 * Exception for problems with catalog build.
 *
 * @author Evgenii Morenkov
 */
public class BuildCatalogException extends Exception {

    public BuildCatalogException(){
        super();
    }

    public BuildCatalogException(String message) {
        super(message);
    }
}
