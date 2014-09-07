package monitor.attribute;

import java.util.concurrent.ConcurrentHashMap;

/**
 * Configuration class.
 *
 * @author Evgenii Morenkov
 */
public class Catalog {
    /**
     * Holds all attribute.
     */
    private ConcurrentHashMap<String, Attribute> attrMap;


    @Override
    public String toString() {
        return "Catalog{" +
                "attrMap=" + attrMap +
                '}';
    }

    /**
     * Gets Holds all attribute..
     *
     * @return Value of Holds all attribute..
     */
    public ConcurrentHashMap<String, Attribute> getAttrMap() {
        return attrMap;
    }

    /**
     * Sets new Holds all attribute..
     *
     * @param attrMap New value of Holds all attribute..
     */
    public void setAttrMap(ConcurrentHashMap<String, Attribute> attrMap) {
        this.attrMap = attrMap;
    }
}
