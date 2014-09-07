package testimpl;

import main.monitor.attribute.Attribute;
import main.monitor.attribute.attributeimpl.StringAttribute;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Evgenii Morenkov
 */
public class AttrServer implements AttrServerMBean {
    private Map<String, Attribute> attributeMap;

    public AttrServer() {
        attributeMap = new HashMap<>();
        Attribute attribute = new StringAttribute();
        attribute.setAttrName("Test1");
        attribute.setAttrType("STRING");
        attribute.setAttrConfig("TEST CONFIG");
        attribute.setPollPeriod(5L);
        attributeMap.put(attribute.getAttrName(), attribute);

        attribute = new StringAttribute();
        attribute.setAttrName("Test2");
        attribute.setAttrType("STRING");
        attribute.setAttrConfig("EMPTY CONFIG");
        attribute.setPollPeriod(4L);
        attributeMap.put(attribute.getAttrName(), attribute);
    }

    @Override
    public ArrayList<Attribute> getAttributes() {
        System.out.println("get attributes.");
        return new ArrayList<>(attributeMap.values());
    }

    @Override
    public Attribute getAttributeByName(String attrName) {
        System.out.println("getAttributeByName");
        return attributeMap.get(attrName);
    }

    @Override
    public void updateAttribute(Attribute attribute) {
        System.out.println("updateAttribute");
        attributeMap.put(attribute.getAttrName(), attribute);
    }

    @Override
    public String toString() {
        return "AttrServer{" +
                "attributeMap=" + attributeMap +
                '}';
    }
}
