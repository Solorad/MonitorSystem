package testimpl;

import main.monitor.attribute.Attribute;

import java.util.ArrayList;

/**
 * @author Evgenii Morenkov
 */
public interface AttrServerMBean {
    /**
     * Return all available Attributes.
     *
     * @return attribute list.
     */
    ArrayList<Attribute> getAttributes();

    /**
     * Getter for particular attribute.
     *
     * @return Attribute or null if such attribute was not found
     */
    Attribute getAttributeByName(String attrName);

    /**
     * Update attribute.
     *
     * @param attribute - new attribute value.
     */
    void updateAttribute(Attribute attribute);

}
