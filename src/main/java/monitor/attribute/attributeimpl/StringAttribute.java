package monitor.attribute.attributeimpl;

import monitor.attribute.Attribute;
import monitor.attribute.exception.AttrWrongDefinitionException;

import java.io.Serializable;

/**
 * Configuration for attribute is String.
 *
 * @author Evgenii Morenkov
 */
public class StringAttribute extends Attribute implements Serializable {
    private String confValue;

    public StringAttribute() {
        attrType = "STRING";
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setConfValue(String value) {
        confValue = value;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getAttrConfig() {
        return attrConfig;
    }

    @Override
    public Attribute getAttrFromString(String value) throws AttrWrongDefinitionException {
        StringAttribute stringAttribute = new StringAttribute();
        // split String representation of attribute for 3 parts: name, attrConfig and poll period.
        // No space symbols are allowed.
        String[] split = value.split("[ ]+");
        // if there are more than 3 parts in string - throw exception.
        if (split.length != 3) {
            throw new AttrWrongDefinitionException(value + ": количество слов: " + split.length);
        }
        stringAttribute.setAttrName(split[0]);
        stringAttribute.setAttrConfig(split[1]);
        stringAttribute.setPollPeriod(Long.valueOf(split[2]));
        return stringAttribute;
    }
}
