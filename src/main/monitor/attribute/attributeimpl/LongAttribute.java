package main.monitor.attribute.attributeimpl;

import main.monitor.attribute.Attribute;
import main.monitor.attribute.exception.AttrWrongDefinitionException;

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

    @Override
    public void setConfValue(String value) {
        confValue = (value == null || value.equals("")) ? null : Long.valueOf(value);
    }

    @Override
    public Attribute getAttrFromString(String line) throws AttrWrongDefinitionException {
        LongAttribute stringAttribute = new LongAttribute();
        String[] split = line.split("[ ]+");
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
