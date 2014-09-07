package main.monitor.attribute.attributeimpl;

import main.monitor.attribute.Attribute;
import main.monitor.attribute.exception.AttrWrongDefinitionException;

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

    @Override
    public void setConfValue(String value) {
        confValue = value;
    }

    @Override
    public String getAttrConfig() {
        return attrConfig;
    }

    @Override
    public Attribute getAttrFromString(String value) throws AttrWrongDefinitionException {
        StringAttribute stringAttribute = new StringAttribute();
        String[] split = value.split("[ ]+");
        if (split.length != 3) {
            throw new AttrWrongDefinitionException(value + ": количество слов: " + split.length);
        }
        stringAttribute.setAttrName(split[0]);
        stringAttribute.setAttrConfig(split[1]);
        stringAttribute.setPollPeriod(Long.valueOf(split[2]));
        return stringAttribute;
    }
}
