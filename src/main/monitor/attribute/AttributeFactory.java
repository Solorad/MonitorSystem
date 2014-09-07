package main.monitor.attribute;

import main.monitor.attribute.attributeimpl.LongAttribute;
import main.monitor.attribute.attributeimpl.StringAttribute;
import main.monitor.attribute.exception.AttrTypeNotFoundException;
import main.monitor.attribute.exception.AttrWrongDefinitionException;

import java.util.HashMap;
import java.util.Map;

/**
 * Factory for attributes.
 *
 * @author Evgenii Morenkov
 */
public class AttributeFactory {
    private static Map<String, Attribute> typeCatalog = new HashMap<>();

    static {
        typeCatalog.put("STRING", new StringAttribute());
        typeCatalog.put("LONG", new LongAttribute());
    }

    public static void addAttributeType(String typeName, Attribute value) {
        typeCatalog.put(typeName, value);
    }

    /**
     * Build attribute from its type and string representation.
     * Uses not-static method 'getAttrFromString(String)'  from the same type attribute instance
     *          (it is forbidden to override static method)
     *
     * @param attrType - type.
     * @param attrDefinition - attrDefinition.
     * @return new Attribute.
     * @throws AttrTypeNotFoundException
     * @throws AttrWrongDefinitionException
     */
    public static Attribute getAttributeByType(String attrType, String attrDefinition)
            throws AttrTypeNotFoundException, AttrWrongDefinitionException {
        if (attrType == null) {
            throw new AttrTypeNotFoundException();
        }
        Attribute builder = typeCatalog.get(attrType.toUpperCase());
        if (builder == null) {
            throw new AttrTypeNotFoundException("No builder was found with type = '" + attrType + "'.");
        }
        return builder.getAttrFromString(attrDefinition);
    }
}
