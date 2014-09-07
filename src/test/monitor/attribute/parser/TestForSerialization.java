package monitor.attribute.parser;

import monitor.attribute.Attribute;
import monitor.attribute.attributeimpl.StringAttribute;

import java.io.*;

/**
 * @author Evgenii Morenkov
 */
public class TestForSerialization {
    public static void main(String args[]) throws IOException, ClassNotFoundException {
        FileOutputStream fos = new FileOutputStream("temp.out");
        ObjectOutputStream oos = new ObjectOutputStream(fos);
        Attribute attribute = new StringAttribute();
        attribute.setAttrName("Test1");
        attribute.setAttrType("STRING");
        attribute.setAttrConfig("TEST CONFIG");
        attribute.setPollPeriod(5L);
        oos.writeObject(attribute);
        oos.flush();
        oos.close();

        FileInputStream fis = new FileInputStream("temp.out");
        ObjectInputStream oin = new ObjectInputStream(fis);
        Attribute attribute1 = (Attribute) oin.readObject();
        System.out.println("attribute1 = " + attribute1);
    }
}
