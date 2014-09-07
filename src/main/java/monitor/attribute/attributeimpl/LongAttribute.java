package monitor.attribute.attributeimpl;

import monitor.attribute.Attribute;
import monitor.attribute.exception.AttrWrongDefinitionException;

/**
 * Configuration for attribute is String.
 *
 * @author Evgenii Morenkov
 */
public class LongAttribute extends Attribute {
    private Long confValue;

    public LongAttribute() {
        attrType = "LONG";
    }

    /**
     * Gets configuration.
     *
     * @return Value of configuration.
     */
    public Long getConfiguration() {
        return confValue;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setConfValue(String value) {
        confValue = (value == null || value.equals("")) ? null : Long.valueOf(value);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Attribute getAttrFromString(String line) throws AttrWrongDefinitionException {
        LongAttribute stringAttribute = new LongAttribute();
        // split String representation of attribute for 3 parts: name, attrConfig and poll period.
        // No space symbols are allowed.
        String[] split = line.split("[ ]+");
        // if there are more than 3 parts in string - throw exception.
        if (split.length != 3) {
            throw new AttrWrongDefinitionException("Incorrect attribute definition string: '"
                                                           + line + "'.");
        }
        stringAttribute.setAttrName(split[0]);
        stringAttribute.setAttrConfig(split[1]);
        stringAttribute.setPollPeriod(Long.valueOf(split[2]));
        return stringAttribute;
    }
}
